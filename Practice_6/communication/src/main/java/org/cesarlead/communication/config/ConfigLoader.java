package org.cesarlead.communication.config;

import org.cesarlead.communication.client.ConfigFeignClient;
import org.cesarlead.communication.dto.ConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);

    private final ConfigFeignClient configFeignClient;
    private final AppConfiguration appConfiguration;

    public ConfigLoader(ConfigFeignClient configFeignClient, AppConfiguration appConfiguration) {
        this.configFeignClient = configFeignClient;
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Cargando configuración de la aplicación desde un servicio externo...");
        try {
            ConfigDTO config = configFeignClient.getAppConfig();
            appConfiguration.setConfig(config);
            log.info("--> Configuración cargada exitosamente. Versión: {}, Checkout Nuevo Habilitado: {}",
                    config.version(), appConfiguration.isNewCheckoutEnabled());
        } catch (Exception e) {
            log.error("!!! FATAL: No se pudo cargar la configuración de la aplicación. {}", e.getMessage());
            // Aquí se podría decidir si la aplicación debe fallar al arrancar.
        }
    }
}
