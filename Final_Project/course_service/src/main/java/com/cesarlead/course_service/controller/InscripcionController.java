package com.cesarlead.course_service.controller;

import com.cesarlead.course_service.dto.InscripcionDto;
import com.cesarlead.course_service.service.InscripcionService;
import com.cesarlead.course_service.util.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.RUTA_INSCRIPCIONES)
@RequiredArgsConstructor
@Tag(name = "API de Inscripciones", description = "Gestiona las inscripciones de estudiantes a cursos")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @Operation(summary = "Inscribir un estudiante a un curso")
    @ApiResponse(responseCode = "201", description = "Inscripción creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "El curso no existe")
    @ApiResponse(responseCode = "409", description = "El estudiante ya está inscrito")
    @PostMapping
    public ResponseEntity<InscripcionDto> crearInscripcion(@Valid @RequestBody InscripcionDto inscripcionDto) {
        InscripcionDto nuevaInscripcion = inscripcionService.crearInscripcion(inscripcionDto);
        return new ResponseEntity<>(nuevaInscripcion, HttpStatus.CREATED);
    }
}
