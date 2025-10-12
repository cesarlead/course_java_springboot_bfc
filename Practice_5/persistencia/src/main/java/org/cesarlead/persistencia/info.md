```java
// Insertar
public void createCustomer(Customer customer) {
    String sql = "INSERT INTO customers (name, email) VALUES (?,?)";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, customer.getName());
        pstmt.setString(2, customer.getEmail());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        // Manejo de excepciones
    }
}
```

```java
// Lectura
public Optional<Customer> findCustomerById(Long id) {
    String sql = "SELECT id, name, email FROM customers WHERE id =?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setLong(1, id);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                return Optional.of(customer);
            }
        }
    } catch (SQLException e) {
        // Manejo de excepciones
    }
    return Optional.empty();
}
```

Desafíos Inherentes del Enfoque JDBC:

* Código Repetitivo (Boilerplate): La estructura de try-with-resources, la creación de PreparedStatement, el
  establecimiento de parámetros y el mapeo de ResultSet se repiten para cada operación de base de datos,
  inflando la base de código.

* Falta de Seguridad de Tipos: Las consultas SQL son meras cadenas de texto para el compilador de Java. Un error de
  sintaxis en el SQL o un desajuste en el nombre de una columna solo se descubrirá en tiempo de ejecución, no en tiempo
  de compilación.

* Mantenibilidad Difícil: A medida que la complejidad de la aplicación y el número de entidades crecen, la capa de
  acceso
  a datos se vuelve extremadamente difícil de mantener. Las consultas complejas con múltiples JOINs son engorrosas de
  construir y mapear manualmente.

* No Portabilidad de SQL: Aunque JDBC abstrae la conexión, el SQL nativo no siempre es portable entre diferentes bases
  de datos. Características específicas o dialectos SQL pueden requerir reescrituras si se cambia el proveedor de la
  base
  de datos.

# Forma Moderna ORM:

Insertar

```java
    public void createCustomer(Customer customer) {
    entityManager.persist(customer); // Y ya está.
}
```

Leer

```java
public Optional<Customer> findCustomerById(Long id) {
    // Devuelve el objeto directamente, o null si no existe.
    Customer customer = entityManager.find(Customer.class, id);
    return Optional.ofNullable(customer);
}
```
