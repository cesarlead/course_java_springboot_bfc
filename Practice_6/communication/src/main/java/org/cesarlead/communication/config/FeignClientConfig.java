package org.cesarlead.communication.config;

import feign.codec.ErrorDecoder;
import org.cesarlead.communication.client.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
