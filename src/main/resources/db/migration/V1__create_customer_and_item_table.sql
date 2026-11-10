CREATE SEQUENCE IF NOT EXISTS seq_customer
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS customer
(
    id            BIGINT NOT NULL,
    first_name    TEXT   NOT NULL,
    last_name     TEXT   NOT NULL,
    email_address TEXT   NOT NULL,
    address       TEXT   NOT NULL,
    phone_number  TEXT   NOT NULL,

    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS seq_item
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS item (
    id BIGINT NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    amount INT NOT NULL,

    CONSTRAINT pk_item PRIMARY KEY (id)
)