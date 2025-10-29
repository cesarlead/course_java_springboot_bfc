package com.cesarlead.loggerprofile.service;

import com.cesarlead.loggerprofile.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementación MOCK del servicio de email.
 * Esta implementación SÓLO se activará si el perfil "dev" está activo.
 */
@Service
@Profile("dev") // Solo se crea este bean en perfil "dev"
@Slf4j
public class MockEmailService implements EmailService {

    @Override
    public void sendWelcomeEmail(UserDTO user) {
        log.info("--- MOCK EMAIL ---");
        log.info("TO: {}", user.getEmail());
        log.info("SUBJECT: ¡Bienvenido a la plataforma, {}!", user.getName());
        log.info("BODY: Este es un email de prueba. No se ha enviado ningún correo real.");
        log.info("--------------------");
    }
}