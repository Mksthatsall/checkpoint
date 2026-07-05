package com.mks.projects.checkpointapp.repository;

import com.mks.projects.checkpointapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

}
