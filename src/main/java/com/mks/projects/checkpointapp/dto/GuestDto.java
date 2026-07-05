package com.mks.projects.checkpointapp.dto;

import com.mks.projects.checkpointapp.entity.User;
import com.mks.projects.checkpointapp.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;


@Data
public class GuestDto {


    private Long Id;


    private User user;


    private String name;


    private Gender gender;


    private Integer age;
}
