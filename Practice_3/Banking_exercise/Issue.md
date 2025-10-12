# **Ejercicio: Exposición de la API de Portafolios de Cliente**
## **Ing. Cesar Fernandez** | **GlobalDesk** | **Date: 2025-10-03**
### cesarlead.com

## **Contexto del Escenario**

Te incorporas al equipo de desarrollo del Core Bancario en **"Innovatec Bank"**. Se te asigna una tarea crítica que ha bloqueado al equipo de desarrollo móvil: el microservicio de consulta de portafolios no es funcional. El código base fue entregado por un proveedor externo, y aunque la lógica de negocio parece estar implementada, la aplicación es inestable y no expone ninguna funcionalidad. Los intentos de ejecutarla y hacer peticiones han resultado en fallos inesperados.

Tu tarea es realizar una reingeniería completa: estabilizar la aplicación, encontrar y corregir cualquier error, y finalmente, construir el endpoint que el equipo móvil necesita desesperadamente, siguiendo los más altos estándares de calidad.

## **Objetivo del Ejercicio**

1.  **Análisis del Código:** Diagnosticar y solucionar los problemas de raíz que impiden que la aplicación funcione, demostrando un entendimiento profundo del Contenedor de Spring y la Inyección de Dependencias.

2.  **Detección de Errores:** Identificar y corregir un error de implementacion, que provoca que los filtros devuelvan resultados incorrectos bajo ciertas condiciones.

3.  **Desarrollo de API Robusta:** Construir un endpoint RESTful, aplicando las mejores prácticas de diseño de API, incluyendo el uso de DTOs para las respuestas.

-----

### **Código Base del Proyecto**

Recibes el siguiente código fuente. La estructura de paquetes es `com.cesarlead.innovatec.banco` con sub-paquetes `model`, `repository`, y `service`.

#### **1. Modelo de Dominio: `Cuenta.java`**

```java
// package com.cesarlead.innovatec.banco.model;
public class Cuenta {
    private Integer id;
    private Integer clienteId;
    private String tipo;
    private String moneda;
    private BigDecimal saldo;
    // Getters, Setters y Constructor...
}
```

#### **2. Capa de Persistencia (Simulada): `CuentaRepository.java`**

```java
// package com.cesarlead.innovatec.banco.repository;
public class CuentaRepository {
    private final List<Cuenta> cuentas = new ArrayList<>();

    public CuentaRepository() {
        cuentas.add(new Cuenta(1, 101, "Ahorros", "USD", new BigDecimal("1250.75")));
        cuentas.add(new Cuenta(2, 101, "Corriente", "USD", new BigDecimal("5300.00")));
        cuentas.add(new Cuenta(3, 102, "Inversión", "EUR", new BigDecimal("8200.50")));
        cuentas.add(new Cuenta(4, 101, "Ahorros", "EUR", new BigDecimal("250.00")));
        cuentas.add(new Cuenta(5, 103, "Corriente", "USD", new BigDecimal("15000.00")));
    }

    public List<Cuenta> buscarPorClienteId(Integer clienteId) {
        return cuentas.stream()
                .filter(cuenta -> cuenta.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }
}
```

#### **3. Capa de Servicio: `PortfolioService.java`**

```java
// package com.cesarlead.innovatec.banco.service;
public class PortfolioService {
    private CuentaRepository cuentaRepository;

    public List<Cuenta> getPortfolioFiltrado(Integer clienteId, String tipoCuenta, BigDecimal saldoMinimo) {
        List<Cuenta> cuentasDelCliente = cuentaRepository.buscarPorClienteId(clienteId);
        Stream<Cuenta> stream = cuentasDelCliente.stream();

        if (tipoCuenta != null && !tipoCuenta.isEmpty()) {
            stream = stream.filter(c -> c.getTipo().equalsIgnoreCase(tipoCuenta));
        }
        if (saldoMinimo != null) {
            stream = stream.filter(c -> c.getSaldo().compareTo(saldoMinimo) > 0);
        }

        return stream.collect(Collectors.toList());
    }
}
```

-----

### **Tu Misión: Tareas a Realizar**

#### **Tarea 1: Estabilización de la Aplicación**

Tu primer objetivo es lograr que la aplicación arranque y sea estable. No se te indica dónde están los errores, debes descubrirlos.

#### **Tarea 2: Construcción y Diseño del `Controller`**

Con la aplicación estabilizada, debes construir la capa de exposición.

1.  Crea la clase `PortfolioController` bajo el paquete `com.cesarlead.innovatec.banco.controller`.
2.  Implementa un único endpoint que cumpla con los siguientes requisitos:
    * **Método y Ruta:** `GET /api/v1/portfolios/clientes/{clienteId}/cuentas`.
    * **Parámetros de Entrada:**
        * `clienteId`: Path Variable, obligatorio.
        * `tipoCuenta`: Query Parameter, opcional.
        * `saldoMinimo`: Query Parameter, opcional.
        * `moneda`: Query Parameter, opcional (ej: "USD", "EUR").

    * **DTO de Respuesta:** No debes devolver la entidad `Cuenta` directamente. Crea un `CuentaDTO` que contenga solo los campos `id`, `tipo`, `moneda` y `saldo`. El controlador debe transformar la lista de entidades a una lista de DTOs antes de responder.

    * **Manejo de Respuestas:** El controlador debe usar `ResponseEntity<>` para devolver:
        * `200 OK` con la lista de `CuentaDTO` si se encuentran resultados.
        * `404 Not Found` si el `clienteId` no corresponde a ninguna cuenta en el sistema.
        * `200 OK` con una lista vacía si el cliente existe, pero ningún producto cumple con los criterios de filtrado.

-----

### **Criterios de Aceptación**

Tu solución será correcta si la API se comporta de la siguiente manera:

1.  **Consulta General:** `GET /api/portfolios/clientes/101/cuentas`
    * **Resultado Esperado:** `200 OK` con un array de 3 cuentas (DTOs).

2.  **Filtro por Tipo:** `GET /api/portfolios/clientes/101/cuentas?tipoCuenta=Ahorros`
    * **Resultado Esperado:** `200 OK` con un array de 2 cuentas (DTOs).

3.  **Filtro por Moneda:** `GET /api/portfolios/clientes/101/cuentas?moneda=USD`
    * **Resultado Esperado:** `200 OK` con un array de 2 cuentas (DTOs).

4.  **Cliente Inexistente:** `GET /api/portfolios/clientes/999/cuentas`
    * **Resultado Esperado:** Código de estado `404 Not Found`.

5.  **Filtro sin Resultados:** `GET /api/portfolios/clientes/102/cuentas?tipoCuenta=Ahorros`
    * **Resultado Esperado:** `200 OK` con un array vacío `[]`.

6.  **Prueba de Lógica de Negocio (Límite Exacto):** `GET /api/portfolios/clientes/101/cuentas?saldoMinimo=5300.00`
    * **Resultado Esperado:** `200 OK` con un array que **INCLUYE** la cuenta corriente cuyo saldo es exactamente 5300.00.

7.  **Filtro Combinado Complejo:** `GET /api/portfolios/clientes/101/cuentas?moneda=USD&tipoCuenta=Corriente&saldoMinimo=5000`
    * **Resultado Esperado:** `200 OK` con un array de 1 cuenta (DTO).

### **Entregables**

1.  **Código Fuente:** El proyecto Spring Boot completo con todas las correcciones y la nueva implementación. (Compartirme repositorio de git)

2.  **Colección de Postman:** incluir Un archivo de colección para Postman (o similar) que permita ejecutar todas las pruebas listadas en los criterios de aceptación.

3.  **Documento de Análisis de Incidencia (`InformeDeIncidencia.md`):** Un documento en formato Markdown que explique de manera clara y profesional los problemas encontrados y cómo los solucionaste. Debe contener dos secciones:

    * **Diagnóstico del Fallo de Integración y Arranque:**
        * Describe por qué la aplicación no funcionaba al principio.
        * Explica qué conceptos de Spring (Beans, Inyección de Dependencias) no se estaban aplicando y cuál era la consecuencia directa de su ausencia.

        * Detalla la solución que implementaste para estabilizar el servicio.

    * **Análisis del Error de Lógica en el Filtrado:**

        * Describe el comportamiento incorrecto que detectaste en el filtro de saldo.

        * Proporciona un ejemplo concreto de una petición que arrojaba un resultado erróneo.

        * Explica la causa raíz del problema en el código y cómo lo corregiste para cumplir con el requisito de negocio.