package org.cesarlead.productservice.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}
