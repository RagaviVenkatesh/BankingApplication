Create database bank;
use bank;

CREATE TABLE Bank (
    bank_id INT PRIMARY KEY AUTO_INCREMENT,
    bank_name VARCHAR(100) NOT NULL,
    bank_branch VARCHAR(100) NOT NULL
);

CREATE TABLE Account (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    bank_id INT,  -- Foreign key to Bank table
    account_type VARCHAR(50) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bank_id) REFERENCES Bank(bank_id)
);

CREATE TABLE SavingsAccount (
    account_id INT PRIMARY KEY,
    interest_rate DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

CREATE TABLE CurrentAccount (
    account_id INT PRIMARY KEY,
    overdraft_limit DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

CREATE TABLE Transaction (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES Account(account_id)
);

CREATE TABLE DepositTransaction (
    transaction_id INT PRIMARY KEY,
    deposit_method VARCHAR(50) NOT NULL,
    FOREIGN KEY (transaction_id) REFERENCES Transaction(transaction_id)
);

CREATE TABLE WithdrawalTransaction (
    transaction_id INT PRIMARY KEY,
    withdrawal_method VARCHAR(50) NOT NULL,
    FOREIGN KEY (transaction_id) REFERENCES Transaction(transaction_id)
);

-- Stored Procedure

DELIMITER //

CREATE PROCEDURE transfer_funds(IN from_account INT, IN to_account INT, IN amount DECIMAL(15, 2))
BEGIN
    DECLARE from_balance DECIMAL(15, 2);
    SELECT balance INTO from_balance FROM Account WHERE account_id = from_account;

    IF from_balance >= amount THEN
        UPDATE Account SET balance = balance - amount WHERE account_id = from_account;
        UPDATE Account SET balance = balance + amount WHERE account_id = to_account;
        INSERT INTO Transaction(account_id, transaction_type, amount) VALUES (from_account, 'Transfer Out', amount);
        INSERT INTO Transaction(account_id, transaction_type, amount) VALUES (to_account, 'Transfer In', amount);
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient balance in from_account';
    END IF;
END //

DELIMITER ;

 -- Function

DELIMITER //

CREATE FUNCTION total_balance_for_bank(bank_id INT) RETURNS DECIMAL(15, 2)
DETERMINISTIC
BEGIN
    DECLARE total_balance DECIMAL(15, 2);
    SELECT SUM(balance) INTO total_balance FROM Account WHERE bank_id = bank_id;
    RETURN total_balance;
END //

DELIMITER ;

-- View

CREATE VIEW AccountBankView AS
SELECT Account.account_id, Account.customer_id, Account.account_type, Account.balance, Bank.bank_name, Bank.bank_branch
FROM Account
JOIN Bank ON Account.bank_id = Bank.bank_id;

-- Indexes

CREATE INDEX idx_account_id ON Transaction(account_id);
CREATE INDEX idx_balance ON Account(balance);

CREATE VIEW DailyTransactionSummary AS
SELECT DATE(transaction_date) AS date, transaction_type, COUNT(*) AS count, SUM(amount) AS total_amount
FROM Transaction
GROUP BY DATE(transaction_date), transaction_type;

INSERT INTO Bank (bank_name, bank_branch) VALUES ('ABC Bank', 'Main Branch');
INSERT INTO Bank (bank_name, bank_branch) VALUES ('XYZ Bank', 'Main Branch');

DELIMITER $$

CREATE PROCEDURE deposit_procedure(IN account_id INT, IN deposit_amount DECIMAL(15, 2))
BEGIN
    -- Check if the account exists
    DECLARE account_exists INT DEFAULT 0;
    SELECT COUNT(*) INTO account_exists FROM Account WHERE account_id = account_id;

    IF account_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Account not found';
    ELSE
        -- Update the balance
        UPDATE Account
        SET balance = balance + deposit_amount
        WHERE account_id = account_id;
    END IF;
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE withdraw_procedure(IN account_id INT, IN withdraw_amount DECIMAL(15, 2))
BEGIN
    -- Check if the account exists and get the current balance
    DECLARE current_balance DECIMAL(15, 2);
    DECLARE insufficient_funds CONDITION FOR SQLSTATE '45000';
    DECLARE account_not_found CONDITION FOR SQLSTATE '45001';

    -- Attempt to select the current balance
    SELECT balance INTO current_balance
    FROM Account
    WHERE account_id = account_id
    LIMIT 1;

    -- If account was not found (no row returned), raise an error
    IF current_balance IS NULL THEN
        SIGNAL account_not_found
        SET MESSAGE_TEXT = 'Account not found';

    -- Check for sufficient funds
    ELSEIF current_balance < withdraw_amount THEN
        SIGNAL insufficient_funds
        SET MESSAGE_TEXT = 'Insufficient funds for withdrawal';

    -- Proceed with withdrawal if funds are sufficient
    ELSE
        UPDATE Account
        SET balance = balance - withdraw_amount
        WHERE account_id = account_id;
    END IF;
END$$

DELIMITER ;


DROP PROCEDURE IF EXISTS withdraw_procedure;

ALTER TABLE Account ADD CONSTRAINT account_id_unique UNIQUE (account_id);

SELECT account_id, COUNT(*)
FROM Account
GROUP BY account_id
HAVING COUNT(*) > 1;





