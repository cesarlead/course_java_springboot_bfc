package org.cesarlead.marketcesarlead.model;

import jakarta.persistence.*;
import org.cesarlead.marketcesarlead.exception.InsufficientStockException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY es mejor para el rendimiento
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> items = new ArrayList<>();

    public Order() {
    }

    public Order(Long id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
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
        calculateTotal();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
