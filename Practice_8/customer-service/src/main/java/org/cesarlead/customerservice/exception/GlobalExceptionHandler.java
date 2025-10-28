package org.cesarlead.customerservice.exception;

import org.cesarlead.customerservice.dto.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Captura todas las excepciones de tipo ResourceNotFoundException.
     * * @param ex La excepción lanzada.
     * @return Una ResponseEntity con estado 404 NOT_FOUND y un body JSON.
     */
    @ExceptionHandler(ResourceNotFoundException.class) // <-- Qué excepción maneja
    public ResponseEntity<ErrorDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {

        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // Devuelve 404
                .body(errorDetail); // Con nuestro DTO de error en el body
    }


     @ExceptionHandler(Exception.class)
     public ResponseEntity<ErrorDetail> handleGenericException(Exception ex) {
         ErrorDetail error = new ErrorDetail(
                 LocalDateTime.now(),
                 "Error interno del servidor");
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // 500
     }
}
