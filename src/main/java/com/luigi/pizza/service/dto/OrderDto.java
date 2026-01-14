package com.luigi.pizza.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Integer idOrder;
    private LocalDateTime date;
    private Double total;

    @Schema(description = "Método de entrega: (D) Delivery, (S) Sit-in, (C) Carry-out", example = "D")
    private String method;
    private String notes;
    private CustomerDto customer; // Usamos el DTO, no la Entity
    private List<OrderItemDto> items;
}