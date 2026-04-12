package com.project.hotel_svc.util;

import com.project.hotel_svc.entity.Hotel;
import com.project.hotel_svc.model.HotelDto;

public class ModelMapper{

    public static HotelDto mapToHotelDto(Hotel hotel) {
        HotelDto dto = new HotelDto();
        dto.setName(hotel.getName());
        dto.setCity(hotel.getCity());
        dto.setAddress(hotel.getAddress());
        dto.setRating(hotel.getRating());
        return dto;
    }

}
