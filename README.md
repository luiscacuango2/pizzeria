# Luigi's Pizza API - Backend de Alto Rendimiento

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)

![Security](https://img.shields.io/badge/Security-JWT_Audited-success?style=for-the-badge&logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Audit](https://img.shields.io/badge/Audit-Full_Traceability-blue?style=for-the-badge)
![Clean Code](https://img.shields.io/badge/Clean_Code-Certified-brightgreen?style=for-the-badge)
![Maintained](https://img.shields.io/badge/Maintained%3F-yes-green.svg?style=for-the-badge)

![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![OpenAPI](https://img.shields.io/badge/openapi-6BA53F?style=for-the-badge&logo=openapi&logoColor=white)

---

**Luigi's Pizza API** es una solución profesional para la gestión de pizzerías, diseñada bajo estándares de **Auditoría Total (Compliance 2026)**. El sistema integra una arquitectura desacoplada que combina la potencia de Spring Data JPA con la eficiencia de procedimientos almacenados en MySQL para procesos críticos de negocio.

---

## Características Principales

* **Auditoría Total (Directriz 2026):** Implementación de trazabilidad obligatoria en todas las entidades. Cada registro en `pizza_order` y `order_item` captura automáticamente el usuario autenticado (`created_by`) y marcas de tiempo precisas.
* **Promociones Dinámicas vía DB:** Lógica de "Random Pizza 20% OFF" ejecutada mediante **Stored Procedures**, garantizando atomicidad transaccional y cálculos financieros exactos.
* **Gestión de Datos Complejos:** Manejo de **Llaves Primarias Compuestas** en detalles de orden y relaciones de integridad referencial robustas.
* **Seguridad Integrada:** Extracción de identidad de auditoría directamente desde el contexto de **Spring Security (JWT)**, eliminando la posibilidad de suplantación de identidad en los registros.
* **Arquitectura de Proyecciones:** Uso de interfaces de proyección para consultas optimizadas que integran datos de clientes, órdenes y productos en un solo DTO.

---

## Stack Tecnológico

| Tecnología | Propósito |
| :--- | :--- |
| **Java 17 (LTS)** | Lenguaje robusto para lógica de negocio y tipado fuerte. |
| **Spring Boot 3** | Ecosistema principal para servicios REST y persistencia. |
| **Gradle** | Gestor de dependencias y automatización de construcción. |
| **MySQL 8.0** | Motor de base de datos con soporte para procedimientos almacenados. |
| **Spring Security** | Gestión de autenticación y protección de endpoints críticos. |
| **Hibernate/JPA** | Mapeo objeto-relacional y gestión de ciclos de vida de entidades. |

---
## Estructura del Proyecto

```text
src/main/java/com/luigi/pizza/
├── controller/          # Endpoints REST y manejo de peticiones HTTP.
├── service/             # Lógica de negocio y orquestación de servicios.
├── persistence/         # Capa de datos.
│   ├── entity/          # Definición de tablas y auditoría JPA.
│   ├── repository/      # Interfaces de Spring Data y llamadas a @Procedure.
│   └── projection/      # Interfaces para consultas personalizadas (DTOs).
├── web/                 # Configuración de Seguridad, JWT y CORS.
└── audit/               # Componentes de Auditoría Total (AuditUsername).
```

---
## Arquitectura de Persistencia

El sistema utiliza una relación **1:N** entre `pizza_order` y `order_item` con una particularidad técnica:
* **Llave Compuesta**: `order_item` se identifica mediante la combinación de `(id_item, id_order)`. Esto permite que la numeración de ítems sea independiente por cada orden (ej. Ítem 1 de la Orden 100).
* **Integridad**: Todas las operaciones críticas están protegidas por manejadores de excepciones en SQL (`SQLEXCEPTION`) que ejecutan un `ROLLBACK` automático ante cualquier fallo.
> **Regla de Auditoría:** Ambas tablas heredan de una clase base o contienen campos de trazabilidad manual para cumplir con el estándar de Auditoría Total.

---
## Configuración e Instalación

### Requisitos previos
* **Java 17+**
* **MySQL 8.0**

### Instalación de Base de Datos
1.  **Esquema Principal:** Ejecute los scripts de creación de tablas.
2.  **Procedimiento Almacenado:** Importe el procedimiento `take_random_pizza_order`:
    ```sql
    -- El script se encuentra en src/main/resources/db/procedure.sql
    SOURCE path/to/procedure.sql;
    ```

### Ejecución de la API
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/luiscacuango2/pizzeria.git
    cd pizzeria
    ```
2.  **Configurar credenciales:** Ajuste el archivo `application.yml` o `application.properties` con sus credenciales de MySQL.
3.  **Compilar y Correr:**
    ```bash
    ./gradlew bootRun
    ```
---

## Ejecución con Docker (Cloud Ready)

Si prefieres no instalar Java o Gradle localmente, puedes desplegar la solución completa usando contenedores.

### Opción 1: Docker Compose (Recomendado)
Esta opción levanta tanto la **API** como la base de datos **MySQL** con los Stored Procedures ya configurados.

```bash
  docker-compose up --build
```
### Opción 2: Docker CLI

Si ya tienes una base de datos MySQL corriendo y solo quieres levantar la API:
1.  **Construir la imagen:
```bash
    docker build -t luiscacuango/pizzeria:v1 .
```
2.  **Correr el contenedor: Recuerda ajustar las variables de entorno para que apunten a tu base de datos actual.
```bash
    docker run -d -p 8080:8080 \
      --name api-pizzeria \
      -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/pizza_db \
      -e SPRING_DATASOURCE_USERNAME=tu_usuario \
      -e SPRING_DATASOURCE_PASSWORD=tu_password \
      luiscacuango/pizzeria:v1
```
---

## Documentación Interactiva (OpenAPI)

El proyecto expone una interfaz interactiva para pruebas rápidas de los endpoints auditados:

* **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **JSON Definition**: `http://localhost:8080/v3/api-docs`

> **Nota de Auditoría**: Para probar endpoints protegidos, use el botón **'Authorize'** e ingrese el token JWT generado en el login.
## Verificación
1. Inicia tu aplicación.
2. Entra a http://localhost:8080/swagger-ui.html.
3. Verás todos tus controladores (Pizza, Order, Customer).
---
## Pruebas y QA con Postman

Se incluye una colección completa para validar el flujo de **Auditoría Total** y los **Stored Procedures**.

### Pasos para importar:
1. Abra Postman.
2. Haga clic en el botón **Import** (esquina superior izquierda).
3. Arrastre el archivo ubicado en: `docs/postman/pizzeria.postman_collection.json`.
4. Configure el **Environment** con la variable `url` apuntando a `http://localhost:8080`.

### Endpoints Incluidos:
* **Auth**: Login y obtención de JWT.
* **Auditoría**: Consulta de logs de creación.
* **Orders**: Generación de orden aleatoria (20% OFF).
---

## Guía de Uso: Promoción Random 20% OFF

El sistema permite generar órdenes aleatorias promocionales. La identidad del auditor se captura automáticamente del token de sesión.

### Ejemplo de Petición (POST):
**Endpoint:** `/api/orders/random`

```json
{
    "idCustomer": "262132898",
    "deliveryMethod": "D"
}
```
---

## Convenciones de Auditoría Forense

Todas las tablas críticas incluyen el bloque de campos auditables:
* `created_by`: Usuario que originó el registro.
* `created_date`: Timestamp de inserción.
* `modified_by`: Último usuario en editar.
* `modified_date`: Timestamp de la última actualización.

### Flujo de Trazabilidad Forense

Para garantizar que los registros no sean alterados sin dejar rastro, seguimos este flujo:
1. **Autenticación**: El cliente envía un JWT válido.
2. **Contexto**: Spring Security almacena el `username` en el `SecurityContext`.
3. **Persistencia**: El componente `AuditUsername` recupera el usuario.
4. **Base de Datos**: El Procedimiento Almacenado recibe el usuario y sella el registro de forma atómica (INSERT + COMMIT).

### Flujo de Auditoría:

1. El controlador recibe el DTO inmutable.
2. El OrderService recupera el username del SecurityContextHolder.
3. Se invoca al @Procedure en el repositorio pasando el usuario como parámetro de auditoría.
4. La base de datos ejecuta el INSERT en pizza_order y order_item con trazabilidad completa.
---
### Arquitectura de Seguridad y Trazabilidad

Para cumplir con la directriz de **Auditoría Total**, el sistema implementa un flujo de datos unidireccional y seguro:

1. **Intercepción de Identidad:** El `JwtFilter` valida el token del encabezado `Authorization`. Si es válido, inyecta el `Username` en el `SecurityContext` de Spring.
2. **Auditoría en Persistencia:** Utilizamos un componente `AuditUsername` que actúa como puente. Mediante la anotación `@CreatedBy` (o capturándolo manualmente en el Service), el sistema garantiza que el desarrollador no tenga que "setear" el usuario a mano, evitando errores humanos.
3. **Persistencia Atómica:** En procesos críticos como la creación de órdenes aleatorias, el `username` se pasa directamente al **Stored Procedure**. Esto asegura que la auditoría se registre en la misma transacción que el pedido, cumpliendo con las propiedades **ACID**.

#### Ejemplo de Flujo de Datos:
`Cliente (JWT) -> Spring Security Context -> Service -> Repositorio (@Procedure) -> MySQL (Audit Log)`

---
## Roadmap 2026
- [ ] Implementación de WebSockets para actualizaciones de órdenes en tiempo real.
- [ ] Dockerización completa de la solución.
- [ ] Pipeline de CI/CD con GitHub Actions.
- [ ] Integración con servicios de mensajería (WhatsApp/Email) para confirmación de pedidos.
- [ ] Módulo de Reportes Avanzados con exportación a PDF/Excel auditada.

---

### Contribución

¡Las contribuciones son bienvenidas! Por favor, lee nuestro archivo [CONTRIBUTING.md](CONTRIBUTING.md) para conocer los detalles sobre nuestro código de conducta y el proceso para enviarnos pull requests.

### Licencia

Este proyecto está bajo la Licencia MIT - mira el archivo [LICENSE](LICENSE) para más detalles.

---

## Autor
**Luis Cacuango**
* [GitHub Profile](https://github.com/luiscacuango2)

---
*Desarrollado con pasión por el Clean Code y la Programación Funcional.*