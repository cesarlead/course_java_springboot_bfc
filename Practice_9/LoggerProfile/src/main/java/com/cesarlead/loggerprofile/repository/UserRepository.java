package com.cesarlead.loggerprofile.repository;

import com.cesarlead.loggerprofile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA crea los métodos CRUD automáticamente
}