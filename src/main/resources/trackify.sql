-- Wallets Table
CREATE TABLE
    IF NOT EXISTS wallets (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL UNIQUE,
        balance DECIMAL(10, 2) NOT NULL CHECK (balance >= 0)
    );

-- Transactions Table
CREATE TABLE
    IF NOT EXISTS transactions (
        id INT AUTO_INCREMENT PRIMARY KEY,
        wallet_id INT NOT NULL,
        amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
        transaction_date DATE NOT NULL DEFAULT (CURRENT_DATE),
        item TEXT NOT NULL,
        category TEXT NOT NULL,
        description TEXT,
        type ENUM ('DEPOSIT', 'WITHDRAWAL') NOT NULL DEFAULT 'WITHDRAWAL',
        payment_method ENUM ('CASH', 'UPI') NOT NULL DEFAULT 'UPI',
        FOREIGN KEY (wallet_id) REFERENCES wallets (id) ON DELETE CASCADE
    );

-- Items Table
CREATE TABLE
    IF NOT EXISTS items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL UNIQUE,
        price DECIMAL(10, 2) NOT NULL CHECK (price > 0)
    );

-- -- Insert Default Wallets
-- INSERT INTO
--     wallets (name, balance)
-- VALUES
--     ('Personal Wallet', 1000.00),
--     ('Business Wallet', 5000.00) ON DUPLICATE KEY
-- UPDATE balance = balance;

-- -- Insert Default Items
-- INSERT INTO
--     items (name, price)
-- VALUES
--     ('Laptop', 1500.00),
--     ('Phone', 800.00),
--     ('Headphones', 200.00) ON DUPLICATE KEY
-- UPDATE price = VALUES(price);

-- -- Insert Default Transactions
-- INSERT INTO
--     transactions (
--         wallet_id,
--         amount,
--         transaction_date,
--         item,
--         category,
--         description,
--         type,
--         payment_method
--     )
-- VALUES
--     (
--         1,
--         1500.00,
--         '2023-01-01',
--         'Laptop',
--         'Electronics',
--         'Bought a new laptop',
--         'WITHDRAWAL',
--         'CASH'
--     ),
--     (
--         1,
--         800.00,
--         '2023-02-01',
--         'Phone',
--         'Electronics',
--         'Bought a new phone',
--         'WITHDRAWAL',
--         'UPI'
--     ),
--     (
--         2,
--         200.00,
--         '2023-03-01',
--         'Headphones',
--         'Electronics',
--         'Bought new headphones',
--         'WITHDRAWAL',
--         'CASH'
--     ),
--     (
--         2,
--         1000.00,
--         '2023-04-01',
--         'Freelance Payment',
--         'Income',
--         'Received payment for freelance work',
--         'DEPOSIT',
--         'UPI'
--     ) ON DUPLICATE KEY
-- UPDATE amount = VALUES(amount);