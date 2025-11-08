# Módulo 7: Documentación (OpenAPI) y Patrón Mapper

Este proyecto es la culminación de todas las prácticas anteriores, enfocándose en dos aspectos críticos para la
producción: la **documentación de la API** (para los consumidores) y la **calidad del código interno** (para los
mantenedores).

Este módulo toma un CRUD robusto (similar al Módulo 5) y lo eleva a un nivel profesional implementando el estándar *
*OpenAPI 3.0** (con Swagger UI) y el **Patrón de Diseño Mapper**.

## 1. El Pilar Principal: Documentación con OpenAPI

Una API sin documentación es inutilizable. Este proyecto utiliza `springdoc-openapi` para generar automáticamente una
documentación interactiva (Swagger UI).

### Decisiones de Diseño:

1. **Configuración Global (`OpenApiConfig`):** Usamos `@OpenAPIDefinition` para establecer la "portada" de nuestra
   documentación: el título, la versión, la información de contacto y la licencia.
2. **Agrupación (`@Tag`)**: El `ProductController` está anotado con `@Tag(name = "Product Management")` para agrupar
   todos sus endpoints bajo una misma sección en la UI.
3. **Descripción de Endpoints (`@Operation`)**: Cada método (GET, POST, etc.) tiene una anotación `@Operation` con un
   `summary` (qué hace) y un `description` (cómo lo hace).
4. **Respuestas de API (`@ApiResponses`)**: ¡El punto más crítico! Documentamos *todos* los códigos de respuesta
   posibles:
    * **200 OK / 201 Created**: El "camino feliz".
    * **400 Bad Request**: Vinculado a nuestro `GlobalExceptionHandler` para `MethodArgumentNotValidException`.
    * **404 Not Found**: Vinculado a `ResourceNotFoundException`.
    * **409 Conflict**: Vinculado a `DuplicateResourceException` (ej. SKU duplicado).
5. **Esquemas (`@Schema`)**:
    * Los DTOs (`ProductRequestDTO`, `ProductResponseDTO`) usan `@Schema` para documentar cada campo, incluyendo
      ejemplos (`example = "SKU-12345"`).
    * El `ErrorResponseDTO` también tiene un `@Schema`, y se vincula en las `@ApiResponses` de error. Esto le dice al
      consumidor el formato *exacto* del JSON de error que recibirá.

## 2. El Pilar Arquitectónico: El Patrón Mapper

Este patrón soluciona una violación de los principios SOLID (Single Responsibility) y DRY (Don't Repeat Yourself) que
existía en servicios anteriores.

* **El Problema:** El `ProductService` tenía *dos* responsabilidades: 1) Lógica de negocio (validar SKU, llamar al repo)
  y 2) Lógica de conversión (convertir DTO a Entidad y viceversa).
* **La Solución:** Introducir una capa de Mapeo.
    1. **`ProductMapper` (Interfaz):** Define el contrato de conversión (`toEntity`, `toResponseDTO`,
       `updateEntityFromDTO`).
    2. **`ProductMapperImpl` (Implementación):** Es el **único** lugar en la aplicación que sabe cómo construir un
       `Product` desde un `ProductRequestDTO`.
    3. **`ProductServiceImpl` (Refactorizado):** Ahora el servicio es limpio y solo se enfoca en la lógica de negocio, *
       *delegando** la conversión al mapper.

Esto hace que el servicio sea más fácil de leer, testear y mantener.

## 3. Otras Mejoras Profesionales

* **Lombok:** Usamos `@RequiredArgsConstructor` en todas las clases (`@Service`, `@Controller`) para una inyección de
  dependencias por constructor limpia y automática. `@Data` y `@NoArgsConstructor` se usan en la entidad `Product`.
* **`GlobalExceptionHandler`:** Ha evolucionado para manejar `DuplicateResourceException` con un `409 CONFLICT`, que es
  el código de estado HTTP semánticamente correcto para una violación de unicidad.
* **`DataInitializer`:** Un `CommandLineRunner` que siembra (seeds) la base de datos en memoria H2 con datos de prueba,
  facilitando el testeo manual.
* **Base de Datos H2:** Todo el proyecto corre en una BBDD en memoria, auto-configurada.

## 4. Cómo Ejecutar

1. Inicia la aplicación ejecutando el método `main` en `DocumentApplication.java`.
2. La aplicación se ejecutará en `http://localhost:8989`.

### URLs Clave:

* **Swagger UI (Documentación Interactiva):**
  `http://localhost:8989/swagger-ui.html`
  (Aquí puedes ver y probar todos los endpoints).
* **Consola H2 (Base de Datos):**
  `http://localhost:8989/h2-console`
  (Usa la JDBC URL: `jdbc:h2:mem:marketdb`)