package com.luigi.pizza.persistence.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // En un futuro, aquí es donde obtendremos el usuario de Spring Security
        // return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());

        return () -> Optional.of("ADMIN_PIZZERIA");
    }
}