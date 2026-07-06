package com.project.booking_svc.service;

import com.project.booking_svc.dto.RoomDTO;
import com.project.booking_svc.exception.ResourceNotFoundException;
import com.project.booking_svc.model.Room;
import com.project.booking_svc.repository.RoomRepository;
import com.project.booking_svc.util.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    /**
     * Create a new room
     */
    public void createRoom(RoomDTO roomDTO) {
        Room room = roomMapper.toEntity(roomDTO);
        roomRepository.save(room);
    }

    /**
     * Get all rooms by hotel ID
     */
    public List<RoomDTO> getRoomsByHotelId(Long hotelId) {
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        return rooms.stream()
                .map(roomMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get room by ID
     */
    public RoomDTO getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        return roomMapper.toDTO(room);
    }

    /**
     * Update room
     *
     * @return
     */
    public RoomDTO updateRoom(Long roomId, RoomDTO roomDTO) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        room.setId(roomId);
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setRoomType(roomDTO.getRoomType());
        room.setCapacity(roomDTO.getCapacity());
        room.setPrice(roomDTO.getPrice());
        room.setStatus(roomDTO.getStatus());
        //room.setDescription(roomDTO.getDescription());

        roomRepository.save(room);
        return roomDTO;
    }

    /**
     * Delete room
     */
    public void deleteRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException("Room not found with ID: " + roomId);
        }
        roomRepository.deleteById(roomId);
    }

    /**
     * Get all rooms by status
     */
    public List<RoomDTO> getRoomsByStatus(String status) {
        List<Room> rooms = roomRepository.findByStatus(status);
        return rooms.stream()
                .map(roomMapper::toDTO)
                .collect(Collectors.toList());
    }
}

