package course.javaspringboot;

public class Exercises {

//    Un banco clasifica a sus clientes según el saldo de su cuenta:
//    Menos de $1,000 → Cliente Básico
//    Entre $1,000 y $10,000 → Cliente Premium
//    Más de $10,000 → Cliente VIP
//    Crea un programa que reciba el saldo de un cliente y muestre su categoría.

    public static void bankClientPatternMatching() {
        double saldo = 5000.00;

        String mensaje = switch (saldo) {

            case double s when s < 1000 -> "Cliente Básico";
            case double s when s < 10000 -> "Cliente Premium";
            case double s -> "Cliente VIP";
        };

        System.out.println(mensaje);
    }

    // Simula un cajero automático con el siguiente menú:
    //Consultar saldo
    //Retirar dinero
    //Depositar dinero
    //Salir
    //El programa debe repetirse hasta que el usuario elija la opción Salir.

    public static void atm() {

        double saldo = 5000.0;
        int opcion;

        do {
            opcion = 2; // En un caso real se leería con Scanner

            switch (opcion) {
                case 1 -> System.out.println("Saldo disponible: $" + saldo);
                case 2 -> {
                    double retiro = 1000.0;
                    if (retiro <= saldo) {
                        saldo -= retiro;
                        System.out.println("Retiro exitoso. Nuevo saldo: $" + saldo);
                    } else {
                        System.out.println("Fondos insuficientes.");
                    }
                }
                case 3 -> {
                    double deposito = 2000.0;
                    saldo += deposito;
                    System.out.println("Depósito exitoso. Nuevo saldo: $" + saldo);
                }
                case 4 -> System.out.println("Gracias por usar el cajero.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 4);

    }


    // Un cliente tiene 3 intentos para ingresar su contraseña en el sistema de banca en línea.
    //Si acierta, se le da acceso.
    //Si falla 3 veces, la cuenta queda bloqueada.

    public static void bankOnline() {
        String passwordCorrecta = "clave123";
        String intento;
        boolean acceso = false;
        int intentos = 0;

        while (intentos < 3 && !acceso) {
            intento = "clave123"; // Simulación. En la práctica se leería con Scanner.

            if (passwordCorrecta.equals(intento)) {
                acceso = true;
                System.out.println("✅ Acceso concedido");
            } else {
                intentos++;
                System.out.println("❌ Contraseña incorrecta. Intento #" + intentos);
            }
        }

        if (!acceso) {
            System.out.println("🚫 Cuenta bloqueada. Contacte a su banco.");
        }
    }

    public static void main(String[] args) {

        bankClientPatternMatching();
        // atm();
        // bankOnline();

    }
}
