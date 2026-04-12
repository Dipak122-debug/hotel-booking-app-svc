package com.project.hotel_svc.controller;

import com.project.hotel_svc.entity.Hotel;
import com.project.hotel_svc.model.HotelDto;
import com.project.hotel_svc.service.HotelService;
import com.project.hotel_svc.util.ModelMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/create")
    public ResponseEntity<Void> createHotel(@RequestBody HotelDto hotelDto) {
        hotelService.registerHotel(hotelDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        List<HotelDto> hotels = hotelService.getAllHotels().stream()
                .map(ModelMapper::mapToHotelDto)
                .toList();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/fetchHotel/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelService.getHotelById(id);
        return hotel.map(h -> ResponseEntity.ok(ModelMapper.mapToHotelDto(h)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateHotel(@PathVariable Long id, @RequestBody HotelDto hotelResponseDto) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(hotelResponseDto.getName());
        hotel.setCity(hotelResponseDto.getCity());
        hotel.setAddress(hotelResponseDto.getAddress());
        hotel.setRating(hotelResponseDto.getRating());
        hotelService.updateHotel(hotel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelDto>> searchHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) BigDecimal maxRating) {
        List<HotelDto> hotels = hotelService.searchHotels(city, minPrice, maxPrice, minRating, maxRating)
                .stream().map(ModelMapper::mapToHotelDto).toList();
        return ResponseEntity.ok(hotels);
    }
}
