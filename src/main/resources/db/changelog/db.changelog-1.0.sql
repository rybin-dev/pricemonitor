--liquibase formatted sql

--changeset rybindev:1
CREATE TABLE IF NOT EXISTS category
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

--changeset rybindev:2
CREATE TABLE IF NOT EXISTS shop
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    info VARCHAR(255) NOT NULL
);

--changeset rybindev:3
CREATE TABLE IF NOT EXISTS product
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    info VARCHAR(255) NOT NULL
);

--changeset rybindev:4
CREATE TABLE IF NOT EXISTS product_category
(
    product_id  BIGINT REFERENCES product (id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES category (id) ON DELETE CASCADE,
    CONSTRAINT pk_product_id_category_id PRIMARY KEY (product_id, category_id)
);

--changeset rybindev:5
CREATE TABLE IF NOT EXISTS product_price
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product (id) ON DELETE CASCADE,
    shop_id    BIGINT REFERENCES shop (id) ON DELETE CASCADE,
    price      BIGINT NOT NULL,
    start_date DATE   NOT NULL,
    end_date   DATE
);

--changeset rybindev:6
CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(128)       NOT NULL,
    role     VARCHAR(50) DEFAULT 'USER'
);

--changeset rybindev:7
CREATE TABLE user_profile
(
    user_id    BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    name       VARCHAR(50),
    avatar_url VARCHAR(128)
);