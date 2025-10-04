package banking.model;

public class CheckingAccount extends Account {
    private final double overdraftLimit;

    public CheckingAccount(Customer customer, double overdraftLimit) {
        super(customer);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El depósito debe ser positivo");
        balance += amount;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) throw new IllegalArgumentException("El retiro debe ser positivo");
        if (balance + overdraftLimit >= amount) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Límite de sobregiro excedido");
        }
    }
}
