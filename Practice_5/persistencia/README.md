# Módulo 5: Persistencia de Datos con Spring Data JPA

Este proyecto es el salto a la persistencia real. Reemplazamos los repositorios en memoria (como `ConcurrentHashMap`)
del Módulo 4 por una **Base de Datos PostgreSQL** real, gestionada profesionalmente a través de **Spring Data JPA** y *
*Hibernate**.

El foco de este módulo es la **capa de datos**: cómo modelar, persistir, consultar y transaccionar datos de forma
eficiente y segura.

## 1. Conceptos Clave

1. **ORM (Object-Relational Mapping):** Usamos JPA (la especificación) y Hibernate (la implementación) para mapear
   Clases Java (`@Entity`) a Tablas SQL.
2. **Modelo Relacional:** Se implementa un modelo de E-commerce (`Customer` -> `Order` -> `OrderItem`) usando
   anotaciones `@OneToMany`, `@ManyToOne` y `@JoinColumn`.
3. **Spring Data Repositories:** Extendemos `JpaRepository` para obtener métodos CRUD (`save`, `findById`, `findAll`) de
   forma gratuita.
4. **Consultas (Querying):** Demostramos las 4 formas principales de consultar datos en Spring:
    * Métodos Derivados (Derived Query Methods)
    * JPQL (`@Query`)
    * SQL Nativo (`@Query(nativeQuery=true)`)
    * JPA Specification API (para consultas dinámicas)
5. **Transaccionalidad (ACID):** Usamos `@Transactional` en la capa de servicio para garantizar la **atomocidad** de las
   operaciones de negocio (ej. "Todo o Nada" al crear una orden).

## 2. Decisiones de Diseño (El "Porqué")

### Data Modeling (Las Entidades)

* **`Customer.java`:** La entidad raíz. Usa `@OneToMany` para su lista de `Order`.
* **`Order.java`:** La entidad de unión. Usa `@ManyToOne` para referenciar a su `Customer` (dueño de la relación) y
  `@OneToMany` para sus `OrderItem`.
    * `cascade = CascadeType.ALL` y `orphanRemoval = true`: Asegura que si se elimina una `Order`, todos sus `OrderItem`
      se eliminen automáticamente.
* **`OrderItem.java`:** El nivel más bajo, pertenece a una `Order`.

### El Poder del Repositorio (`CustomerRepository`)

Este repositorio es una demostración de las diferentes estrategias de consulta:

* **Derivadas:** `Optional<Customer> findByEmail(String email);`
    * Spring "entiende" el nombre del método y genera el SQL `WHERE email = ?`.
* **JPQL:** `@Query("SELECT c FROM Customer c WHERE ...")`
    * Permite escribir consultas complejas en un lenguaje similar a SQL pero orientado a objetos (sobre las Entidades,
      no las tablas).
* **Nativo:** `@Query(..., nativeQuery = true)`
    * Permite escribir SQL puro de PostgreSQL, útil para consultas muy optimizadas o que usan funciones específicas de
      la BBDD.
* **Modificación:** `@Modifying`
    * Requerido para consultas que cambian datos (`UPDATE`, `DELETE`), ya que deben ejecutarse dentro de un contexto
      transaccional diferente.

### Transacciones de Servicio (`OrderServiceImpl`)

El método `placeOrder` está anotado con `@Transactional`. Esto es crítico.

1. Inicia una transacción en la BBDD.
2. Ejecuta `orderRepository.save(order)`.
3. Ejecuta `inventoryService.updateStock(...)` (un servicio simulado).
4. **Si AMBOS tienen éxito**, la transacción hace `COMMIT` y los datos se guardan.
5. **Si `inventoryService` falla**, la transacción hace `ROLLBACK` y la orden *nunca* se guarda, manteniendo la BBDD
   consistente.

### El Nivel PRO! Búsqueda Dinámica con `Specification`

El `endpoint` `GET /api/v1/customers/search` permite filtros opcionales (por nombre, email o estado premium).

* **El Mal Camino:** Un `if/else` gigante para construir un string de JPQL.
* **El Camino Profesional:** Usar `JpaSpecificationExecutor`.
    * **`CustomerSpecifications.java`**: Es una "fábrica" de predicados (`WHERE`). Cada método devuelve un objeto
      `Specification` que describe una condición (ej. `hasName(...)`).
    * **`CustomerServiceImpl.java`**: El método `searchCustomers` colecciona dinámicamente *solo* las especificaciones
      que el usuario proveyó en una `List`.
    * **`reduce(Specification::and)`**: Usa la API de Streams para combinar todos los "bloques" de la lista en una única
      consulta `AND` (`WHERE name = ? AND email LIKE ? ...`).
    * Esto es type-safe, limpio, reutilizable (DRY) y fácil de testear.

## 3. Cómo Ejecutar

1. **Pre-requisito:** Necesitas tener una instancia de **PostgreSQL** corriendo.
2. Crea una base de datos (ej. `globaldesk`).
3. Actualiza `application.properties` con tu URL, usuario y contraseña de PostgreSQL.
4. **`spring.jpa.hibernate.ddl-auto=update`**: Al arrancar la aplicación, Hibernate leerá tus `@Entity` y *
   *automáticamente creará o actualizará las tablas** en tu BBDD.
5. **`spring.jpa.show-sql=true`**: Revisa tu consola. Verás el SQL exacto que Hibernate está generando por ti. Es la
   mejor forma de aprender.
6. Ejecuta la aplicación.