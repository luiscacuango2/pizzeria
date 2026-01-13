package com.luigi.pizza.service.mapper;

import com.luigi.pizza.persistence.entity.OrderEntity;
import com.luigi.pizza.service.dto.OrderDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, OrderItemMapper.class})
public interface OrderMapper {

    // Convierte una orden individual
    OrderDto toDto(OrderEntity orderEntity);

    // Convierte la lista de órdenes (ideal para getTodayOrders, getOutsideOrders, etc.)
    List<OrderDto> toDtoList(List<OrderEntity> orderEntities);

    OrderEntity toEntity(OrderDto orderDto);
}