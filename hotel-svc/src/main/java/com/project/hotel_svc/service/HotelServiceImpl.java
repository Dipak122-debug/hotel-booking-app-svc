package com.project.hotel_svc.service;

import com.project.hotel_svc.entity.Hotel;
import com.project.hotel_svc.model.HotelDto;
import com.project.hotel_svc.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);

    @Autowired
    private HotelRepository hotelRepository;



    @Override
    public void registerHotel(HotelDto hotel) {
        logger.info("Registering new hotel: {}", hotel.getName());
        try {
            Hotel newHotel = new Hotel();
            newHotel.setName(hotel.getName());
            newHotel.setAddress(hotel.getAddress());
            newHotel.setRating(hotel.getRating());
            newHotel.setCity(hotel.getCity());

            hotelRepository.save(newHotel);
            logger.info("Hotel registered successfully: {}", hotel.getName());
        } catch (Exception e) {
            logger.error("Error registering hotel: {}", hotel.getName(), e);
            throw e;
        }
    }

    @Override
    public List<Hotel> getAllHotels() {
        logger.info("Retrieving all hotels");
        try {
            List<Hotel> hotels = hotelRepository.findAll();
            logger.info("Retrieved {} hotels", hotels.size());
            return hotels;
        } catch (Exception e) {
            logger.error("Error retrieving all hotels", e);
            throw e;
        }
    }

    @Override
    public Optional<Hotel> getHotelById(Long id) {
        logger.info("Retrieving hotel by ID: {}", id);
        try {
            Optional<Hotel> hotel = hotelRepository.findById(id);
            if (hotel.isPresent()) {
                logger.info("Hotel found with ID: {}", id);
            } else {
                logger.warn("Hotel not found with ID: {}", id);
            }
            return hotel;
        } catch (Exception e) {
            logger.error("Error retrieving hotel by ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public void updateHotel(Hotel hotel) {
        logger.info("Updating hotel with ID: {}", hotel.getId());
        try {
            hotelRepository.save(hotel);
            logger.info("Hotel updated successfully with ID: {}", hotel.getId());
        } catch (Exception e) {
            logger.error("Error updating hotel with ID: {}", hotel.getId(), e);
            throw e;
        }
    }

    @Override
    public void deleteHotel(Long id) {
        logger.info("Deleting hotel with ID: {}", id);
        try {
            hotelRepository.deleteById(id);
            logger.info("Hotel deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting hotel with ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public List<Hotel> searchHotels(String city, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal minRating, BigDecimal maxRating) {
        return hotelRepository.searchHotels(city, minPrice, maxPrice, minRating, maxRating);
    }
}
