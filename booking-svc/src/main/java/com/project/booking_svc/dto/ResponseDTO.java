package com.project.booking_svc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    private int status;
    private String message;
    private T data;

    public ResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }
}

