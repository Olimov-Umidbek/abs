CREATE TABLE users(
    id uuid NOT NULL,
    name VARCHAR(64) NOT NULL,
    surname VARCHAR(64) NOT NULL,
    status VARCHAR(16) NOT NULL,
    balance NUMERIC NOT NULL,
    version INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    primary key(id)
);

CREATE TABLE transactions (
    id UUID NOT NULL,
    amount NUMERIC NOT NULL,
    status VARCHAR(16) NOT NULL,
    sender_id UUID NOT NULL,
    receiver_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id, created_at)
) PARTITION BY RANGE (created_at);

ALTER TABLE transactions ADD CONSTRAINT fk_sender_id FOREIGN KEY(sender_id) REFERENCES users(id);
ALTER TABLE transactions ADD CONSTRAINT fk_receiver_id FOREIGN KEY(receiver_id) REFERENCES users(id);


CREATE TABLE transaction_histories(
    id BIGINT NOT NULL,
    status VARCHAR(16) NOT NULL,
    transaction_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id, created_at)
) PARTITION BY RANGE (created_at);
CREATE SEQUENCE transaction_histories_seq START 1;
