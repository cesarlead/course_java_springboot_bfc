Ing. César Fernández

## Parte 1: Ejercicio Práctico: Mini Sistema de Gestión de Proyectos

**Objetivo:**

Construir una API REST básica para gestionar proyectos, tareas y empleados. Deberás modelar las entidades y sus relaciones, crear la capa de persistencia y servicio, y exponer los endpoints necesarios, siguiendo las mejores prácticas vistas en clase.

**Entidades a Modelar:**

1.  **`Project`**:

    * `id` (Long)
    * `name` (String)
    * `startDate` (LocalDate)
    * `endDate` (LocalDate)

2.  **`Task`**:

    * `id` (Long)
    * `description` (String)
    * `status` (Enum: `PENDING`, `IN_PROGRESS`, `COMPLETED`)
    * `dueDate` (LocalDate)

3.  **`Employee`**:

    * `id` (Long)
    * `firstName` (String)
    * `lastName` (String)
    * `role` (String, ej: "Developer", "Manager")

**Relaciones a Implementar:**

* Un `Project` tiene muchas `Tasks` (**OneToMany**). Una `Task` pertenece a un solo `Project` (**ManyToOne**).
* Un `Project` puede tener muchos `Employees` asignados, y un `Employee` puede estar en varios `Projects` (**ManyToMany**).

**Requisitos del Ejercicio:**

1.  **Configuración del Proyecto:**

    * Crea un nuevo proyecto Spring Boot usando Spring Initializr.
    * Incluye las dependencias: `Spring Web`, `Spring Data JPA` y `H2 Database` (para una configuración sencilla sin base de datos externa).

2.  **Capa de Persistencia:**

    * Crea las tres clases de entidad (`Project`, `Task`, `Employee`) y anótalas correctamente con `@Entity` y las demás anotaciones de JPA (`@Id`, `@GeneratedValue`, etc.).
    * Implementa las relaciones (`@OneToMany`, `@ManyToOne`, `@ManyToMany`) de forma bidireccional. Presta atención a quién es el dueño de la relación (Es importante)!!!
    * Crea las interfaces de repositorio correspondientes extendiendo `JpaRepository`.

3.  **Consultas Personalizadas:**

    * En `TaskRepository`, añade un método para buscar todas las tareas de un proyecto que tengan un estado específico (ej: `findByProjectAndStatus`).
    * En `ProjectRepository`, añade un método para encontrar todos los proyectos en los que un empleado específico está asignado.

4.  **Capa de API y Lógica:**

    * Crea los **DTOs** (`Data Transfer Objects`) necesarios para las respuestas de la API (ej: `ProjectResponseDTO`, `TaskDTO`). Recuerda la importancia de no exponer las entidades directamente.
    * Implementa las clases de **Servicio** (`@Service`) que contendrán la lógica de negocio.
    * Crea los **Controladores** (`@RestController`) para exponer los siguientes endpoints:
        * `POST /projects`: Crear un nuevo proyecto.
        * `GET /projects/{projectId}/tasks`: Obtener todas las tareas de un proyecto.
        * `POST /projects/{projectId}/tasks`: Añadir una nueva tarea a un proyecto existente.
        * `POST /projects/{projectId}/employees/{employeeId}`: Asignar un empleado existente a un proyecto.

**Desafío Adicional (No es Opcional!!!):**
Implementa un endpoint `GET /tasks/search` que utilice **Especificaciones de JPA** (`JpaSpecificationExecutor`) para permitir filtrar tareas dinámicamente por `status` y `dueDate`.

-----

## Parte 2: Problema a Resolver - La Referencia Circular

Este es un problema clásico que surge al trabajar con relaciones bidireccionales y serialización JSON.

### **El Problema:**

En el código de ejemplo de la clase, teníamos una relación bidireccional entre `Customer` y `Order`.

* La clase `Customer` tiene una `List<Order>`.
* La clase `Order` tiene una referencia de vuelta a `Customer`.

Si intentamos devolver una entidad `Customer` directamente desde un controlador, el serializador JSON (Jackson) entra en un bucle infinito:

1.  Jackson empieza a convertir el `Customer` a JSON.
2.  Encuentra la lista de `Orders` y empieza a convertir el primer `Order`.
3.  Dentro del `Order`, encuentra la referencia al `Customer`.
4.  Intenta convertir ese `Customer`, que a su vez tiene una lista de `Orders`... y así sucesivamente.
5.  Esto consume toda la memoria de la pila y causa un error `StackOverflowError`.

**Código que Causa el Problema:**

```java
// En el CustomerController
@GetMapping("/{id}")
public Customer getCustomerById(@PathVariable Long id) {
    // ERROR!!! Devolver la entidad directamente causa el bucle infinito.
    return customerRepository.findById(id).orElseThrow();
}
```

### **Tu Tarea: Resolver el Ciclo!!!**

Debes refactorizar el código para romper esta referencia circular.

**Solución (Recomendada y Mejor Práctica): El Patrón DTO**

Esta es la solución ideal que vimos en clase.

1.  **Crea los DTOs:**
    * Un `CustomerResponseDTO` que contenga los datos del cliente que quieres mostrar, incluyendo una `List<OrderSummaryDTO>`.
    * Un `OrderSummaryDTO` que contenga solo los datos del pedido, **sin la referencia de vuelta al `Customer`**.
2.  **Actualiza el Servicio:** Modifica el método en tu capa de servicio para que, en lugar de devolver la entidad `Customer`, la mapee a un `CustomerResponseDTO`.
3.  **Actualiza el Controlador:** Asegúrate de que el controlador devuelva el `CustomerResponseDTO`.
