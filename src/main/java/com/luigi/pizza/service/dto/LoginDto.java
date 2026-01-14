package com.luigi.pizza.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginDto {
    @Schema(example = "admin", description = "Nombre de usuario registrado")
    private String username;

    @Schema(example = "admin123", description = "Contraseña segura")
    private String password;
}
