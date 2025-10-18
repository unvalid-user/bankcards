INSERT INTO roles (id, name)
VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER');

INSERT INTO users (phone_number, password, role_id)
VALUES
    ('admin', '$2a$10$NGYj/DULa7A2IxgAf49aAO5sSoIUhkLauyxKOimKcu4pfE4WNKi5C', 1), -- admin
    ('user1', '$2a$10$eEO75GIaTgAcFn6rCIxKDuJE9h/ElWpv6hrxCEuso3cnNP9YuqUci', 2), -- user
    ('user2', '$2a$10$eEO75GIaTgAcFn6rCIxKDuJE9h/ElWpv6hrxCEuso3cnNP9YuqUci', 2); -- user

INSERT INTO cards (masked_number, number, user_id, expiration_date, status, balance)
VALUES
    ('1234', 'EncryptedData1234', 2, '2027-10-12', 'ACTIVE', 123.40),
    ('2345', 'EncryptedData2345', 2, '2027-10-12', 'ACTIVE', 234.50),
    ('3456', 'EncryptedData3456', 2, '2027-10-12', 'BLOCKED', 345.60),
    ('4567', 'EncryptedData4567', 3, '2027-10-12', 'ACTIVE', 456.70),
    ('5678', 'EncryptedData5678', 3, '2027-10-12', 'BLOCKED', 567.80),
    ('6789', 'EncryptedData6789', 3, '2022-10-12', 'EXPIRED', 678.90),
    ('7890', 'EncryptedData7890', 3, '2027-10-12', 'ACTIVE', 789.00);