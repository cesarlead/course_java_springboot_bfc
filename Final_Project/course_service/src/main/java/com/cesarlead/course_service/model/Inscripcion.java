package com.cesarlead.course_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones", indexes = {
        // Índice para buscar rápidamente por curso
        @Index(name = "idx_curso_id", columnList = "cursoId"),
        // Índice para asegurar que un estudiante no se inscriba dos veces al mismo curso
        @Index(name = "idx_curso_estudiante_unico", columnList = "cursoId, estudianteId", unique = true)
})
@Data
@NoArgsConstructor
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cursoId;

    @Column(nullable = false)
    private Long estudianteId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaInscripcion;

    @PrePersist
    protected void alCrear() {
        this.fechaInscripcion = LocalDateTime.now();
    }
}
