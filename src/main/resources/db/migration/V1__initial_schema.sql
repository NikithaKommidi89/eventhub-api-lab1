-- V1__initial_schema.sql
-- EventHub initial database schema

CREATE TABLE IF NOT EXISTS categories (
                                          id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name        VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS events (
                                      id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      title         VARCHAR(100)   NOT NULL,
    description   VARCHAR(1000),
    ticket_price  DECIMAL(10, 2) NOT NULL,
    category_id   BIGINT,
    is_active     BOOLEAN        DEFAULT TRUE,
    event_date    DATETIME,
    created_at    DATETIME,
    updated_at    DATETIME,
    CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES categories (id)
    );

CREATE TABLE IF NOT EXISTS users (
                                     id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name       VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    phone      VARCHAR(20),
    created_at DATETIME     NOT NULL
    );

CREATE TABLE IF NOT EXISTS registrations (
                                             id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             user_id           BIGINT         NOT NULL,
                                             registration_date DATETIME       NOT NULL,
                                             status            VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    total_amount      DECIMAL(10, 2),
    CONSTRAINT fk_registration_user FOREIGN KEY (user_id) REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS registration_items (
                                                  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  registration_id BIGINT         NOT NULL,
                                                  event_id        BIGINT         NOT NULL,
                                                  quantity        INT            NOT NULL DEFAULT 1,
                                                  unit_price      DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_item_registration FOREIGN KEY (registration_id) REFERENCES registrations (id),
    CONSTRAINT fk_item_event        FOREIGN KEY (event_id)        REFERENCES events (id)
    );