package com.luigi.pizza.web.controller;

import com.luigi.pizza.service.dto.LoginDto;
import com.luigi.pizza.web.config.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Endpoint crítico para la generación de identidad y tokens de acceso")
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtutil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtutil) {
        this.authenticationManager = authenticationManager;
        this.jwtutil = jwtutil;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión y obtener Token JWT",
            description = "### 🔑 Primer paso de la Auditoría Total\n" +
                    "Valida las credenciales del usuario y genera un token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa. El JWT se encuentra en el Header 'Authorization'.",
                    content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas - Intento de acceso no autorizado registrado",
                    content = @Content
            )
    })
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(loginToken);

        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getPrincipal());

        String jwt = this.jwtutil.create(loginDto.getUsername());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }

}
