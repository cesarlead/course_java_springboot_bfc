package course.javaspringboot;

public class ControlStructures {

    public static void main(String[] args) {

        // ===============================
        // 1. Estructuras condicionales
        // ===============================

        int edad = 20;

        // IF - ELSE IF - ELSE
        if (edad < 13) {
            System.out.println("Categoría: Niño");
        } else if (edad < 18) {
            System.out.println("Categoría: Adolescente");
        } else {
            System.out.println("Categoría: Adulto");
        }
        // Nota: siempre ordenar de menor a mayor o de mayor a menor
        // para evitar "condiciones atrapadas".

        // SWITCH mejorado (Java 14+ con expresiones)
        int dia = 3;
        String nombreDia = switch (dia) {
            case 1 -> "Lunes";
            case 2 -> "Martes";
            case 3 -> "Miércoles";
            case 4 -> "Jueves";
            case 5 -> "Viernes";
            case 6, 7 -> "Fin de semana"; // varios casos juntos
            default -> "Día inválido";
        };
        System.out.println("Hoy es: " + nombreDia);

        // ===============================
        // 2. Bucles: FOR tradicional
        // ===============================
        System.out.println("Números del 1 al 5:");
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " "); // imprime en la misma línea
        }
        System.out.println(); // salto de línea

        // ===============================
        // 3. Bucles: FOR-EACH (colecciones o arrays)
        // ===============================
        String[] cursos = {"Java", "Spring Boot", "Docker"};
        for (String curso : cursos) {
            System.out.println("Curso: " + curso);
        }
        // Nota PRO: el for-each evita errores de índices y es más legible.

        // ===============================
        // 4. Bucles: WHILE
        // ===============================
        int intentos = 0;
        boolean acceso = false;
        String passwordIngresado = "1234";

        while (!acceso && intentos < 3) {
            // Simulación: en la vida real se leería con Scanner o UI
            if ("1234".equals(passwordIngresado)) {
                acceso = true;
                System.out.println("Acceso concedido ✅");
            } else {
                intentos++;
                System.out.println("Contraseña incorrecta. Intento #" + intentos);
            }
        }

        if (!acceso) {
            System.out.println("Acceso bloqueado 🚫");
        }

        // ===============================
        // 5. Bucles: DO-WHILE
        // ===============================
        int opcion;
        do {
            opcion = 2; // Simulación: en la práctica se pediría al usuario
            System.out.println("Menú: 1) Jugar  2) Salir");
        } while (opcion != 2);
        System.out.println("Programa finalizado.");

        // ===============================
        // 6. Control de flujo dentro de bucles
        // ===============================
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue; // salta el resto de la iteración si es par
            }
            if (i == 7) {
                break; // rompe el bucle completamente
            }
            System.out.println("Número impar: " + i);
        }
    }
}
