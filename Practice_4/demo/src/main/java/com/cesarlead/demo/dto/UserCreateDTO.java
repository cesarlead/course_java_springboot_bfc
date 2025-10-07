package com.cesarlead.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UserCreateDTO(
        @NotBlank(message = "El nombre de usuario no puede estar en blanco.")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
        String username,

        @NotBlank(message = "El correo electrónico es obligatorio.")
        @Email(message = "El formato del correo electrónico no es válido.")
        String email,

        @NotEmpty(message = "La contraseña no puede estar en blanco.")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
        String password,

        // @NotNull asegura que el objeto 'address' en sí no sea nulo.
        @NotNull(message = "La dirección no puede ser nula.")
        // Importante! Activa la validación en cascada. Sin @Valid, los campos
        // de AddressDTO no serían validados.
        @Valid
        AddressDTO address
) {
}
