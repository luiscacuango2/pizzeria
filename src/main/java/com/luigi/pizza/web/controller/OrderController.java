package com.luigi.pizza.web.controller;

import com.luigi.pizza.persistence.projection.OrderSummary;
import com.luigi.pizza.service.OrderService;
import com.luigi.pizza.service.dto.OrderDto;
import com.luigi.pizza.service.dto.RandomOrderDto;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Gestión de pedidos con trazabilidad forense y ejecución de Stored Procedures")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1. Obtener todas las órdenes con Paginación (Evita Warning de PageImpl)
    @GetMapping
    @Operation(summary = "Lista todas las órdenes", description = "Retorna una lista paginada de todas las órdenes registradas en el sistema.")
    public ResponseEntity<PagedModel<OrderDto>> getAllOrders(@Parameter(hidden = true) Pageable pageable) {
        Page<OrderDto> orders = this.orderService.getAllOrder(pageable);
        return ResponseEntity.ok(new PagedModel<>(orders));
    }

    // 2. Órdenes de hoy (Normalmente son pocas, se puede mantener Lista o Paginado)
    @GetMapping("/today")
    @Operation(summary = "Órdenes del día", description = "Recupera las órdenes cuya fecha de creación coincide con la fecha actual del servidor.")
    public ResponseEntity<List<OrderDto>> getTodayOrders() {
        return ResponseEntity.ok(this.orderService.getTodayOrders());
    }

    @GetMapping("/outside")
    @Operation(summary = "Órdenes externas", description = "Lista órdenes que requieren entrega fuera del local.")
    public ResponseEntity<List<OrderDto>> getOutsideOrders() {
        return ResponseEntity.ok(this.orderService.getOutsideOrders());
    }

    // 3. Órdenes por cliente (Paginado para manejar clientes frecuentes)
    @GetMapping("/customer/{idCustomer}")
    @Operation(summary = "Historial por cliente", description = "Retorna las órdenes asociadas a un ID de cliente específico, ordenadas por fecha.")
    public ResponseEntity<PagedModel<OrderDto>> getCustomerOrders(
            @PathVariable Integer idCustomer,
            @PageableDefault(size = 5, sort = "orderDate") Pageable pageable) {

        Page<OrderDto> orders = this.orderService.getCustomerOrders(idCustomer, pageable);
        return ResponseEntity.ok(new PagedModel<>(orders));
    }

    @GetMapping("/summary/{idOrder}")
    @Operation(summary = "Resumen detallado", description = "Proyección optimizada que combina datos de la orden, cliente y total de productos.")
    public ResponseEntity<OrderSummary> getSummary(@PathVariable Integer idOrder) {
        return ResponseEntity.ok(this.orderService.getSummary(idOrder));
    }

    // 4. Métodos de Escritura (Auditados)

    @PostMapping
    @Operation(
            summary = "Crear nueva orden manual",
            description = "Registra una orden estándar. El sistema inyecta automáticamente el usuario autenticado como **'created_by'**.",
            security = @SecurityRequirement(name = "Bearer Authentication") // Muestra el candado
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden creada y auditada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Requiere login previo")
    })
    public ResponseEntity<OrderDto> save(@Valid @RequestBody OrderDto orderDto) {
        // Sanitización para evitar "Tainted Data" antes de guardar/auditar
        // Si OrderDto es Record, el service creará la entidad limpia
        return ResponseEntity.ok(this.orderService.save(orderDto));
    }

    @PostMapping("/random")
    @Operation(
            summary = "Generar orden aleatoria (Promoción 20% OFF)",
            description = "Llama a un Stored Procedure que selecciona una pizza al azar y realiza una **Auditoría Total**.",
            security = @SecurityRequirement(name = "Bearer Authentication") // Muestra el candado
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden creada exitosamente y auditada en DB"),
            @ApiResponse(responseCode = "403", description = "Privilegios insuficientes (Requiere ADMIN o CUSTOMER)"),
            @ApiResponse(responseCode = "500", description = "Error interno - Problema en la ejecución del Stored Procedure")
    })
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER"}) // Restringir quién puede disparar promociones
    public ResponseEntity<Boolean> saveRandomOrder(@RequestBody RandomOrderDto dto) {
        // Simplemente pasamos el Record inmutable al servicio
        return ResponseEntity.ok(this.orderService.saveRandomOrder(dto));
    }

    // 5. Nuevo: Método para actualizar estado (Auditado)
    @PatchMapping("/{idOrder}/status")
    @Operation(
            summary = "Actualizar estado de orden",
            description = "Modifica el estado del flujo de entrega. Cada cambio genera una estampa en los registros de auditoría.",
            security = @SecurityRequirement(name = "Bearer Authentication") // Muestra el candado
    )
    public ResponseEntity<OrderDto> updateStatus(
            @PathVariable Integer idOrder,
            @RequestParam @Parameter(description = "Nuevo estado de la orden", example = "CONFIRMED") String status) {
        String safeStatus = SanitizerUtil.sanitize(status);
        return ResponseEntity.ok(this.orderService.updateStatus(idOrder, safeStatus));
    }
}