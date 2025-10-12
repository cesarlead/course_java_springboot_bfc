package org.cesarlead.persistencia.service;

import org.cesarlead.persistencia.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    Optional<Customer> getCustomerById(Long id);

    List<Customer> getAllCustomers();

    Customer updateCustomer(Long id, Customer customerDetails);

    void deleteCustomer(Long id);

    List<Customer> searchCustomers(String name, String emailFragment, Boolean isPremium);
}
