package com.cesarlead.student_services.mapper;

import com.cesarlead.student_services.dto.CrearEstudianteDto;
import com.cesarlead.student_services.dto.EstudianteDto;
import com.cesarlead.student_services.model.Estudiante;
import org.springframework.stereotype.Component;

@Component
public class MapperEstudiantes {

    public Estudiante mapToEntity(CrearEstudianteDto dto) {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(dto.nombre());
        estudiante.setApellido(dto.apellido());
        estudiante.setEmail(dto.email());
        // fechaCreacion se establece con @PrePersist
        return estudiante;
    }

    public EstudianteDto mapToDto(Estudiante estudiante) {
        return new EstudianteDto(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getEmail(),
                estudiante.getFechaCreacion()
        );
    }
}
