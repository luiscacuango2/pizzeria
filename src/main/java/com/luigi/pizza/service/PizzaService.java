package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.PizzaEntity;
import com.luigi.pizza.persistence.repository.PizzaPagSortRepository;
import com.luigi.pizza.persistence.repository.PizzaRepository;
import com.luigi.pizza.service.dto.PizzaDto;
import com.luigi.pizza.service.dto.UpdatePizzaPriceDto;
import com.luigi.pizza.service.excepcional.EmailApiException;
import com.luigi.pizza.service.mapper.PizzaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaPagSortRepository pizzaPagSortRepository;
    private final PizzaMapper pizzaMapper; // 1. Inyectamos el Mapper

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository,
                        PizzaPagSortRepository pizzaPagSortRepository,
                        PizzaMapper pizzaMapper) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
        this.pizzaMapper = pizzaMapper;
    }

    // 2. Usamos .map() para transformar la página
    public Page<PizzaDto> getAllPizzas(int page, int elements) {
        Pageable pageRequest = PageRequest.of(page, elements);
        return this.pizzaPagSortRepository.findAll(pageRequest)
                .map(this.pizzaMapper::toDto);
    }

    public Page<PizzaDto> getAvailablePizzas(int page, int elements, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(page, elements, sort);
        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest)
                .map(this.pizzaMapper::toDto);
    }

    // 3. Convertimos Listas usando el mapper
    public List<PizzaDto> getAvailableVeganPizzas() {
        return this.pizzaMapper.toDtoList(this.pizzaRepository.findAllByVeganTrue());
    }

    public PizzaDto getPizzaByName(String name) {
        return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name)
                .map(this.pizzaMapper::toDto)
                .orElseThrow(() -> new RuntimeException("La Pizza no existe"));
    }

    public List<PizzaDto> getCheapestPizzas(Double price) {
        return this.pizzaMapper.toDtoList(
                this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price)
        );
    }

    public List<PizzaDto> getPizzaByDescription(String description) {
        return this.pizzaMapper.toDtoList(
                this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(description)
        );
    }

    public List<PizzaDto> getPizzaByDescriptionNot(String description) {
        return this.pizzaMapper.toDtoList(
                this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(description)
        );
    }

    public PizzaDto getPizzaById(Integer idPizza) {
        return this.pizzaRepository.findById(idPizza)
                .map(this.pizzaMapper::toDto)
                .orElse(null);
    }

    // Doble conversión: DTO -> Entity (para guardar) -> DTO (para retornar)
    public PizzaDto savePizza(PizzaDto pizzaDto) {
        PizzaEntity entity = this.pizzaMapper.toEntity(pizzaDto); // 1. Convertimos el DTO a Entidad para la persistencia
        PizzaEntity savedEntity = this.pizzaRepository.save(entity); // 2. Guardamos en la DB
        return this.pizzaMapper.toDto(savedEntity); // 3. Retornamos el DTO de lo que se guardó
    }

    public boolean exists(Integer idPizza) {
        return this.pizzaRepository.existsById(idPizza);
    }

    public void deletePizza(Integer idPizza) {
        this.pizzaRepository.deleteById(idPizza);
    }

    @Transactional // Si queremos que guarde lo primero (noRollbackFor = EmailApiException.class)
    public void updatePrice(UpdatePizzaPriceDto dto) {
        this.pizzaRepository.findById(dto.getPizzaId()).ifPresent(pizzaEntity -> {
            PizzaDto updatedDto = this.pizzaMapper.toDto(pizzaEntity).toBuilder()
                    .price(dto.getNewPrice())
                    .build();
            this.savePizza(updatedDto);
        });
    }

    private void sendEmail() {
        throw new EmailApiException();
    }
}