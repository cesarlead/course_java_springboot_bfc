package com.cesarlead.loggerprofile.service;

import com.cesarlead.loggerprofile.dto.UserDTO;

public interface EmailService {

    /**
     * Envía un correo de bienvenida al usuario.
     * @param user El DTO del usuario al que se le enviará el correo.
     */
    void sendWelcomeEmail(UserDTO user);
}