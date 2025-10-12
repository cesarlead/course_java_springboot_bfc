## Enunciado del Ejercicio: Sistema de Gestión Bancaria Simple

### Objetivo

Diseñar e implementar en Java una aplicación de consola para simular las operaciones básicas de un banco. El sistema
debe ser capaz de gestionar clientes y diferentes tipos de cuentas bancarias, aplicando los principios fundamentales de
la Programación Orientada a Objetos (POO): encapsulación, herencia, polimorfismo y abstracción.

### Requisitos Funcionales

#### 1\. Modelado de Entidades

* **`Customer`**: Cada cliente debe tener un **nombre** y un **documento de identidad** único. Una vez creado, estos
  datos no deben poder modificarse.
* **`Account`**: Debe ser una clase **abstracta** que sirva como base para todos los tipos de cuentas.
    * Debe estar asociada a un `Customer`.
    * Debe tener un `balance` (saldo).
    * Debe definir los métodos abstractos `deposit(double amount)` y `withdraw(double amount)`.
* **`SavingsAccount` (Cuenta de Ahorros)**:
    * Hereda de `Account`.
    * Tiene un atributo adicional: `interestRate` (tasa de interés).
    * La operación `withdraw` solo se permite si el saldo es suficiente (`balance >= amount`).
* **`CheckingAccount` (Cuenta Corriente)**:
    * Hereda de `Account`.
    * Tiene un atributo adicional: `overdraftLimit` (límite de sobregiro).
    * La operación `withdraw` se permite si el monto no excede el saldo más el límite de sobregiro (
      `balance + overdraftLimit >= amount`).

#### 2\. Manejo de Errores

* Crea una excepción personalizada y verificada (checked exception) llamada `InsufficientFundsException`.
* Esta excepción debe ser lanzada por los métodos `withdraw` cuando una operación de retiro no pueda completarse según
  las reglas de cada tipo de cuenta.

#### 3\. Lógica del Negocio (Clase `Bank`)

* Crea una clase `Bank` que centralice todas las operaciones y actúe como la fachada del sistema.
* Debe permitir:
    * `addCustomer()`: Añadir nuevos clientes.
    * `openSavingsAccount()` y `openCheckingAccount()`: Abrir diferentes tipos de cuentas para un cliente existente.
    * `transfer()`: Transferir dinero entre dos cuentas. Esta operación debe ser atómica en su lógica (retiro de una
      cuenta y depósito en otra).
    * `getAccounts()`: Devolver una lista de todas las cuentas del banco. La lista devuelta no debe ser modificable
      desde fuera de la clase `Bank`.
    * `filterAccountsByBalance()`: Un método que, utilizando la API de Streams de Java, devuelva una nueva lista con las
      cuentas que tengan un saldo igual o superior a un monto especificado.

#### 4\. Aplicación Principal (`App`)

* Crea una clase `App` con un método `main` para demostrar el funcionamiento del sistema.
* En el `main`, realiza las siguientes acciones:
    1. Crea una instancia de `Bank`.
    2. Crea al menos dos clientes.
    3. Abre una cuenta de ahorros para un cliente y una cuenta corriente para el otro.
    4. Realiza depósitos en ambas cuentas.
    5. Ejecuta una transferencia exitosa entre las cuentas.
    6. Realiza un retiro de la cuenta corriente que haga uso del límite de sobregiro.
    7. Maneja la excepción `InsufficientFundsException` con un bloque `try-catch`.
    8. Imprime el estado final de todas las cuentas.
    9. Usa el método de filtrado para mostrar solo las cuentas con un saldo superior a un valor determinado.

-----