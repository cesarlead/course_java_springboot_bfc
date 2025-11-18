package com.cesarlead.testing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean completed;

    public Task() {
    }

    // Constructores, Getters y Setters (Omitidos por brevedad)
    // Constructor para 'crearTarea'
    public Task(String title) {
        this.title = title;
        this.completed = false;
    }

    // Constructor para mocks
    public Task(Long id, String title, boolean completada) {
        this.id = id;
        this.title = title;
        this.completed = completada;
    }
}
