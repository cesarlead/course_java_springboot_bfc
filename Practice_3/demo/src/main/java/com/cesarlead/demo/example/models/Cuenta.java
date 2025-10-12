package com.cesarlead.demo.example.models;

import java.math.BigDecimal;

public record Cuenta(Integer id, String numeroCuenta, String nombreTitular, BigDecimal saldo) {
}
