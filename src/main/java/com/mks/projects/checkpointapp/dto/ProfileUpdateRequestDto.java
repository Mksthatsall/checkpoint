package com.mks.projects.checkpointapp.dto;


import com.mks.projects.checkpointapp.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {

    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;

}
