package com.luigi.pizza.web.controller;

import com.luigi.pizza.persistence.projection.OrderSummary;
import com.luigi.pizza.service.OrderService;
import com.luigi.pizza.service.dto.OrderDto;
import com.luigi.pizza.service.dto.RandomOrderDto;
import com.luigi.pizza.web.util.SanitizerUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
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

    // 1. Obtener todas las órdenes con Paginación (Evita Warning de PageImpl)
    @GetMapping
    public ResponseEntity<PagedModel<OrderDto>> getAllOrders(Pageable pageable) {
        Page<OrderDto> orders = this.orderService.getAllOrder(pageable);
        return ResponseEntity.ok(new PagedModel<>(orders));
    }

    // 2. Órdenes de hoy (Normalmente son pocas, se puede mantener Lista o Paginado)
    @GetMapping("/today")
    public ResponseEntity<List<OrderDto>> getTodayOrders() {
        return ResponseEntity.ok(this.orderService.getTodayOrders());
    }

    @GetMapping("/outside")
    public ResponseEntity<List<OrderDto>> getOutsideOrders() {
        return ResponseEntity.ok(this.orderService.getOutsideOrders());
    }

    // 3. Órdenes por cliente (Paginado para manejar clientes frecuentes)
    @GetMapping("/customer/{idCustomer}")
    public ResponseEntity<PagedModel<OrderDto>> getCustomerOrders(
            @PathVariable Integer idCustomer,
            @PageableDefault(size = 5, sort = "orderDate") Pageable pageable) {

        Page<OrderDto> orders = this.orderService.getCustomerOrders(idCustomer, pageable);
        return ResponseEntity.ok(new PagedModel<>(orders));
    }

    @GetMapping("/summary/{idOrder}")
    public ResponseEntity<OrderSummary> getSummary(@PathVariable Integer idOrder) {
        return ResponseEntity.ok(this.orderService.getSummary(idOrder));
    }

    // 4. Métodos de Escritura (Auditados)

    @PostMapping
    public ResponseEntity<OrderDto> save(@Valid @RequestBody OrderDto orderDto) {
        // Sanitización para evitar "Tainted Data" antes de guardar/auditar
        // Si OrderDto es Record, el service creará la entidad limpia
        return ResponseEntity.ok(this.orderService.save(orderDto));
    }

    @PostMapping("/random")
    public ResponseEntity<Boolean> saveRandomOrder(@RequestBody RandomOrderDto dto) {
        // Simplemente pasamos el Record inmutable al servicio
        return ResponseEntity.ok(this.orderService.saveRandomOrder(dto));
    }

    // 5. Nuevo: Método para actualizar estado (Auditado)
    @PatchMapping("/{idOrder}/status")
    public ResponseEntity<OrderDto> updateStatus(
            @PathVariable Integer idOrder,
            @RequestParam String status) {
        String safeStatus = SanitizerUtil.sanitize(status);
        return ResponseEntity.ok(this.orderService.updateStatus(idOrder, safeStatus));
    }
}