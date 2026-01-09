package com.luigi.pizza.web.controller;

import com.luigi.pizza.persistence.entity.OrderEntity;
import com.luigi.pizza.persistence.projection.OrderSummary;
import com.luigi.pizza.service.OrderService;
import com.luigi.pizza.service.dto.RandomOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return ResponseEntity.ok(this.orderService.getAllOrder());
    }

    @GetMapping("/today")
    public ResponseEntity<List<OrderEntity>> getTodayOrders() {
        return ResponseEntity.ok(this.orderService.getTodayOrders());
    }

    @GetMapping("/outside")
    public ResponseEntity<List<OrderEntity>> getOutsideOrders() {
        return ResponseEntity.ok(this.orderService.getOutsideOrders());
    }

    @GetMapping("/customer/{idCustomer}")
    public ResponseEntity<List<OrderEntity>> getCustomerOrders(@PathVariable Integer idCustomer) {
        return ResponseEntity.ok(this.orderService.getCustomerOrders(idCustomer));
    }

    @GetMapping("/summary/{idOrder}")
    public ResponseEntity<OrderSummary> getSummary(@PathVariable Integer idOrder) {
        return ResponseEntity.ok(this.orderService.getSummary(idOrder));
    }

    @PostMapping("/random")
    public ResponseEntity<Boolean> saveRandomOrder(@RequestBody RandomOrderDto dto) {
        return ResponseEntity.ok(this.orderService.saveRandomOrder(dto));
    }
}
