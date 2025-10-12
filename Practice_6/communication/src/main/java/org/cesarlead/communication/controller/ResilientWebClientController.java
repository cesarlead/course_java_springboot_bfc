package org.cesarlead.communication.controller;

import org.cesarlead.communication.dto.ProductDTO;
import org.cesarlead.communication.service.ResilientWebClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/resilient-webclient/products")
public class ResilientWebClientController {

    private final ResilientWebClientService webClientService;

    public ResilientWebClientController(ResilientWebClientService webClientService) {
        this.webClientService = webClientService;
    }

    @GetMapping("/{id}")
    public Mono<ProductDTO> getProductById(@PathVariable Long id) {
        return webClientService.findProductById(id);
    }
}
