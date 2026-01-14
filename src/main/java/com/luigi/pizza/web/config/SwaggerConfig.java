package com.luigi.pizza.web.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Luigi's Pizza API")
                        .version("1.0")
                        .description("### Sistema de Gestión de flujos de trabajo en pizzerías.\n" +
                                "Diseñada bajo un esquema de Arquitectura en Capas y principios de Auditoría Total. \n\n" +
                                "**Características clave:**\n" +
                                "* Centralizar la administración de clientes, inventario de pizzas y procesamiento de órdenes.\n" +
                                "* Ejecución de Procedimientos Almacenados atómicos.\n" +
                                "* Auditoría automática de usuarios vía Spring Security.\n" +
                                "* Cumplimiento de estándares de integridad referencial.\n\n" +
                                "### Guía de Uso de la API\n" +
                                "Esta API implementa **Auditoría Total**. Siga estos pasos para realizar pruebas:\n\n" +
                                "1. **Autenticación**: Diríjase al controlador **Authentication** y realice un Login exitoso.\n" +
                                "2. **Token**: Copie el valor de la propiedad **Authorization** de la respuesta.\n" +
                                "3. **Autorización**: Suba al botón verde **'Authorize'** en la parte superior derecha de esta página.\n" +
                                "4. **Validación**: Pegue el token. Ahora podrá usar los métodos de órdenes y el sistema registrará su usuario automáticamente.")
                        .contact(new Contact()
                                .name("Luis Cacuango - Developer")
                                .email("luiscacuango2084@gmail.com")
                                .url("https://github.com/luiscacuango2"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
