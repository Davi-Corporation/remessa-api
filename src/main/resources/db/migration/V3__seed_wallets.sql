INSERT INTO wallet (user_id, balance_brl, balance_usd)
SELECT id, 10000.00, 0.00
FROM users
WHERE email = 'joao.silva@email.com';

INSERT INTO wallet (user_id, balance_brl, balance_usd)
SELECT id, 5000.00, 250.00
FROM users
WHERE email = 'maria.souza@email.com';
