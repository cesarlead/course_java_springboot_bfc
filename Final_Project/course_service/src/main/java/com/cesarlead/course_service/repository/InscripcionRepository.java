package com.cesarlead.course_service.repository;

import com.cesarlead.course_service.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByCursoId(Long cursoId);

    Optional<Inscripcion> findByCursoIdAndEstudianteId(Long cursoId, Long estudianteId);
}
