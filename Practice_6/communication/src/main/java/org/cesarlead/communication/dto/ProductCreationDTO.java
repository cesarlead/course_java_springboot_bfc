package org.cesarlead.communication.dto;

import java.math.BigDecimal;

public record ProductCreationDTO(
        String name,
        String sku,
        BigDecimal price
) {
}
