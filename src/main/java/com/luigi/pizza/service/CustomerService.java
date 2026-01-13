package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.CustomerEntity;
import com.luigi.pizza.persistence.repository.CustomerRepository;
import com.luigi.pizza.service.dto.CustomerDto;
import com.luigi.pizza.service.mapper.CustomerMapper;
import com.luigi.pizza.web.util.SanitizerUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper; // Inyectamos el mapper

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    // Obtener todos los clientes (Paginado)
    public Page<CustomerDto> getAll(Pageable pageable) {
        return this.customerRepository.findAll(pageable)
                .map(this.customerMapper::toDto);
    }

    // Búsqueda por nombre (Paginado y Sanitizado)
    public Page<CustomerDto> findByName(String name, Pageable pageable) {
        // Sanitizamos el parámetro de búsqueda para evitar caracteres maliciosos en la Query
        String safeName = SanitizerUtil.sanitize(name);
        return this.customerRepository.findByNameContaining(safeName, pageable)
                .map(this.customerMapper::toDto);
    }

    public CustomerDto getById(Integer idCustomer) {
        return this.customerRepository.findById(idCustomer)
                .map(this.customerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + idCustomer + " no encontrado"));
    }
    public CustomerDto findByPhone(String phone) {
        CustomerEntity entity = this.customerRepository.findByPhoneNumber(phone);
        if (entity == null) {
            throw new RuntimeException("Cliente con teléfono " + phone + " no encontrado");
        }
        // Transformamos antes de retornar
        return this.customerMapper.toDto(entity);
    }

    public CustomerDto findByEmail(String email) {
        // Asumiendo que tienes este método en tu repositorio
        return this.customerRepository.findByEmail(email)
                .map(this.customerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Cliente con email " + email + " no encontrado"));
    }

    @Transactional
    public CustomerDto save(CustomerDto customerDto) {
        // El mapeo ahora usará los métodos del record
        CustomerEntity entity = this.customerMapper.toEntity(customerDto);

        // La auditoría sigue funcionando igual porque la entidad no cambia
        CustomerEntity savedEntity = this.customerRepository.save(entity);

        return this.customerMapper.toDto(savedEntity);
    }

    @Transactional
    public void delete(Integer idCustomer) {
        this.customerRepository.deleteById(idCustomer);
    }

    public boolean exists(Integer idCustomer) {
        return this.customerRepository.existsById(idCustomer);
    }
}