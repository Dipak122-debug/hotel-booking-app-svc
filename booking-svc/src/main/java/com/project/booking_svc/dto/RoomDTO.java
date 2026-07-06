package com.project.booking_svc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long hotelId;
    private String roomNumber;
    private String roomType;
    private Integer capacity;
    private Double price;
    private String status;
   // private String description;
}

