package org.cesarlead.customerservice.service;

import org.cesarlead.customerservice.model.Customer;

public interface ICustumerService {
    Customer findCustomerById(Long id);
}
