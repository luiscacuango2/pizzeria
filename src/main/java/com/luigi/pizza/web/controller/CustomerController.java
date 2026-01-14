package com.luigi.pizza.web.controller;

import com.luigi.pizza.service.CustomerService;
import com.luigi.pizza.service.dto.CustomerDto;
import com.luigi.pizza.web.util.SanitizerUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Gestión de datos de clientes y perfiles de usuario")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // --- LECTURA (READ) ---
    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Retorna una página de clientes ordenados por nombre. **Acceso: ADMIN**.")
    public ResponseEntity<PagedModel<CustomerDto>> getAll(
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<CustomerDto> customers = this.customerService.getAll(pageable);
        return ResponseEntity.ok(new PagedModel<>(customers));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Recupera la información detallada de un cliente específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "ID de cliente no registrado")
    })
    public ResponseEntity<CustomerDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.customerService.getById(id));
    }

    @GetMapping("/phone/{phone}")
    @Operation(summary = "Buscar por teléfono", description = "Utilizado para identificación rápida en pedidos telefónicos.")
    public ResponseEntity<CustomerDto> getByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(this.customerService.findByPhone(phone));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar por correo electrónico", description = "Verifica la existencia de un perfil mediante su email único.")
    public ResponseEntity<CustomerDto> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(this.customerService.findByEmail(email));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<CustomerDto>> searchByName(
            @Parameter(description = "Nombre o parte del nombre a buscar", example = "Luis") @RequestParam String name,
            @Parameter(hidden = true) @PageableDefault(size = 10) Pageable pageable) {
        Page<CustomerDto> customers = this.customerService.findByName(name, pageable);
        return ResponseEntity.ok(new PagedModel<>(customers));
    }

    // --- ESCRITURA (WRITE) - Auditada y Sanitizada ---
    @PostMapping
    @Operation(
            summary = "Registrar nuevo cliente",
            description = "Crea un cliente sanitizando entradas para prevenir XSS/Injection. **Sella automáticamente el usuario creador**.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado y auditado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o correo duplicado")
    })
    public ResponseEntity<CustomerDto> save(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto cleanDto = new CustomerDto(
                null,
                SanitizerUtil.sanitize(customerDto.name()),
                SanitizerUtil.sanitize(customerDto.email()),
                SanitizerUtil.sanitize(customerDto.phoneNumber())
        );
        return ResponseEntity.ok(this.customerService.save(cleanDto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar perfil de cliente",
            description = "Actualiza los datos del cliente. Cada modificación queda registrada con fecha y autor en la DB.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
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
    @Operation(
            summary = "Eliminar registro de cliente",
            description = "Eliminación física del cliente. **Advertencia:** Solo posible si no tiene órdenes asociadas (Integridad Referencial).",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "409", description = "Conflicto: El cliente tiene historial de órdenes y no puede borrarse")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (this.customerService.exists(id)) {
            this.customerService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}