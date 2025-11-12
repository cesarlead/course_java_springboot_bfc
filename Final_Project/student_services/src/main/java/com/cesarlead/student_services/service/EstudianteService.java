package com.cesarlead.student_services.service;

import com.cesarlead.student_services.dto.CrearEstudianteDto;
import com.cesarlead.student_services.dto.EstudianteDto;

import java.util.List;

public interface EstudianteService {
    EstudianteDto crearEstudiante(CrearEstudianteDto crearDto);

    EstudianteDto getEstudiantePorId(Long id);

    List<EstudianteDto> getTodosLosEstudiantes();
}
