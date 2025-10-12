package org.cesarlead.communication.client;

import org.cesarlead.communication.dto.ConfigDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "config-service", url = "${product-service.url}") // Reutilizamos la URL base
public interface ConfigFeignClient {

    @GetMapping("/config/app-info")
    ConfigDTO getAppConfig();
}
