package com.luigi.pizza.web.controller;

import com.luigi.pizza.persistence.entity.PizzaEntity;
import com.luigi.pizza.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<PizzaEntity>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }

    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> getPizzaById(@PathVariable Integer idPizza) {
        return ResponseEntity.ok(pizzaService.getPizzaById(idPizza));
    }

    @GetMapping("/available")
    public ResponseEntity<List<PizzaEntity>> getAvailablePizzas() {
        return ResponseEntity.ok(pizzaService.getAvailablePizzas());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PizzaEntity> getPizzaByName(@PathVariable String name) {
        return ResponseEntity.ok(pizzaService.getPizzaByName(name));
    }

    @GetMapping("/description/{description}")
    public ResponseEntity<List<PizzaEntity>> getPizzaByDescription(@PathVariable String description) {
        return ResponseEntity.ok(pizzaService.getPizzaByDescription(description));
    }

    @GetMapping("/descriptionnot/{description}")
    public ResponseEntity<List<PizzaEntity>> getPizzaByDescriptionNot(@PathVariable String description) {
        return ResponseEntity.ok(pizzaService.getPizzaByDescriptionNot(description));
    }

    @GetMapping("/vegan")
    public ResponseEntity<List<PizzaEntity>> getAvailableVeganPizzas() {
        return ResponseEntity.ok(pizzaService.getAvailableVeganPizzas());
    }

    @PostMapping
    public ResponseEntity<PizzaEntity> addPizza(@RequestBody PizzaEntity pizza) {
        if (pizza.getIdPizza() == null || !pizzaService.exists(pizza.getIdPizza())) {
            return ResponseEntity.ok(pizzaService.savePizza(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<PizzaEntity> updatePizza(@RequestBody PizzaEntity pizza) {
        if (pizza.getIdPizza() != null && pizzaService.exists(pizza.getIdPizza())) {
            return ResponseEntity.ok(pizzaService.savePizza(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idPizza}")
    public ResponseEntity<Void> deletePizza(@PathVariable Integer idPizza) {
        if (pizzaService.exists(idPizza)) {
            pizzaService.deletePizza(idPizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
