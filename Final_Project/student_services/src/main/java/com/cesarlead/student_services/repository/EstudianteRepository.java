package com.cesarlead.student_services.repository;

import com.cesarlead.student_services.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // Método para verificar duplicados
    Optional<Estudiante> findByEmail(String email);
}
