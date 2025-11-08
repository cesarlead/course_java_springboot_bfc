# Módulo 3: Tu Primer Endpoint con Spring Web

Este proyecto es el "Hola Mundo" de un microservicio. El objetivo es configurar el proyecto Spring Boot más simple
posible que pueda recibir una petición web HTTP y responder.

Se enfoca en el "setup" inicial y la magia de Spring para convertir JSON en objetos Java.

## Conceptos Clave

1. **`@SpringBootApplication`**: La anotación que inicia todo. Combina `@Configuration`, `@EnableAutoConfiguration` y
   `@ComponentScan`.
2. **`@RestController`**: Le dice a Spring que esta clase manejará peticiones HTTP y que las respuestas se deben
   convertir a JSON (o similar) automáticamente.
3. **`@RequestMapping`**: Define la ruta base para todos los endpoints en este controlador (ej. `/api/v1/users`).
4. **`@PostMapping`**: Mapea específicamente peticiones HTTP POST a este método.
5. **`@RequestBody`**: La "magia" principal. Le dice a Spring: "Toma el cuerpo (body) de la petición HTTP, que viene en
   JSON, y conviértelo en un objeto Java de este tipo (`UserRegistrationRequest`)".
6. **DTO (Data Transfer Object)**: Usamos un `record` de Java (`UserRegistrationRequest`) para definir la "forma" de los
   datos que esperamos recibir.
7. **`ResponseEntity`**: Un objeto *wrapper* que nos permite tener control total sobre la respuesta HTTP, incluyendo el
   **código de estado** (ej. `200 OK`).

## Ejecución

1. Ejecuta el método `main` en `Exercise01Application.java`.
2. Usa un cliente API (como Postman o Insomnia) para hacer la siguiente petición:

```http
POST http://localhost:8080/api/v1/users/register
Content-Type: application/json

{
  "username": "cesarlead",
  "email": "test@test.com",
  "password": "123",
  "age": 30
}
```