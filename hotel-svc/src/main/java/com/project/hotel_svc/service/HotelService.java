package com.project.hotel_svc.service;

import com.project.hotel_svc.entity.Hotel;
import com.project.hotel_svc.model.HotelDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HotelService {

    void registerHotel(HotelDto hotelDto);

    List<Hotel> getAllHotels();

    Optional<Hotel> getHotelById(Long id);

    void updateHotel(Hotel hotel);

    void deleteHotel(Long id);

    List<Hotel> searchHotels(String city, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal minRating, BigDecimal maxRating);
}
