package org.cesarlead.marketcesarlead.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}
