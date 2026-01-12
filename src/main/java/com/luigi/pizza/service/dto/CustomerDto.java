package com.luigi.pizza.service.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private String idCustomer;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
}