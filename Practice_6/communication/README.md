# Módulo 6: Comunicación y Resiliencia entre Microservicios

Este proyecto es el núcleo de una arquitectura de microservicios. Su objetivo es demostrar cómo un servicio (este) se
comunica con otros servicios externos de forma profesional y, lo más importante, **resiliente**.

Exploramos y comparamos las dos técnicas modernas principales en el ecosistema Spring:

1. **Spring Cloud OpenFeign**: Un cliente REST declarativo (basado en interfaces).
2. **Spring WebClient**: Un cliente REST reactivo y no bloqueante (moderno).

Además, este proyecto implementa patrones de resiliencia avanzados, asumiendo que los servicios externos *pueden
fallar*.

## 1. El Problema: Los Servicios Externos Fallan

En un sistema distribuido, una llamada de red puede fallar por muchas razones:

* El servicio está caído (Error 500).
* El recurso no existe (Error 404).
* La red es lenta (Timeout).

Un cliente "ingenuo" que no maneja esto colapsará toda la cadena de peticiones. Este proyecto implementa las soluciones.

## 2. Enfoque 1: OpenFeign (Declarativo y Resiliente)

**Tecnología:** `spring-cloud-starter-openfeign`

### Cómo Funciona

1. **`@FeignClient` (Ej. `ProductFeignClient`)**:
    * Simplemente definimos una **interfaz** Java.
    * Anotamos los métodos con `@GetMapping`, `@PostMapping`, etc.
    * Spring mágicamente crea una implementación en tiempo de ejecución.
    * Es ideal por su simplicidad (KISS) y legibilidad.

2. **Manejo de Errores PRO (`CustomErrorDecoder`)**:
    * En lugar de dejar que Feign lance sus propias excepciones, creamos un `ErrorDecoder` personalizado.
    * Este `decoder` intercepta las respuestas HTTP.
    * **Traduce** errores HTTP (ej. 404, 503) a nuestras **excepciones de dominio** personalizadas (
      `ProductNotFoundException`, `ServiceUnavailableException`).
    * **Beneficio:** El resto de nuestra aplicación ahora solo depende de *nuestras* excepciones, no de Feign (bajo
      acoplamiento).

3. **Controlador (`ResilientFacadeController`)**:
    * Este controlador usa `@ExceptionHandler` para atrapar nuestras excepciones personalizadas y devolver respuestas
      HTTP 404 o 503 limpias al cliente final.

## 3. Enfoque 2: WebClient (Reactivo y Resiliente)

**Tecnología:** `spring-boot-starter-webflux`

Este es el cliente moderno, no bloqueante, diseñado para alta concurrencia. No consume un hilo por petición en espera.

### Cómo Funciona

1. **`WebClientConfig`**:
    * Configuramos un `Bean` de `WebClient` usando el *builder*.
    * Definimos la URL base y cabeceras por defecto.
    * Creamos un `Bean` especial (`resilientWebClient`) que configura **Timeouts** de conexión, respuesta y lectura (
      `ChannelOption.CONNECT_TIMEOUT_MILLIS`).

2. **Programación Reactiva (`ApiClientService`)**:
    * El servicio usa `WebClient` y devuelve tipos reactivos:
        * `Mono<T>`: Para respuestas de 0 o 1 elemento (ej. `getUserById`).
        * `Flux<T>`: Para respuestas de 0 a N elementos (ej. `getAllUsers`).
    * Se usa el *builder* fluido para construir peticiones GET, POST, PUT y DELETE.

3. **Patrones de Resiliencia Avanzados (`ResilientWebClientService`)**:
    * **Manejo de Errores (`.onStatus(...)`)**: Es el equivalente reactivo al `ErrorDecoder`. Traduce errores 4xx y 5xx
      a `Mono.error(...)` usando nuestras excepciones.
    * **Estrategia de Reintentos (`.retryWhen(...)`)**:
        * **Backoff Exponencial:** Reintenta 3 veces, esperando 1s, 2s, 4s...
        * **Jitter:** Añade aleatoriedad a la espera para evitar "tormentas de reintentos".
        * **Filtro:** **Crítico**. Solo reintenta si el error es `ServiceUnavailableException` (un 5xx). No reintenta en
          un 404 (no tiene sentido).
    * **Estrategia de Fallback (`.onErrorResume(...)`)**:
        * Este es un patrón de "Respuesta Alternativa".
        * Si después de todos los reintentos, la llamada falla con `ProductNotFoundException`, en lugar de devolver un
          error, **devuelve un producto por defecto**. Esto mantiene la aplicación funcionando.

## 4. Bonus: Carga de Configuración Externa

* **`ConfigLoader` (`ApplicationRunner`)**: Esta clase se ejecuta *al arrancar* la aplicación.
* **Patrón:** Usa `ConfigFeignClient` para llamar a un "Servicio de Configuración" externo.
* **Feature Toggles:** Carga configuraciones dinámicas (ej. `isNewCheckoutEnabled()`) en un `Bean` (`AppConfiguration`)
  para ser usadas por toda la aplicación.

## 5. Cómo Ejecutar

1. Este proyecto es un **cliente**. Depende de un servicio externo (simulado o real).
2. El servicio externo debe estar corriendo en la URL definida en `application.properties` (ej.
   `http://localhost:8080/api/v1/cesarlead`).
3. **Debugging:** La propiedad `feign.client.config.default.loggerLevel=full` está activada. Esto imprimirá en la
   consola los **logs completos** de cada petición y respuesta HTTP de Feign (cabeceras, cuerpo, etc.), lo cual es
   invaluable para depurar.