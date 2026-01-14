package com.luigi.pizza.web.controller;

import com.luigi.pizza.service.PizzaService;
import com.luigi.pizza.service.dto.PizzaDto;
import com.luigi.pizza.service.dto.UpdatePizzaPriceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
@Tag(name = "Pizzas", description = "Gestión de Pizzas")
public class PizzaController {
    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    @Operation(summary = "Listado paginado de todas las pizzas", description = "Recupera el catálogo completo incluyendo pizzas inactivas. Útil para reportes administrativos.")
    public ResponseEntity<Page<PizzaDto>> getAllPizzas(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "8") int elements) {
        return ResponseEntity.ok(pizzaService.getAllPizzas(page, elements));
    }

    @GetMapping("/{idPizza}")
    @Operation(summary = "Buscar pizza por ID", description = "Retorna el detalle técnico de una pizza específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pizza encontrada"),
            @ApiResponse(responseCode = "404", description = "ID de pizza no existe")
    })
    public ResponseEntity<PizzaDto> getPizzaById(@PathVariable Integer idPizza) {
        return ResponseEntity.ok(pizzaService.getPizzaById(idPizza));
    }

    @GetMapping("/available")
    @Operation(summary = "Catálogo para clientes (Pizzas disponibles)", description = "Muestra únicamente las pizzas marcadas como disponibles y permite ordenarlas por precio o nombre.")
    public ResponseEntity<Page<PizzaDto>> getAvailablePizzas(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "8") int elements,
                                                             @Parameter(description = "Campo por el cual ordenar", example = "price") @RequestParam(defaultValue = "price") String sortBy,
                                                             @Parameter(description = "Sentido del orden (ASC/DESC)", example = "ASC") @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(pizzaService.getAvailablePizzas(page, elements, sortBy, sortDirection));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar por nombre exacto")
    public ResponseEntity<PizzaDto> getPizzaByName(@PathVariable String name) {
        return ResponseEntity.ok(pizzaService.getPizzaByName(name));
    }

    @GetMapping("/description/{description}")
    public ResponseEntity<List<PizzaDto>> getPizzaByDescription(@PathVariable String description) {
        return ResponseEntity.ok(pizzaService.getPizzaByDescription(description));
    }

    @GetMapping("/descriptionnot/{description}")
    public ResponseEntity<List<PizzaDto>> getPizzaByDescriptionNot(@PathVariable String description) {
        return ResponseEntity.ok(pizzaService.getPizzaByDescriptionNot(description));
    }

    @GetMapping("/cheapest/{price}")
    public ResponseEntity<List<PizzaDto>> getCheapestPizzas(@PathVariable Double price) {
        return ResponseEntity.ok(pizzaService.getCheapestPizzas(price));
    }

    @GetMapping("/vegan")
    @Operation(summary = "Listar opciones veganas", description = "Filtra el menú para mostrar solo pizzas con etiqueta vegana y disponibles.")
    public ResponseEntity<List<PizzaDto>> getAvailableVeganPizzas() {
        return ResponseEntity.ok(pizzaService.getAvailableVeganPizzas());
    }

    @PostMapping
    @Operation(
            summary = "Crear nueva pizza",
            description = "Registra una nueva Pizza en el catálogo. **Requiere ROLE_ADMIN**. Se grabará el auditor en la DB.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pizza creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID duplicado o datos inválidos")
    })
    public ResponseEntity<PizzaDto> addPizza(@Valid @RequestBody PizzaDto pizzaDto) {
        if (pizzaDto.getIdPizza() == null || !pizzaService.exists(pizzaDto.getIdPizza())) {
            return ResponseEntity.ok(pizzaService.savePizza(pizzaDto));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    @Operation(
            summary = "Actualizar pizza completa",
            description = "Modifica todos los campos de una pizza existente. **Requiere ROLE_ADMIN**.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<PizzaDto> updatePizza(@RequestBody PizzaDto pizza) {
        if (pizza.getIdPizza() != null && pizzaService.exists(pizza.getIdPizza())) {
            return ResponseEntity.ok(pizzaService.savePizza(pizza));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/price")
    @Operation(
            summary = "Ajuste de precio",
            description = "Actualiza el precio de una pizza mediante un DTO simplificado. **Acceso restringido a ADMIN**.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> updatePrice(@Valid @RequestBody UpdatePizzaPriceDto dto) {
        if (pizzaService.exists(dto.getPizzaId())) {
            pizzaService.updatePrice(dto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idPizza}")
    @Operation(
            summary = "Eliminar pizza",
            description = "Elimina físicamente el registro del catálogo. Se recomienda cautela por la integridad referencial de órdenes pasadas.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> deletePizza(@PathVariable Integer idPizza) {
        if (pizzaService.exists(idPizza)) {
            pizzaService.deletePizza(idPizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
