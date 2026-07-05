package com.mks.projects.checkpointapp.service;


import com.mks.projects.checkpointapp.dto.HotelDto;
import com.mks.projects.checkpointapp.dto.HotelInfoDto;
import com.mks.projects.checkpointapp.dto.RoomDto;
import com.mks.projects.checkpointapp.entity.Hotel;
import com.mks.projects.checkpointapp.entity.Room;
import com.mks.projects.checkpointapp.entity.User;
import com.mks.projects.checkpointapp.exception.ResourceNotFoundException;
import com.mks.projects.checkpointapp.exception.UnAuthorisedException;
import com.mks.projects.checkpointapp.repository.HotelRepository;
import com.mks.projects.checkpointapp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.mks.projects.checkpointapp.util.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;
    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}", hotelDto.getName());
        Hotel hotel= modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel= hotelRepository.save(hotel);
        log.info("Creating a new hotel with ID: {}", hotelDto.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting the hotel with ID: {}", id);
        Hotel hotel=hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID"+id));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+ id);
        }



        return modelMapper.map(hotel, HotelDto.class);


    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating the hotel with ID: {}", id);
        Hotel hotel=hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID"+id));


        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+ id);
        }

         modelMapper.map(hotelDto, hotel);
         hotel.setId(id);

         hotelRepository.save(hotel);

         return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void  deleteHotelById(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID" + id));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+ id);
        }


        for (Room room : hotel.getRooms()) {
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());

            //delete the inventories for this hotel
        }
        hotelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Updating the hotel with ID: {}", hotelId);
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID"+hotelId));
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+ hotelId);
        }
       hotel.setActive(true);

       //TODO create invenotry for all the rooms for this hotel

        //assuming only do it once
        for(Room room : hotel.getRooms()){
            inventoryService.intializeRoomForAYear(room);
        }

    }

    //public method
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID"+hotelId));

        List<RoomDto> rooms= hotel.getRooms().
                stream()
                .map((element) -> modelMapper
                        .map(element, RoomDto.class)).collect(Collectors
                        .toList());


        return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class), rooms);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        User user= getCurrentUser();
        log.info("Getting all hotels for the admin user with id:{}", user.getId());
        List<Hotel> hotel= hotelRepository.findByOwner(user);

        return hotel
                .stream()
                .map((element) ->
                        modelMapper
                                .map(element, HotelDto.class)).collect(Collectors.toList());

    }
}

