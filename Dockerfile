# Etapa 1: Construcción (Build)
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Dar permisos de ejecución al wrapper de gradle y descargar dependencias
RUN ./gradlew --no-daemon dependencies

# Copiar el código fuente y construir el jar
COPY src src
RUN ./gradlew --no-daemon clean bootJar

# Etapa 2: Imagen de ejecución (Runtime)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear un usuario no privilegiado para mayor seguridad (Best Practice de Auditoría)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto de la API
EXPOSE 8080

# Configurar variables de entorno por defecto (pueden sobrescribirse al correr el contenedor)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]