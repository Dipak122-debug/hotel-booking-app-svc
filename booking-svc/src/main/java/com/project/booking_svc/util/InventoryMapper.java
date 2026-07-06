package com.project.booking_svc.util;

import com.project.booking_svc.dto.InventoryDTO;
import com.project.booking_svc.model.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryDTO toDTO(Inventory inventory) {
        if (inventory == null) {
            return null;
        }

        return InventoryDTO.builder()
                .id(inventory.getId())
                .roomId(inventory.getRoomId())
                .date(inventory.getDate())
                .availableCount(inventory.getAvailableCount())
                .totalCount(inventory.getTotalCount())
                .build();
    }

    public Inventory toEntity(InventoryDTO inventoryDTO) {
        if (inventoryDTO == null) {
            return null;
        }

        return Inventory.builder()
                .id(inventoryDTO.getId())
                .roomId(inventoryDTO.getRoomId())
                .date(inventoryDTO.getDate())
                .availableCount(inventoryDTO.getAvailableCount())
                .totalCount(inventoryDTO.getTotalCount())
                .build();
    }
}

