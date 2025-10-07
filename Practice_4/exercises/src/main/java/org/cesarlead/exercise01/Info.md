## **Ejercicio 1: API de Registro - Validaciones Fundamentales y Manejo de Errores**

##### **Enunciado**

Se te ha proporcionado la base de una API REST para el registro de nuevos usuarios. Actualmente, el endpoint
`POST api/v1/users/register` acepta cualquier dato sin validarlo, lo que puede llevar a inconsistencias en la base de
datos.
Además, si ocurre un error, la API devuelve una traza de error (stack trace) larga y poco amigable para el cliente.

**Tus tareas son:**

1. **Validar el DTO:** Agrega las anotaciones de validación necesarias al DTO `UserRegistrationRequest.java` para
   cumplir las siguientes reglas:
    * `username`: No puede ser nulo ni vacío. Debe tener entre 3 y 20 caracteres.
    * `email`: No puede ser nulo y debe tener un formato de email válido.
    * `password`: No puede ser nula. Debe tener entre 8 y 30 caracteres. Debe contener al menos una letra mayúscula, una
      minúscula y un número.
    * `age`: No puede ser nulo. El usuario debe tener al menos 18 años.
2. **Activar la Validación:** Asegúrate de que la validación se active en el controlador (`UserController.java`).
3. **Crear un Manejador de Excepciones Global:** Implementa una clase que capture las excepciones de validación (
   `MethodArgumentNotValidException`). Este manejador debe interceptar el error y, en lugar de la traza por defecto,
   devolver:
    * Un código de estado HTTP `400 Bad Request`.
    * Un cuerpo de respuesta (body) en formato JSON que contenga un mapa de los campos que fallaron y sus respectivos
      mensajes de error.

**Ejemplo de respuesta JSON esperada si fallan varias validaciones:**

```json
{
  "email": "El formato del email es inválido.",
  "password": "La contraseña debe tener entre 8 y 30 caracteres.",
  "age": "El usuario debe tener al menos 18 años."
}
```

## **Ejercicio 2: API de Usuarios - Múltiples Escenarios con Grupos de Validación**

### **Enunciado**

Partiendo de la solución del ejercicio 1, la aplicación ha crecido. Ahora, el mismo DTO que usas para registrar un
usuario nuevo se reutilizará para actualizar el perfil de un usuario existente. Sin embargo, las reglas de negocio son
diferentes:

* **Para Crear un Usuario (POST /api/v1/users):** Todos los campos son obligatorios (`username`, `email`, `password`,
  `age`). La contraseña es un requisito indispensable.
* **Para Actualizar un Usuario (PUT /api/v1/users/{id}):** El `username` y el `email` siguen siendo obligatorios. Sin
  embargo, la `password` y la `age` son **opcionales**. Un usuario puede querer actualizar su email sin necesidad de
  cambiar su contraseña.

El reto es hacer que el mismo DTO (`UserRequest.java`) aplique un conjunto de reglas para la creación y otro para la
actualización.

**Tus tareas son:**

1. **Definir los Grupos de Validación:** Crea dos interfaces vacías que servirán como marcadores para nuestros grupos:
   `OnCreate.java` y `OnUpdate.java`.
2. **Asignar Validaciones a los Grupos:** Modifica el DTO `UserRequest.java`. Utiliza el atributo `groups` en cada
   anotación de validación para especificar cuándo debe aplicarse.
    * Algunas validaciones se aplicarán siempre (ej. el formato del email).
    * Otras se aplicarán solo al crear (ej. que la contraseña no sea nula).
3. **Adaptar el Controlador:** Modifica `UserController.java` para que maneje dos endpoints:
    * `POST /api/v1/users` para la creación.
    * `PUT /api/v1/users/{id}` para la actualización.
4. **Activar los Grupos Correctos:**

NOTA: Las interfaces NUEVAS son solo marcadores, por eso deben de estar vacias (ej.)

```java

import jakarta.validation.groups.Default;

/**
 * Validation group for user creation operations.
 * Extends Default to include validations that don't have a group specified.
 */
public interface OnCreate extends Default {
}
```
