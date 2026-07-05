package com.mks.projects.checkpointapp.controller;


import com.mks.projects.checkpointapp.dto.BookingDto;
import com.mks.projects.checkpointapp.dto.BookingRequest;
import com.mks.projects.checkpointapp.dto.GuestDto;
import com.mks.projects.checkpointapp.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> intializeBooking(@RequestBody BookingRequest bookingRequest){

        return ResponseEntity.ok(bookingService.initializeBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId
            , @RequestBody List<GuestDto> guestDtoList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guestDtoList));
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String, String>> initiatePayment(
            @PathVariable Long bookingId) {

        String sessionUrl = bookingService.initiatePayments(bookingId);

        return ResponseEntity.ok(Map.of("sessionUrl", sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long bookingId) {

         bookingService.cancelBooking(bookingId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookingId}/status")
    public ResponseEntity<Map<String, String>> getBookingStatus(
            @PathVariable Long bookingId) {



        return ResponseEntity.ok(Map.of("status", bookingService.getBookingStatus(bookingId)));
    }



}
