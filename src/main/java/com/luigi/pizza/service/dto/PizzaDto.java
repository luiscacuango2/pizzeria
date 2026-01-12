package com.luigi.pizza.service.dto;

import lombok.Data;

@Data
public class PizzaDto {
    private Integer idPizza;
    private String name;
    private String description;
    private Double price;
    private Boolean vegetarian;
    private Boolean available;
}