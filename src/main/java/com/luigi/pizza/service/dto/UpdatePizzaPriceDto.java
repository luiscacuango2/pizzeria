package com.luigi.pizza.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdatePizzaPriceDto {
    @NotNull
    private Integer pizzaId;

    @NotNull
    @Positive(message = "El nuevo precio debe ser mayor a cero") // Validamos precios negativos
    private Double newPrice;
}
