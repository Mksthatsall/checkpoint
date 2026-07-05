package com.mks.projects.checkpointapp.dto;


import com.mks.projects.checkpointapp.entity.HotelContactInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class HotelDto {

    private Long Id;


    private String name;


    private String city;


    private String[] photos;


    private String[] amenities;


    @Embedded
    private HotelContactInfo contactInfo;


    @Column(nullable=false)
    private boolean active;
}
