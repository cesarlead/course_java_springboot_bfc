package com.cesarlead.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${api.error.style:CUSTOM}") // Por defecto es CUSTOM si no se especifica
    private String errorStyle;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Para errores de validación, un mapa de campo -> error es más útil para el frontend.
        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing // En caso de duplicados
                ));

        if ("RFC7807".equalsIgnoreCase(errorStyle)) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "La petición contiene datos inválidos.");
            problemDetail.setTitle("Error de Validación");
            problemDetail.setType(URI.create("/errors/validation-error"));
            problemDetail.setInstance(URI.create(request.getRequestURI()));
            problemDetail.setProperty("errors", errors); // Propiedad extendida
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        } else {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));

        if ("RFC7807".equalsIgnoreCase(errorStyle)) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "La URI de la petición contiene parámetros inválidos.");
            problemDetail.setTitle("Error de Validación de Parámetros");
            problemDetail.setType(URI.create("/errors/parameter-validation-error"));
            problemDetail.setInstance(URI.create(request.getRequestURI()));
            problemDetail.setProperty("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        } else {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    // Manejador para excepciones de negocio personalizadas
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        if ("RFC7807".equalsIgnoreCase(errorStyle)) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
            problemDetail.setTitle("Recurso No Encontrado");
            problemDetail.setType(URI.create("/errors/resource-not-found"));
            problemDetail.setInstance(URI.create(request.getRequestURI()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
        } else {
            ApiErrorResponse error = new ApiErrorResponse(
                    request.getRequestURI(),
                    ex.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) {
        // **Principio de Seguridad CRÍTICO y NO NEGOCIABLE**:
        // 1. Loguear el stack trace completo para el equipo de desarrollo. Esto es para NOSOTROS.
        log.error("Error inesperado en la petición {} : {}", request.getRequestURI(), ex.getMessage(), ex);

        // 2. Devolver una respuesta genérica y segura al cliente. Esto es para ELLOS.
        // NUNCA filtrar detalles de implementación (ej. "NullPointerException", nombres de clases,
        // fragmentos de queries SQL) al usuario final. Es una vulnerabilidad de seguridad grave.
        String genericMessage = "Ocurrió un error interno inesperado. Por favor, contacte al soporte.";

        if ("RFC7807".equalsIgnoreCase(errorStyle)) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, genericMessage);
            problemDetail.setTitle("Error Interno del Servidor");
            problemDetail.setType(URI.create("/errors/internal-server-error"));
            problemDetail.setInstance(URI.create(request.getRequestURI()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
        } else {
            ApiErrorResponse error = new ApiErrorResponse(
                    request.getRequestURI(),
                    genericMessage,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
