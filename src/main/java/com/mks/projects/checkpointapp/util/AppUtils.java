package com.mks.projects.checkpointapp.util;

import com.mks.projects.checkpointapp.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtils {

    public  static User getCurrentUser() {

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
