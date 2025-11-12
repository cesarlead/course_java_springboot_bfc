package com.cesarlead.course_service.controller;

import com.cesarlead.course_service.dto.CrearCursoDto;
import com.cesarlead.course_service.dto.CursoDto;
import com.cesarlead.course_service.service.CursoService;
import com.cesarlead.course_service.service.InscripcionService;
import com.cesarlead.course_service.util.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.RUTA_CURSOS)
@RequiredArgsConstructor
@Tag(name = "API de Cursos", description = "Gestiona el CRUD de cursos")
public class CursoController {

    private final CursoService cursoService;
    private final InscripcionService inscripcionService;

    @Operation(summary = "Crear un nuevo curso")
    @ApiResponse(responseCode = "201", description = "Curso creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<CursoDto> crearCurso(@Valid @RequestBody CrearCursoDto crearDto) {
        CursoDto nuevoCurso = cursoService.crearCurso(crearDto);
        return new ResponseEntity<>(nuevoCurso, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener un curso por su ID")
    @ApiResponse(responseCode = "200", description = "Curso encontrado")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<CursoDto> getCursoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.getCursoPorId(id));
    }


    @Operation(summary = "Obtener IDs de estudiantes inscritos en un curso")
    @ApiResponse(responseCode = "200", description = "Lista de IDs de estudiantes")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @GetMapping("/{cursoId}/estudiantes")
    public ResponseEntity<List<Long>> getEstudiantesPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(inscripcionService.getEstudiantesPorCursoId(cursoId));
    }
}
