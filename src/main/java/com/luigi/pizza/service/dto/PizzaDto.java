package com.luigi.pizza.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que representa una Pizza en el catálogo")
public class PizzaDto implements Serializable {

    @Schema(description = "ID único de la pizza. Dejar nulo para creaciones nuevas.", example = "1")
    private Integer idPizza;

    @NotBlank(message = "El nombre de la pizza es obligatorio")
    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    @Schema(description = "Nombre comercial de la pizza", example = "Pepperoni Especial", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 150, message = "La descripción es muy larga")
    @Schema(description = "Detalle de ingredientes y preparación", example = "Mozzarella, pepperoni y masa artesanal")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @Schema(description = "Precio de venta al público", example = "15.50", minimum = "0.01")
    private Double price;

    @Schema(description = "Indica si la pizza es apta para vegetarianos", example = "false")
    private Boolean vegetarian;

    @Schema(description = "Indica si la pizza contiene ingredientes de origen animal", example = "false")
    private Boolean vegan;

    @NotNull(message = "Debe indicar si la pizza está disponible")
    @Schema(description = "Estado de disponibilidad en el menú", example = "true")
    private Boolean available;
}