package com.mar.solutions.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseDTO {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String details;
}
