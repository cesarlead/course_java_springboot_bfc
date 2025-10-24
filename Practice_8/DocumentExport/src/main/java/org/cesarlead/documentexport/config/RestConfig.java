package org.cesarlead.documentexport.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Creamos un Bean de RestTemplate.
        // Spring lo inyectará donde lo necesitemos.
        return builder.build();
    }
}
