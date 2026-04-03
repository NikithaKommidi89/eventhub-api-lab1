-- V4__add_security.sql
-- Add security tables

ALTER TABLE users ADD COLUMN password VARCHAR(255);
ALTER TABLE users ADD COLUMN enabled BOOLEAN DEFAULT TRUE;

CREATE TABLE roles (
                       id   BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE password_reset_tokens (
                                       id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       token      VARCHAR(255) NOT NULL UNIQUE,
                                       user_id    BIGINT NOT NULL,
                                       expiry     DATETIME NOT NULL,
                                       used       BOOLEAN DEFAULT FALSE,
                                       CONSTRAINT fk_reset_token_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Seed roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Give admin role to first user
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 1);