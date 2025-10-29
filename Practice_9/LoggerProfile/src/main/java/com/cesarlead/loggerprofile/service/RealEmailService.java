package com.cesarlead.loggerprofile.service;

import com.cesarlead.loggerprofile.config.AppProperties;
import com.cesarlead.loggerprofile.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!dev") // Se activa si el perfil NO es "dev"
@Slf4j
@RequiredArgsConstructor
public class RealEmailService implements EmailService {

    private final AppProperties appProperties;

    @Override
    public void sendWelcomeEmail(UserDTO user) {
        log.warn("--- REAL EMAIL (Simulación) ---");
        log.warn("Intentando enviar email real a: {}", user.getEmail());

        // Obtenemos la key de las propiedades cargadas por el perfil
        String apiKey = appProperties.getApiKey();

        // Esta línea ahora es segura
        log.warn("Usando servicio de notificaciones... (API Key en uso: ...{})",
                apiKey.substring(apiKey.length() - 4));

        log.warn("Email para {} enviado exitosamente.", user.getName());
        log.warn("--------------------------------");
    }
}