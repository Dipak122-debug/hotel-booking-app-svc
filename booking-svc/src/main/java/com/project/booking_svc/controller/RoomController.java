package com.project.booking_svc.controller;

import com.project.booking_svc.dto.ResponseDTO;
import com.project.booking_svc.dto.RoomDTO;
import com.project.booking_svc.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Room Management", description = "APIs for managing hotel rooms")
public class RoomController {

    private final RoomService roomService;

    /**
     * POST /rooms - Create a new room
     */
    @PostMapping
    @Operation(summary = "Create a new room", description = "Creates a new room with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> createRoom(@RequestBody RoomDTO roomDTO) {
        roomService.createRoom(roomDTO);
        // Return 201 CREATED with no body
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * GET /rooms/{hotelId} - Get all rooms for a hotel
     */
    @GetMapping("/{hotelId}")
    @Operation(summary = "Get rooms by hotel", description = "Retrieves all rooms for a specific hotel using hotel ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Hotel not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<List<RoomDTO>>> getRoomsByHotel(
            @Parameter(description = "Hotel ID") @PathVariable Long hotelId) {
        List<RoomDTO> rooms = roomService.getRoomsByHotelId(hotelId);
        ResponseDTO<List<RoomDTO>> response = ResponseDTO.<List<RoomDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Rooms retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /rooms/room/{roomId} - Get room by ID
     */
    @GetMapping("/room/{roomId}")
    @Operation(summary = "Get room by ID", description = "Retrieves a specific room details using room ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<RoomDTO>> getRoomById(
            @Parameter(description = "Room ID") @PathVariable Long roomId) {
        RoomDTO room = roomService.getRoomById(roomId);
        ResponseDTO<RoomDTO> response = ResponseDTO.<RoomDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Room retrieved successfully")
                .data(room)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /rooms/{roomId} - Update a room
     */
    @PutMapping("/{roomId}")
    @Operation(summary = "Update room", description = "Updates an existing room with new details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<RoomDTO>> updateRoom(
            @Parameter(description = "Room ID") @PathVariable Long roomId,
            @RequestBody RoomDTO roomDTO) {
            roomService.updateRoom(roomId, roomDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /rooms/{roomId} - Delete a room
     */
    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete room", description = "Deletes a room from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<String>> deleteRoom(
            @Parameter(description = "Room ID") @PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        ResponseDTO<String> response = new ResponseDTO<>(
                HttpStatus.OK.value(),
                "Room deleted successfully"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /rooms/status/{status} - Get rooms by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get rooms by status", description = "Retrieves all rooms with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<List<RoomDTO>>> getRoomsByStatus(
            @Parameter(description = "Room status (e.g., AVAILABLE, OCCUPIED, MAINTENANCE)") @PathVariable String status) {
        List<RoomDTO> rooms = roomService.getRoomsByStatus(status);
        ResponseDTO<List<RoomDTO>> response = ResponseDTO.<List<RoomDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Rooms retrieved successfully")
                .data(rooms)
                .build();
        return ResponseEntity.ok(response);
    }
}

