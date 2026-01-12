package com.luigi.pizza.service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Integer idOrder;
    private LocalDateTime date;
    private Double total;
    private String method;
    private String notes;
    private CustomerDto customer; // Usamos el DTO, no la Entity
    private List<OrderItemDto> items;
}