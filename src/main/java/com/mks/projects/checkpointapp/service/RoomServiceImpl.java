package com.mks.projects.checkpointapp.service;

import com.mks.projects.checkpointapp.dto.RoomDto;
import com.mks.projects.checkpointapp.entity.Hotel;
import com.mks.projects.checkpointapp.entity.Room;
import com.mks.projects.checkpointapp.entity.User;
import com.mks.projects.checkpointapp.exception.ResourceNotFoundException;
import com.mks.projects.checkpointapp.exception.UnAuthorisedException;
import com.mks.projects.checkpointapp.repository.HotelRepository;
import com.mks.projects.checkpointapp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.mks.projects.checkpointapp.util.AppUtils.getCurrentUser;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
     log.info("Creating a room with in hotel with Id {}", hotelId);
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID"+hotelId));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+ hotelId);
        }


        Room room = modelMapper.map(roomDto, Room.class);
     room.setHotel(hotel);

     room=roomRepository.save(room);

        if(hotel.isActive()){
            inventoryService.intializeRoomForAYear(room);
        }

     return modelMapper.map(room, RoomDto.class);

     //TODO create Inventory as soon as room is created and if hotel is active



    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms with in hotel with Id {}", hotelId);
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID"+hotelId));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+ hotelId);
        }

        return hotel.getRooms().stream()
                .map((element) -> modelMapper
                        .map(element, RoomDto.class)).collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting the  room with  Id {}", roomId);
        Room room=roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID" +roomId));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public void deleteRoomById(Long roomId) {

        log.info("Deleting the  room with  Id {}", roomId);


        Room room=roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID" +roomId));


        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+ roomId);
        }


        //TODO: delete all future inventory for this room
        inventoryService.deleteFutureInventories(room);
        roomRepository.deleteById(roomId);


    }

    @Override
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        log.info("Updating the room with ID: {}", roomId);
        Hotel hotel=hotelRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("No hotel is there with ID"+roomId));


        User user= getCurrentUser();

        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+ roomId);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("No room is there with ID"+roomId));


        modelMapper.map(roomDto, room);

        room.setId(roomId);


        // TODO: if price or inventory is updated, then update the inventory for this room
        room = roomRepository.save(room);

        return modelMapper.map(room, RoomDto.class);
    }
}
