package com.cesarlead.course_service.service.impl;

import com.cesarlead.course_service.client.EstudianteFeignClient;
import com.cesarlead.course_service.dto.InscripcionDto;
import com.cesarlead.course_service.exception.CursoNotFoundException;
import com.cesarlead.course_service.exception.ExternalServiceDownException;
import com.cesarlead.course_service.exception.InscripcionDuplicadaException;
import com.cesarlead.course_service.exception.StudentNotFoundException;
import com.cesarlead.course_service.mapper.InscripcionMapper;
import com.cesarlead.course_service.model.Inscripcion;
import com.cesarlead.course_service.repository.InscripcionRepository;
import com.cesarlead.course_service.service.CursoService;
import com.cesarlead.course_service.service.InscripcionService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final CursoService cursoService;
    private final InscripcionMapper mapper;
    private final EstudianteFeignClient estudianteFeignClient;

    @Transactional
    public InscripcionDto crearInscripcion(InscripcionDto inscripcionDto) {
        Long cursoId = inscripcionDto.cursoId();
        Long estudianteId = inscripcionDto.estudianteId();

        log.info("Iniciando inscripción para el curso {} y el estudiante {}", cursoId, estudianteId);

        log.info("Validando existencia de Estudiante ID: {}", estudianteId);
        validarEstudianteExiste(estudianteId);
        log.info("Estudiante ID: {} validado exitosamente", estudianteId);

        if (!cursoService.existsById(cursoId)) {
            log.warn("Intento de inscripción a curso no existente: {}", cursoId);
            throw new CursoNotFoundException("Curso no encontrado con ID: " + cursoId);
        }

        inscripcionRepository.findByCursoIdAndEstudianteId(cursoId, estudianteId)
                .ifPresent(inscripcion -> {
                    throw new InscripcionDuplicadaException("Inscripción duplicada para el curso " + cursoId + " y el estudiante " + estudianteId);
                });

        Inscripcion nuevaInscripcion = mapper.toEntity(inscripcionDto);
        Inscripcion guardada = inscripcionRepository.save(nuevaInscripcion);

        log.info("Inscripción creada con ID: {}", guardada.getId());
        return mapper.toDto(guardada);
    }

    @Transactional(readOnly = true)
    public List<Long> getEstudiantesPorCursoId(Long cursoId) {
        log.info("Buscando IDs de estudiantes para el curso: {}", cursoId);

        if (!cursoService.existsById(cursoId)) {
            log.warn("Búsqueda de estudiantes para curso no existente: {}", cursoId);
            throw new CursoNotFoundException("Curso no encontrado con ID: " + cursoId);
        }

        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(cursoId);

        return inscripciones.stream()
                .map(Inscripcion::getEstudianteId)
                .distinct()
                .toList();
    }

    /**
     * Método helper envuelto en un Circuit Breaker.
     * Si el servicio de estudiantes falla, el método 'fallbackValidarEstudiante' será llamado.
     */
    @CircuitBreaker(name = "estudiantes", fallbackMethod = "fallbackValidarEstudiante")
    public void validarEstudianteExiste(Long estudianteId) {
        try {
            // 1. Llamamos al cliente Feign.
            log.debug("Intentando validar Estudiante ID: {} vía Feign", estudianteId);
            estudianteFeignClient.getEstudiantePorId(estudianteId);

        } catch (FeignException.NotFound e) {
            // 2. ESTO ESTÁ BIEN: Es un error de negocio (404), no un fallo del sistema.
            //    El estudiante no existe, lo lanzamos como nuestra propia excepción.
            log.warn("Validación fallida: Estudiante no encontrado ID: {}", estudianteId);
            throw new StudentNotFoundException("Estudiante no encontrado con ID: " + estudianteId);

        }

        // Si Feign lanza cualquier otra excepción (503 Service Unavailable,
        // 500 Internal Error, Connection Refused, Timeout)...
        // la excepción se propagará "hacia arriba".
        // El @CircuitBreaker la interceptará, la contará como un fallo,
        // y ejecutará el 'fallbackMethod'.
    }

    /**
     * MÉTODO FALLBACK (Plan B)
     * Este método se ejecuta si el Circuit Breaker 'estudiantes' está ABIERTO
     * o si validarEstudianteExiste() lanza una excepción que no sea la 'NotFound'.
     *
     * @param estudianteId El argumento original del método
     * @param t            La excepción que causó el fallo (ahora la recibiremos)
     */
    public void fallbackValidarEstudiante(Long estudianteId, Throwable t) {
        // 't' será la FeignException$ServiceUnavailable
        log.error("Fallback activado para estudiante ID: {}. Causa: {}", estudianteId, t.getMessage());

        // Lanzamos nuestra excepción de servicio caído, que GlobalExceptionHandler
        // convertirá en un 503 Service Unavailable.
        throw new ExternalServiceDownException("El servicio de estudiantes no está disponible. Intente más tarde.");
    }

    // TODO: REFACTORIZAR CHICOS LOS MENSJAES PARA ELMININAR LOS MAGIC STRINGS
}
