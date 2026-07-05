package com.mks.projects.checkpointapp.service;

import com.mks.projects.checkpointapp.dto.BookingDto;
import com.mks.projects.checkpointapp.dto.BookingRequest;
import com.mks.projects.checkpointapp.dto.GuestDto;
import com.mks.projects.checkpointapp.dto.HotelReportDto;
import com.stripe.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingService {


    BookingDto initializeBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

  String   getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();
}
