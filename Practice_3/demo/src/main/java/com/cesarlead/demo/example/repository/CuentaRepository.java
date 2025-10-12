package com.cesarlead.demo.example.repository;

import com.cesarlead.demo.example.models.Cuenta;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CuentaRepository {
    private final Map<Integer, Cuenta> cuentas = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(2); // Empezamos en 2 porque ya tenemos 2 cuentas

    // Datos de prueba precargados
    {
        cuentas.put(1, new Cuenta(1, "ES1234567890", "César Lead", new BigDecimal("1500.75")));
        cuentas.put(2, new Cuenta(2, "ES0987654321", "Luis GlobalDesk", new BigDecimal("3250.00")));
    }

    public List<Cuenta> findAll() {
        return new ArrayList<>(cuentas.values());
    }

    public Optional<Cuenta> findById(int id) {
        return Optional.ofNullable(cuentas.get(id));
    }

    public Cuenta save(Cuenta cuenta) {
        if (cuenta.id() == null) {
            int newId = idCounter.incrementAndGet();
            cuenta = new Cuenta(newId,
                    cuenta.numeroCuenta(),
                    cuenta.nombreTitular(),
                    cuenta.saldo());
        }
        cuentas.put(cuenta.id(), cuenta);
        return cuenta;
    }

    public void deleteById(int id) {
        cuentas.remove(id);
    }

    public boolean existsById(int id) {
        return cuentas.containsKey(id);
    }
}
