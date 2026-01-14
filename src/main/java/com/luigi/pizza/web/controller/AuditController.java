package com.luigi.pizza.web.controller;

import com.luigi.pizza.service.OrderService;
import com.luigi.pizza.service.dto.AuditReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit", description = "Endpoints de cumplimiento y supervisión de actividad de usuarios")
public class AuditController {

    private final OrderService orderService;

    public AuditController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/report/{username}")
    @Operation(
            summary = "Reporte de actividad diaria por usuario",
            description = "Genera un listado detallado de todas las acciones (creación, modificación) realizadas por un usuario específico en una fecha determinada. **Solo para Administradores**.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requieren privilegios de ROLE_ADMIN"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT ausente o inválido")
    })
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<AuditReportDto>> getUserReport(
            @Parameter(description = "Nombre de usuario a auditar", example = "admin")
            @PathVariable String username,
            @Parameter(description = "Fecha de la actividad (Formato ISO: YYYY-MM-DD)", example = "2026-01-14")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(this.orderService.getCustomerActivityReport(username, date));
    }
}