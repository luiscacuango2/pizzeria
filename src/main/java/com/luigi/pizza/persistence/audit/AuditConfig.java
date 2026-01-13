package com.luigi.pizza.persistence.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// Activamos la Auditoría JPA vinculándola con el Bean 'auditUsername' del componente AuditUsername
@EnableJpaAuditing(auditorAwareRef = "auditUsername")
public class AuditConfig {
}