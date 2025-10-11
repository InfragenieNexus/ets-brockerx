-- ======================
-- USERS
-- ======================

-- Utilisateur 1
INSERT INTO users (date_of_birth,
                   mfa_enabled,
                   created_at,
                   address,
                   email,
                   first_name,
                   kyc_document_hash,
                   last_name,
                   password,
                   phone,
                   status,
                   totp_secret)
VALUES ('2025-09-09',
        false,
        '2025-09-25 11:27:20.516075',
        '123 rue Test',
        'test.test@hotmail.com',
        'Test',
        NULL,
        'User1',
        '$2b$10$CjIShmW4TLQjRacq7igli./2M7H.H6hosHQwsfo2aSQ.qnsSjFs5O',
        '555-0101',
        'ACTIVE',
        NULL);

-- Utilisateur 2
INSERT INTO users (date_of_birth,
                   mfa_enabled,
                   created_at,
                   address,
                   email,
                   first_name,
                   kyc_document_hash,
                   last_name,
                   password,
                   phone,
                   status,
                   totp_secret)
VALUES ('1990-06-15',
        true,
        '2025-09-25 12:00:00.000000',
        '456 avenue Exemple',
        'alice.ex@example.com',
        'Alice',
        NULL,
        'Smith',
        '$2a$10$anotherhashedpasswordexample...',
        '555-0202',
        'ACTIVE',
        'ABC123');

-- Utilisateur 3
INSERT INTO users (date_of_birth,
                   mfa_enabled,
                   created_at,
                   address,
                   email,
                   first_name,
                   kyc_document_hash,
                   last_name,
                   password,
                   phone,
                   status,
                   totp_secret)
VALUES ('1985-12-01',
        false,
        '2025-09-25 13:15:10.000000',
        '789 boulevard Exemple',
        'bob@example.com',
        'Bob',
        NULL,
        'Brown',
        '$2b$10$CjIShmW4TLQjRacq7igli./2M7H.H6hosHQwsfo2aSQ.qnsSjFs5O',
        '555-0303',
        'PENDING',
        NULL);

-- ======================
-- WALLETS
-- ======================

INSERT INTO wallets (balance,
                     user_id)
VALUES (1000.0, 1);

INSERT INTO wallets (balance,
                     user_id)
VALUES (500.0, 2);

INSERT INTO wallets (balance,
                     user_id)
VALUES (0.0, 3);

-- ======================
-- TRANSACTIONS
-- ======================

INSERT INTO transactions (amount,
                          status,
                          idempotency_key,
                          created_at,
                          user_id)
VALUES (250.0, 'Settled', 'txn-0001', '2025-09-25 14:00:00', 1);

INSERT INTO transactions (amount,
                          status,
                          idempotency_key,
                          created_at,
                          user_id)
VALUES (50.0, 'Pending', 'txn-0002', '2025-09-25 14:30:00', 1);

INSERT INTO transactions (amount,
                          status,
                          idempotency_key,
                          created_at,
                          user_id)
VALUES (100.0, 'Settled', 'txn-0003', '2025-09-25 15:00:00', 2);

INSERT INTO transactions (amount,
                          status,
                          idempotency_key,
                          created_at,
                          user_id)
VALUES (75.0, 'Failed', 'txn-0004', '2025-09-25 15:30:00', 3);

-- ======================
-- ORDERS
-- ======================
INSERT INTO orders (user_id, symbol, side, type, quantity, price, time_in_force,
                    status, created_at, idempotency_key)
VALUES (2, 'AAPL', 'BUY', 'LIMIT', 10, 150.00, 'DAY', 'ACK', NOW(), 'seed-1'),
       (2, 'AAPL', 'SELL', 'LIMIT', 5, 152.00, 'DAY', 'ACK', NOW(), 'seed-2'),
       (2, 'GOOG', 'BUY', 'MARKET', 20, 200.00, 'IOC', 'ACK', NOW(), 'seed-3'),
       (2, 'TSLA', 'SELL', 'LIMIT', 15, 240.50, 'DAY', 'ACK', NOW(), 'seed-4');

-- ======================
-- POSITIONS
-- ======================

INSERT INTO positions (user_id, symbol, quantity)
VALUES (1, 'AAPL', 20), -- L'utilisateur 1 possède 20 actions AAPL
       (1, 'GOOG', 5),  -- L'utilisateur 1 possède 5 actions GOOG
       (2, 'AAPL', 10), -- L'utilisateur 2 possède 10 actions AAPL
       (2, 'TSLA', 8),  -- L'utilisateur 2 possède 8 actions TSLA
       (3, 'GOOG', 12), -- L'utilisateur 3 possède 12 actions GOOG
       (3, 'TEST', 10);
