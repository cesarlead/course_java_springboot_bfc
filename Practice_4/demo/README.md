# Módulo 4: Arquitectura de API REST Profesional con Spring Boot

Este proyecto no es un solo demo, es una **arquitectura de referencia** que muestra cómo construir un microservicio
robusto, seguro y mantenible.

Implementa un conjunto de "mejores prácticas" de la industria que van más allá de un simple endpoint. Cubre la
arquitectura de N-Capas, la validación de datos, el manejo global de errores y el monitoreo.

## 1. Arquitectura de N-Capas (Separación de Conceptos)

El proyecto está dividido en capas claras, siguiendo el **Principio de Responsabilidad Única (SOLID)**.

* `Controller`: (Ej. `CuentaController`, `UserController`) Responsable *solo* de manejar la petición HTTP y la
  respuesta. No contiene lógica de negocio.
* `Service`: (Ej. `CuentaService`, `GreetingService`) Contiene toda la lógica de negocio (reglas, cálculos,
  orquestación).
* `Repository`: (Ej. `CuentaRepository`, `GreetingRepository`) Responsable *solo* de hablar con la fuente de datos (en
  este caso, una base de datos en memoria).

Usamos **Inyección de Dependencias por Constructor** en todas las capas, que es la práctica recomendada para garantizar
la inmutabilidad y facilitar las pruebas.

## 2. CRUD API con DTOs (El "Core" del Servicio)

El paquete `...example...` (`CuentaController`, `CuentaService`, `CuentaRepository`) implementa un servicio CRUD
completo para "Cuentas".

* **DTO (Data Transfer Objects)**: Usamos `CrearCuentaDTO` para la *entrada* y `Cuenta` (un `record`) como el *modelo*.
  Esto es vital para no exponer nuestro modelo de datos interno en la API.
* **Base de Datos en Memoria**: `CuentaRepository` simula una base de datos usando `ConcurrentHashMap` y
  `AtomicInteger`. Esto demuestra cómo gestionar el estado y la concurrencia (thread-safety) sin una base de datos real.

## 3. Validación de Datos (Defender tu API)

Este proyecto soluciona la debilidad del Módulo 3 usando `jakarta.validation`.

* **Validación de Body (`@Valid`)**: En `UserController`, el `@RequestBody` usa `@Valid` para activar las reglas de
  `UserCreateDTO` (`@NotBlank`, `@Email`, `@Size`).
* **Validación Anidada**: `UserCreateDTO` también tiene un `@Valid` en `AddressDTO`, lo que permite validar objetos
  complejos.
* **Validación de Parámetros (`@Validated`)**: En `UserController`, el `@PathVariable` usa `@Min` para validar la propia
  URL (`GET /users/0` fallará).

## 4. Manejo Global de Excepciones (El Pilar de la Robustez)

El paquete `...exception...` es la parte más avanzada. Usando `@RestControllerAdvice`, centralizamos *todo* el manejo de
errores en `GlobalExceptionHandler`.

Esto evita bloques `try-catch` repetitivos en los controladores (principio DRY).

* **Errores 400 (Bad Request)**: Capturamos `MethodArgumentNotValidException` (errores de DTO) y
  `ConstraintViolationException` (errores de URL) y los formateamos como un JSON claro para el frontend.
* **Errores 404 (Not Found)**: Usamos una excepción de negocio personalizada (`ResourceNotFoundException`).
* **Errores 500 (Server Error)**: Capturamos `Exception` (el "catch-all").
    * **Seguridad:** 1. **Logueamos** el *stack trace* completo (para el desarrollador). 2. Devolvemos un **mensaje
      genérico** (para el cliente), NUNCA el detalle del error.

## 5. Formato de Error RFC 7807 (Estándar Profesional)

El manejador de excepciones puede devolver dos tipos de errores, configurables en `application.properties`:

* `api.error.style: CUSTOM`: Un JSON simple (`{ "path": "...", "message": "..." }`).
* `api.error.style: RFC7807`: (Recomendado) Devuelve un `ProblemDetail` estandarizado, que es la mejor práctica moderna
  para APIs REST.

## 6. Monitoreo (Spring Boot Actuator)

El paquete `...health...` demuestra cómo pensar en la "salud" del servicio.

* `CustomHealthIndicator`: Creamos un chequeo de salud personalizado (simulando un servicio de pagos).
* `application.properties`: Exponemos los endpoints de Actuator (`health`, `info`, `metrics`) en un puerto de gestión
  separado (`9091`) por seguridad.