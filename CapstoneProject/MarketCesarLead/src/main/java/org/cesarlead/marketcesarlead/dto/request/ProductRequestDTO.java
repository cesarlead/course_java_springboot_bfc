package org.cesarlead.marketcesarlead.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "El nombre no puede estar en blanco")
        String name,
        @Positive(message = "El precio debe ser un número positivo")
        BigDecimal price,
        @Positive(message = "El stock debe ser un número positivo")
        Integer stock
) {
}