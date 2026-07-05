package com.mks.projects.checkpointapp.dto;


import com.mks.projects.checkpointapp.entity.User;
import lombok.Data;

@Data
public class SignUpRequestDto {

    private String email;
    private String password;
    private String name;


}
