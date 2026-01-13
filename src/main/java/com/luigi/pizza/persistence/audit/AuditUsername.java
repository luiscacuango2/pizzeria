package com.luigi.pizza.persistence.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

// @Component es más preciso que @Service para una utilidad de auditoría
@Component("auditUsername") // Forzamos el nombre para evitar ambigüedades
public class AuditUsername implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 1. Obtener la autenticación del contexto actual del hilo (ThreadLocal)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Validaciones de seguridad en cascada
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("SYSTEM"); // En lugar de empty, "SYSTEM" ayuda a identificar procesos automáticos
        }

        // 3. Extraer el nombre de usuario de forma segura
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername());
        }

        // Si por alguna razón el principal es un String (como en tests o configs básicas)
        return Optional.of(principal.toString());
    }
}