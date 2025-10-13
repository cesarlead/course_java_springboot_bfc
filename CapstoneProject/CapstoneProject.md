---
El objetivo de estas actividades es consolidar y evaluar los conocimientos adquiridos. Para ello, desarrollarán de forma
  incremental una API REST para un sistema de gestión de una pequeña tienda en línea.

  **Nombre del Proyecto:** **API REST "MarketCesarLead"**

  **Descripción del Proyecto:** Se construirá el backend para una aplicación de e-commerce. Esta API gestionará productos,
  clientes y órdenes de compra. A lo largo de los módulos, se añadirán funcionalidades como la persistencia en base de
  datos y la comunicación con servicios externos para obtener tasas de cambio de moneda.
---

# **Proyecto Integrador**

## GlobalDesk | Instructor: Ing. César Fernández

### **Desarrollo Paso a Paso: Actividades por Módulo**

Aquí se detalla la actividad específica para cada módulo, construyendo sobre el trabajo del módulo anterior.

#### **Actividad Módulo 1: Modelado del Dominio y Lógica de Negocio (Fundamentos de Java y POO)**

**Objetivo:**

Aplicar los principios de la Programación Orientada a Objetos para modelar las entidades centrales del dominio y la
lógica de negocio fundamental, sin depender aún de ningún framework.

**Descripción:**

En esta fase, crearán las clases base (POJOs) que representarán los conceptos clave de la aplicación. Deberán enfocarse
en la estructura de datos, el encapsulamiento y la lógica de negocio esencial.

**Requisitos Técnicos:**

1. **Clase `Product`:**
    * Atributos: `id` (Long), `name` (String), `price` (BigDecimal), `stock` (Integer).
    * Encapsulamiento: Todos los atributos deben ser privados con sus respectivos getters y setters.
    * Constructores: Uno por defecto y uno con todos los parámetros.

2. **Clase `Customer`:**
    * Atributos: `id` (Long), `name` (String), `email` (String).
    * Encapsulamiento y constructores, similar a `Product`.

3. **Clase `Order`:**
    * Atributos: `id` (Long), `customer` (Customer), `items` (una `List<Product>`), `total` (BigDecimal).

    * Lógica de Negocio:

        * Un método `calculateTotal()` que calcule la suma de los precios de todos los productos en la lista `items` y
          actualice el atributo `total`.

        * Un método `addProduct(Product product)` que verifique si hay stock disponible (`product.getStock() > 0`). Si
          hay stock, lo añade a la lista y decrementa el stock del producto. Si no, lanza una excepción personalizada.

4. **Excepción Personalizada:**

    * Crear una excepción `InsufficientStockException` que herede de `RuntimeException`. Debe tener un constructor que
      acepte un mensaje de error.

**Entregables:**

* El código fuente de las clases `Product`, `Customer`, `Order` y la excepción `InsufficientStockException`.

* Una clase `Main` con un método `main` que demuestre el funcionamiento: crear clientes, productos, una orden, añadir
  productos (uno con stock y otro sin stock para probar la excepción) y calcular el total.

**Criterios de Evaluación:**

* Correcta aplicación de los principios de POO (especialmente encapsulamiento).

* Implementación correcta de la lógica de negocio en la clase `Order`.

* Creación y uso adecuado de la excepción personalizada.

---

#### **Actividad Módulo 2: Exposición de la API REST (Fundamentos de una Aplicación)**

**Objetivo:**

Tomar el modelo de dominio del Módulo 1 y exponerlo a través de endpoints REST utilizando Spring Boot. Implementar
validaciones básicas y un manejo de errores global.

**Descripción:**

Se creará un proyecto Spring Boot desde cero. Se implementarán los `Controllers` necesarios para gestionar los productos
y las órdenes, aplicando las mejores prácticas para el diseño de APIs REST y la gestión de errores.

**Requisitos Técnicos:**

1. **Creación del Proyecto:**
    * Utilizar **Spring Initializr** con las siguientes dependencias: `Spring Web`, `Spring Boot DevTools`,
      `Validation`, `Spring Boot Actuator`.

    * Configurar el proyecto para usar Java 17 y Maven.

2. **Controladores:**

    * **`ProductController`:**

        * `GET /api/v1/products`: Retorna una lista de todos los productos.

        * `POST /api/v1/products`: Crea un nuevo producto. El cuerpo de la petición debe ser validado (`@Valid`).

    * **`OrderController`:**

        * `POST /api/v1/orders`: Crea una nueva orden. Recibirá el ID del cliente y una lista de IDs de productos.

3. **DTOs (Data Transfer Objects):**

    * Crear DTOs para las peticiones (request) y respuestas (response) para desacoplar la API del modelo de dominio. Por
      ejemplo, `ProductRequestDTO`, `ProductResponseDTO`, `OrderRequestDTO`.

4. **Validaciones:**

    * En `ProductRequestDTO`, añadir validaciones: `@NotBlank` para el nombre, `@Positive` para el precio y el stock.

5. **Manejo de Errores:**

    * Implementar una clase con `@ControllerAdvice` para capturar `MethodArgumentNotValidException` (errores de
      validación) y la `InsufficientStockException` del módulo anterior.

    * Debe devolver una respuesta JSON estandarizada y legible con un código de estado HTTP apropiado (e.g., 400 Bad
      Request, 409 Conflict).

6. **Actuator:**

    * Habilitar y configurar los endpoints `/health` e `/info` de Spring Actuator.

** Entregables:**

* La estructura completa del proyecto Spring Boot.

* El código de los `Controllers`, `Services` (capa de lógica de negocio), `DTOs` y el `ControllerAdvice`.

**Criterios de Evaluación:**

* Correcta estructura del proyecto (paquetes para `controller`, `service`, `dto`, `model`, `exception`).

* Uso adecuado de las anotaciones de Spring MVC y los métodos HTTP.

* Implementación del patrón DTO para desacoplar capas.

* Manejo de errores centralizado, robusto y con mensajes claros.

* Calidad y cobertura de las pruebas de integración.

---

#### **Actividad Módulo 3: Persistencia de Datos con Spring Data JPA**

**Objetivo:**

Integrar una base de datos para persistir la información de productos, clientes y órdenes, utilizando Spring Data JPA y
una base de datos en memoria (H2).

**Descripción:**

Se transformarán las clases del modelo de dominio en entidades JPA. Se crearán repositorios para interactuar con la base
de datos y se refactorizará la capa de servicio para utilizar estos repositorios.

**Requisitos Técnicos:**

1. **Dependencias:**

    * Añadir `Spring Data JPA` y `H2 Database` al `pom.xml`.

2. **Configuración:**

    * Configurar la conexión a la base de datos H2 en `application.properties`. Habilitar la consola de H2.

3. **Entidades JPA:**

    * Anotar las clases `Product`, `Customer` y `Order` con `@Entity`.

    * Definir las claves primarias (`@Id`, `@GeneratedValue`).

    * Mapear las relaciones:

        * `Order` a `Customer`: `@ManyToOne`.

        * `Order` a `Product`: `@ManyToMany` (una orden puede tener muchos productos, y un producto puede estar en
          muchas órdenes).

4. **Repositorios:**

    * Crear interfaces que extiendan de `JpaRepository` para `Product`, `Customer` y `Order`.

    * En `ProductRepository`, añadir un método de consulta derivado para buscar productos por nombre:
      `Optional<Product> findByName(String name);`.

5. **Capa de Servicio Transaccional:**

    * Refactorizar los servicios para inyectar y usar los repositorios.

    * Anotar los métodos que modifican la base de datos (e.g., `createOrder`, `createProduct`) con `@Transactional` para
      garantizar la atomicidad. El método para crear una orden debe ser transaccional para que, si falla la
      actualización del stock de un producto, toda la operación se revierta.

**Entregables:**

* El proyecto actualizado con la capa de persistencia.

* Las clases de entidad debidamente anotadas.

* Las interfaces de repositorio.

* La capa de servicio actualizada utilizando los repositorios y `@Transactional`.

**Criterios de Evaluación:**

* Correcto mapeo de entidades y relaciones JPA.

* Implementación correcta de los repositorios de Spring Data.

* Uso adecuado de `@Transactional` para garantizar la integridad de los datos.

---

#### **Actividad Módulo 4: Comunicación con APIs Externas**

**Objetivo:**

Integrar la API con un servicio externo para una funcionalidad avanzada. Implementar resiliencia y optimización mediante
el uso de caché.

**Descripción:**

Se añadirá una nueva funcionalidad: permitir que los clientes vean el precio de un producto en una moneda diferente (
e.g., USD). Para esto, se consumirá una API pública de conversión de divisas.

**Requisitos Técnicos:**

1. **Dependencia:**

    * Añadir `Spring WebFlux` para usar `WebClient`.

2. **Cliente API:**

    * Crear un servicio (`CurrencyConversionService`) que utilice `WebClient` para conectarse a una API de divisas
      gratuita (ej. [ExchangeRate-API](https://www.exchangerate-api.com/) o similar).

    * Implementar un método `getConversionRate(String from, String to)` que devuelva la tasa de cambio.

    * **Manejo de Errores:** Implementar manejo de errores con `onStatus()` para gestionar respuestas HTTP 4xx o 5xx de
      la API externa.

    * **Timeouts:** Configurar timeouts de conexión y lectura en el `WebClient`.

3. **Nuevo Endpoint:**

    * Añadir un nuevo endpoint en `ProductController`:

        * `GET /api/v1/products/{id}/price/{currency}`

    * Este endpoint obtendrá el producto de la base de datos, llamará al `CurrencyConversionService` para obtener la
      tasa de cambio, calculará el precio en la nueva moneda y lo devolverá.

4. **Caché:**

    * Habilitar el caché en la aplicación (`@EnableCaching`).

    * Anotar el método `getConversionRate` con `@Cacheable`. Esto evitará llamar a la API externa repetidamente para la
      misma conversión en un corto período de tiempo. Configurar el caché para que expire cada 1 hora.

**Entregables:**

* El proyecto final con la nueva funcionalidad.

* El código del `WebClient` y el servicio de conversión.

* El nuevo endpoint en el controlador.

* La configuración de caché.

**Criterios de Evaluación:**

* Implementación correcta y eficiente de `WebClient`.

* Manejo de errores y timeouts robusto en la comunicación externa.

* Uso efectivo de `@Cacheable` para optimizar el rendimiento.

---

### **Instrucciones Generales para la Entrega y Restricciones**

**Formato de Entrega:**

1. Cada entrega debe realizarse a través de un repositorio **Git** (e.g., GitHub, GitLab).

2. Para cada módulo, el participante debe crear una **rama separada** (e.g., `feature/module-1`, `feature/module-2`). El
   trabajo final del módulo debe estar en un commit en esa rama.

3. El repositorio debe contener un archivo `README.md` bien documentado que explique cómo clonar, compilar y ejecutar el
   proyecto. Debe incluir ejemplos de peticiones `curl` o una colección de Postman/Insomnia para probar los endpoints.

4. El código debe seguir las convenciones de estilo de Java y estar razonablemente comentado donde la lógica no sea
   trivial.

