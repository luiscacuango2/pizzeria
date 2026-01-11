package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.OrderEntity;
import com.luigi.pizza.persistence.projection.OrderSummary;
import com.luigi.pizza.persistence.repository.OrderRepository;
import com.luigi.pizza.service.dto.RandomOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Secured("ROLE_ADMIN")
    public List<OrderEntity> getCustomerOrders(Integer idCustomer) {
        return this.orderRepository.findCustomerOrders(idCustomer);
    }

    public OrderSummary getSummary(Integer idOrder) {
        return this.orderRepository.findSummary(idOrder);
    }

    @Transactional
    public boolean saveRandomOrder(RandomOrderDto randomOrderDto) {
        return this.orderRepository.saveRandomOrder(randomOrderDto.getIdCustomer(), randomOrderDto.getDeliveryMethod());
    }
}
