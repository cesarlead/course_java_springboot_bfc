package cesarlead.model;

import cesarlead.exception.InsufficientStockException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private Customer customer;
    private List<Product> items;
    private BigDecimal total;

    public Order(Long id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.total = BigDecimal.ZERO;
    }

    /**
     * Adds a product to the order if stock is available.
     * Decrements the product's stock upon successful addition.
     *
     * @param product The product to add.
     * @throws InsufficientStockException if the product has no stock.
     */
    public void addProduct(Product product) {
        if (product.getStock() <= 0) {
            throw new InsufficientStockException("No hay stock disponible para el producto: " + product.getName());
        }
        this.items.add(product);
        product.setStock(product.getStock() - 1);
        System.out.println("Producto '" + product.getName() + "' añadido. Stock restante: " + product.getStock());
    }

    /**
     * Calculates the total price of all items in the order and updates the total attribute.
     */
    public void calculateTotal() {
        this.total = items.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Product> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
