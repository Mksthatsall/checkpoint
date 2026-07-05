package com.mks.projects.checkpointapp.controller;


import com.mks.projects.checkpointapp.dto.BookingDto;
import com.mks.projects.checkpointapp.dto.ProfileUpdateRequestDto;
import com.mks.projects.checkpointapp.dto.UserDto;
import com.mks.projects.checkpointapp.entity.User;
import com.mks.projects.checkpointapp.service.BookingService;
import com.mks.projects.checkpointapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;


    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequestDto profileUpdateRequestDto){

        userService.updateProfile(profileUpdateRequestDto);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingDto>> getMyBooking() {

        return ResponseEntity.ok(bookingService.getMyBookings());
    }


    @GetMapping("/profile")
    public ResponseEntity<UserDto> getMyProfile() {

        return ResponseEntity.ok(userService.getMyProfile());
    }


}
