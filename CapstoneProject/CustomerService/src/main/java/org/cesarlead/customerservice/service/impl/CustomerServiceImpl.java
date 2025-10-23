package org.cesarlead.customerservice.service.impl;


import org.cesarlead.customerservice.dto.CustomerRequestDTO;
import org.cesarlead.customerservice.dto.CustomerResponseDTO;
import org.cesarlead.customerservice.exception.DuplicateResourceException;
import org.cesarlead.customerservice.exception.ResourceNotFoundException;
import org.cesarlead.customerservice.mapper.MapperCustomer;
import org.cesarlead.customerservice.model.Customer;
import org.cesarlead.customerservice.repository.CustomerRepository;
import org.cesarlead.customerservice.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final MapperCustomer mapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, MapperCustomer mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(mapper::mapToCustomerResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = findCustomerEntityById(id);
        return mapper.mapToCustomerResponseDTO(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        // Validation: Check for duplicate email
        customerRepository.findByEmail(request.email()).ifPresent(c -> {
            throw new DuplicateResourceException("El email '" + request.email() + "' ya está en uso.");
        });

        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());

        Customer savedCustomer = customerRepository.save(customer);
        return mapper.mapToCustomerResponseDTO(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {
        Customer customerToUpdate = findCustomerEntityById(id);

        // Validation: Check if the new email is already taken by *another* customer
        customerRepository.findByEmail(request.email()).ifPresent(existingCustomer -> {
            if (!existingCustomer.getId().equals(id)) {
                throw new DuplicateResourceException("El email '" + request.email() + "' ya está en uso.");
            }
        });

        customerToUpdate.setName(request.name());
        customerToUpdate.setEmail(request.email());

        Customer updatedCustomer = customerRepository.save(customerToUpdate);
        return mapper.mapToCustomerResponseDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customerToDelete = findCustomerEntityById(id);
        customerRepository.delete(customerToDelete);
    }


    private Customer findCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

}
