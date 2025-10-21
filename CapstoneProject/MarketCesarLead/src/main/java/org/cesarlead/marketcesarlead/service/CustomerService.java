package org.cesarlead.marketcesarlead.service;

import org.cesarlead.marketcesarlead.exception.ResourceNotFoundException;
import org.cesarlead.marketcesarlead.model.Customer;
import org.cesarlead.marketcesarlead.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + customerId));
    }
}
