package com.luigi.pizza;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class LuigiPizzeriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuigiPizzeriaApplication.class, args);
    }

    /**
     * Estandarización de Auditoría Temporal (UTC)
     * Como establecimos el 12 de enero de 2026, todas las entidades deben ser auditadas.
     * Configurar el TimeZone a UTC garantiza que los campos 'created_date' y 'modified_date'
     * sean consistentes sin importar dónde esté el servidor.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}