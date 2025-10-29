package com.cesarlead.loggerprofile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String name;

    @Email(message = "El formato del email es inválido")
    @NotEmpty(message = "El email no puede estar vacío")
    private String email;
}