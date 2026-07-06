package com.project.booking_svc.controller;

import com.project.booking_svc.dto.ResponseDTO;
import com.project.booking_svc.dto.InventoryDTO;
import com.project.booking_svc.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for managing room inventory and availability")
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * POST /inventory - Create new inventory
     */
    @PostMapping
    @Operation(summary = "Create inventory", description = "Creates a new inventory record for a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        inventoryService.createInventory(inventoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * GET /inventory/{inventoryId} - Get inventory by ID
     */
    @GetMapping("/{inventoryId}")
    @Operation(summary = "Get inventory by ID", description = "Retrieves inventory details using inventory ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<InventoryDTO>> getInventoryById(
            @Parameter(description = "Inventory ID") @PathVariable Long inventoryId) {
        InventoryDTO inventory = inventoryService.getInventoryById(inventoryId);
        ResponseDTO<InventoryDTO> response = ResponseDTO.<InventoryDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Inventory retrieved successfully")
                .data(inventory)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /inventory/room/{roomId}/date/{date} - Get inventory for specific room and date
     */
    @GetMapping("/room/{roomId}/date/{date}")
    @Operation(summary = "Get inventory by room and date", description = "Retrieves inventory for a specific room on a given date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "404", description = "Inventory not found for specified room and date"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<InventoryDTO>> getInventoryByRoomAndDate(
            @Parameter(description = "Room ID") @PathVariable Long roomId,
            @Parameter(description = "Date in format yyyy-MM-dd") @PathVariable @DateTimeFormat(iso = DATE) LocalDate date) {
        InventoryDTO inventory = inventoryService.getInventoryByRoomAndDate(roomId, date);
        ResponseDTO<InventoryDTO> response = ResponseDTO.<InventoryDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Inventory retrieved successfully")
                .data(inventory)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /inventory/room/{roomId} - Get all inventory for a room
     */
    @GetMapping("/room/{roomId}")
    @Operation(summary = "Get inventory by room", description = "Retrieves all inventory records for a specific room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory list retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<List<InventoryDTO>>> getInventoryByRoomId(
            @Parameter(description = "Room ID") @PathVariable Long roomId) {
        List<InventoryDTO> inventories = inventoryService.getInventoryByRoomId(roomId);
        ResponseDTO<List<InventoryDTO>> response = ResponseDTO.<List<InventoryDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Inventory list retrieved successfully")
                .data(inventories)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /inventory/update - Update inventory
     */
    @PutMapping("/update")
    @Operation(summary = "Update inventory", description = "Updates an existing inventory record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<InventoryDTO>> updateInventory(
            @Parameter(description = "Inventory ID") @RequestParam Long inventoryId,
            @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO updatedInventory = inventoryService.updateInventory(inventoryId, inventoryDTO);
        ResponseDTO<InventoryDTO> response = ResponseDTO.<InventoryDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Inventory updated successfully")
                .data(updatedInventory)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /inventory/{inventoryId} - Delete inventory
     */
    @DeleteMapping("/{inventoryId}")
    @Operation(summary = "Delete inventory", description = "Deletes an inventory record from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<String>> deleteInventory(
            @Parameter(description = "Inventory ID") @PathVariable Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        ResponseDTO<String> response = new ResponseDTO<>(
                HttpStatus.OK.value(),
                "Inventory deleted successfully"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /inventory/range - Get inventory for date range
     */
    @GetMapping("/range")
    @Operation(summary = "Get inventory by date range", description = "Retrieves inventory records for a specified date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory list retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range or format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDTO<List<InventoryDTO>>> getInventoryByDateRange(
            @Parameter(description = "Start date in format yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = DATE) LocalDate startDate,
            @Parameter(description = "End date in format yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = DATE) LocalDate endDate) {
        List<InventoryDTO> inventories = inventoryService.getInventoryByDateRange(startDate, endDate);
        ResponseDTO<List<InventoryDTO>> response = ResponseDTO.<List<InventoryDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Inventory list retrieved successfully")
                .data(inventories)
                .build();
        return ResponseEntity.ok(response);
    }
}

