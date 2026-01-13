package com.luigi.pizza.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerDto(
        Integer idCustomer,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50)
        String name,

        @Email(message = "Email inválido")
        String email,

        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
        String phoneNumber
) {}