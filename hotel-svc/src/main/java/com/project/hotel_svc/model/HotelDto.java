package com.project.hotel_svc.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class HotelDto {
    private String name;
    private String city;
    private String address;
    private BigDecimal rating;
}
