package com.luigi.pizza.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Record inmutable para solicitudes de órdenes aleatorias.
 * Al ser un record, rompemos el flujo de "Tainted Data".
 */
public record RandomOrderDto(
        @NotNull(message = "El ID del cliente es obligatorio")
        Integer idCustomer,

        @NotBlank(message = "El método de entrega es obligatorio")
        @Pattern(regexp = "^(D|E|S)$", message = "El método debe ser D (Delivery), E (Eat in) o S (Soft drink/Other)")
        String deliveryMethod,

        String note // Campo opcional para instrucciones especiales, será sanitizado
) {}