package com.project.hotel_svc.controller;

import com.project.hotel_svc.entity.Hotel;
import com.project.hotel_svc.model.HotelDto;
import com.project.hotel_svc.service.HotelService;
import com.project.hotel_svc.util.ModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Hotels", description = "API for managing hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/create")
    @Operation(summary = "Create a new hotel", description = "Registers a new hotel with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotel created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> createHotel(@RequestBody HotelDto hotelDto) {
        hotelService.registerHotel(hotelDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    @Operation(summary = "Get all hotels", description = "Retrieves a list of all hotels.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of hotels retrieved successfully")
    })
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        List<HotelDto> hotels = hotelService.getAllHotels().stream()
                .map(ModelMapper::mapToHotelDto)
                .toList();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/fetchHotel/{id}")
    @Operation(summary = "Get hotel by ID", description = "Retrieves a hotel by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotel retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelService.getHotelById(id);
        return hotel.map(h -> ResponseEntity.ok(ModelMapper.mapToHotelDto(h)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update hotel", description = "Updates an existing hotel with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotel updated successfully"),
        @ApiResponse(responseCode = "404", description = "Hotel not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
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
    @Operation(summary = "Delete hotel", description = "Deletes a hotel by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Hotel deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels", description = "Searches hotels based on city, price range, and rating range.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of matching hotels retrieved successfully")
    })
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
