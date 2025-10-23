package org.cesarlead.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequestDTO(
        @NotBlank(message = "El nombre no puede estar en blanco")
        String name,

        @Email(message = "Debe ser una dirección de email válida")
        @NotBlank(message = "El email no puede estar en blanco")
        String email
) {
}
