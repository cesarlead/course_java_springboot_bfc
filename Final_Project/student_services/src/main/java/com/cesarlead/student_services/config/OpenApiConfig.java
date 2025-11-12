package com.cesarlead.student_services.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Define un Bean de tipo OpenAPI para configurar los metadatos
     * de la documentación de Swagger.
     *
     * @return Un objeto OpenAPI configurado.
     */
    @Bean
    public OpenAPI customOpenApiConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Estudiantes")
                        .version("1.0.0")
                        .description("Microservicio para gestionar la información base (CRUD) de los estudiantes de la Academia Digital.")
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
