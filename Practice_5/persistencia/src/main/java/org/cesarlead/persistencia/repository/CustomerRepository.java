package org.cesarlead.persistencia.repository;

import org.cesarlead.persistencia.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    // Busca un cliente por su dirección de email exacta
    Optional<Customer> findByEmail(String email);

    // Busca clientes cuyo nombre contiene una cadena de texto, ignorando mayúsculas/minúsculas
    List<Customer> findByNameContainingIgnoreCase(String nameFragment);

    // Busca clientes por nombre Y email
    List<Customer> findByNameAndEmail(String name, String email);

    // Busca los 3 clientes más antiguos ordenados por fecha de registro (asumiendo un campo 'registrationDate')
    List<Customer> findTop3ByOrderByCreationDateAsc();

    // Custom
    // Consulta JPQL para buscar clientes por un fragmento de nombre (insensible a mayúsculas/minúsculas)
    @Query("SELECT c FROM Customer c WHERE lower(c.name) LIKE lower(concat('%', :nameFragment, '%'))")
    List<Customer> findCustomersByNameFragment(@Param("nameFragment") String nameFragment);

    // Consulta JPQL para actualizar el email de un cliente
    @Modifying
    @Query("UPDATE Customer c SET c.email = :newEmail WHERE c.id = :id")
    int updateCustomerEmail(@Param("id") Long id, @Param("newEmail") String newEmail);

    // Consulta SQL nativa para buscar un cliente por email
    @Query(value = "SELECT * FROM customers WHERE email = :email", nativeQuery = true)
    Optional<Customer> findByEmailNative(@Param("email") String email);

}

