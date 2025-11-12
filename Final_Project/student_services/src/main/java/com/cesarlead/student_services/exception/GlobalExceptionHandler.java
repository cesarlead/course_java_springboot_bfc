package com.cesarlead.student_services.exception;

import com.cesarlead.student_services.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Manejador para Errores de Validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        ErrorResponseDto errorDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de Validación",
                errors,
                request.getRequestURI()
        );
        log.warn("Error de validación en {}: {}", request.getRequestURI(), errors);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    // Manejador para "No Encontrado" (404)
    @ExceptionHandler(EstudianteNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEstudianteNotFound(EstudianteNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso No Encontrado",
                List.of(ex.getMessage()),
                request.getRequestURI()
        );
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    // Manejador para Conflictos (Email duplicado - 409)
    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailDuplicado(EmailDuplicadoException ex, HttpServletRequest request) {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflicto de Datos",
                List.of(ex.getMessage()),
                request.getRequestURI()
        );
        log.warn("Conflicto de datos: {}", ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }

    // Manejador genérico (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, HttpServletRequest request) {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error Interno del Servidor",
                List.of("Ocurrió un error inesperado", ex.getMessage()),
                request.getRequestURI()
        );
        log.error("Error interno no controlado en {}: ", request.getRequestURI(), ex);
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
