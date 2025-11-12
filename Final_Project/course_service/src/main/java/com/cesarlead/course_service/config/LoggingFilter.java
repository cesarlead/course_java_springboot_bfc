package com.cesarlead.course_service.config;

import com.cesarlead.course_service.util.ApiConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el TraceId de la cabecera o generar uno nuevo
        String traceId = request.getHeader(ApiConstants.TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        try {
            // 2. Poner el TraceId en el MDC
            MDC.put(ApiConstants.TRACE_ID_MDC_KEY, traceId);

            // 3. Añadir el TraceId a la respuesta (para que el cliente lo vea)
            response.addHeader(ApiConstants.TRACE_ID_HEADER, traceId);

            // Continuar con la cadena de filtros
            filterChain.doFilter(request, response);

        } finally {
            // 4. Limpiar el MDC después de que la petición termine
            MDC.remove(ApiConstants.TRACE_ID_MDC_KEY);
        }
    }
}
