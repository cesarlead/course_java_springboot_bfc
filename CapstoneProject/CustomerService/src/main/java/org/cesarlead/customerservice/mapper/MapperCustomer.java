package org.cesarlead.customerservice.mapper;

import org.cesarlead.customerservice.dto.CustomerResponseDTO;
import org.cesarlead.customerservice.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class MapperCustomer {
    public CustomerResponseDTO mapToCustomerResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }

}
