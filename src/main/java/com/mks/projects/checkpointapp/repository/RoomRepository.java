package com.mks.projects.checkpointapp.repository;

import com.mks.projects.checkpointapp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
