
## **Complemento de Validaciones y Limpieza de datos (segun dudas expresadas en clase)**

Este manual complementa los ejercicios prácticos y sirve como una guía de referencia rápida para implementar validaciones de datos de forma robusta, limpia y segura en aplicaciones reales.

-----

### **1. Grupos de Validación: Adaptando Reglas al Contexto**

#### **Qué son y por qué los necesitas?**

Imagina que usas una misma clase `UserRequest` para dos acciones: **crear** un usuario y **actualizarlo**. Las reglas no son las mismas:

* Al **crear**, la contraseña es obligatoria.

* Al **actualizar**, el usuario podría no querer cambiar su contraseña, por lo que debería ser opcional.

Los **grupos de validación** son como "etiquetas" que te permiten marcar qué regla de validación se debe aplicar en cada contexto. Esto evita tener que crear múltiples clases DTO casi idénticas, manteniendo tu código **DRY (Don't Repeat Yourself)**.

#### **Cómo se implementan?**

El proceso tiene 3 pasos clave:

**Paso 1: Definir las interfaces de grupo.**
Son simplemente interfaces vacías que actúan como identificadores.

```java
// File: com/cesarlead/validation/groups/OnCreate.java
public interface OnCreate {}

// File: com/cesarlead/validation/groups/OnUpdate.java
public interface OnUpdate {}
```

**Paso 2: Asignar las validaciones a los grupos en el DTO.**
Usas el atributo `groups` en cada anotación. Una validación puede pertenecer a uno o varios grupos.

```java
public class UserRequest {

    // Esta validación se aplica en ambos casos
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String email;

    // Esta validación SOLO se aplica al crear el usuario
    @NotBlank(groups = OnCreate.class)
    private String password;
}
```

**Paso 3: Activar el grupo correcto en el Controlador.**
Para especificar qué grupo usar, reemplazas `@Valid` por `@Validated` y le pasas la clase del grupo.

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @PostMapping
    // Activa solo las reglas del grupo OnCreate
    public void createUser(@Validated(OnCreate.class) @RequestBody UserRequest request) {
        // ...
    }

    @PutMapping("/{id}")
    // Activa solo las reglas del grupo OnUpdate
    public void updateUser(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody UserRequest request) {
        // ...
    }
}
```

-----

### **2. Validaciones Personalizadas: El Caso de la Contraseña**

#### **Por qué crear tus propias anotaciones?**

A veces, las anotaciones estándar (`@Size`, `@Pattern`, etc.) no son suficientes o ensucian tu código.

* **Para lógica compleja:** Validar si un NIF es válido o si un `username` ya existe en la base de datos.
* **Para reutilizar y limpiar (DRY):** La validación de una contraseña segura es el ejemplo perfecto. En lugar de repetir 3 o 4 anotaciones en cada DTO que la necesite, creas una sola: `@ValidPassword`.

#### **Cómo se implementa `@ValidPassword`?**

**Paso 1: Crear la interfaz de la anotación.**
Define la anotación y la enlaza con su clase de lógica (`PasswordValidator.class`).

```java
// File: com/cesarlead/validation/annotation/ValidPassword.java
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class) // Enlace a la lógica (Importante)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "La contraseña no cumple con los requisitos de seguridad.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

**Paso 2: Implementar la lógica del validador.**
Esta clase contiene la lógica real que determina si la contraseña es válida.

```java
// File: com/cesarlead/validation/validator/PasswordValidator.java
import com.cesarlead.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            return false; // O true si quieres que @NotNull maneje esto por separado
        }
        // Lógica de la expresión regular u otras comprobaciones
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,30}$";
        return password.matches(regex);
    }
}
```

**Paso 3: Usar la nueva anotación.**
El DTO ahora es mucho más limpio y la intención es más clara.

```java
// Antes (verboso y repetitivo)
@NotBlank
@Size(min = 8, max = 30)
@Pattern(regexp = "...")
private String password;

// Después (limpio, semántico y reutilizable)
@ValidPassword
private String password;
```

-----

## **3. Sanitización de Datos: Limpiar Caracteres Especiales**

### **El Concepto Clave: Validación ≠ Sanitización**

Este es uno de los principios más importantes de la seguridad de aplicaciones:

* **Validación:** **VERIFICA** si los datos son correctos. Responde "sí" o "no". No modifica los datos.
* **Sanitización:** **LIMPIA** los datos. **MODIFICA** el input para eliminar o neutralizar caracteres peligrosos (como `<script>`, `'` o `"`).

**La sanitización DEBE ocurrir ANTES de la validación.** Primero limpias la entrada para hacerla segura, y luego validas si el resultado limpio cumple las reglas de negocio.

#### **Cómo y dónde sanitizar?**

**Nunca intentes crear tu propia lógica de sanitización.** Es extremadamente fácil cometer errores que dejen huecos de seguridad. Usa librerías estándar de la industria.

* **Herramienta recomendada:** **OWASP Java Encoder**. Es la librería de referencia para codificar la salida y prevenir ataques de **Cross-Site Scripting (XSS)**.

* **Dónde se aplica?** La sanitización se realiza en la capa más externa posible, usualmente en la **capa de Servicio** o **Controlador**, justo después de recibir los datos y antes de pasarlos a la lógica de negocio o a la base de datos.

**Ejemplo práctico:**
Imagina un campo de comentarios donde un atacante podría inyectar HTML malicioso.

```java
// En tu UserService.java
import org.owasp.encoder.Encode; // Importar la librería de OWASP

public void saveComment(CommentDTO commentDto) {
    // 1. SANITIZAR PRIMERO
    // Codifica cualquier carácter HTML para que el navegador lo muestre como texto
    // y no lo ejecute. <script> se convierte en &lt;script&gt;
    String sanitizedText = Encode.forHtml(commentDto.getText());

    // 2. VALIDAR Y PROCESAR DESPUÉS
    // Ahora que el texto es seguro, puedes aplicar tus reglas de negocio
    if (sanitizedText.length() > 500) {
        throw new ValidationException("El comentario es demasiado largo.");
    }

    // 3. Guardar el dato YA SANITIZADO en la base de datos
    Comment comment = new Comment();
    comment.setText(sanitizedText);
    commentRepository.save(comment);
}
```

Bueno chicos con esta pequena guia, les mostre como crear validaciones personalizadas, y como realizar la limpieza de datos de la forma correcta y profesional (les di la respuesta del ejercicio 2, practiquen!!) resuelvan los ejercicios.
