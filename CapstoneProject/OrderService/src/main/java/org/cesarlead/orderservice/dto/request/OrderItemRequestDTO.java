package org.cesarlead.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDTO(
        @NotNull(message = "El ID del producto no puede ser nulo")
        Long productId,

        @NotNull
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer quantity
) {
}
