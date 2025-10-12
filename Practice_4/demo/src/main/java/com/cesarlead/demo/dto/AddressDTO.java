package com.cesarlead.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
        @NotBlank(message = "La calle es obligatoria.")
        String street,
        @NotBlank(message = "La ciudad es obligatoria.")
        String city
) {
}
