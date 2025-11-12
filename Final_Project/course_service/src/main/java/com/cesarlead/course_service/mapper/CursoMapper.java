package com.cesarlead.course_service.mapper;

import com.cesarlead.course_service.dto.CrearCursoDto;
import com.cesarlead.course_service.dto.CursoDto;
import com.cesarlead.course_service.model.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoDto toDto(Curso curso) {
        return new CursoDto(
                curso.getId(),
                curso.getTitulo(),
                curso.getDescripcion()
        );

    }

    public Curso toEntity(CrearCursoDto dto) {
        Curso curso = new Curso();
        curso.setTitulo(dto.titulo());
        curso.setDescripcion(dto.descripcion());
        return curso;
    }
}
