### **Java como Pilar de la Tecnología Financiera (FinTech) 🏦**

En el dinámico mundo de las FinTech, la elección de la tecnología no es trivial. **Java** se erige como un pilar
fundamental por cuatro razones no negociables: **seguridad**, **fiabilidad**, **rendimiento** y **escalabilidad**. Su
robusta Máquina Virtual (JVM), su modelo de seguridad multicapa y un ecosistema maduro lo convierten en la elección
predilecta para sistemas de misión crítica que procesan miles de millones de transacciones diarias.

### **El ADN del Sistema - Clases y Objetos **

La Programación Orientada a Objetos (POO) es un paradigma que nos permite modelar entidades del mundo real directamente
en el código. Los dos conceptos fundamentales son la **clase** y el **objeto**.

* **Clase**: Es el **plano o plantilla** (`blueprint`). Define la estructura (atributos) y el comportamiento (métodos)
  de un concepto. Piensa en el plano de una casa: especifica que tendrá paredes, puertas y ventanas, pero no es una casa
  real.
* **Objeto**: Es la **instancia concreta** y viva de una clase. Es la casa construida a partir del plano. Puedes
  construir muchas casas (objetos) a partir de un solo plano (clase), y cada una es independiente, con sus propios
  habitantes y color de pintura (su propio estado).

-----

### **Anatomía de una Clase: El Plano Detallado**

Una clase en Java se compone principalmente de tres elementos: atributos, constructores y métodos.

#### **1. Atributos (Propiedades o Campos): El Estado Interno**

Son las variables que pertenecen a la clase y definen sus características o estado. Cada objeto creado a partir de esta
clase tendrá su propia copia de estos atributos.

* **Ejemplo**: En nuestra clase `CuentaBancaria`, el estado de cada cuenta se define por su `numeroCuenta`, `titular` y
  `saldo`.

#### **2. Constructores: El Ritual de Creación**

Un constructor es un bloque de código especial que se invoca **automáticamente** cada vez que se crea un nuevo objeto de
la clase (usando la palabra clave `new`). Su principal responsabilidad es **inicializar los atributos** para asegurar
que el objeto nazca en un estado válido y consistente.

* **Características clave**:
    * Tiene el mismo nombre que la clase.
    * No tiene tipo de retorno (ni siquiera `void`).
    * Si no defines ningún constructor, Java proporciona uno por defecto, vacío y sin argumentos.

```java
public class CuentaBancaria {
    String numeroCuenta;
    String titular;
    double saldo;

    // --- CONSTRUCTOR ---
    // Este constructor asegura que cada cuenta se cree con datos iniciales.
    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;
        System.out.println("¡Nueva cuenta creada para " + this.titular + "!");
    }
}
```

#### **3. Métodos: El Comportamiento**

Son las funciones definidas dentro de la clase que operan sobre los atributos del objeto. Definen lo que un objeto *
*puede hacer**.

* **Ejemplo**: Una `CuentaBancaria` no solo *tiene* un saldo, sino que *puede* realizar acciones como `depositar()` y
  `retirar()`, las cuales modifican su estado interno.

-----

### **Creando un Objeto: De la Plantilla a la Realidad (Instanciación)**

Cuando escribes `new CuentaBancaria(...)`, desencadenas un proceso fundamental:

```java
// Creación (instanciación) de dos objetos independientes
CuentaBancaria cuentaDeJuan = new CuentaBancaria("ES01-2345", "Juan Pérez", 1000.0);
CuentaBancaria cuentaDeAna = new CuentaBancaria("ES02-6789", "Ana García", 5000.0);
```

#### **¿Qué Sucede en la Memoria? 🧠**

1. **Reserva de Memoria**: La palabra `new` le pide al sistema que asigne un bloque de memoria en una zona llamada *
   *Heap**. Este espacio es lo suficientemente grande para almacenar todos los atributos del objeto (`numeroCuenta`,
   `titular`, `saldo`).
2. **Ejecución del Constructor**: A continuación, se llama al constructor de la clase. Su código se ejecuta para
   inicializar los atributos dentro de ese bloque de memoria recién creado.
3. **Asignación de Referencia**: Finalmente, la dirección de memoria de ese objeto en el Heap se asigna a la variable (
   `cuentaDeJuan`). La variable, que vive en otra zona de memoria llamada **Stack**, no contiene el objeto en sí, sino
   una **referencia** (un "puntero" o "control remoto") que apunta a él.

Por eso, `cuentaDeJuan` y `cuentaDeAna` son dos objetos distintos que ocupan diferentes espacios en el Heap, aunque
ambas variables de referencia estén en el Stack.

-----

### **Controlando la Visibilidad: Modificadores de Acceso**

Aquí resolvemos el **antipatrón** del acceso directo. Los modificadores de acceso son palabras clave que definen desde
dónde se puede acceder a un atributo o método. Son la base del **encapsulamiento**.

| Modificador | Visibilidad                                     | Propósito Principal                                                                      |
|:------------|:------------------------------------------------|:-----------------------------------------------------------------------------------------|
| `public`    | ✅ Desde cualquier lugar                         | Exponer la funcionalidad principal de la clase (su "API pública").                       |
| `protected` | ✅ Dentro de la misma clase, paquete y subclases | Permitir que las clases hijas modifiquen o accedan a ciertos aspectos del padre.         |
| `(default)` | ✅ Dentro de la misma clase y paquete            | Agrupar clases que necesitan colaborar estrechamente.                                    |
| `private`   | ✅ **Solo** dentro de la misma clase             | **Proteger el estado interno del objeto.** Esta es la mejor práctica para los atributos. |

-----

### **El Concepto `static`: Miembros de la Clase, no del Objeto**

La palabra clave `static` rompe la regla de que "cada objeto tiene su propia copia". Un miembro `static` pertenece a la
**clase misma**, no a ninguna instancia individual. Solo existe una copia, compartida por todos los objetos de esa
clase.

* **Analogía**: Piensa en el **nombre del banco** (`static`) frente al **saldo de una cuenta** (no estático). Todos los
  objetos `CuentaBancaria` comparten el mismo nombre de banco, pero cada uno tiene su propio saldo individual.

#### **Atributos `static`**

Se usan para constantes o contadores globales de la clase.

* **Ejemplo**: Un contador para saber cuántas cuentas se han creado en total o el nombre del banco.

#### **Métodos `static`**

Son funciones de utilidad que se pueden llamar directamente desde la clase, sin necesidad de crear un objeto.

* **Limitación clave**: Un método `static` no puede acceder a atributos o métodos no estáticos, porque no está asociado
  a ningún objeto en particular.
* **Ejemplo**: Una función para validar un formato de número de cuenta (IBAN) antes de crear el objeto.

-----

### **Ejemplo**

```java
public class CuentaBancaria {

    // --- ATRIBUTOS (ESTADO) ---
    // 'private' para proteger el estado interno (Encapsulamiento)
    private final String numeroCuenta; // 'final' porque el número de cuenta no debería cambiar
    private String titular;
    private double saldo;

    // 'static': Pertenece a la CLASE, compartido por todos los objetos.
    private static int contadorCuentas = 0;
    public static final String NOMBRE_BANCO = "FinTech Java Bank";

    // --- CONSTRUCTOR ---
    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
        // Validación básica en el constructor
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;
        contadorCuentas++; // Incrementa el contador global cada vez que se crea una cuenta
    }

    // --- MÉTODOS (COMPORTAMIENTO) ---
    // 'public': Parte de la interfaz pública de la clase
    public void depositar(double monto) {
        if (monto > 0) {
            this.saldo += monto;
        }
    }

    public void retirar(double monto) {
        if (monto > 0 && this.saldo >= monto) {
            this.saldo -= monto;
        }
    }

    // Métodos "getters" para permitir acceso de solo lectura al estado
    public double getSaldo() {
        return this.saldo;
    }

    public String getTitular() {
        return this.titular;
    }

    // --- MÉTODO STATIC ---
    // Se puede llamar sin crear un objeto: CuentaBancaria.getContadorCuentas()
    public static int getContadorCuentas() {
        return contadorCuentas;
    }
}
```

### **Módulo 2: Los Pilares del Diseño Robusto en POO**

Estos son los principios que transforman el código simple en software mantenible y seguro.

#### **1. Encapsulamiento: La Bóveda de Seguridad 🔐**

Es el principio de **proteger el estado interno de un objeto** y exponer el control a través de métodos públicos.

* **Implementación**: Se usan modificadores de acceso (`private`) para los atributos.
* **Propósito**: Garantizar que el objeto siempre se encuentre en un estado válido.

**Ejemplo Refactorizado:**

```java
public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;

    // Constructor para garantizar que un objeto se cree en un estado válido
    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        // Validación en el constructor
        this.saldo = saldoInicial >= 0 ? saldoInicial : 0;
    }

    // Método público para interactuar con el estado (getter)
    public double getSaldo() {
        return this.saldo;
    }

    // Método público con lógica de negocio para modificar el estado
    public void depositar(double monto) {
        if (monto > 0) {
            this.saldo += monto;
        }
    }

    public void retirar(double monto) {
        if (monto > 0 && this.saldo >= monto) {
            this.saldo -= monto;
        }
    }
}
```

Ahora es imposible hacer `miCuenta.saldo = -500;`. Cualquier cambio debe pasar por los filtros de `depositar()` o
`retirar()`.

#### **2. Herencia: Creando Familias de Objetos 👨‍👩‍👧‍👦**

Permite que una clase hija herede atributos y métodos de una clase padre, modelando una relación **"es un"**. Su
objetivo principal es la **reutilización de código**.

* **Implementación**: Palabra clave `extends`.

**Ejemplo de Jerarquía:**

```java
// CuentaAhorro "ES UNA" CuentaBancaria con una característica adicional
public class CuentaAhorro extends CuentaBancaria {
    private double tasaInteres;

    public CuentaAhorro(String numero, String titular, double saldo, double tasa) {
        super(numero, titular, saldo); // Llama al constructor del padre
        this.tasaInteres = tasa;
    }

    public void aplicarInteres() {
        double interes = getSaldo() * tasaInteres;
        depositar(interes);
    }
}
```

⭐ **Principio de Diseño Clave: Favorecer la Composición sobre la Herencia**

Este es un mantra en el diseño de software moderno.

* **Herencia (`es un`)**: Crea un acoplamiento fuerte. Un cambio en la clase padre puede romper a todas sus hijas. Es
  rígida.
* **Composición (`tiene un`)**: Crea un acoplamiento débil y flexible. Una clase *contiene* una instancia de otra y
  delega responsabilidades.

Un `Cliente` no "es una" `Cuenta`, sino que **"tiene una"** o varias. Se modela así:

```java
public class Cliente {
    private final String idCliente;
    private final String nombre;
    // Composición: Un Cliente TIENE UNA lista de cuentas.
    private final List<CuentaBancaria> cuentas;

    public Cliente(String idCliente, String nombre) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.cuentas = new ArrayList<>();
    }

    public void agregarCuenta(CuentaBancaria cuenta) {
        this.cuentas.add(cuenta);
    }
}
```

La composición nos da la flexibilidad de agregar, quitar o cambiar el tipo de las "partes" sin afectar al "todo".

#### **3. Polimorfismo: Un Comportamiento, Múltiples Formas 🎭**

Es la capacidad de objetos de diferentes clases de responder al mismo mensaje (método) de manera específica.

* **Implementación**: Sobrescritura de métodos (`@Override`).

**Ejemplo Polimórfico:**

Se añade un método `generarExtracto()` a `CuentaBancaria` y se sobrescribe en sus hijas.

```java
// En CuentaBancaria
public String generarExtracto() {
    return "Extracto Básico: Saldo = " + getSaldo();
}

// En CuentaAhorro
@Override
public String generarExtracto() {
    return "Extracto Cuenta de Ahorro: Saldo = " + getSaldo() + ", Interés = " + tasaInteres;
}

// Uso polimórfico
List<CuentaBancaria> cuentas = new ArrayList<>();
cuentas.

add(new CuentaBancaria("C001", "Juan",1000));
        cuentas.

add(new CuentaAhorro("A001", "Ana",5000,0.05));

// Java sabe qué versión del método llamar en tiempo de ejecución
        for(
CuentaBancaria cuenta :cuentas){
        System.out.

println(cuenta.generarExtracto());
        }
```

#### **4. Abstracción: Ocultando la Complejidad ☁️**

Consiste en exponer solo la funcionalidad esencial, ocultando los detalles de implementación. Se enfoca en el **"qué"**
hace un objeto, no en el **"cómo"**. El encapsulamiento es la técnica que nos permite lograr la abstracción.

-----

### **Módulo 3: Contratos y Esqueletos - Interfaces y Clases Abstractas**

Java ofrece dos herramientas poderosas para definir abstracciones y "contratos" de comportamiento. La elección correcta
es crucial.

| Característica  | Clase Abstracta                                                                                      | Interfaz                                                                                           |
|:----------------|:-----------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------|
| **Propósito**   | Modelar una relación **"es un"** (is-a). Compartir estado y comportamiento base.                     | Definir un contrato o capacidad **"es capaz de"** (can-do).                                        |
| **Herencia**    | Una clase solo puede `extender` **una** clase abstracta.                                             | Una clase puede `implementar` **múltiples** interfaces.                                            |
| **Miembros**    | Puede tener métodos `abstract` (sin cuerpo) y métodos concretos (con cuerpo). Puede tener atributos. | Antes de Java 8, solo métodos `abstract`. Ahora puede tener métodos `default` y `static`.          |
| **Caso de Uso** | Crear una jerarquía de clases muy relacionadas (e.g., `Figura` -\> `Círculo`, `Cuadrado`).           | Definir capacidades para clases no relacionadas (e.g., `Comparable`, `Serializable`, `Auditable`). |

**Ejemplo Práctico:**

1. **Clase Abstracta**: Convertimos `CuentaBancaria` en `Cuenta`, forzando a todas las subclases a definir cómo se
   calcula su costo de mantenimiento.

   ```java
   public abstract class Cuenta {
       // ... atributos y métodos comunes ...

       // Método abstracto: un contrato que las hijas DEBEN implementar
       public abstract double calcularCostoMantenimiento();
   }
   ```

2. **Interfaz**: Definimos una capacidad `Auditable` que puede ser aplicada a cualquier clase, relacionada o no.

   ```java
   public interface Auditable {
       // Todas las clases que implementen esta interfaz deben proveer este método.
       String generarLogTransaccion();
   }

   // Una cuenta de inversión puede ser una Cuenta Y también ser Auditable.
   public class CuentaInversion extends Cuenta implements Auditable {
       // ... implementación de métodos de Cuenta ...

       @Override
       public String generarLogTransaccion() {
           return "LOG: Transacción de alto valor en cuenta de inversión " + getNumeroCuenta();
       }
   }
   ```

-----

### **Módulo 4: Gestión de Riesgos - Manejo de Excepciones ⚠️**

En sistemas financieros, los fallos no son una opción, son una certeza. El manejo de excepciones es la estrategia para
construir sistemas resilientes.

| Tipo          | Herencia           | Propósito                                                       | Compilador                                       | Ejemplo en FinTech                                                         |
|:--------------|:-------------------|:----------------------------------------------------------------|:-------------------------------------------------|:---------------------------------------------------------------------------|
| **Checked**   | `Exception`        | Errores **recuperables y esperados** del negocio.               | **Obliga** a manejarla (`try-catch` o `throws`). | `SaldoInsuficienteException`, `FondoDeInversionNoDisponibleException`.     |
| **Unchecked** | `RuntimeException` | **Errores de programación (bugs)** o de sistema irrecuperables. | **No obliga** a manejarla.                       | `NullPointerException`, `IllegalArgumentException` (e.g., monto negativo). |

**Excepciones Personalizadas:**

Crear tus propias excepciones hace el código auto-documentado y permite un manejo de errores más granular.

```java
// Excepción personalizada que lleva contexto del error
public class SaldoInsuficienteException extends Exception {
    private final double saldoActual;
    private final double montoRequerido;

    public SaldoInsuficienteException(String message, double saldo, double monto) {
        super(message);
        this.saldoActual = saldo;
        this.montoRequerido = monto;
    }
    // ... getters ...
}
```

⭐ **Mejor Práctica: `try-with-resources`**

Para manejar recursos externos (conexiones a BBDD, archivos), esta sintaxis garantiza que el recurso se cierre
automáticamente, evitando fugas.

```java
// La conexión 'con' se cerrará automáticamente al salir del bloque try,
// ya sea que termine normal o por una excepción.
try(Connection con = dataSource.getConnection();
PreparedStatement stmt = con.prepareStatement(SQL_QUERY)){

        // ... Lógica de la transacción ...

        }catch(
SQLException e){
        // Manejar el error de base de datos
        log.

error("Error en la transacción",e);
// Lanzar una excepción de negocio
    throw new

TransaccionFallidaException("La operación no pudo completarse.",e);
}
```

-----

### **Módulo 5: Administración de Datos - El Framework de Colecciones 🧩**

Proporciona estructuras de datos de alto rendimiento para gestionar grupos de objetos.

⭐ **Regla de Oro: Programar Contra la Interfaz**

Siempre declara tus variables con el tipo de la interfaz (`List`, `Set`, `Map`), no la implementación (`ArrayList`,
`HashMap`). Esto te da la flexibilidad de cambiar la implementación subyacente sin romper tu código.

**`List<Cliente> clientes = new ArrayList<>(); // ✅ CORRECTO`**
**`ArrayList<Cliente> clientes = new ArrayList<>(); // ❌ INCORRECTO (rígido)`**

| Interfaz   | Implementación Común | Orden          | Duplicados    | Rendimiento (Promedio)                                | Caso de Uso Típico en FinTech                                                                             |
|:-----------|:---------------------|:---------------|:--------------|:------------------------------------------------------|:----------------------------------------------------------------------------------------------------------|
| **`List`** | `ArrayList`          | Sí (inserción) | Sí            | Acceso por índice: $O(1)$. Inserción/Borrado: $O(n)$. | Almacenar el historial de transacciones de una cuenta (el orden importa). **Es tu elección por defecto**. |
| **`Set`**  | `HashSet`            | No             | **No**        | Añadir/Buscar/Borrar: $O(1)$.                         | Almacenar un conjunto de identificadores únicos de clientes para evitar duplicados.                       |
| **`Map`**  | `HashMap`            | No             | Claves únicas | `get`/`put`: $O(1)$.                                  | Buscar un cliente por su DNI o una cuenta por su número de cuenta. Acceso ultra rápido por clave.         |

**Requisito Técnico Crucial para `Set` y `Map`:** Para que tus objetos (`Cliente`, `Cuenta`) funcionen correctamente
como elementos de un `HashSet` o claves de un `HashMap`, es **obligatorio sobrescribir los métodos `equals()`
y `hashCode()`**. El contrato es simple: si `a.equals(b)` es `true`, entonces `a.hashCode()` debe ser igual a
`b.hashCode()`.

-----

### **Módulo 6:Procesamiento Declarativo con Streams y Lambdas**

Java 8 no solo añadió características; introdujo una nueva filosofía. Abandonamos el control minucioso de los bucles (
`for`, `while`) para simplemente *declarar* nuestra intención. En lugar de decirle al programa los pasos exactos para
generar un reporte, simplemente le pedimos: "dame este reporte". El sistema ya sabe cómo hacerlo de la manera más
eficiente.

-----

### **Paso 1: La Base de Todo - Expresiones Lambda e Interfaces Funcionales**

Para entender los Streams, primero debemos dominar sus herramientas: las Lambdas.

Una **expresión Lambda** es, en esencia, una función sin nombre que puedes tratar como un dato. Puedes pasarla como
argumento a un método, retornarla desde otro método o asignarla a una variable. Su anatomía es simple:

`(parámetros) -> { cuerpo de la expresión }`

Por ejemplo, `(Cuenta c) -> c.getSaldo() > 50000` es una función que recibe un objeto `Cuenta` y devuelve `true` si su
saldo supera los 50,000.

Pero, cómo "almacena" Java estas funciones anónimas??? Aquí es donde entran las **Interfaces Funcionales**.

Una interfaz funcional es una interfaz que tiene **un solo método abstracto** (SAM - Single Abstract Method). Actúa como
el "tipo de dato" para una expresión lambda. Java viene con un arsenal de interfaces funcionales predefinidas en el
paquete `java.util.function`. Las 4 más importantes que debes dominar son:

1. **`Predicate<T>`**:

    * **Propósito**: Evaluar una condición sobre un objeto. Responde a una pregunta de "sí" o "no".
    * **Método abstracto**: `boolean test(T t)`
    * **Analogía**: Un portero de discoteca. Recibe una persona (`T`) y decide si puede entrar o no (`boolean`).
    * **Ejemplo**: `Predicate<Cuenta> esVip = c -> c.getSaldo() > 50000;`

2. **`Function<T, R>`**:

    * **Propósito**: Transformar un objeto de un tipo `T` a otro tipo `R`.
    * **Método abstracto**: `R apply(T t)`
    * **Analogía**: Un traductor. Recibe una palabra en un idioma (`T`) y la devuelve en otro (`R`).
    * **Ejemplo**: `Function<Cuenta, String> obtenerTitular = Cuenta::getTitular;` (Esto se llama **método de referencia
      **, una forma aún más concisa de una lambda que solo llama a un método existente).

3. **`Consumer<T>`**:

    * **Propósito**: Realizar una acción con un objeto sin devolver nada.
    * **Método abstracto**: `void accept(T t)`
    * **Analogía**: Una impresora. Recibe un documento (`T`) y lo imprime (`void`), no devuelve nada.
    * **Ejemplo**: `Consumer<String> imprimirNombre = nombre -> System.out.println(nombre);` o
      `Consumer<String> imprimirNombre = System.out::println;`

4. **`Supplier<T>`**:

    * **Propósito**: Proveer un objeto sin recibir ningún parámetro.
    * **Método abstracto**: `T get()`
    * **Analogía**: Una fábrica de objetos. No necesita nada para empezar a producir (`get()`) y te entrega un
      producto (`T`).
    * **Ejemplo**: `Supplier<ArrayList<String>> CreadorDeListas = ArrayList::new;`

-----

### **Paso 2: El Pipeline - La API de Streams en Acción**

Un Stream es una secuencia de elementos que soporta operaciones de agregación. Piénsalo como una cadena de montaje en
una fábrica: los objetos entran por un lado, pasan por varias estaciones (operaciones) y un resultado final sale por el
otro.

**Características:**

* **No almacenan datos**: Son un conducto, no un contenedor.

* **Inmutables**: No modifican la colección original. Siempre producen un nuevo resultado.

* **Evaluación Perezosa (Lazy Evaluation)**: Las operaciones intermedias no se ejecutan hasta que una operación terminal
  es invocada. Esto permite optimizaciones increíbles.

#### **La Anatomía de un Pipeline de Stream**

Un pipeline siempre sigue esta estructura:

1. **Fuente (Source)**: De dónde vienen los datos.

    * `todasLasCuentas.stream()`: Desde una colección.

    * `Stream.of("a", "b", "c")`: Desde elementos individuales.

    * `Arrays.stream(miArray)`: Desde un array.

2. **Operaciones Intermedias (Intermediate Operations)**: Transforman o filtran el stream. Son *lazy* y siempre
   devuelven un nuevo stream. Puedes encadenar tantas como necesites.

    * `filter(Predicate<T>)`: Deja pasar solo los elementos que cumplen la condición.

    * `map(Function<T, R>)`: Transforma cada elemento.

    * `sorted()`: Ordena los elementos (según su orden natural o un `Comparator`).

    * `distinct()`: Elimina elementos duplicados.

    * `limit(long n)`: Trunca el stream a un tamaño máximo `n`.

    * `skip(long n)`: Descarta los primeros `n` elementos.

3. **Operación Terminal (Terminal Operation)**: Inicia el procesamiento y produce un resultado final (o un efecto
   secundario). Cierra el stream.

    * `collect(Collectors.toList())`: Agrupa los resultados en una lista.

    * `forEach(Consumer<T>)`: Aplica una acción a cada elemento.

    * `count()`: Devuelve el número de elementos.

    * `findFirst()`, `findAny()`: Devuelven un `Optional` con el primer elemento que encuentran.

    * `reduce()`: Combina todos los elementos en un único resultado (ej: sumar todo).

    * `anyMatch()`, `allMatch()`, `noneMatch()`: Verifican si los elementos cumplen una condición.

-----

### **Mejores Prácticas: Concatenando Pipelines de Forma Eficiente**

El orden de las operaciones intermedias es **crucial** para el rendimiento. La regla de oro es: **Filtra primero,
transforma después.**

* **Ineficiente**:

  ```java
  // Transforma 1000 cuentas a sus titulares y LUEGO filtra los 5 VIPs ordenados.
  listaDeMilCuentas.stream()
          .map(Cuenta::getTitular) // Operación sobre 1000 elementos
          .sorted()                // Ordena 1000 strings
          .filter(titular -> esVip(titular)) // Filtra al final
          .collect(Collectors.toList());
  ```

* **Eficiente**:

  ```java
  // Filtra PRIMERO las 5 cuentas VIP y LUEGO solo transforma y ordena esos 5 elementos.
  listaDeMilCuentas.stream()
          .filter(c -> c.getSaldo() > 50000) // Filtra primero, quedan solo 5
          .map(Cuenta::getTitular)             // Operación sobre 5 elementos
          .sorted()                            // Ordena 5 strings
          .collect(Collectors.toList());
  ```

La segunda versión es órdenes de magnitud más rápida porque las operaciones más costosas (`map`, `sorted`) se aplican
sobre un conjunto de datos mucho más pequeño. **Piensa en tu pipeline como un embudo.**

-----

### **La Ventaja Estratégica: `.parallelStream()`**

Aquí es donde la magia declarativa brilla. En el mundo FinTech, procesar millones de transacciones para detectar fraudes
o calcular riesgos es una tarea diaria.

Simplemente cambiando `.stream()` por `.parallelStream()`, le pides a Java que divida el trabajo entre todos los núcleos
de tu CPU usando el framework Fork/Join.

```java
// Mismo código, pero ahora se ejecuta en paralelo
List<String> titularesVip = todasLasCuentas.parallelStream()
                .filter(c -> c.getSaldo() > 50000)
                .map(Cuenta::getTitular)
                .sorted() // Ojo: sorted en paralelo tiene su propio overhead
                .collect(Collectors.toList());
```

**Advertencia\!!!** No es una bala de plata. Úsalo cuando:

1. Tienes una **gran cantidad de datos**. El costo de coordinar los hilos puede hacer que sea más lento para listas
   pequeñas.

2. Las operaciones en cada elemento son **costosas computacionalmente** (CPU-bound).

3. Tus lambdas son **stateless** (no dependen ni modifican un estado externo). Modificar una variable compartida desde
   un stream paralelo es una receta para el desastre (race conditions).

-----

### **Ejercicios Prácticos para Afianzar Conocimiento**

Usemos una clase `Transaccion` para nuestros ejemplos:

```java
class Transaccion {
    private final String id;
    private final double monto;
    private final String ciudad;
    private final boolean esFraude;
    // Constructor, getters...
}
```

**Ejercicio 1: Detección de Fraude**

* **Tarea**: De una lista de transacciones, obtener los IDs de todas las transacciones fraudulentas ocurridas en "
  Caracas" y devolverlos en una lista.

```java
List<String> idsFraudeCaracas = transacciones.stream()
        .filter(t -> t.esFraude() && t.getCiudad().equals("Caracas")) // Filtra por dos condiciones
        .map(Transaccion::getId)                                    // Extrae el ID
        .collect(Collectors.toList());                              // Recolecta en una lista
```

**Ejercicio 2: Análisis de Riesgo**

* **Tarea**: Calcular el monto total de todas las transacciones no fraudulentas con un monto superior a 10,000.

```java
double montoTotalRiesgoBajo = transacciones.stream()
        .filter(t -> !t.esFraude() && t.getMonto() > 10000) // Filtra las transacciones seguras y de alto valor
        .mapToDouble(Transaccion::getMonto)               // Convierte a un stream de doubles para poder sumar
        .sum();                                           // Operación terminal de suma
```

**Ejercicio 3: Perfil de Cliente**

* **Tarea**: Encontrar la primera transacción de un cliente específico (por ID de transacción) y, si existe, imprimir
  sus detalles.

* **Pista**: Esto introducirá el concepto de `Optional<T>`, que es la forma moderna en Java de manejar valores que
  pueden ser nulos.

```java
transacciones.stream()
        .

filter(t ->t.

getId().

equals("TXN12345"))      // Filtra por el ID deseado
        .

findFirst()                                    // Operación terminal que devuelve un Optional<Transaccion>
        .

ifPresent(System.out::println);                // ifPresent es un método de Optional que ejecuta un Consumer si el valor existe
```

**Ejercicio 4: Agrupación y Reportes (Avanzado)**

* **Tarea**: Crear un mapa donde las llaves son las ciudades y los valores son una lista de todas las transacciones
  ocurridas en esa ciudad.

```java
import static java.util.stream.Collectors.groupingBy;

Map<String, List<Transaccion>> transaccionesPorCiudad = transacciones.stream()
        .collect(groupingBy(Transaccion::getCiudad)); // El colector groupingBy es extremadamente poderoso

// Ahora puedes, por ejemplo, imprimir el número de transacciones por ciudad
transaccionesPorCiudad.

forEach(
    (ciudad, lista) ->System.out.

println(ciudad +": "+lista.size() +" transacciones")
        );
```