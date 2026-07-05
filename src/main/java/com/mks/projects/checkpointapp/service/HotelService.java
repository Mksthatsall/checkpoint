package com.mks.projects.checkpointapp.service;

import com.mks.projects.checkpointapp.dto.HotelDto;
import com.mks.projects.checkpointapp.dto.HotelInfoDto;
import com.mks.projects.checkpointapp.entity.Hotel;

import java.util.List;

public interface HotelService {


    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long Id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

   void deleteHotelById(Long id);

   void activateHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);

    List<HotelDto> getAllHotels();
}
