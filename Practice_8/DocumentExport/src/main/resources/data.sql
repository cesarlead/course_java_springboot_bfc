-- Tabla de Cuentas (relacionada con un customerId)
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type VARCHAR(50),
    balance DECIMAL(19, 2) NOT NULL
);

-- Tabla de Transacciones (relacionada con una cuenta)
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Datos para Cliente 1 (Cesar Fernandez)
INSERT INTO accounts (customer_id, account_number, account_type, balance)
VALUES (1, 'CHK-1001', 'Checking', 1500.75);
INSERT INTO accounts (customer_id, account_number, account_type, balance)
VALUES (1, 'SAV-2001', 'Savings', 8200.00);

-- Transacciones para Cliente 1
INSERT INTO transactions (account_id, amount, transaction_date, description)
VALUES (1, -50.25, CURRENT_TIMESTAMP(), 'Grocery Store');
INSERT INTO transactions (account_id, amount, transaction_date, description)
VALUES (1, -120.00, CURRENT_TIMESTAMP() - 1 DAY, 'Electric Bill');
INSERT INTO transactions (account_id, amount, transaction_date, description)
VALUES (2, 1000.00, CURRENT_TIMESTAMP() - 2 DAY, 'Initial Deposit');

-- Datos para Cliente 2 (Jose Zamora)
INSERT INTO accounts (customer_id, account_number, account_type, balance)
VALUES (2, 'CHK-1002', 'Checking', 500.00);