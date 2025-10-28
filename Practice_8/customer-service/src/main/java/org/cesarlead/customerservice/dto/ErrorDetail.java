package org.cesarlead.customerservice.dto;

import java.time.LocalDateTime;

public record ErrorDetail(
        LocalDateTime timestamp,
        String message
) {
}
