package com.cesarlead.demo.controller;

import com.cesarlead.demo.dto.UserCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        // Lógica para crear el usuario...
        // Si la validación pasa, este código se ejecuta.
        return ResponseEntity.ok("Usuario creado exitosamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(
            @PathVariable
            @Min(value = 1, message = "El ID debe ser un número positivo.") Long id
    ) {
        // Lógica para buscar el usuario...
        return ResponseEntity.ok("Buscando usuario con ID: " + id);
    }


}
