package com.luigi.pizza.service.dto;

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
public class PizzaDto implements Serializable {

    private Integer idPizza;

    @NotBlank(message = "El nombre de la pizza es obligatorio")
    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 150, message = "La descripción es muy larga")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double price;

    private Boolean vegetarian;
    private Boolean vegan;

    @NotNull(message = "Debe indicar si la pizza está disponible")
    private Boolean available;
}