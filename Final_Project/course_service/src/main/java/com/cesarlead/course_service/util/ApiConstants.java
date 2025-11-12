package com.cesarlead.course_service.util;

public final class ApiConstants {

    private ApiConstants() {
    }

    // Rutas base
    public static final String API_V1_BASE = "/api/v1";
    public static final String RUTA_CURSOS = API_V1_BASE + "/cursos";
    public static final String RUTA_INSCRIPCIONES = API_V1_BASE + "/inscripciones";

    // Mensajes de validación
    public static final String TITULO_NO_BLANCO = "El título no puede estar vacío";
    public static final String ID_CURSO_REQUERIDO = "El ID del curso es requerido";
    public static final String ID_ESTUDIANTE_REQUERIDO = "El ID del estudiante es requerido";

    // Trazabilidad
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String TRACE_ID_MDC_KEY = "traceId";
}
