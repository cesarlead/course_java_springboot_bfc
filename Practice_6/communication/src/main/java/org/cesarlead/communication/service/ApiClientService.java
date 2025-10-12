package org.cesarlead.communication.service;

import com.sun.org.slf4j.internal.Logger;
import org.cesarlead.communication.dto.ProductCreationDTO;
import org.cesarlead.communication.dto.ProductDTO;
import org.cesarlead.communication.dto.ProductUpdateDTO;
import org.cesarlead.communication.dto.UserDTO;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ApiClientService {

    private static final Logger log = LoggerFactory.getLogger(ApiClientService.class);

    private final WebClient webClient;

    // Inyección de dependencias del WebClient configurado en WebClientConfig
    public ApiClientService(WebClient externalApiClient) {
        this.webClient = externalApiClient;
    }

    // --- LECTURA ---
    public Mono<UserDTO> getUserById(Long id) {
        log.info("CLIENTE: Solicitando usuario con ID: {}", id);
        return webClient
                .get() // 2. Metodo HTTP GET: Inicia la construcción de una petición GET.
                .uri("/users/{id}", id) // 3. URI: Define el endpoint. El placeholder "{id}" se reemplaza por el valor de la variable 'id'.
                .retrieve() // 4. Ejecución: Envía la petición y obtiene la respuesta. `retrieve()` es ideal para casos de éxito.
                //    Para manejo avanzado de errores (ej. 4xx, 5xx), se usaría `.exchangeToMono()` o `.exchangeToFlux()`.
                .bodyToMono(UserDTO.class); // 5. Deserialización: Convierte el cuerpo de la respuesta JSON a un objeto `Mono<UserDTO>`.
        //    'Mono' representa un flujo reactivo de 0 o 1 elemento.
    }

    public Flux<UserDTO> getAllUsers(String role) {
        log.info("CLIENTE: Solicitando todos los usuarios{}", (role != null && !role.isEmpty() ? " con rol: " + role : ""));
        return webClient
                .get() // 1. Metodo HTTP GET.
                .uri(uriBuilder -> { // 2. URI Builder: Permite construir URIs complejas de forma programática.
                    uriBuilder.path("/users");
                    if (role != null && !role.isEmpty()) {
                        // 3. Query Param: Añade un parámetro de consulta si 'role' no es nulo.
                        //    Ejemplo: /users?role=admin
                        uriBuilder.queryParam("role", role);
                    }
                    return uriBuilder.build(); // Construye la URI final.
                })
                .retrieve() // 4. Ejecución de la petición.
                .bodyToFlux(UserDTO.class); // 5. Deserialización a Flux: Convierte la respuesta (que se espera sea un array JSON)
        //    a un flujo de múltiples elementos `Flux<UserDTO>`.
    }

    // --- ESCRITURA ---
    public Mono<ProductDTO> createProduct(ProductCreationDTO newProduct) {
        log.info("CLIENTE: Creando producto: {}", newProduct.name());
        return webClient
                .post() // 1. Metodo HTTP POST: Inicia una petición para crear un nuevo recurso.
                .uri("/products") // 2. URI del endpoint de creación.
                .bodyValue(newProduct) // 3. Cuerpo de la petición: El objeto `newProduct` se serializa a JSON
                //    y se envía como el cuerpo (payload) de la petición.
                .retrieve() // 4. Ejecución.
                .bodyToMono(ProductDTO.class); // 5. Deserialización: Se espera que la API devuelva el producto recién creado,
        //    que se convierte en un `Mono<ProductDTO>`.
    }

    public Mono<ProductDTO> updateProduct(Long id, ProductUpdateDTO productUpdate) {
        log.info("CLIENTE: Actualizando producto ID {} con: {}", id, productUpdate.name());
        return webClient
                .put() // 1. Metodo HTTP PUT: Inicia una petición para reemplazar completamente un recurso existente.
                .uri("/products/{id}", id) // 2. URI del recurso específico a actualizar.
                .bodyValue(productUpdate) // 3. Cuerpo de la petición: Contiene los nuevos datos para el producto.
                .retrieve() // 4. Ejecución.
                .bodyToMono(ProductDTO.class); // 5. Deserialización de la respuesta.
    }

    public Mono<Void> deleteProduct(Long id) {
        log.info("CLIENTE: Eliminando producto ID: {}", id);
        return webClient
                .delete() // 1. Metodo HTTP DELETE: Inicia una petición para eliminar un recurso.
                .uri("/products/{id}", id) // 2. URI del recurso a eliminar.
                .retrieve() // 3. Ejecución.
                .bodyToMono(Void.class); // 4. Sin cuerpo: Para operaciones como DELETE, que suelen devolver una respuesta 204 No Content
        //    (sin cuerpo), se usa `Mono<Void>` para señalar que la operación ha finalizado.
    }
}