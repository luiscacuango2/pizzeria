# Guía de Contribución - Luigi's Pizza API

¡Gracias por tu interés en contribuir a la API de Luigi's Pizza! Este proyecto se rige por altos estándares de calidad de código y una política estricta de **Auditoría Total**. Para mantener la integridad del sistema, te pedimos que sigas estas directrices.

---

## Flujo de Trabajo (Workflow)

1.  **Fork del Proyecto**: Crea una copia del repositorio en tu cuenta de GitHub.
2.  **Creación de Rama**: Crea una rama para tu funcionalidad o corrección:
    ```bash
    git checkout -b feature/nueva-funcionalidad
    # o
    git checkout -b fix/correccion-error
    ```
3.  **Desarrollo**: Realiza tus cambios siguiendo las [Convenciones de Código](#-convenciones-de-codigo).
4.  **Pruebas**: Asegúrate de que los cambios no rompan la integridad de la base de datos (especialmente los Procedimientos Almacenados).
5.  **Pull Request**: Envía tus cambios para revisión.

---

## Convenciones de Código

### 1. Estándares de Java
* Seguimos las **Google Java Style Guide**.
* Los nombres de variables y métodos deben ser descriptivos y en inglés (`camelCase`).
* Cada nuevo servicio o controlador debe estar debidamente documentado con Javadoc si la lógica es compleja.

### 2. Auditoría Obligatoria (Compliance 2026)
Cualquier cambio que afecte la persistencia de datos **debe** incluir trazabilidad:
* **Entidades**: Deben extender de `AuditableEntity` o incluir los campos `created_by`, `created_date`, etc.
* **SQL/Procedures**: No se aceptan `INSERT` directos que dejen campos de auditoría nulos. Siempre se debe propagar el usuario del sistema.

### 3. Commits (Conventional Commits)
Usamos mensajes de commit semánticos para facilitar la generación de changelogs:
* `feat:` Una nueva característica para el usuario.
* `fix:` Corrección de un bug.
* `docs:` Cambios solo en la documentación.
* `style:` Cambios que no afectan el significado del código (espacios, formato).
* `refactor:` Cambio de código que no corrige un error ni añade una funcionalidad.

---

## Modificaciones en la Base de Datos

Si tu contribución incluye cambios en el esquema de MySQL o en los Procedimientos Almacenados:
1.  Incluye el script SQL en la carpeta `src/main/resources/db/migration/`.
2.  Asegúrate de manejar **Llaves Compuestas** correctamente si el cambio afecta a `order_item`.
3.  Prueba que el `ROLLBACK` funcione correctamente ante errores de integridad.

---

## Pruebas Unitarias e Integración

Antes de enviar un Pull Request, verifica:
* Que la aplicación compila correctamente con `./mvnw clean compile`.
* Que no hay regresiones en el flujo de seguridad JWT.
* Que el endpoint de promociones aleatorias sigue registrando el auditor correctamente.

---

## Código de Conducta

Al participar en este proyecto, te comprometes a mantener un entorno profesional, respetuoso y colaborativo. No se tolerará ningún tipo de acoso o comportamiento inapropiado.

---

## ¿Preguntas?
Si tienes dudas sobre cómo implementar una nueva auditoría o manejar una transacción compleja, abre un **Issue** con la etiqueta `question`.

---
*Desarrollado con compromiso por la Integridad de Datos.*