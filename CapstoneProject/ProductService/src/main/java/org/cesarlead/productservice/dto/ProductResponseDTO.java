package org.cesarlead.productservice.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer stock // El servicio de pedidos necesita saber el stock
) {
}
