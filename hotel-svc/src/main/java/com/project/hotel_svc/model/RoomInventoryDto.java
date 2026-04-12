package com.project.hotel_svc.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RoomInventoryDto {
    private Long roomId;
    private LocalDate availableDate;
    private Integer availableRooms;
}
