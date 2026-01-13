package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.OrderEntity;
import com.luigi.pizza.persistence.projection.OrderSummary;
import com.luigi.pizza.persistence.repository.OrderRepository;
import com.luigi.pizza.service.dto.OrderDto;
import com.luigi.pizza.service.dto.RandomOrderDto;
import com.luigi.pizza.service.mapper.OrderMapper;
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
    private final OrderMapper orderMapper;
    private final String DELIVERY = "D";
    private final String CARRYOUT = "C";
    private final String ON_SITE = "S";

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> getAllOrder() {
        return this.orderMapper.toDtoList(this.orderRepository.findAll());
    }

    public List<OrderDto> getTodayOrders() {
        LocalDateTime today = LocalDate.now().atTime(0, 0);
        return this.orderMapper.toDtoList(this.orderRepository.findAllByOrderDateAfter(today));
    }

    public List<OrderDto> getOutsideOrders() {
        List<String> deliveryMethods = List.of(DELIVERY, CARRYOUT); // Arrays.asList(DELIVERY, CARRYOUT);
        return this.orderMapper.toDtoList(this.orderRepository.findAllByDeliveryMethodIn(deliveryMethods));
    }

    @Secured("ROLE_ADMIN")
    public List<OrderDto> getCustomerOrders(Integer idCustomer) {
        List<OrderEntity> orders = this.orderRepository.findCustomerOrders(idCustomer);
        return this.orderMapper.toDtoList(orders);
    }

    public OrderSummary getSummary(Integer idOrder) {
        return this.orderRepository.findSummary(idOrder);
    }

    @Transactional
    public boolean saveRandomOrder(RandomOrderDto randomOrderDto) {
        return this.orderRepository.saveRandomOrder(randomOrderDto.getIdCustomer(), randomOrderDto.getDeliveryMethod());
    }
}
