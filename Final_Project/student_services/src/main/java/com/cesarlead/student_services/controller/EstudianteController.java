package com.cesarlead.student_services.controller;

import com.cesarlead.student_services.dto.CrearEstudianteDto;
import com.cesarlead.student_services.dto.EstudianteDto;
import com.cesarlead.student_services.service.EstudianteService;
import com.cesarlead.student_services.util.ApiConstants;
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
@RequestMapping(ApiConstants.RUTA_ESTUDIANTES)
@RequiredArgsConstructor
@Tag(name = "API de Estudiantes", description = "Gestiona el CRUD de estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @Operation(summary = "Crear un nuevo estudiante")
    @ApiResponse(responseCode = "201", description = "Estudiante creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "409", description = "El email ya existe")
    @PostMapping
    public ResponseEntity<EstudianteDto> crearEstudiante(@Valid @RequestBody CrearEstudianteDto crearDto) {
        EstudianteDto nuevoEstudiante = estudianteService.crearEstudiante(crearDto);
        // Devolvemos 201 Created
        return new ResponseEntity<>(nuevoEstudiante, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener un estudiante por su ID")
    @ApiResponse(responseCode = "200", description = "Estudiante encontrado")
    @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDto> getEstudiantePorId(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.getEstudiantePorId(id));
    }

    @Operation(summary = "Obtener todos los estudiantes")
    @ApiResponse(responseCode = "200", description = "Lista de estudiantes")
    @GetMapping
    public ResponseEntity<List<EstudianteDto>> getTodosLosEstudiantes() {
        return ResponseEntity.ok(estudianteService.getTodosLosEstudiantes());
    }

    //TODO: CREAR ENDPOINT EXISTE STUDENT
}
