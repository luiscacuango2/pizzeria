package com.luigi.pizza.service.mapper;

import com.luigi.pizza.persistence.entity.OrderItemEntity;
import com.luigi.pizza.service.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "idItem", target = "idItem")
    @Mapping(source = "idPizza", target = "idPizza")
    @Mapping(source = "pizza.name", target = "pizzaName") // Navega: entity.getPizza().getName()
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price")
    OrderItemDto toDto(OrderItemEntity orderItemEntity);

    List<OrderItemDto> toDtoList(List<OrderItemEntity> orderItemEntities);

    @Mapping(target = "order", ignore = true) // Evitamos recursividad con Order
    @Mapping(target = "pizza", ignore = true) // La pizza se asocia por ID en la DB
    OrderItemEntity toEntity(OrderItemDto orderItemDto);
}