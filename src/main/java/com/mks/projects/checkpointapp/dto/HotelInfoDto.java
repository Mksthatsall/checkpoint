package com.mks.projects.checkpointapp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelInfoDto {
    private HotelDto hotelDto;
    private List<RoomDto> rooms;

}
