package com.cesarlead.course_service.dto;

import com.cesarlead.course_service.util.ApiConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearCursoDto(
        @NotBlank(message = ApiConstants.TITULO_NO_BLANCO)
        @Size(min = 3, max = 255)
        String titulo,
        String descripcion
) {

}
