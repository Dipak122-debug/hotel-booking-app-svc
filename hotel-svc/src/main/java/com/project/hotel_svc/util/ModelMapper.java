package com.project.hotel_svc.util;

import com.project.hotel_svc.entity.Hotel;
import com.project.hotel_svc.entity.Room;
import com.project.hotel_svc.model.HotelDto;
import com.project.hotel_svc.model.RoomDto;

public class ModelMapper{

    public static HotelDto mapToHotelDto(Hotel hotel) {
        HotelDto dto = new HotelDto();
        dto.setName(hotel.getName());
        dto.setCity(hotel.getCity());
        dto.setAddress(hotel.getAddress());
        dto.setRating(hotel.getRating());
        return dto;
    }

    public static RoomDto mapToRoomDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setHotelId(room.getHotel().getId());
        dto.setRoomType(room.getRoomType());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setTotalRooms(room.getTotalRooms());
        return dto;
    }

}
