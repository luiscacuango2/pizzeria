package com.luigi.pizza.service.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponseDto {
    private String message;
    private int code;
    private String traceId;
    private LocalDateTime timestamp;
    private Map<String, String> details; // Útil para errores de validación (campo: error)
}