## **Guía del Proyecto: Transformación a Microservicios**

**Objetivo:** El objetivo de este proyecto es refactorizar nuestra aplicación monolítica de gestión de pedidos en una
arquitectura moderna basada en tres microservicios independientes. Aprenderás a definir responsabilidades, gestionar
datos distribuidos y orquestar la comunicación entre servicios, tal como se hace en la industria.

---

### ### **Fase 1: Diseño y Planificación (El Rol del Arquitecto)**

Antes de escribir una sola línea de código, debes pensar como un arquitecto. Responde a estas preguntas en un documento
de diseño simple:

1. **Cuáles serán los 3 microservicios?** Define un nombre claro para cada uno (ej. `servicio-productos`).
2. **Cuál es la responsabilidad ÚNICA de cada servicio?** (ej. "Este servicio es la única fuente de verdad para el
   inventario de productos").
3. **Qué datos le pertenecen a cada servicio?** Dibuja un modelo de base de datos simple para cada uno. **Pista:** Las
   claves foráneas (`foreign keys`) entre las bases de datos de distintos servicios no son posibles. Cómo solucionarás
   esto?
4. **Cómo se comunicarán para crear un pedido?** Dibuja un diagrama de secuencia simple que muestre las llamadas de red
   entre los servicios para completar la operación.

---

### **Fase 2: Construcción de los Servicios (El Rol del Desarrollador)**

Crea un proyecto Spring Boot independiente para cada microservicio que diseñaste.

#### **1. Servicio de Productos (`servicio-productos`)**

* **Responsabilidad:** Gestionar el catálogo y el stock de productos.
* **Tareas:**
    * Crea la entidad `Product` y su repositorio.
    * Implementa un `ProductController` con, al menos, dos endpoints:
        * Uno para **obtener los detalles de un producto** por su ID.
        * Otro para **decrementar el stock** de un producto.

#### **2. Servicio de Clientes (`servicio-clientes`)**

* **Responsabilidad:** Gestionar la información de los clientes.
* **Tareas:**
    * Crea la entidad `Customer` y su repositorio.
    * Implementa un `CustomerController` con un endpoint para **obtener los detalles de un cliente** por su ID.

#### **3. Servicio de Pedidos (`servicio-pedidos`)**

* **Responsabilidad:** Orquestar el proceso de creación de un pedido.
* **Tareas:**
    * Crea las entidades `Order` y `OrderItem` y sus repositorios.
    * **Desafío Clave:** Piensa en la entidad `OrderItem`. Si ya no puedes hacer un `@ManyToOne` a la tabla de
      productos (porque está en otra base de datos), qué información **debes copiar y almacenar** en `OrderItem` para
      que el pedido sea históricamente correcto?
    * Implementa el `OrderController` con el endpoint para **crear un nuevo pedido**.

---

### **Fase 3: Orquestación y Comunicación (El Desafío Real)**

Esta es la parte más importante. El `OrderService` debe ahora comunicarse con los otros dos servicios a través de la red
para poder funcionar.

* **Tu Herramienta:** Utiliza `WebClient` (o `RestTemplate`, `Feign`) en el `OrderService` para realizar llamadas
  HTTP a los
  otros servicios.
* **El Flujo Lógico a Implementar:**
    1. Al recibir una solicitud para crear un pedido, el `OrderService` debe primero llamar al **Servicio de Clientes**
       para validar que el cliente existe.
    2. Luego, debe iterar por cada producto solicitado y llamar al **Servicio de Productos** para obtener su precio y
       stock actual.
    3. Con esta información, debe realizar sus validaciones internas (hay stock suficiente?) y calcular el total.
    4. Si todo es correcto, debe guardar el pedido en su propia base de datos.
    5. Finalmente, debe realizar una última llamada al **Servicio de Productos** para confirmar la reducción de stock.

---

### **Puntos Clave a Considerar**

* **Adiós, `@Transactional` Mágico:** La anotación `@Transactional` en el `OrderService` solo afectará a su propia base
  de datos. No puede revertir cambios en la base de datos de productos si algo falla a mitad de camino. Cómo manejarías
  un error si el pedido se guarda pero la llamada para decrementar el stock falla? (No necesitas una solución perfecta,
  pero piensa en el problema).
* **La Red No es Confiable:** Las llamadas HTTP pueden fallar. Asegúrate de que tu `WebClient` maneje posibles errores
  de los otros servicios.
* **Configuración:** Cada servicio se ejecutará en un puerto diferente. Asegúrate de configurarlo en el
  `application.properties` de cada uno.

NOTA: RECUERDEN SEGUIR EL PATRON DE INTERFACES (EN EL SERVICE), en el ejemplo no esta implementado.
