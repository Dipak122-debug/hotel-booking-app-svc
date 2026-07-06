package com.project.booking_svc.service;

import com.project.booking_svc.dto.InventoryDTO;
import com.project.booking_svc.exception.ResourceNotFoundException;
import com.project.booking_svc.model.Inventory;
import com.project.booking_svc.repository.InventoryRepository;
import com.project.booking_svc.util.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    /**
     * Create new inventory
     */
    public void createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        inventoryRepository.save(inventory);
    }

    /**
     * Get inventory by ID
     */
    public InventoryDTO getInventoryById(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + inventoryId));
        return inventoryMapper.toDTO(inventory);
    }

    /**
     * Get inventory by room ID and date
     */
    public InventoryDTO getInventoryByRoomAndDate(Long roomId, LocalDate date) {
        Inventory inventory = inventoryRepository.findByRoomIdAndDate(roomId, date)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for room ID: " + roomId + " and date: " + date));
        return inventoryMapper.toDTO(inventory);
    }

    /**
     * Get all inventory for a room
     */
    public List<InventoryDTO> getInventoryByRoomId(Long roomId) {
        List<Inventory> inventories = inventoryRepository.findByRoomId(roomId);
        return inventories.stream()
                .map(inventoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update inventory
     */
    public InventoryDTO updateInventory(Long inventoryId, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + inventoryId));

        inventory.setRoomId(inventoryDTO.getRoomId());
        inventory.setDate(inventoryDTO.getDate());
        inventory.setAvailableCount(inventoryDTO.getAvailableCount());
        inventory.setTotalCount(inventoryDTO.getTotalCount());

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.toDTO(updatedInventory);
    }

    /**
     * Delete inventory
     */
    public void deleteInventory(Long inventoryId) {
        if (!inventoryRepository.existsById(inventoryId)) {
            throw new ResourceNotFoundException("Inventory not found with ID: " + inventoryId);
        }
        inventoryRepository.deleteById(inventoryId);
    }

    /**
     * Get inventory for date range
     */
    public List<InventoryDTO> getInventoryByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Inventory> inventories = inventoryRepository.findByDateBetween(startDate, endDate);
        return inventories.stream()
                .map(inventoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}

