package org.cesarlead.customerservice.service;

import org.cesarlead.customerservice.model.Customer;
import org.cesarlead.customerservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    public Optional<Customer> findCustomerById(Long id) {

        return customerRepository.findById(id);
    }
}
