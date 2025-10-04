package com.cesarlead.demo.example.service;

import com.cesarlead.demo.example.dtos.CrearCuentaDTO;
import com.cesarlead.demo.example.models.Cuenta;
import com.cesarlead.demo.example.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {
    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> findById(int id) {
        return cuentaRepository.findById(id);
    }

    public Cuenta crearCuenta(CrearCuentaDTO crearCuentaDTO) {
        // Lógica de negocio: una nueva cuenta siempre se crea con saldo cero.
        Cuenta nuevaCuenta = new Cuenta(null,
                crearCuentaDTO.numeroCuenta(),
                crearCuentaDTO.nombreTitular(),
                BigDecimal.ZERO);
        return cuentaRepository.save(nuevaCuenta);
    }

    public Optional<Cuenta> actualizarCuenta(int id, Cuenta cuentaActualizada) {
        if (!cuentaRepository.existsById(id)) {
            return Optional.empty();
        }
        // Creamos una nueva instancia con el ID correcto para asegurar la inmutabilidad
        Cuenta cuentaGuardar = new Cuenta(id,
                cuentaActualizada.numeroCuenta(),
                cuentaActualizada.nombreTitular(),
                cuentaActualizada.saldo());
        return Optional.of(cuentaRepository.save(cuentaGuardar));
    }

    public boolean deleteById(int id) {
        if (!cuentaRepository.existsById(id)) {
            return false;
        }
        cuentaRepository.deleteById(id);
        return true;
    }
}
