package org.cesarlead.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "El nombre no puede estar en blanco")
        String name,

        @Positive(message = "El precio debe ser un número positivo")
        BigDecimal price,

        @PositiveOrZero(message = "El stock debe ser cero o un número positivo")
        Integer stock
) {
}
