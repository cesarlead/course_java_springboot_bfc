package org.cesarlead.documentexport.client;


import org.cesarlead.documentexport.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class CustomerApiClient {

    private final RestTemplate restTemplate;
    private final String customerServiceUrl;

    public CustomerApiClient(RestTemplate restTemplate,
                             @Value("${client.customer-service.url}") String customerServiceUrl) {
        this.restTemplate = restTemplate;
        this.customerServiceUrl = customerServiceUrl;
    }

    /**
     * Llama al customer-service para obtener detalles del cliente.
     * Devuelve un Optional para manejar elegantemente el caso de 404.
     */
    public Optional<CustomerDTO> getCustomerById(Long customerId) {
        String url = customerServiceUrl + "/" + customerId;
        try {
            CustomerDTO customer = restTemplate.getForObject(url, CustomerDTO.class);
            return Optional.ofNullable(customer);
        } catch (HttpClientErrorException.NotFound e) {
            // Si el customer-service devuelve 404, lo capturamos y devolvemos un Optional vacío.
            return Optional.empty();
        }
        // Otras excepciones (p.ej., 500, timeout) se propagarán
    }
}
