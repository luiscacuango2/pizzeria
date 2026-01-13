package com.luigi.pizza.web.controller;

import com.luigi.pizza.service.CustomerService;
import com.luigi.pizza.service.OrderService;
import com.luigi.pizza.service.dto.CustomerDto; // Importar DTO
import com.luigi.pizza.service.dto.OrderDto;    // Importar DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;

    @Autowired
    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDto> getByPhone(@PathVariable String phone) {
        // El servicio ya devuelve un DTO
        return ResponseEntity.ok(this.customerService.findByPhone(phone));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable String id) {
        // El servicio de órdenes debe ser actualizado para devolver List<OrderDto>
        return ResponseEntity.ok(this.orderService.getCustomerOrders(Integer.parseInt(id)));
    }
}