package com.luigi.pizza.persistence.repository;

import com.luigi.pizza.persistence.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// Combinamos ambas interfaces para tener CRUD y Paginación
public interface CustomerRepository extends ListCrudRepository<CustomerEntity, Integer>,
        PagingAndSortingRepository<CustomerEntity, Integer> {

    // 1. Búsqueda por teléfono
    @Query(value = "SELECT c FROM CustomerEntity c WHERE c.phoneNumber = :phone")
    CustomerEntity findByPhoneNumber(@Param("phone") String phone);

    // 2. Búsqueda por Email
    Optional<CustomerEntity> findByEmail(String email);

    // 3. Verificar si existe por email
    boolean existsByEmail(String email);

    // 4. Búsqueda personalizada por nombre PAGINADA
    // Cambiamos List por Page y añadimos el parámetro Pageable
    @Query(value = "SELECT c FROM CustomerEntity c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<CustomerEntity> findByNameContaining(@Param("name") String name, Pageable pageable);

    // 5. Método findAll paginado (necesario para el Service)
    @Override
    Page<CustomerEntity> findAll(Pageable pageable);
}