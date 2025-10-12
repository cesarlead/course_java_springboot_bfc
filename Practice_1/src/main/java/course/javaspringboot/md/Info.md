# 🚀 Curso de Fundamentos de Java (Versión Enriquecida)

**GlobalDesk** | **César Fernández – Ingeniero de Software (Java & Spring Boot)**

-----

## 🎯 Objetivo del Curso

* Aprender de forma **directa, práctica y de alto impacto**.
* No solo comprender la sintaxis, sino **pensar como un ingeniero de software** que resuelve problemas reales.
* Enfoque total en **mejores prácticas, arquitectura limpia y código listo para producción**.

> **Mentalidad del Curso:** No aprendemos comandos, aprendemos conceptos. El código que escribimos hoy debe ser
> mantenible mañana.

-----

## 📖 Historia de Java

* Nace en los 90 con **James Gosling y su equipo en Sun Microsystems**.
* Principio clave: **Write Once, Run Anywhere (WORA)**, que revolucionó el desarrollo multiplataforma.
* La **Máquina Virtual de Java (JVM)** es el corazón de esta independencia: actúa como una capa de abstracción entre tu
  código y el hardware/sistema operativo.

```java
// Ejemplo: mismo código, corre en Linux, Windows o Mac
public class HolaJava {
    public static void main(String[] args) {
        System.out.println("¡Hola Mundo, desde la JVM!");
    }
}
```

> **Curiosidad:** El proyecto Java se llamaba originalmente "Oak" (roble) por un árbol que James Gosling veía desde su
> oficina. Se cambió a "Java" inspirado en el café. Hoy, Java es propiedad de **Oracle**, que continúa gestionando su
> evolución.

-----

## Por qué Java en 2025???

Java no solo sobrevive, sino que prospera. Su relevancia se debe a:

* **Ecosistema Robusto y Probado:** Millones de librerías, frameworks y herramientas que han sido probadas en batalla
  durante décadas.
* **Rendimiento de Alto Nivel:** La JVM moderna (con su compilador Just-In-Time - JIT) optimiza el código en tiempo de
  ejecución, alcanzando un rendimiento cercano al de lenguajes compilados nativos como C++.
* **Comunidad y Soporte Gigantes:** Si tienes un problema, es casi seguro que alguien ya lo resolvió. La documentación y
  los foros son inmensos.
* **Dominio en el Backend Empresarial:** Es el lenguaje preferido para sistemas críticos que exigen alta disponibilidad
  y escalabilidad (banca, microservicios, Big Data, e-commerce). **Netflix, Google, LinkedIn, Amazon y la mayoría de las
  instituciones financieras** lo usan intensivamente.
* **Evolución Constante y Segura:** Java introduce nuevas características en cada versión, pero mantiene una *
  *retrocompatibilidad** casi sagrada, protegiendo la inversión de las empresas.

```java
// Ejemplo: uso moderno (Streams desde Java 8)
// Un código más funcional, legible y expresivo.
List<String> nombres = List.of("Ana", "Luis", "Cesar");
nombres.

stream()
       .

filter(n ->n.

startsWith("P"))
        .

forEach(System.out::println); // Salida: Cesar
```

-----

## 📌 Java 17 (LTS): La Elección Profesional

No todas las versiones de Java son iguales. En el mundo corporativo, se prioriza la estabilidad.

* **LTS (Long-Term Support):** Versiones como Java 8, 11, 17 y 21 reciben soporte y actualizaciones de seguridad durante
  años. Las empresas las adoptan porque garantizan una plataforma estable para sus aplicaciones críticas.
* **Versiones No-LTS:** Se liberan cada 6 meses, introducen características nuevas y sirven como "prueba" para lo que
  vendrá en la próxima LTS. Son ideales para experimentar, pero no para producción a largo plazo.

**Por eso elegimos Java 17:** es una versión LTS moderna, estable y totalmente compatible con los principales frameworks
como Spring Boot 3.

```java
// Ejemplo de Switch Expressions (desde Java 14, estable en 17)
// Más seguro, conciso y menos propenso a errores que el switch tradicional.
String role = "ADMIN";
String permiso = switch (role) {
    case "ADMIN", "SUPER_ADMIN" -> "Acceso total"; // Múltiples casos
    case "USER" -> "Acceso limitado";
    default -> "Sin acceso";
};
System.out.

println(permiso);
```

-----

## ⚙️ Por qué Maven y no Gradle???

Ambas son excelentes herramientas de construcción (build tools), pero la elección depende del contexto. Para este curso,
enfocado en el estándar empresarial, **Maven es la opción estratégica**.

* **Maven = Estándar de la Industria:** Es el rey en el desarrollo Java empresarial. La mayoría de la documentación
  oficial de **Spring, Jakarta EE, y librerías corporativas** se muestra primero (y a veces, únicamente) con Maven.
* **Convención sobre Configuración:** Maven impone una estructura de proyecto estándar. Esto es una ventaja enorme en
  equipos grandes, ya que un desarrollador puede entender cualquier proyecto Maven casi al instante. Menos sorpresas,
  más productividad.
* **Declarativo y Simple (XML):** Aunque verboso, el `pom.xml` es explícito y fácil de entender. No hay "magia" oculta
  en scripts de programación, lo que reduce la posibilidad de errores complejos en el build. Para un novato, es más
  fácil leer XML que un script de Groovy o Kotlin (Gradle).
* **Ecosistema Maduro y Estable:** Maven Central es el repositorio de artefactos más grande y antiguo. La integración
  con IDEs y herramientas de CI/CD (como Jenkins) es impecable.

### 🛠️ Ejemplo: Dependencia en Maven (pom.xml)

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### 🛠️ Ejemplo: La misma en Gradle (build.gradle)

```groovy
// Más conciso, pero requiere entender la sintaxis de Groovy/Kotlin
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

-----

## ⚡ Por qué Spring Boot???

Spring es el framework más dominante en Java, y Spring Boot es su evolución.

* **Antes (Spring Tradicional):** Configurar un proyecto requería tediosos archivos XML para definir "beans" (objetos
  gestionados por Spring) y sus interconexiones. Era potente, pero lento de iniciar.
* **Ahora (Spring Boot):** Sigue los principios de **"opinioned defaults"** y **"autoconfiguración"**. Spring Boot
  observa las librerías que añades (tu "classpath") y configura automáticamente la aplicación.
    * ¿Añades `spring-boot-starter-web`? Configura un servidor web Tomcat y te permite crear controladores REST.
    * ¿Añades `spring-boot-starter-data-jpa`? Configura una conexión a base de datos con Hibernate.

> **La Magia de Spring Boot:** Se basa en dos conceptos clave de Spring Framework:
>
> 1. **Inversión de Control (IoC):** Tú no creas los objetos, se los pides al "contenedor" de Spring. Él se encarga de
     su ciclo de vida.
> 2. **Inyección de Dependencias (DI):** Si un componente A necesita un componente B, no lo crea. Simplemente declara su
     dependencia y Spring se la "inyecta". Esto desacopla el código y lo hace infinitamente más fácil de probar y
     mantener.

```java
// Ejemplo: API REST funcional en menos de 20 líneas
@SpringBootApplication
@RestController // Anotación que combina @Controller y @ResponseBody
public class DemoApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }

    @GetMapping("/hola") // Mapea peticiones GET a /hola
    public String hola() {
        return "Hola desde Spring Boot 🚀";
    }
}
```

-----

## 🧱 Fundamentos de Java

### 🔹 Variables y Tipos: La Base de Todo

* **Primitivos (`int`, `double`, `boolean`):** Viven en la *stack memory*. Son extremadamente rápidos y eficientes en
  uso de memoria. No pueden ser `null`.
* **Wrappers (`Integer`, `Double`, `Boolean`):** Son objetos que "envuelven" a un primitivo. Viven en la *heap memory*.
  Son más lentos y pesados, pero necesarios para las colecciones (`List<Integer>`, no `List<int>`) y pueden ser `null`.

```java
int numeroPrimitivo = 10;
Integer numeroObjeto = 10; // Autoboxing: Java convierte el primitivo en objeto automáticamente.

// CUIDADO!!! El autoboxing en bucles grandes puede matar el rendimiento.
// El siguiente código crea 1,000,000 de objetos Integer innecesarios.
for(
int i = 0;
i< 1_000_000;i++){
Integer x = i; // Mala práctica dentro de un bucle de alto rendimiento.
}

// La forma correcta
        for(
int i = 0;
i< 1_000_000;i++){
int x = i; // Usa siempre primitivos si es posible.
}
```

* **El caso especial de `String`:** No es un primitivo, es un objeto inmutable. Esto significa que una vez creado, su
  valor no puede cambiar. Cualquier "modificación" en realidad crea un nuevo objeto String.

> **TIP:** Para comparar Strings, usa siempre `.equals()`, nunca `==`. El operador `==` compara referencias de memoria (
> si son el mismo objeto), mientras que `.equals()` compara el contenido.

### 🔹 Operadores

* **Short-circuit (`&&`, `||`):** Fundamentales para escribir código seguro y eficiente. Si en `A && B` la condición `A`
  es falsa, `B` **nunca se evalúa**.

```java
String s = null;
// Esto evita un NullPointerException gracias al cortocircuito.
// Si s fuera null, la segunda parte (s.length()) nunca se ejecutaría.
if(s !=null&&s.

length() < 10){
        System.out.

println("Cadena válida");
}
```

* **Operadores a nivel de bits (`&`, `|`, `^`, `<<`, `>>`):** Son para operaciones de muy bajo nivel. No los usarás a
  diario, pero son cruciales en ciertas áreas como criptografía, compresión o drivers. El `shift` es una forma ultra
  rápida de multiplicar o dividir por 2.

```java
int x = 4;      // Binario: 0100
int y = x << 1; // Desplaza los bits una posición a la izquierda: 1000
System.out.

println(y); // Salida: 8 (multiplicó por 2)
```

### 🔹 Estructuras de Control

* **Switch con Pattern Matching (Java 17+):** Una de las mejoras más potentes del lenguaje. Permite no solo comparar
  valores, sino también tipos y estructuras de objetos.

```java
// Código más seguro y expresivo
Object obj = "Hola Mundo";

switch(obj){
        // Si obj es un String, lo asigna a 's' y ejecuta el código
        case
String s ->System.out.

println("Es una cadena: "+s.toUpperCase());
        case
Integer i ->System.out.

println("Es un número: "+i);
    case null->System.out.

println("Es nulo!");

default ->System.out.

println("Es de otro tipo");
}
```

* **Guard Clauses (Cláusulas de Guarda):** Una práctica de código limpio fundamental. En lugar de anidar `if-else`,
  valida las condiciones no deseadas al principio del método y sal temprano. Esto reduce la complejidad ciclomática y
  hace el código mucho más legible.

```java
// MAL: Anidamiento excesivo (Arrow Code)
public void procesar(String dato) {
    if (dato != null) {
        if (!dato.isEmpty()) {
            // Lógica principal aquí...
            System.out.println("Procesando: " + dato);
        }
    }
}

// BIEN: Usando Guard Clauses
public void procesarMejor(String dato) {
    if (dato == null || dato.isEmpty()) {
        return; // Salida temprana
    }
    // La lógica principal queda limpia y sin anidamiento.
    System.out.println("Procesando: " + dato);
}
```

### 🔹 Iteración y Manejo de Recursos

```java
List<Integer> numeros = List.of(1, 2, 3, 4, 5);

// Enfoque Imperativo (CÓMO hacerlo)
System.out.

println("Imperativo:");
for(
int n :numeros){
        if(n %2==0){
        System.out.

println(n *2);
    }
            }

```

* **`try-with-resources`:** La única forma correcta de manejar recursos externos (ficheros, conexiones a base de datos,
  sockets). Garantiza que el recurso se cierre (`.close()`) automáticamente, incluso si ocurre una excepción.

```java
// Cualquier clase que implemente la interfaz AutoCloseable puede usarse aquí.
try(BufferedReader reader = new BufferedReader(new FileReader("archivo.txt"))){
String linea;
    while((linea =reader.

readLine())!=null){
        System.out.

println(linea);
    }
            }catch(
IOException e){
        // Manejo de la excepción
        e.

printStackTrace();
}
// 'reader' ya está cerrado aqui.
```

-----

## ✅ Reglas para Escribir Código Java de Calidad

1. **Prioriza Primitivos:** En bucles, arrays grandes y cálculos intensivos, usa `int`, `double`, etc., sobre `Integer`,
   `Double` para un rendimiento óptimo.
2. **Abraza la Inmutabilidad:** Usa `final` para variables locales, parámetros de métodos y campos de clase siempre que
   sea posible. Esto hace tu código más seguro, especialmente en entornos concurrentes.
3. **Usa las Herramientas Modernas del Lenguaje:** Adopta `switch expressions`, `pattern matching` y `records` (a partir
   de Java 16) para un código más conciso y seguro.
4. **Aplica Guard Clauses:** Mantén tus métodos limpios y legibles validando y saliendo temprano. Evita el "código
   flecha".
5. **Siempre `try-with-resources`:** Para cualquier recurso que implemente `AutoCloseable`, es la única opción segura
   para evitar fugas de recursos (resource leaks).
6. **Usa la Colección Adecuada:** No uses siempre `ArrayList`. Entiende cuándo usar `List` (elementos ordenados, permite
   duplicados), `Set` (elementos únicos) o `Map` (pares clave-valor).
7. **Compara Strings con `.equals()`:** Un error de principiante clásico es usar `==`. Grábatelo a fuego.