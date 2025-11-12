package com.cesarlead.student_services.service.impl;

import com.cesarlead.student_services.dto.CrearEstudianteDto;
import com.cesarlead.student_services.dto.EstudianteDto;
import com.cesarlead.student_services.exception.EmailDuplicadoException;
import com.cesarlead.student_services.exception.EstudianteNotFoundException;
import com.cesarlead.student_services.mapper.MapperEstudiantes;
import com.cesarlead.student_services.model.Estudiante;
import com.cesarlead.student_services.repository.EstudianteRepository;
import com.cesarlead.student_services.service.EstudianteService;
import com.cesarlead.student_services.util.ApiConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final MapperEstudiantes mapper;

    @Override
    @Transactional
    public EstudianteDto crearEstudiante(CrearEstudianteDto crearDto) {
        log.error("Iniciando creación de estudiante con email: {}", crearDto.email());

        estudianteRepository.findByEmail(crearDto.email()).ifPresent(e -> {
            log.warn("Intento de crear estudiante con email duplicado: {}", crearDto.email());
            throw new EmailDuplicadoException(crearDto.email());
        });

        Estudiante estudiante = mapper.mapToEntity(crearDto);
        Estudiante guardado = estudianteRepository.save(estudiante);

        log.info("Estudiante creado con ID: {}", guardado.getId());
        return mapper.mapToDto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteDto getEstudiantePorId(Long id) {
        log.info("Buscando estudiante con ID: {}", id);
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new EstudianteNotFoundException(ApiConstants.ESTUDIANTE_NO_ENCONTRADO, id));
        return mapper.mapToDto(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteDto> getTodosLosEstudiantes() {
        log.info("Buscando todos los estudiantes");
        return estudianteRepository.findAll().stream()
                .map(mapper::mapToDto)
                .toList();
    }

}
