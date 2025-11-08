# Módulo 2: Simulación de Sistema Bancario (POO)

Este proyecto es una aplicación práctica que implementa los conceptos fundamentales de la Programación Orientada a
Objetos (POO) para simular un sistema bancario simple.

A diferencia de los scripts del Módulo 1, este es un sistema cohesivo con múltiples clases que interactúan entre sí.

* **Ver el Enunciado:
  ** [Lee los requisitos completos en Enunciado.md](/src/main/java/course/javaspringboot/banking_exercise/enunciado.md)

## 1. Conceptos POO Aplicados

Este proyecto es una demostración de los 4 pilares de la POO y los principios SOLID:

* **Abstracción:** La clase `Account` es `abstract`. Define el "contrato" (`deposit`, `withdraw`) que todas las cuentas
  *deben* tener, sin definir *cómo* lo hacen todas.
* **Herencia:** `SavingsAccount` y `CheckingAccount` heredan la funcionalidad común (como `balance`, `customer` y
  `deposit`) de `Account`.
* **Polimorfismo:** El `Bank` maneja una `List<Account>`. No le importa si un objeto es `SavingsAccount` o
  `CheckingAccount`; puede llamar a `withdraw()` en cualquiera, y Java ejecutará la versión correcta (con sobregiro o
  sin él).
* **Encapsulación:** La lógica de negocio está protegida.
    * El `Bank` actúa como una **Fachada** (Facade Pattern), ocultando la complejidad de gestionar las listas y mapas.
    * `getAccounts()` devuelve una lista *inmutable* (`Collections.unmodifiableList`), protegiendo la lista interna del
      banco.
    * `Customer` es inmutable (sus atributos son `final`).

## 2. Decisiones de Diseño (El "Porqué")

* **`Bank` (Servicio):** Centraliza la lógica de negocio. Es la única clase con la que `App` (el cliente) necesita
  hablar.
* **`Account` (Modelo):** Clase `abstract` que aplica DRY al compartir la lógica de `deposit()` y el estado (`balance`).
* **`InsufficientFundsException` (Excepción):** Se creó una excepción *checked* personalizada porque "fondos
  insuficientes" es un evento de negocio esperado que el llamador (la `App`) *debe* manejar (con `try-catch`).
* **Estructuras de Datos:**
    * `Map<String, Customer>`: Se eligió un `HashMap` para `customers` para permitir búsquedas instantáneas por
      documento (Clave).
    * `List<Account>`: Una lista simple para todas las cuentas creadas.
* **Java Moderno:** El proyecto utiliza `Optional` para evitar `NullPointerException` al buscar clientes y la API de
  `Stream` para filtros de datos funcionales.

## 3. Cómo Ejecutar

El punto de entrada es `banking/App.java`.

1. Compila todo el proyecto desde la carpeta `src/` (o superior).
2. Ejecuta la clase `App`:

```bash
javac -d out src/banking/App.java src/banking/model/*.java src/banking/service/*.java
java -cp out banking.App
Salida Esperada:

CheckingAccount | Cliente: Bob (456) | Balance: -100.0
SavingsAccount | Cliente: Alice (123) | Balance: 800.0

Cuentas con balance >= 500:
SavingsAccount | Cliente: Alice (123) | Balance: 800.0
```