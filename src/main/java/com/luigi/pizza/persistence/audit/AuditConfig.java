package com.luigi.pizza.persistence.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// Apuntamos al nombre del componente AuditUsername
@EnableJpaAuditing(auditorAwareRef = "auditUsername")
public class AuditConfig {
}