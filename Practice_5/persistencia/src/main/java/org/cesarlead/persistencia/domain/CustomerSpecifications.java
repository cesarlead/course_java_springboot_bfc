package org.cesarlead.persistencia.domain;

import org.cesarlead.persistencia.model.Customer;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CustomerSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Specification<Customer> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Customer> emailContains(String emailFragment) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + emailFragment + "%");
    }

    public static Specification<Customer> isPremiumCustomer() {
        // Asumiendo que hay un campo 'isPremium' de tipo boolean
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isPremium"));
    }
}

