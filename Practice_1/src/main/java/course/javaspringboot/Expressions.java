package course.javaspringboot;

public class Expressions {

    public static void main(String[] args) {

        // ===============================
        // 1. Expresiones aritméticas
        // ===============================
        int a = 10;
        int b = 3;
        int resultado = (a + b) * 2; // (10 + 3) * 2 = 26
        System.out.println("Expresión aritmética: (a + b) * 2 = " + resultado);

        // ===============================
        // 2. Expresiones booleanas (relacionales y lógicas)
        // ===============================
        boolean esMayor = a > b;           // true → se evalúa a un valor boolean
        boolean condicion = (a > 5) && (b < 5); // true && true → true
        System.out.println("Expresión booleana esMayor: " + esMayor);
        System.out.println("Expresión booleana compuesta: " + condicion);

        // ===============================
        // 3. Expresiones condicionales (ternarias)
        // ===============================
        int edad = 20;
        String categoria = (edad >= 18) ? "Adulto" : "Menor";
        // El operador ternario es una expresión porque produce un valor
        System.out.println("Categoría según edad (expresión ternaria): " + categoria);

        // ===============================
        // 4. Expresiones con asignación
        // ===============================
        int x;
        System.out.println("Expresión de asignación: x = " + (x = 42));
        // El valor de la expresión es el mismo que se asigna → 42

        // ===============================
        // 5. Expresiones dentro de control de flujo
        // ===============================
        if ((a + b) > 12) {
            // (a + b) > 12 es una expresión booleana que se evalúa a true o false
            System.out.println("La suma de a + b es mayor a 12");
        }

        // While también usa una expresión booleana
        int contador = 0;
        while (contador < 3) { // expresión booleana "contador < 3"
            System.out.println("Iteración #" + contador);
            contador++;
        }

        // ===============================
        // 6. Expresiones con objetos y métodos
        // ===============================
        String mensaje = "Java";
        // "mensaje.toUpperCase()" es una expresión porque devuelve un valor
        System.out.println("Expresión con objeto: " + mensaje.toUpperCase());

        // Incluso la concatenación de Strings es una expresión
        String saludo = "Hola, " + mensaje; // "Hola, Java"
        System.out.println("Expresión de concatenación: " + saludo);

        // ===============================
        // 7. Expresiones anidadas (complejas)
        // ===============================
        int resultadoComplejo = ((a + b) * (edad / 2)) - (x % b);
        // Paso a paso:
        // (a + b) → 13
        // (edad / 2) → 10
        // 13 * 10 → 130
        // (x % b) → 42 % 3 = 0
        // Resultado final → 130 - 0 = 130
        System.out.println("Expresión compleja: " + resultadoComplejo);
    }
}
