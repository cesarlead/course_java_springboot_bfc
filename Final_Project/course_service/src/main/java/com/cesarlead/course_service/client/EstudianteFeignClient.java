package com.cesarlead.course_service.client;

import com.cesarlead.course_service.dto.EstudianteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
 * name = "student-services": Debe coincidir con el nombre en application.properties
 * path = "/api/v1/estudiantes": Es la ruta base del controlador de estudiantes
 */
@FeignClient(
        name = "student-services",
        url = "http://localhost:8081",
        path = "/api/v1/estudiantes"
)
public interface EstudianteFeignClient {

    /*
     * Mapea al método "getEstudiantePorId" en EstudianteController.
     * Usamos ResponseEntity<> para poder manejar los códigos de estado.
     * Si Feign recibe un 404, lanzará una FeignException.NotFound
     */
    @GetMapping("/{id}")
    ResponseEntity<EstudianteResponse> getEstudiantePorId(@PathVariable("id") Long id);

    // NOTA: Una mejor práctica a futuro sería que 'student-services'
    // expusiera un endpoint HEAD /:id/exists que solo devuelva 200 o 404
    // y sea más ligero. Pero nos adaptamos a lo que existe (GET /:id).
    // Deben de implementar esta mejora muchachos!!
}
