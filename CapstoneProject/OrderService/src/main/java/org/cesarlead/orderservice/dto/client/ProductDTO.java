package org.cesarlead.orderservice.dto.client;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer stock
) {
}
