package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.CustomerEntity;
import com.luigi.pizza.persistence.repository.CustomerRepository;
import com.luigi.pizza.service.dto.CustomerDto;
import com.luigi.pizza.service.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public CustomerDto findByPhone(String phone) {
        CustomerEntity entity = this.customerRepository.findByPhoneNumber(phone);
        // Transformamos antes de retornar
        return this.customerMapper.toDto(entity);
    }
}