package com.cesarlead.demo.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        if (isPaymentServiceAvailable()) {
            // Si el servicio funciona, respondemos UP y podemos añadir metadatos útiles.
            return Health.up()
                    .withDetail("service", "Servicio de Pagos Externo")
                    .withDetail("status", "Operativo y Disponible")
                    .build();
        } else {
            // Si falla, respondemos DOWN. Esto cambiará el estado general del microservicio.
            return Health.down()
                    .withDetail("service", "Servicio de Pagos Externo")
                    .withDetail("error", "El servicio no responde o devolvió un error crítico.")
                    .build();
        }
    }

    private boolean isPaymentServiceAvailable() {
        // En un caso real:
        // 1. Usar un cliente HTTP (ej. RestTemplate, WebClient) con un timeout corto.
        // 2. Apuntar a un endpoint de 'health' o 'status' del servicio externo, no a uno funcional.
        // 3. Simplemente retornamos 'true' para este ejemplo.
        return true;
    }

}
