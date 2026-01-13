package com.luigi.pizza.web.controller;

import com.luigi.pizza.service.CustomerService;
import com.luigi.pizza.service.dto.CustomerDto;
import com.luigi.pizza.web.util.SanitizerUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // --- LECTURA (READ) ---

    @GetMapping
    public ResponseEntity<PagedModel<CustomerDto>> getAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<CustomerDto> customers = this.customerService.getAll(pageable);
        return ResponseEntity.ok(new PagedModel<>(customers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.customerService.getById(id));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDto> getByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(this.customerService.findByPhone(phone));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDto> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(this.customerService.findByEmail(email));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<CustomerDto>> searchByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CustomerDto> customers = this.customerService.findByName(name, pageable);
        return ResponseEntity.ok(new PagedModel<>(customers));
    }

    // --- ESCRITURA (WRITE) - Auditada y Sanitizada ---

    @PostMapping
    public ResponseEntity<CustomerDto> save(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto cleanDto = new CustomerDto(
                null,
                SanitizerUtil.sanitize(customerDto.name()),
                SanitizerUtil.sanitize(customerDto.email()),
                SanitizerUtil.sanitize(customerDto.phoneNumber())
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.customerService.save(cleanDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(@PathVariable Integer id,
                                              @Valid @RequestBody CustomerDto customerDto) {
        if (!this.customerService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        CustomerDto cleanDto = new CustomerDto(
                id,
                SanitizerUtil.sanitize(customerDto.name()),
                SanitizerUtil.sanitize(customerDto.email()),
                SanitizerUtil.sanitize(customerDto.phoneNumber())
        );
        return ResponseEntity.ok(this.customerService.save(cleanDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (this.customerService.exists(id)) {
            this.customerService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}