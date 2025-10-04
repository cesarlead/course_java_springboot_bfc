package banking;

import banking.model.Account;
import banking.model.Customer;
import banking.model.InsufficientFundsException;
import banking.service.Bank;

public class App {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // Crear clientes
        Customer alice = bank.addCustomer("Alice", "123");
        Customer bob = bank.addCustomer("Bob", "456");

        // Abrir cuentas
        Account aliceSavings = bank.openSavingsAccount(alice, 0.02);
        Account bobChecking = bank.openCheckingAccount(bob, 500.0);

        try {
            // Operaciones
            aliceSavings.deposit(1000);
            bobChecking.deposit(300);

            bank.transfer(aliceSavings, bobChecking, 200);

            bobChecking.withdraw(600); // Bob usa sobregiro

        } catch (InsufficientFundsException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Mostrar cuentas
        bank.getAccounts().forEach(System.out::println);

        // Filtrar con Streams
        System.out.println("\nCuentas con balance >= 500:");
        bank.filterAccountsByBalance(500).forEach(System.out::println);
    }
}
