package cesarlead;

import cesarlead.exception.InsufficientStockException;
import cesarlead.model.Customer;
import cesarlead.model.Order;
import cesarlead.model.Product;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // 1. Setup: Crear clientes y productos
        Customer customer = new Customer(1L, "César Fernández", "cesar.fernandez@example.com");
        Product laptop = new Product(101L, "Laptop Pro", new BigDecimal("1200.50"), 5);
        Product mouse = new Product(102L, "Mouse Gamer", new BigDecimal("75.00"), 10);
        Product keyboard = new Product(103L, "Teclado Mecánico", new BigDecimal("150.25"), 0); // Sin stock

        System.out.println("--- Creando nueva orden para " + customer.getName() + " ---");
        Order order = new Order(1L, customer);

        // 2. Demostración: Añadir productos con stock
        System.out.println("\n--- Intentando añadir productos ---");
        try {
            order.addProduct(laptop);
            order.addProduct(mouse);
        } catch (InsufficientStockException e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }

        // 3. Demostración: Probar la excepción de stock
        System.out.println("\n--- Intentando añadir producto sin stock ---");
        try {
            order.addProduct(keyboard);
        } catch (InsufficientStockException e) {
            System.err.println("Error capturado correctamente: " + e.getMessage());
        }

        // 4. Cálculo del total
        order.calculateTotal();
        System.out.println("\n--- Resumen de la Orden ---");
        System.out.println("ID de Orden: " + order.getId());
        System.out.println("Cliente: " + order.getCustomer().getName());
        System.out.println("Número de productos: " + order.getItems().size());
        System.out.println("Total de la orden: $" + order.getTotal());
    }
}
