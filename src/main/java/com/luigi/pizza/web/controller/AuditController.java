package com.luigi.pizza.web.controller;

import com.luigi.pizza.service.OrderService;
import com.luigi.pizza.service.dto.AuditReportDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final OrderService orderService;

    public AuditController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/report/{username}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<AuditReportDto>> getUserReport(
            @PathVariable String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(this.orderService.getCustomerActivityReport(username, date));
    }
}