package com.mks.projects.checkpointapp.controller;

import com.mks.projects.checkpointapp.dto.HotelDto;
import com.mks.projects.checkpointapp.dto.HotelInfoDto;
import com.mks.projects.checkpointapp.dto.HotelPriceDto;
import com.mks.projects.checkpointapp.dto.HotelSearchRequest;
import com.mks.projects.checkpointapp.service.HotelService;
import com.mks.projects.checkpointapp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotel(@RequestBody HotelSearchRequest hotelSearchRequest){
        Page<HotelPriceDto> page= inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}
