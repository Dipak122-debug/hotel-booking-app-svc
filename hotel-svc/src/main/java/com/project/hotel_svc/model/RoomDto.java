package com.project.hotel_svc.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomDto {
    private Long hotelId;
    private String roomNumber;
    private String roomType;
    private BigDecimal pricePerNight;
    private Integer totalRooms;
}
