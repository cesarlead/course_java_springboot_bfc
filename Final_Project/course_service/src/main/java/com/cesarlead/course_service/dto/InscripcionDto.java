package com.cesarlead.course_service.dto;

import com.cesarlead.course_service.util.ApiConstants;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InscripcionDto(
        Long id,

        @NotNull(message = ApiConstants.ID_CURSO_REQUERIDO)
        Long cursoId,

        @NotNull(message = ApiConstants.ID_ESTUDIANTE_REQUERIDO)
        Long estudianteId,

        LocalDateTime fechaInscripcion
) {

}
