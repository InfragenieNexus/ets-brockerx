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
        '$2a$10$SVKgXR1u9UqF7QWVt63RauNr8W2viqBnQQVTrtRccqCkBwHVzRB/C',
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
        '$2a$10$yetanotherhashedpassword...',
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
