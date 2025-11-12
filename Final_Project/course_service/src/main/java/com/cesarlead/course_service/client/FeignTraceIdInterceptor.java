package com.cesarlead.course_service.client;

import com.cesarlead.course_service.util.ApiConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Interceptor de Feign para propagar el X-Trace-Id.
 * <p>
 * Esto lee el traceId del MDC del hilo actual (establecido por el LoggingFilter)
 * y lo añade como una cabecera a la petición Feign saliente.
 */
@Component
public class FeignTraceIdInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        // 1. Obtener el TraceId del MDC del hilo actual
        String traceId = MDC.get(ApiConstants.TRACE_ID_MDC_KEY);

        // 2. Si existe, añadirlo a la cabecera de la petición saliente
        if (traceId != null && !traceId.isEmpty()) {
            // Asegurarnos de no añadirlo si ya existe (aunque no debería)
            if (template.headers().get(ApiConstants.TRACE_ID_HEADER) == null) {
                template.header(ApiConstants.TRACE_ID_HEADER, traceId);
            }
        }
    }
}
