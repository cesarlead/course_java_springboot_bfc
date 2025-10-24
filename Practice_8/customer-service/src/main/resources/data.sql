-- Creamos la tabla de clientes
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Insertamos datos de ejemplo
INSERT INTO customers (first_name, last_name, email)
VALUES ('Cesar', 'Fernandez', 'cesar.fernandez@email.com');

INSERT INTO customers (first_name, last_name, email)
VALUES ('Jose', 'Zamora', 'jose.zamora@email.com');