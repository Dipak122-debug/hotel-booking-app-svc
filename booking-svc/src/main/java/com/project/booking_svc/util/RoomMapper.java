package com.project.booking_svc.util;

import com.project.booking_svc.dto.RoomDTO;
import com.project.booking_svc.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomDTO toDTO(Room room) {
        if (room == null) {
            return null;
        }

        return RoomDTO.builder()
                .hotelId(room.getHotelId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .price(room.getPrice())
                .status(room.getStatus())
                .build();
    }

    public Room toEntity(RoomDTO roomDTO) {
        if (roomDTO == null) {
            return null;
        }

        return Room.builder()
                .hotelId(roomDTO.getHotelId())
                .roomNumber(roomDTO.getRoomNumber())
                .roomType(roomDTO.getRoomType())
                .capacity(roomDTO.getCapacity())
                .price(roomDTO.getPrice())
                .status(roomDTO.getStatus())
                .build();
    }
}

