package com.cesarlead.course_service.exception;

public class CursoNotFoundException extends RuntimeException {
    public CursoNotFoundException(String message) {
        super(message);
    }
}
