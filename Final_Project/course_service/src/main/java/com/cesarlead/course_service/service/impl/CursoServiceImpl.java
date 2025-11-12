package com.cesarlead.course_service.service.impl;

import com.cesarlead.course_service.dto.CrearCursoDto;
import com.cesarlead.course_service.dto.CursoDto;
import com.cesarlead.course_service.exception.CursoNotFoundException;
import com.cesarlead.course_service.mapper.CursoMapper;
import com.cesarlead.course_service.model.Curso;
import com.cesarlead.course_service.repository.CursoRepository;
import com.cesarlead.course_service.service.CursoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper mapper;

    @Transactional
    public CursoDto crearCurso(CrearCursoDto crearDto) {
        log.info("Creando nuevo curso con título: {}", crearDto.titulo());
        Curso curso = mapper.toEntity(crearDto);
        Curso guardado = cursoRepository.save(curso);
        log.info("Curso creado con ID: {}", guardado.getId());
        return mapper.toDto(guardado);
    }

    @Transactional(readOnly = true)
    public CursoDto getCursoPorId(Long id) {
        log.info("Buscando curso con ID: {}", id);
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Curso no encontrado con ID: " + id));
        return mapper.toDto(curso);
    }

    public boolean existsById(Long id) {
        return cursoRepository.existsById(id);
    }
}
