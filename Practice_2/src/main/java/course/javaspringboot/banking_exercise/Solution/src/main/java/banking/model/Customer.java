package banking.model;

public class Customer {
    private final String name;
    private final String document;

    public Customer(String name, String document) {
        this.name = name;
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public String getDocument() {
        return document;
    }

    @Override
    public String toString() {
        return name + " (" + document + ")";
    }
}

