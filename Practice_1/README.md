# Módulo 1: Fundamentos de la Sintaxis de Java

Este módulo sirve como una introducción "cero a héroe" a la sintaxis fundamental de Java. Cada archivo (`.java`) es un
programa independiente y ejecutable que se enfoca en un solo concepto.

El objetivo es construir una base sólida (KISS) antes de avanzar a conceptos más complejos como la Programación
Orientada a Objetos.

## Conceptos Cubiertos

| Archivo                  | Concepto Clave            | Descripción                                                                                                                                                                   |
|:-------------------------|:--------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Variables.java`         | Declaración y Tipos       | Demuestra la declaración de variables, el uso de `final` para constantes y la diferencia entre primitivos y *wrappers*.                                                       |
| `DataTypes.java`         | Primitivos vs. Referencia | Explica la diferencia fundamental entre los tipos de datos que viven en el **Stack** (pila) y los que viven en el **Heap** (montón).                                          |
| `Operators.java`         | Operadores                | Cubre operadores aritméticos, relacionales, lógicos (incluyendo *short-circuit*), ternarios y de asignación.                                                                  |
| `Expressions.java`       | Expresiones               | Muestra cómo Java evalúa expresiones, desde simples asignaciones hasta expresiones lógicas y aritméticas anidadas.                                                            |
| `ControlStructures.java` | Flujo de Control          | Demuestra las estructuras condicionales (`if`, `switch`) y los bucles (`for`, `while`, `do-while`, `for-each`).                                                               |
| `Exercises.java`         | **Ejercicios Prácticos**  | **(El más importante)** Aplica todos los conceptos anteriores en tres escenarios prácticos: un cajero automático, un inicio de sesión bancario y un clasificador de clientes. |

## Cómo Usar este Módulo

Cada archivo `.java` contiene su propio método `public static void main(String[] args)`.

Puedes compilar y ejecutar cada uno de forma independiente desde tu terminal o IDE para ver los conceptos en acción.

**Ejemplo (usando terminal):**

```bash
# Navega a la carpeta que contiene los archivos
javac Exercises.java
java Exercises