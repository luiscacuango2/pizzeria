package com.luigi.pizza.web.controller;

import com.luigi.pizza.service.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j // Agregamos logs para debugear en consola
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Método privado centralizado con Trace ID
    private ErrorResponseDto.ErrorResponseDtoBuilder baseError(String message, HttpStatus status) {
        return ErrorResponseDto.builder()
                .message(message)
                .code(status.value())
                .traceId(UUID.randomUUID().toString().substring(0, 8)) // Genera ID de rastreo
                .timestamp(LocalDateTime.now());
    }

    // Error 401: Credenciales inválidas en el Login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Intento de login fallido: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(baseError("Usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED).build());
    }

    // Error 400: Cuando fallan las validaciones (@Valid en tus DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(f -> errors.put(f.getField(), f.getDefaultMessage()));

        log.error("Errores de validación: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(baseError("Datos inválidos", HttpStatus.BAD_REQUEST)
                        .details(errors)
                        .build());
    }

    // NUEVO: Manejo de errores de tipo de dato (ej: enviar texto en lugar de un ID numérico)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String detail = String.format("El parámetro '%s' debe ser de tipo %s", ex.getName(), ex.getRequiredType().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(baseError("Error en el formato de los parámetros", HttpStatus.BAD_REQUEST)
                        .details(Map.of("error", detail))
                        .build());
    }

    // NUEVO: Manejo de RuntimeException personalizadas (como cuando una pizza no existe)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
        log.error("Error de negocio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(baseError(ex.getMessage(), HttpStatus.BAD_REQUEST).build());
    }

    // Error 500: Cualquier otro error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        ErrorResponseDto error = baseError("Lo sentimos, ocurrió un error interno inesperado", HttpStatus.INTERNAL_SERVER_ERROR).build();

        // Aquí logueamos el Trace ID junto con la excepción real
        log.error("ERROR 500 [ID: {}]: {}", error.getTraceId(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}