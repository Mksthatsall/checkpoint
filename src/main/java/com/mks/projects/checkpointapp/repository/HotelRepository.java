package com.mks.projects.checkpointapp.repository;

import com.mks.projects.checkpointapp.entity.Hotel;
import com.mks.projects.checkpointapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {


    List<Hotel> findByOwner(User user);
}
