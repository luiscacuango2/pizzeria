package com.luigi.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                // 1. Configuración de Autorización
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/pizzas/**")
                        .authenticated() // Cambiar a permitAll para probar
                        .anyRequest()
                        .authenticated()
                )
                // 2. Configuración de Autenticación (FUERA de la lambda de arriba)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
