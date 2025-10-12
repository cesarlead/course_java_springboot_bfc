package course.javaspringboot;

public class Operators {


    public static void main(String[] args) {

        // =========================
        // 1. Operadores aritméticos
        // =========================
        int a = 10;
        int b = 3;

        System.out.println("Suma: " + (a + b));        // 13
        System.out.println("Resta: " + (a - b));       // 7
        System.out.println("Multiplicación: " + (a * b)); // 30
        System.out.println("División entera: " + (a / b)); // 3 → trunca decimales
        System.out.println("Módulo: " + (a % b));      // 1 → resto de la división

        // Trampa: división entre enteros pierde decimales
        double divisionReal = (double) a / b; // Casting explícito para resultado correcto
        System.out.println("División real: " + divisionReal); // 3.333...

        // =========================
        // 2. Operadores de asignación
        // =========================
        int x = 5;
        x += 3;  // equivale a x = x + 3
        x *= 2;  // equivale a x = x * 2
        System.out.println("Valor final de x: " + x); // 16

        // =========================
        // 3. Operadores relacionales (booleanos)
        // =========================
        System.out.println("¿a es mayor que b?: " + (a > b));  // true
        System.out.println("¿a es igual a b?: " + (a == b));   // false
        System.out.println("¿a es distinto de b?: " + (a != b)); // true

        // Comparación de objetos → usar equals, no "=="
        String s1 = new String("Java");
        String s2 = new String("Java");
        System.out.println("Comparación con == : " + (s1 == s2));        // false → compara referencias
        System.out.println("Comparación con equals: " + s1.equals(s2)); // true → compara contenido

        // =========================
        // 4. Operadores lógicos
        // =========================
        boolean esAdulto = true;
        boolean tienePermiso = false;

        System.out.println("AND lógico (&&): " + (esAdulto && tienePermiso)); // false
        System.out.println("OR lógico (||): " + (esAdulto || tienePermiso));  // true
        System.out.println("NOT lógico (!): " + (!esAdulto));                 // false

        // Cortocircuito (short-circuit evaluation)
        // La segunda condición NO se evalúa si la primera ya decide el resultado
        int y = 0;
        boolean test = (y != 0) && (10 / y > 1); // No lanza error porque (y != 0) es false
        System.out.println("Cortocircuito evita división por cero: " + test);

        // =========================
        // 5. Operadores unarios e incremento/decremento
        // =========================
        int n = 5;
        System.out.println("Post-incremento n++: " + (n++)); // imprime 5, luego n = 6
        System.out.println("Pre-incremento ++n: " + (++n));  // n = 7, imprime 7

        // =========================
        // 6. Operadores ternarios
        // =========================
        int edad = 20;
        String categoria = (edad >= 18) ? "Adulto" : "Menor";
        System.out.println("Categoría según edad: " + categoria);

        // =========================
        // 7. Operadores a nivel de bits (bitwise)
        // =========================
        int m = 6;  // 110 en binario
        int n2 = 4; // 100 en binario

        System.out.println("AND bitwise (6 & 4): " + (m & n2));  // 100 → 4
        System.out.println("OR bitwise (6 | 4): " + (m | n2));   // 110 → 6
        System.out.println("XOR bitwise (6 ^ 4): " + (m ^ n2));  // 010 → 2
        System.out.println("Desplazamiento izquierda (4 << 1): " + (n2 << 1)); // 1000 → 8
        System.out.println("Desplazamiento derecha (4 >> 1): " + (n2 >> 1));  // 10 → 2
    }
}
