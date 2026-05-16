package com.project.hotel_svc.controller;

import com.project.hotel_svc.entity.Room;
import com.project.hotel_svc.model.RoomDto;
import com.project.hotel_svc.model.RoomInventoryDto;
import com.project.hotel_svc.service.RoomInventoryService;
import com.project.hotel_svc.service.RoomService;
import com.project.hotel_svc.util.ModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@Tag(name = "Rooms", description = "API for managing hotel rooms and room inventory")
@RequiredArgsConstructor
public class RoomController {


    private RoomService roomService;

    private RoomInventoryService roomInventoryService;

    @Autowired
    public RoomController(RoomService roomService, RoomInventoryService roomInventoryService) {
        this.roomService = roomService;
        this.roomInventoryService = roomInventoryService;
    }

    @PostMapping
    @Operation(summary = "Create a new room", description = "Creates a new room for a hotel with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<Void> createRoom(@RequestBody RoomDto roomDto) {
        roomService.createRoom(roomDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{hotelId}")
    @Operation(summary = "Get rooms by hotel ID", description = "Retrieves a list of all rooms for a specific hotel.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of rooms retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<List<RoomDto>> getRoomsByHotelId(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.getRoomsByHotelId(hotelId);
        List<RoomDto> roomDtos = rooms.stream()
                .map(ModelMapper::mapToRoomDto)
                .toList();
        return ResponseEntity.ok(roomDtos);
    }

    @PutMapping("/inventory/update")
    @Operation(summary = "Update room inventory", description = "Updates the available rooms inventory for a specific date.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<Void> updateRoomInventory(@RequestBody RoomInventoryDto roomInventoryDto) {
        roomInventoryService.updateRoomInventory(roomInventoryDto);
        return ResponseEntity.ok().build();
    }

}

