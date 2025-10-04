package banking.model;

public abstract class Account {
    protected double balance;
    protected Customer customer;

    public Account(Customer customer) {
        this.customer = customer;
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount) throws InsufficientFundsException;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " | Cliente: " + customer +
                " | Balance: " + balance;
    }
}
