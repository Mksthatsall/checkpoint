package com.mks.projects.checkpointapp.dto;

import com.mks.projects.checkpointapp.entity.Guest;
import com.mks.projects.checkpointapp.entity.Hotel;
import com.mks.projects.checkpointapp.entity.Room;
import com.mks.projects.checkpointapp.entity.User;
import com.mks.projects.checkpointapp.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Data
public class BookingDto {

    private Long Id;


    private Integer roomCount;


    private LocalDate checkInDate;


    private LocalDate checkOutDate;


    private LocalDateTime createdAt;


    private LocalDate updatedAt;



    private BookingStatus bookingStatus;


    private Set<GuestDto> guests;

}
