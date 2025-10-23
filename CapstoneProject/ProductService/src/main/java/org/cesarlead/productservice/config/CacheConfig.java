package org.cesarlead.productservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Spring Boot auto-configurará Caffeine basado en las propiedades
    // y la dependencia en el pom.xml.
    // Podemos añadir personalización aquí si es necesario.
}
