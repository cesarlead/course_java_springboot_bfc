package com.cesarlead.demo.repository;

import org.springframework.stereotype.Repository;

@Repository
public class GreetingRepository {

    public String getGreetingData() {
        // En un caso real, aquí hablaríamos con una base de datos.
        // Hoy, simplemente devolveremos el saludo base.
        return "Hola Mundo";
    }
}
