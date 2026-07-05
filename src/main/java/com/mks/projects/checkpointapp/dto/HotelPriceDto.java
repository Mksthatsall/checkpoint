package com.mks.projects.checkpointapp.dto;

import com.mks.projects.checkpointapp.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto {

    private Hotel hotel;
    private Double price;


}
