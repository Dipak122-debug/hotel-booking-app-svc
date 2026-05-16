package com.project.hotel_svc.service;

import com.project.hotel_svc.entity.Hotel;
import com.project.hotel_svc.entity.Room;
import com.project.hotel_svc.model.RoomDto;
import com.project.hotel_svc.repository.HotelRepository;
import com.project.hotel_svc.repository.RoomRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {


    private final RoomRepository roomRepository;

    private final HotelRepository hotelRepository;


    @Override
    public void createRoom(RoomDto roomDto) {
        log.info("Creating new room for hotel ID: {}", roomDto.getHotelId());
        try {
            Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                    .orElseThrow(() -> {
                        log.error("Hotel not found with ID: {}", roomDto.getHotelId());
                        return new RuntimeException("Hotel not found with ID: " + roomDto.getHotelId());
                    });

            Room room = new Room();
            room.setHotel(hotel);
            room.setRoomType(roomDto.getRoomType());
            room.setPricePerNight(roomDto.getPricePerNight());
            room.setTotalRooms(roomDto.getTotalRooms());
            room.setCreatedAt(LocalDateTime.now());
            room.setRoomNumber(roomDto.getRoomNumber());

            roomRepository.save(room);
            log.info("Room created successfully for hotel ID: {}", roomDto.getHotelId());
        } catch (Exception e) {
            log.error("Error creating room for hotel ID: {}", roomDto.getHotelId(), e);
            throw e;
        }
    }

    @Override
    public List<Room> getRoomsByHotelId(Long hotelId) {
        log.info("Retrieving rooms for hotel ID: {}", hotelId);
        try {
            List<Room> rooms = roomRepository.findByHotelId(hotelId);
            log.info("Retrieved {} rooms for hotel ID: {}", rooms.size(), hotelId);
            return rooms;
        } catch (Exception e) {
            log.error("Error retrieving rooms for hotel ID: {}", hotelId, e);
            throw e;
        }
    }

}

