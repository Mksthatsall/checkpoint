package com.mks.projects.checkpointapp.service;

import com.mks.projects.checkpointapp.dto.ProfileUpdateRequestDto;
import com.mks.projects.checkpointapp.dto.UserDto;
import com.mks.projects.checkpointapp.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {


    User getUserById(Long id);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
