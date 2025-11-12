package com.cesarlead.student_services.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDto(
        LocalDateTime timestamp,
        int status,
        String error,
        List<String> messages,
        String path
) {
    
}
