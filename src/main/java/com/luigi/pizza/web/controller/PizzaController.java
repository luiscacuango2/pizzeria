package com.luigi.pizza.web.controller;

import com.luigi.pizza.persistence.entity.PizzaEntity;
import com.luigi.pizza.service.PizzaService;
import com.luigi.pizza.service.dto.PizzaDto;
import com.luigi.pizza.service.dto.UpdatePizzaPriceDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {
    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ResponseEntity<Page<PizzaDto>> getAllPizzas(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "8") int elements) {
        return ResponseEntity.ok(pizzaService.getAllPizzas(page, elements));
    }

    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaDto> getPizzaById(@PathVariable Integer idPizza) {
        return ResponseEntity.ok(pizzaService.getPizzaById(idPizza));
    }

    @GetMapping("/available")
    public ResponseEntity<Page<PizzaDto>> getAvailablePizzas(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "8") int elements,
                                                                @RequestParam(defaultValue = "price") String sortBy,
                                                                @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(pizzaService.getAvailablePizzas(page, elements, sortBy, sortDirection));
    }

    @GetMapping("/name/{name}")
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
    public ResponseEntity<List<PizzaDto>> getAvailableVeganPizzas() {
        return ResponseEntity.ok(pizzaService.getAvailableVeganPizzas());
    }

    @PostMapping
    public ResponseEntity<PizzaDto> addPizza(@Valid @RequestBody PizzaDto pizzaDto) {
        if (pizzaDto.getIdPizza() == null || !pizzaService.exists(pizzaDto.getIdPizza())) {
            return ResponseEntity.ok(pizzaService.savePizza(pizzaDto));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<PizzaDto> updatePizza(@RequestBody PizzaDto pizza) {
        if (pizza.getIdPizza() != null && pizzaService.exists(pizza.getIdPizza())) {
            return ResponseEntity.ok(pizzaService.savePizza(pizza));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/price")
    public ResponseEntity<Void> updatePrice(@Valid @RequestBody UpdatePizzaPriceDto dto) {
        if (pizzaService.exists(dto.getPizzaId())) {
            pizzaService.updatePrice(dto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idPizza}")
    public ResponseEntity<Void> deletePizza(@PathVariable Integer idPizza) {
        if (pizzaService.exists(idPizza)) {
            pizzaService.deletePizza(idPizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
