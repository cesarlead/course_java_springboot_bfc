package com.cesarlead.course_service.mapper;

import com.cesarlead.course_service.dto.InscripcionDto;
import com.cesarlead.course_service.model.Inscripcion;
import org.springframework.stereotype.Component;

@Component
public class InscripcionMapper {

    public InscripcionDto toDto(Inscripcion inscripcion) {
        return new InscripcionDto(
                inscripcion.getId(),
                inscripcion.getCursoId(),
                inscripcion.getEstudianteId(),
                inscripcion.getFechaInscripcion()
        );

    }

    public Inscripcion toEntity(InscripcionDto dto) {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setCursoId(dto.cursoId());
        inscripcion.setEstudianteId(dto.estudianteId());
        return inscripcion;
    }
}
