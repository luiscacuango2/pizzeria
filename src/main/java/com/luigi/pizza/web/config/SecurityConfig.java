package com.luigi.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Aqui toca poner disable(), nueva forma de configurar
                .cors(Customizer.withDefaults())
                // 1. Configuración de Autorización
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
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
