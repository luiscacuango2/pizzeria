package com.luigi.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Aqui toca poner disable(), nueva forma de configurar
                .cors(Customizer.withDefaults())
                // 1. Configuración de Autorización
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/**").denyAll()
                        .anyRequest()
                        .authenticated()
                )
                // 2. Configuración de Autenticación (FUERA de la lambda de arriba)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
