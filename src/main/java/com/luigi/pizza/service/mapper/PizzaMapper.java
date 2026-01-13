package com.luigi.pizza.service.mapper;

import com.luigi.pizza.persistence.entity.PizzaEntity;
import com.luigi.pizza.service.dto.PizzaDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PizzaMapper {
    // MapStruct mapeará automáticamente idPizza, name, description, price, vegetarian, vegan y available
    PizzaDto toDto(PizzaEntity pizzaEntity);

    List<PizzaDto> toDtoList(List<PizzaEntity> pizzas);

    PizzaEntity toEntity(PizzaDto pizzaDto);
}