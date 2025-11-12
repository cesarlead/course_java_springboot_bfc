package com.cesarlead.student_services.exception;

public class EstudianteNotFoundException extends RuntimeException {
    public EstudianteNotFoundException(String message, Long id) {
        super(message);
    }
}
