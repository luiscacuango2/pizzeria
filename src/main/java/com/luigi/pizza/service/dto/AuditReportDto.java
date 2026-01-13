package com.luigi.pizza.service.dto;

import java.time.LocalDateTime;

public record AuditReportDto(
        String entityName,
        String action,
        String username,
        LocalDateTime timestamp,
        String details
) {}