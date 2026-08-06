CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       person_type VARCHAR(2) NOT NULL,
                       cpf_cnpj VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE wallet (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL UNIQUE,
                        balance_brl DECIMAL(19,2) NOT NULL DEFAULT 0,
                        balance_usd DECIMAL(19,2) NOT NULL DEFAULT 0,

                        CONSTRAINT fk_wallet_user
                            FOREIGN KEY (user_id)
                                REFERENCES users(id)
);

CREATE TABLE exchange_rates (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                quotation_date DATE NOT NULL UNIQUE,
                                buy_rate DECIMAL(10,4) NOT NULL,
                                created_at DATETIME NOT NULL
);

CREATE TABLE transfers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           sender_id BIGINT NOT NULL,
                           receiver_id BIGINT NOT NULL,
                           amount_brl DECIMAL(19,2) NOT NULL,
                           amount_usd DECIMAL(19,2) NOT NULL,
                           exchange_rate DECIMAL(10,4) NOT NULL,
                           created_at DATETIME NOT NULL,

                           CONSTRAINT fk_transfer_sender
                               FOREIGN KEY (sender_id)
                                   REFERENCES users(id),

                           CONSTRAINT fk_transfer_receiver
                               FOREIGN KEY (receiver_id)
                                   REFERENCES users(id)
);