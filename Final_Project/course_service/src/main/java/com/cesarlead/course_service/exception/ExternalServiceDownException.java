package com.cesarlead.course_service.exception;

public class ExternalServiceDownException extends RuntimeException {
    public ExternalServiceDownException(String message) {
        super(message);
    }
}
