package com.cesarlead.course_service.service;

import com.cesarlead.course_service.dto.InscripcionDto;

import java.util.List;

public interface InscripcionService {
    InscripcionDto crearInscripcion(InscripcionDto inscripcionDto);

    List<Long> getEstudiantesPorCursoId(Long cursoId);
}
