package course.javaspringboot.M04_Excepciones;

import java.util.HashMap;
import java.util.Map;

public class AppBancaria {

    public static void main(String[] args) {
        Map<String, CuentaBancaria> banco = new HashMap<>();
        banco.put("C001", new CuentaBancaria("C001", 1000));
        banco.put("C002", new CuentaBancaria("C002", 500));

        ServicioTransferencias servicio = new ServicioTransferencias(banco);

        // Intento 1: Saldo insuficiente
        try {
            System.out.println("Intentando transferir 2000 de C001 a C002...");
            servicio.realizarTransferencia("C001", "C002", 2000);
        } catch (SaldoInsuficienteException e) {
            System.err.println("Error de Negocio: " + e.getMessage());
        } catch (CuentaNoEncontradaException | TransferenciaInvalidaException e) {
            System.err.println("Error Operacional: " + e.getMessage());
        }

        // Intento 2: Cuenta no encontrada
        try {
            System.out.println("\nIntentando transferir 100 de C001 a C003...");
            servicio.realizarTransferencia("C001", "C003", 100);
        } catch (Exception e) { // Captura genérica para mostrar el tipo
            System.err.println("Error Capturado (" + e.getClass().getSimpleName() + "): " + e.getMessage());
        }
    }
}
