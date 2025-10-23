## 1\. Introducción: Qué es Springdoc y OpenAPI?

Para documentar APIs en Spring, la herramienta estándar de facto es **Springdoc-openapi**. Esta biblioteca genera
automáticamente la especificación **OpenAPI 3** (la evolución de Swagger 2) analizando tu código Spring Boot (
controladores, modelos, anotaciones de validación, etc.).

Además, incluye **Swagger UI**, una interfaz de usuario web interactiva que nos permite visualizar la documentación y
probar los *endpoints* directamente desde el navegador.

-----

## 2\. Desarrollo Paso a Paso

### Paso 1: Añadir la Dependencia (El "Starter")

El primer paso es agregar la dependencia de Springdoc al archivo `pom.xml` (si usas Maven). Esta única dependencia
incluye tanto el motor de generación de OpenAPI como la interfaz de usuario de Swagger.

```xml

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

Si ejecutas la aplicación ahora y vas a `http://localhost:8080/swagger-ui.html`, ya verás una
documentación básica de tus *endpoints* existentes.

El resto de los pasos consiste en *enriquecer* esa documentación automática con detalles.

### Paso 2: Configuración Global de la API

Es una buena práctica definir la información general de tu API (título, versión, descripción). La forma más limpia de
hacerlo es con una clase de configuración dedicada.

Crea una clase `OpenApiConfig.java`:

```java
// package org.cesarlead.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Mi API de CesarLead",
                version = "v1.0.0",
                description = "API REST para la gestión de productos y pedidos.",
                contact = @Contact(
                        name = "Soporte Técnico",
                        email = "soporte@cesarlead.com",
                        url = "https://cesarlead.com/"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class OpenApiConfig {
    // Esta clase está vacía a propósito.
    // Solo usamos la anotación a nivel de clase para la configuración global.
}
```

### Paso 3: Documentando Controladores y Endpoints

Ahora, vamos a anotar los controladores para agrupar *endpoints* y describir qué hacen.

* `@Tag`: Agrupa un conjunto de *endpoints* (normalmente, un controlador).
* `@Operation`: Describe un *endpoint* específico (un método del controlador).

```java
// package org.cesarlead.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Gestión de Productos", description = "Endpoints para CRUD de productos")
public class ProductoController {

    @GetMapping
    @Operation(
            summary = "Obtener listado de productos",
            description = "Devuelve una lista paginada de todos los productos activos."
    )
    public String obtenerProductos() {
        // Lógica del servicio...
        return "Listado de productos";
    }

    // ... otros endpoints
}
```

### Paso 4: Documentando Parámetros (Query y Path)

Springdoc es lo suficientemente inteligente como para detectar `@PathVariable` y `@RequestParam`, pero podemos añadir
descripciones y ejemplos usando `@Parameter`.

```java
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// ... dentro de ProductoController ...

@GetMapping("/{id}")
@Operation(summary = "Obtener un producto por ID")
public String obtenerProductoPorId(
        @Parameter(description = "ID único del producto", required = true, example = "123")
        @PathVariable Long id
) {
    // Lógica...
    return "Datos del producto " + id;
}

@GetMapping("/buscar")
@Operation(summary = "Buscar productos por término de búsqueda")
public String buscarProductos(
        @Parameter(description = "Término para buscar en nombre o descripción", example = "laptop")
        @RequestParam(required = false) String q
) {
    // Lógica...
    return "Resultados de búsqueda para: " + q;
}
```

### Paso 5: Documentando Cuerpos de Petición (Request Body) y Modelos

Para los métodos `POST` o `PUT`, documentar el DTO (Data Transfer Object) de entrada es crucial. Esto se hace con la
anotación `@Schema` directamente en la clase DTO.

**En el Controlador:**

Simplemente anota el método con `@Operation`. Springdoc detectará automáticamente el `@RequestBody`.

```java
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// ... dentro de ProductoController ...

@PostMapping
@Operation(summary = "Crear un nuevo producto")
public String crearProducto(
        @RequestBody CreateProductoDTO productoDTO
) {
    // Lógica para crear el producto...
    return "Producto creado";
}
```

**En el DTO (El Modelo):**

Aquí es donde ocurre la magia. Usamos `@Schema` para describir el modelo y sus campos.

```java
// package org.cesarlead.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la creación de un nuevo producto")
public class CreateProductoDTO {

    @Schema(
            description = "Nombre del producto",
            example = "Laptop Pro 15\"",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Schema(
            description = "Código SKU único del producto",
            example = "LP-15-PRO-BLK"
    )
    private String sku;

    @Schema(
            description = "Precio del producto en centavos",
            example = "150099", // 1500.99
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0"
    )
    private Integer precio;

    // Getters y Setters
}
```

### Paso 6: Documentando las Respuestas (Responses)

Un *endpoint* no solo debe documentar su entrada, sino también sus posibles salidas (Ej. 200 OK, 404 Not Found, 500
Error).

* `@ApiResponses`: Contenedor para múltiples respuestas.
* `@ApiResponse`: Define una respuesta HTTP específica.

Vamos a mejorar nuestro método `obtenerProductoPorId`:

```java
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
// ... otras importaciones

// ... dentro de ProductoController ...

@GetMapping("/{id}")
@Operation(summary = "Obtener un producto por ID")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Producto encontrado exitosamente",
                content = {@Content(
                        mediaType = "application/json",
                        // Asumiendo que tienes un DTO de respuesta llamado ProductoResponseDTO
                        schema = @Schema(implementation = ProductoResponseDTO.class)
                )}
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado",
                content = @Content // Respuesta 404 sin cuerpo
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content
        )
})
public ProductoResponseDTO obtenerProductoPorId(
        @Parameter(description = "ID único del producto", required = true, example = "123")
        @PathVariable Long id
) {
    // Lógica que busca el producto y puede lanzar una Excepción (ej. ProductoNotFoundException)
    // ...
    // return servicio.buscarPorId(id);
}
```

### Paso 7: Configurando la Seguridad (Ej. JWT / Bearer Token)

Este es un paso fundamental. Si tu API está protegida, necesitas indicarle a Swagger UI cómo debe enviar el token (por
ejemplo, un JWT).

**Paso 7a: Definir el Esquema de Seguridad**

Volvemos a nuestra clase `OpenApiConfig.java` y la modificamos para *definir* un esquema de seguridad llamado "
bearerAuth".

```java
// package org.cesarlead.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Mi API de cesarlead", version = "v1.0.0")
        // ... (El @Info del Paso 2 sigue aquí) ...
)
@SecurityScheme(
        name = "bearerAuth", // Un nombre para referenciar este esquema
        type = SecuritySchemeType.HTTP,
        scheme = "bearer", // Esquema HTTP: "bearer"
        bearerFormat = "JWT", // Formato del token
        description = "Autenticación basada en JWT (Bearer Token)"
)
public class OpenApiConfig {
    // ...
}
```

**Paso 7b: Aplicar el Esquema a los Endpoints**

Ahora que el esquema "bearerAuth" está definido, podemos exigir que se use. Puedes hacerlo a nivel global (para toda la
API), a nivel de controlador (para todos los *endpoints* de esa clase) o a nivel de método (para un *endpoint*
específico).

Usaremos la anotación `@SecurityRequirement`.

**Opción A: Aplicar a todo un Controlador**

```java
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
// ...

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Gestión de Productos", description = "Endpoints para CRUD de productos")
@SecurityRequirement(name = "bearerAuth") // <-- APLICA A TODOS LOS MÉTODOS DE ESTA CLASE
public class ProductoController {
    // ... todos los endpoints aquí ahora requerirán autenticación ...
}
```

**Opción B: Aplicar solo a un método**

Si, por ejemplo, `GET` es público pero `POST` es protegido:

```java
// ...

@GetMapping
@Operation(summary = "Obtener listado de productos (Público)")
public String obtenerProductos() {
    // ...
}

@PostMapping
@Operation(summary = "Crear un nuevo producto (Protegido)")
@SecurityRequirement(name = "bearerAuth") // <-- SOLO APLICA A ESTE MÉTODO
public String crearProducto(@RequestBody CreateProductoDTO productoDTO) {
    // ...
}
```

-----

## Recurso Adicional: Referencia de Anotaciones

Aquí tienen los dos enlaces más importantes que necesitaran:

1. **Guía Oficial de Springdoc (Práctica):**

    * [https://springdoc.org/](https://springdoc.org/)
    * Esta es la documentación principal de la biblioteca que acabamos de usar. Explica cómo integrar todo con Spring.

2. **Javadoc de Anotaciones de Swagger (La Referencia Técnica):**

    * [Javadoc de
      `io.swagger.v3.oas.annotations`](https://javadoc.io/doc/io.swagger.core.v3/swagger-annotations/latest/io/swagger/v3/oas/annotations/package-summary.html%5D\(https://javadoc.io/doc/io.swagger.core.v3/swagger-annotations/latest/io/swagger/v3/oas/annotations/package-summary.html\))
    * Este es el "diccionario" de todas las anotaciones (`@Operation`, `@Parameter`, `@Schema`, `@SecurityScheme`, etc.)
      y todas las propiedades que aceptan.