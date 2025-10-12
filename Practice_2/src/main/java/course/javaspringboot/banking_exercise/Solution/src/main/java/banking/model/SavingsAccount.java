package banking.model;

public class SavingsAccount extends Account {
    private final double interestRate;

    public SavingsAccount(Customer customer, double interestRate) {
        super(customer);
        this.interestRate = interestRate;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El depósito debe ser positivo");
        balance += amount;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) throw new IllegalArgumentException("El retiro debe ser positivo");
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Fondos insuficientes en cuenta de ahorros");
        }
    }
}
