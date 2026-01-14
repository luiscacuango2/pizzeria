package com.luigi.pizza.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO para la actualización masiva o individual de precios")
public class UpdatePizzaPriceDto {
    @NotNull
    @Schema(description = "ID de la pizza a modificar", example = "5")
    private Integer pizzaId;

    @NotNull
    @Positive(message = "El nuevo precio debe ser mayor a cero") // Validamos precios negativos
    @Schema(description = "Nuevo precio que se registrará en la auditoría", example = "18.99")
    private Double newPrice;
}
