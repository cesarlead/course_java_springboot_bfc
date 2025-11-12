package com.cesarlead.course_service.service;

import com.cesarlead.course_service.dto.CrearCursoDto;
import com.cesarlead.course_service.dto.CursoDto;

public interface CursoService {
    CursoDto crearCurso(CrearCursoDto crearDto);

    CursoDto getCursoPorId(Long id);

    boolean existsById(Long id);

}
