-- seed.sql
INSERT INTO users (email, password, status)
VALUES ('admin@example.com', '$2a$10$hashedpassword', 'ACTIVE'),
       ('user@example.com', '$2a$10$hashedpassword', 'ACTIVE');

