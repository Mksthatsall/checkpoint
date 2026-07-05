package com.mks.projects.checkpointapp.repository;

import com.mks.projects.checkpointapp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}