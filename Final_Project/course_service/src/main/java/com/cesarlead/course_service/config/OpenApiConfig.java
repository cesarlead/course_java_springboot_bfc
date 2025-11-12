package com.cesarlead.course_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApiConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Cursos e Inscripciones")
                        .version("1.0.0")
                        .description("Microservicio para gestionar el catálogo de cursos y las inscripciones de los estudiantes.") // Modificado
                        .contact(new Contact()
                                .name("Cesar Fernandez (CesarLead)")
                                .email("service@cesarlead.com")
                                .url("https://cesarlead.com/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                );
    }
}
