package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.OrderEntity;
import com.luigi.pizza.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final String DELIVERY = "D";
    private final String CARRYOUT = "C";
    private final String ON_SITE = "S";

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderEntity> getAllOrder() {
        List<OrderEntity> orders = orderRepository.findAll();
        orders.forEach(o -> System.out.println(o.getCustomer().getName()));
        return orders;
    }

    public List<OrderEntity> getTodayOrders() {
        LocalDateTime today = LocalDate.now().atTime(0, 0);
        return this.orderRepository.findAllByOrderDateAfter(today);
    }

    public List<OrderEntity> getOutsideOrders() {
        List<String> deliveryMethods = List.of(DELIVERY, CARRYOUT); // Arrays.asList(DELIVERY, CARRYOUT);
        return this.orderRepository.findAllByDeliveryMethodIn(deliveryMethods);
    }

}
