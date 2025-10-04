package banking.service;

import banking.model.*;

import java.util.*;

public class Bank {
    private final Map<String, Customer> customers = new HashMap<>();
    private final List<Account> accounts = new ArrayList<>();

    public Customer addCustomer(String name, String document) {
        Customer c = new Customer(name, document);
        customers.put(document, c);
        return c;
    }

    public Account openSavingsAccount(Customer customer, double interestRate) {
        Account acc = new SavingsAccount(customer, interestRate);
        accounts.add(acc);
        return acc;
    }

    public Account openCheckingAccount(Customer customer, double overdraftLimit) {
        Account acc = new CheckingAccount(customer, overdraftLimit);
        accounts.add(acc);
        return acc;
    }

    public void transfer(Account from, Account to, double amount) throws InsufficientFundsException {
        from.withdraw(amount);
        to.deposit(amount);
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public Optional<Customer> findCustomerByDocument(String document) {
        return Optional.ofNullable(customers.get(document));
    }

    public List<Account> filterAccountsByBalance(double minBalance) {
        return accounts.stream()
                .filter(a -> a.getBalance() >= minBalance)
                .toList();
    }
}
