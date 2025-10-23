package org.cesarlead.customerservice.service;

import org.cesarlead.customerservice.dto.CustomerRequestDTO;
import org.cesarlead.customerservice.dto.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO getCustomerById(Long id);

    CustomerResponseDTO createCustomer(CustomerRequestDTO request);

    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request);

    void deleteCustomer(Long id);
}
