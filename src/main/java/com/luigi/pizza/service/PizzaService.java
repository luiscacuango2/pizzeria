package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.PizzaEntity;
import com.luigi.pizza.persistence.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public List<PizzaEntity> getAllPizzas() {
        return this.pizzaRepository.findAll();
    }

    public PizzaEntity getPizzaById(Integer idPizza) {
        return this.pizzaRepository.findById(idPizza).orElse(null);
    }

    public PizzaEntity savePizza(PizzaEntity pizza) {
        return this.pizzaRepository.save(pizza);
    }

    public boolean exists(Integer idPizza) {
        return this.pizzaRepository.existsById(idPizza);
    }

    public void deletePizza(Integer idPizza) {
        this.pizzaRepository.deleteById(idPizza);
    }
}
