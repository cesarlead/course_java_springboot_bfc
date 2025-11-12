package com.cesarlead.student_services.dto;

import com.cesarlead.student_services.util.ApiConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearEstudianteDto(
        @NotBlank(message = ApiConstants.NOMBRE_NO_BLANCO)
        @Size(min = 2, max = 100)
        String nombre,

        @NotBlank(message = ApiConstants.APELLIDO_NO_BLANCO)
        @Size(min = 2, max = 100)
        String apellido,

        @NotBlank
        @Email(message = ApiConstants.EMAIL_NO_VALIDO)
        String email
) {
}
