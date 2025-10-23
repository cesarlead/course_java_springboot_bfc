package org.cesarlead.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDTO(
        @NotNull(message = "El ID del cliente no puede ser nulo")
        Long customerId,

        @NotEmpty(message = "La lista de productos no puede estar vacía")
        List<@Valid OrderItemRequestDTO> products
) {
}
