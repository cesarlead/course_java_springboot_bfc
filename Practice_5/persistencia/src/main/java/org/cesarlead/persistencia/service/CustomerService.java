package org.cesarlead.persistencia.service;

import org.cesarlead.persistencia.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    Customer getCustomerById(Long id);

    List<Customer> getAllCustomers();

    Customer updateCustomer(Long id, Customer customerDetails);

    void deleteCustomer(Long id);

    List<Customer> searchCustomers(String name, String emailFragment, Boolean isPremium);
}
