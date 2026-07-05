package com.mks.projects.checkpointapp.service;


import com.mks.projects.checkpointapp.dto.BookingDto;
import com.mks.projects.checkpointapp.dto.BookingRequest;
import com.mks.projects.checkpointapp.dto.GuestDto;
import com.mks.projects.checkpointapp.dto.HotelReportDto;
import com.mks.projects.checkpointapp.entity.*;
import com.mks.projects.checkpointapp.entity.enums.BookingStatus;
import com.mks.projects.checkpointapp.exception.ResourceNotFoundException;
import com.mks.projects.checkpointapp.exception.UnAuthorisedException;
import com.mks.projects.checkpointapp.repository.*;
import com.mks.projects.checkpointapp.strategy.PricingService;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.stripe.model.Event;
import java.math.BigDecimal;
import org.springframework.security.access.AccessDeniedException;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.mks.projects.checkpointapp.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    private final BookingRepository bookingRepository;


    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final CheckoutService checkoutService;
    private final PricingService pricingService;

    @Value("${frontend.url}")
    private String frontendUrl;
    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequest bookingRequest) {

        log.info("Initializing booking for hotel : {}, room: {}, date {}-{}",
                bookingRequest.getHotelId(), bookingRequest.getRoomId(),
                bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with Id: " + bookingRequest.getHotelId()));

        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with Id: " + bookingRequest.getHotelId()));


        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount()
        );


        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate()) + 1;

        if (inventoryList.size() != daysCount) {
            throw new IllegalStateException("Room is not available anymore");
        }

        //Reserve the room/ update the booked count of inventories


        inventoryRepository.initBooking(room.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount());

//


        inventoryRepository.saveAll(inventoryList);

        // Create the Booking



        //TODO : Calculate dynamic amount

        BigDecimal pricingForOneRoom= pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice=pricingForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount()));

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomCount(bookingRequest.getRoomsCount())
                .amount(totalPrice)
                .build();


        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);





    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding Guests for booking with id: {}", bookingId);


       Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with Id: " + bookingId));



       User user= getCurrentUser();

       if(!user.equals(booking.getUser())){

           throw new UnAuthorisedException("Booking does not belong to this user with id: "+ user.getId());

       }

       if(hasBookingExpired(booking)){
           throw new IllegalStateException(("Booking has already expired"));
       }


       if(booking.getBookingStatus() != BookingStatus.RESERVED){
           throw new IllegalStateException("Booking is not under reserved state, cannot add guest");

       }

       for(GuestDto guestDto : guestDtoList){
           Guest guest=modelMapper.map(guestDto, Guest.class);
           guest.setUser(user);
           guest= guestRepository.save(guest);
           booking.getGuests().add(guest);
       }

       booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
       booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public String initiatePayments(Long bookingId) {

        Booking booking= bookingRepository.findById(bookingId).orElseThrow(
                ()-> new ResourceNotFoundException("Booking not found with id: " + bookingId)
        );

        User user = getCurrentUser();

        if (!user.getId().equals(booking.getUser().getId())) {

            throw new UnAuthorisedException(
                    "Booking does not belong to this user with id: " + user.getId());

        }

        if(hasBookingExpired(booking)){
            throw new IllegalStateException(("Booking has already expired"));
        }

       String sessionUrl= checkoutService.getCheckOutSession(booking, frontendUrl + "/payments/success" ,
                frontendUrl + "/payments/failure");


        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);

        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session == null) return;

            String sessionId = session.getId();
            Booking booking =
                    bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(() ->
                            new ResourceNotFoundException("Booking not found for session ID: "+sessionId));

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomCount());

            inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomCount());

            log.info("Successfully confirmed the booking for Booking ID: {}", booking.getId());
        } else {
            log.warn("Unhandled event type: {}", event.getType());
        }

    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with id: "+bookingId)
        );
        User user = getCurrentUser();
        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());

        inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());

        // handle the refund

        try {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with id: " + bookingId));

        User user = getCurrentUser();

        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException(
                    "Booking does not belong to this user with id: " + user.getId());
        }

        return booking.getBookingStatus().name();
    }

    @Override
    public List<BookingDto> getAllBookingsByHotelId(Long hotelId) {

        Hotel hotel= hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        User user= getCurrentUser();

        log.info("Getting all booking for the hotel with ID: {}", hotelId);

        if(user.equals(hotel.getOwner())){
            throw new AccessDeniedException("You are not the owner of the hotel with id:" + hotelId);
        }

       List<Booking> bookings= bookingRepository.findByHotel(hotel);

        return bookings.stream()
                .map((element) -> modelMapper
                .map(element, BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {

        Hotel hotel= hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        User user= getCurrentUser();

        log.info("Generating report for hotel with ID: {}", hotelId);

        if(user.equals(hotel.getOwner())){
            throw new AccessDeniedException("You are not the owner of the hotel with id:" + hotelId);
        }

        LocalDateTime startDateTime= startDate.atStartOfDay();
        LocalDateTime endDateTime= startDate.atTime(LocalTime.MAX);


        List<Booking> bookings= bookingRepository.findByHotelAndCreatedAtBetween(hotel, startDateTime, endDateTime);

        Long totalConfirmedBookings = bookings
                .stream()
                .filter(booking -> booking.getBookingStatus() == BookingStatus.CONFIRMED)
                .count();

        BigDecimal totalRevenueOfConfirmedBookings= bookings.stream()
                .filter(booking -> booking.getBookingStatus() == BookingStatus.CONFIRMED)
                .map(Booking::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal:: add);

        BigDecimal avgRevenue= totalConfirmedBookings==0 ? BigDecimal.ZERO :
                totalRevenueOfConfirmedBookings
                .divide(BigDecimal.valueOf(totalConfirmedBookings), RoundingMode.HALF_UP);



        return new HotelReportDto( totalConfirmedBookings,totalRevenueOfConfirmedBookings, avgRevenue);
    }

    @Override
    public List<BookingDto> getMyBookings() {

        User user= getCurrentUser();


        return bookingRepository.findByUser(user)
                .stream()
                .map((element) -> modelMapper.map(element, BookingDto.class))
                .collect(Collectors.toList());
    }


    public boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(40).isBefore(LocalDateTime.now());
    }


}
