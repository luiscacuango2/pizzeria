package com.luigi.pizza.persistence.repository;

import com.luigi.pizza.persistence.entity.OrderEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends ListCrudRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByOrderDateAfter(LocalDateTime date);
    List<OrderEntity> findAllByDeliveryMethodIn(List<String> deliveryMethods);
}
