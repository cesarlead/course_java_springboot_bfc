package com.cesarlead.loggerprofile.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LegacyLogService {

    // --- El Mal Log: Forma Clásica y Verbosa ---
    // 1. Es 'static final', pero debe ser instanciado en CADA clase.
    // 2. Es propenso a errores (copiar/pegar y olvidar cambiar 'LegacyLogService.class')
    private static final Logger logger = LoggerFactory.getLogger(LegacyLogService.class);

    public void logLegacy(String transactionId) {
        // --- La Mala Práctica: Concatenación de Strings ---
        // 1. Ineficiente: Crea nuevos objetos String en memoria.
        // 2. Ilegible: Difícil de leer con muchas variables.
        // 3. Costoso: LA OPERACIÓN DE CONCATENACIÓN SE HACE SIEMPRE,
        //    incluso si el nivel de log (ej. DEBUG) está deshabilitado.
        logger.debug("MALA PRÁCTICA: Procesando transacción " + transactionId + " para el usuario " + "admin");

        System.out.println("PEOR PRÁCTICA: Transacción " + transactionId + " en System.out");
    }
}