package com.cesarlead.document.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Mi API de CesarLead",
                version = "v1.0.0",
                description = "API REST para la gestión de productos y pedidos.",
                contact = @Contact(
                        name = "Soporte Técnico",
                        email = "soporte@cesarlead.com",
                        url = "https://cesarlead.com/"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class OpenApiConfig {
    // Esta clase está vacía a propósito.
    // Solo usamos la anotación a nivel de clase para la configuración global.
}
