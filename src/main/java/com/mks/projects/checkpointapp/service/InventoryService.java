package com.mks.projects.checkpointapp.service;

import com.mks.projects.checkpointapp.dto.*;
import com.mks.projects.checkpointapp.entity.Room;
import com.mks.projects.checkpointapp.repository.InventoryRepository;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {


    void intializeRoomForAYear(Room room);


    void deleteFutureInventories(Room room);

    void deleteAllInventories(Room room);


       Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) ;

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
