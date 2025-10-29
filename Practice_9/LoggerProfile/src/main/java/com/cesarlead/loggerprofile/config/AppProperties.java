package com.cesarlead.loggerprofile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.config")
@Data
public class AppProperties {

    /**
     * La API Key para el servicio externo de notificaciones.
     */
    private String apiKey;

    /**
     * Flag para determinar si usamos el servicio de email mock.
     */
    private boolean emailMock;
}