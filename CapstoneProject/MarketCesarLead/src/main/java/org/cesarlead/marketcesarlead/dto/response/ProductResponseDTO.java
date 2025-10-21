package org.cesarlead.marketcesarlead.dto.response;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal price
) {
}
