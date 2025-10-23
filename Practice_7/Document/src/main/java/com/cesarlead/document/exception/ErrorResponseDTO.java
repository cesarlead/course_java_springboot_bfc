package com.cesarlead.document.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standardized error response structure for the API.")
public record ErrorResponseDTO(
        @Schema(description = "Timestamp when the error occurred.", example = "2025-10-22T10:30:00")
        LocalDateTime timestamp,
        @Schema(description = "HTTP Status code.", example = "404")
        int status,
        @Schema(description = "HTTP Status error reason.", example = "Not Found")
        String error,
        @Schema(description = "Detailed error message.", example = "Product with id 10 not found.")
        String message,
        @Schema(description = "The path where the error occurred.", example = "/api/v1/products/10")
        String path
) {
}
