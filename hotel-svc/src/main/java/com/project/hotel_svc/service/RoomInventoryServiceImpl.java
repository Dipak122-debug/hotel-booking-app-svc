package com.project.hotel_svc.service;

import com.project.hotel_svc.entity.Room;
import com.project.hotel_svc.entity.RoomInventory;
import com.project.hotel_svc.model.RoomInventoryDto;
import com.project.hotel_svc.repository.RoomInventoryRepository;
import com.project.hotel_svc.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing room inventory operations.
 * This class handles the business logic for updating available room inventory
 * for specific dates. It provides an upsert operation that creates new inventory
 * records if they don't exist or updates existing ones.
 *
 * @author Hotel Service Team
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoomInventoryServiceImpl implements RoomInventoryService {

    /**
     * Repository for performing CRUD operations on RoomInventory entities.
     */
    @Autowired
    private RoomInventoryRepository roomInventoryRepository;

    /**
     * Repository for performing CRUD operations on Room entities.
     */
    @Autowired
    private RoomRepository roomRepository;

    /**
     * Updates or creates room inventory for a specific room and date.
     *
     * This method performs an upsert operation:
     * - If inventory for the room and date exists, it updates the available rooms count
     * - If inventory doesn't exist, it creates a new inventory record
     *
     * @param roomInventoryDto Data Transfer Object containing:
     *                         - roomId: The ID of the room to update inventory for
     *                         - availableDate: The date for which inventory is being updated
     *                         - availableRooms: The number of available rooms for that date
     *
     * @throws RuntimeException if the room with the given ID is not found
     * @throws Exception if any other database or processing error occurs
     *
     * @example
     *   RoomInventoryDto dto = new RoomInventoryDto();
     *   dto.setRoomId(1L);
     *   dto.setAvailableDate(LocalDate.of(2026, 4, 26));
     *   dto.setAvailableRooms(5);
     *   service.updateRoomInventory(dto);
     */
    @Override
    public void updateRoomInventory(RoomInventoryDto roomInventoryDto) {
        // Log the start of inventory update operation
        log.info("Updating room inventory for room ID: {} on date: {}",
                roomInventoryDto.getRoomId(), roomInventoryDto.getAvailableDate());
        try {
            // Step 1: Fetch the room by ID, throw exception if not found
            Room room = roomRepository.findById(roomInventoryDto.getRoomId())
                    .orElseThrow(() -> {
                        log.error("Room not found with ID: {}", roomInventoryDto.getRoomId());
                        return new RuntimeException("Room not found with ID: " + roomInventoryDto.getRoomId());
                    });

            // Step 2: Try to find existing inventory record for the room and date
            // If not found, create a new RoomInventory object (upsert pattern)
            RoomInventory roomInventory = roomInventoryRepository
                    .findByRoomIdAndAvailableDate(roomInventoryDto.getRoomId(), roomInventoryDto.getAvailableDate())
                    .orElse(new RoomInventory());

            // Step 3: Set/update the inventory fields
            roomInventory.setRoom(room);
            roomInventory.setAvailableDate(roomInventoryDto.getAvailableDate());
            roomInventory.setAvailableRooms(roomInventoryDto.getAvailableRooms());

            // Step 4: Save the inventory record (insert if new, update if existing)
            roomInventoryRepository.save(roomInventory);

            // Log successful completion of operation
            log.info("Room inventory updated successfully for room ID: {} on date: {}",
                    roomInventoryDto.getRoomId(), roomInventoryDto.getAvailableDate());
        } catch (Exception e) {
            // Log error details for troubleshooting
            log.error("Error updating room inventory for room ID: {}", roomInventoryDto.getRoomId(), e);
            // Re-throw the exception to be handled by the caller
            throw e;
        }
    }

}

