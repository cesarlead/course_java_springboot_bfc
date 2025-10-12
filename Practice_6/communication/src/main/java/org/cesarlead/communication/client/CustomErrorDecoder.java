package org.cesarlead.communication.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.cesarlead.communication.exeption.ProductNotFoundException;
import org.cesarlead.communication.exeption.ServiceUnavailableException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        // Primero, manejamos el caso específico de un 404 en la búsqueda de un producto.
        if (methodKey.contains("getProductById") && response.status() == 404) {
            return new ProductNotFoundException("Product not found. Invoked method: " + methodKey);
        }

        // Luego, manejamos errores genéricos del cliente (4xx).
        if (response.status() >= 400 && response.status() <= 499) {
            return new ProductNotFoundException("Client error: " + response.status() + " - " + response.reason());
        }

        // Y errores del servidor (5xx).
        if (response.status() >= 500 && response.status() <= 599) {
            return new ServiceUnavailableException("Service unavailable. Status: " + response.status());
        }

        // Para cualquier otro error, delegamos al decodificador por defecto.
        return defaultErrorDecoder.decode(methodKey, response);
    }
}