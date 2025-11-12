package com.cesarlead.student_services.util;

public class ApiConstants {
    private ApiConstants() {
    }

    // Rutas base
    public static final String API_V1_BASE = "/api/v1";
    public static final String RUTA_ESTUDIANTES = API_V1_BASE + "/estudiantes";

    // Mensajes de error de validación
    public static final String NOMBRE_NO_BLANCO = "El nombre no puede estar vacío";
    public static final String APELLIDO_NO_BLANCO = "El apellido no puede estar vacío";
    public static final String EMAIL_NO_VALIDO = "El formato del email no es válido";

    // Trazabilidad
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String TRACE_ID_MDC_KEY = "traceId";

    // Mensajes de error de negocio
    public static final String EMAIL_DUPLICADO = "El email ya existe {}";
    public static final String ESTUDIANTE_NO_ENCONTRADO = "El estudiante no se encuentra {}";
}
