package com.cesarlead.student_services.dto;

import java.time.LocalDateTime;

public record EstudianteDto(
        Long id,
        String nombre,
        String apellido,
        String email,
        LocalDateTime fechaCreacion
) {
}
