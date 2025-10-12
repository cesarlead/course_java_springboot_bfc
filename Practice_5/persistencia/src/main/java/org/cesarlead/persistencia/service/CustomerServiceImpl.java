package org.cesarlead.persistencia.service;

import org.cesarlead.persistencia.domain.CustomerSpecifications;
import org.cesarlead.persistencia.model.Customer;
import org.cesarlead.persistencia.repository.CustomerRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    public List<Customer> searchCustomers(String name, String emailFragment, Boolean isPremium) {

        // --- PASO 1: RECOPILACIÓN DE CONDICIONES ---
        // Creamos una lista para almacenar solo las especificaciones que realmente se usarán.
        List<Specification<Customer>> specifications = new ArrayList<>();

        // 2. Añadimos bloques a la lista SOLO SI el parámetro correspondiente existe.

        if (name != null && !name.isEmpty()) {
            specifications.add(CustomerSpecifications.hasName(name));
        }

        if (emailFragment != null && !emailFragment.isEmpty()) {
            specifications.add(CustomerSpecifications.emailContains(emailFragment));
        }

        if (isPremium != null && isPremium) {
            specifications.add(CustomerSpecifications.isPremiumCustomer());
        }

        // --- PASO 2: COMPOSICIÓN DE LA CONSULTA FINAL ---
        // Si la lista está vacía, no hay filtros que aplicar. Devolvemos todos los clientes.
        if (specifications.isEmpty()) {
            return customerRepository.findAll();
        }

        // Usamos Streams de Java para combinar todas las especificaciones de la lista.
        // El metodo 'reduce' aplica la operación 'and' entre cada elemento de la lista,
        // resultando en una única Specification que es la combinación de todas.
        Optional<Specification<Customer>> finalSpecification = specifications.stream()
                .reduce(Specification::and);

        // [
        //  Specification_1 { "el nombre debe ser 'Cesar'" },
        //  Specification_2 { "el email debe contener 'test.com'" },
        //  Specification_3 { "isPremium debe ser 'true'" }
        //]

        // El resultado de 'reduce' es un Optional, que contendrá la especificación combinada
        // si la lista no estaba vacía. Luego la pasamos al repositorio.
        return customerRepository.findAll(finalSpecification.get());

        //SELECT
        //    c.id,
        //    c.full_name,
        //    c.email,
        //    c.is_premium,
        //    c.creation_date
        //FROM
        //    customers c
        //WHERE
        //    c.full_name = 'Cesar'
        //    AND c.email LIKE '%test.com%'
        //    AND c.is_premium = true;
    }


}

