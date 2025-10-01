### Solucionar "Preview Features" en IntelliJ IDEA

Muchachones para corregir el error `primitive patterns are a preview feature` en Java.

#### El Problema: Qué significa este error???

Este error aparece cuando tu código utiliza una característica muy nueva de Java que aún no es estándar y está en fase
de "vista previa". El compilador y la Máquina Virtual de Java (JVM) las desactivan por defecto para evitar que se use
código experimental en producción.

#### La Lógica de la Solución: Compilar y Ejecutar

Para solucionarlo, debes dar permiso en dos lugares diferentes:

1. **Al Compilador:** Para que entienda y transforme tu código `.java` a `.class`.
2. **A la JVM (Ejecución):** Para que pueda correr el código `.class` ya compilado.

Sigue estos tres pasos para configurar tu entorno correctamente.

#### Paso 1: Habilitar "Preview" para el Compilador

Primero, dile al compilador de IntelliJ que acepte la nueva sintaxis.

1. Ve a `File` -\> `Settings`.
2. Navega a `Build, Execution, Deployment` -\> `Compiler` -\> `Java Compiler`.
3. En el campo **`Additional command line parameters`**, escribe `--enable-preview`.

-----

#### Paso 2: Habilitar "Preview" para la Ejecución (JVM)

Ahora, configura la Máquina Virtual de Java (JVM)

1. En la esquina superior derecha, busca el nombre de tu configuración de ejecución y haz clic para expandir el menú.
   Selecciona la opción **`Edit...`**

2. En la ventana de `Run/Debug Configurations`, busca el campo de opciones de la VM. Si no aparece, haz clic en *
   *`Modify options`** y activa **`Add VM options`**.

3. En el campo que ahora es visible, llamado **`VM options`**, escribe `--enable-preview`.

-----

#### Paso 3: Asegurar que el Código sea Ejecutable

Finalmente, Java necesita un punto de entrada para iniciar tu programa. Asegúrate de que tu método sea llamado desde un
`public static void main(String[] args)`.

```java
package course.javaspringboot;

public class Exercises {

    // El método `main` es el punto de entrada que Java buscará
    public static void main(String[] args) {
        // Desde aquí llamamos a tu método para que se ejecute
        bankClientPatternMatching();
    }

    // Tu método con la característica de "preview"
    public static void bankClientPatternMatching() {
        double saldo = 5000.00;

        String mensaje = switch ((Double) saldo) {
            case Double s when s < 1000 -> "Cliente Básico";
            case Double s when s < 10000 -> "Cliente Premium";
            case Double s -> "Cliente VIP";
        };

        System.out.println(mensaje);
    }
}
```

Y Listo muchachones, todo ready, pero lo ideal es utilizar la clase wrapper `Double` en lugar del tipo primitivo
`double`.

Nota: le adjunte unos cap de pantalla, para que puedan ver como se configura el entorno.