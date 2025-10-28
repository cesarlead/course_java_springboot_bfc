package org.cesarlead.customerservice.service;

import org.cesarlead.customerservice.exception.ResourceNotFoundException;
import org.cesarlead.customerservice.model.Customer;
import org.cesarlead.customerservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findCustomerById(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id: " + id)
                );
    }
}
