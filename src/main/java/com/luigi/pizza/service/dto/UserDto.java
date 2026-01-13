package com.luigi.pizza.service.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDto {
    private String username;
    private String email;
    private Boolean locked;
    private Boolean disabled;
    private List<String> roles; // Solo los nombres de los roles (ej. ["ADMIN", "CUSTOMER"])
}