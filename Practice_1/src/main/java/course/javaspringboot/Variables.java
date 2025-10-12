package course.javaspringboot;

public class Variables {

    public static void main(String[] args) {

        // ====== Primitivos ======
        int edad = 30;                // Primitivo → rápido, ocupa menos memoria
        double salario = 4500.75;     // Números con decimales (64 bits)
        char inicial = 'C';           // Un solo carácter
        boolean activo = true;        // Solo true/false

        // ====== Wrappers (Objetos) ======
        Integer puntos = 120;         // Wrapper de int (vive en el Heap)
        Double tasaInteres = 3.75;    // Wrapper de double
        Boolean esAdmin = Boolean.FALSE; // Mejor práctica: usar constantes predefinidas

        // ====== Constantes ======
        final double PI = 3.14159;    // Convención: mayúsculas con guiones bajos
        final int MAX_INTENTOS = 5;

        // ====== Autoboxing / Unboxing ======
        Integer valorObj = edad;      // Autoboxing: de int → Integer
        int valorPrim = valorObj;     // Unboxing: de Integer → int

        // ====== Ejemplo aplicado ======
        double bono = salario * 0.10;
        double total = salario + bono;

        System.out.println("Empleado activo: " + activo);
        System.out.println("Edad: " + edad);
        System.out.println("Puntos: " + puntos);
        System.out.println("Salario total: $" + total);
    }

}
