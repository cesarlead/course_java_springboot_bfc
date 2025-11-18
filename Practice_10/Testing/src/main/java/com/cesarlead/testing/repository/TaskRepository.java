package com.cesarlead.testing.repository;

import com.cesarlead.testing.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Spring Data JPA nos da mágicamente:
    // findById(), findAll(), save(), deleteById(), etc.
}
