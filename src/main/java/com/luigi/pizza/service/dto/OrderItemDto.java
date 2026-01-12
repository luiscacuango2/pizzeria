package com.luigi.pizza.service.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Integer idItem;      // El ID de la línea de detalle
    private Integer idPizza;     // Solo el ID de la pizza para no traer todo el objeto
    private String pizzaName;    // Útil para que el cliente sepa qué pidió sin otra consulta
    private Double quantity;     // Cantidad de pizzas de este tipo
    private Double price;        // El precio al que se vendió (importante por si el menú cambia luego)
}