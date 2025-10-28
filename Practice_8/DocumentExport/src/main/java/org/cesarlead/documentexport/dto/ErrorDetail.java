package org.cesarlead.documentexport.dto;

import java.time.LocalDateTime;

public record ErrorDetail(
        LocalDateTime timestamp,
        String message
) {
}
