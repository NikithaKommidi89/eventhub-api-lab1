-- Seed categories
INSERT INTO categories (name, description) VALUES ('Concert', 'Live music performances');
INSERT INTO categories (name, description) VALUES ('Conference', 'Professional and tech conferences');
INSERT INTO categories (name, description) VALUES ('Workshop', 'Hands-on learning workshops');
INSERT INTO categories (name, description) VALUES ('Sports', 'Sporting events and matches');
INSERT INTO categories (name, description) VALUES ('Festival', 'Cultural and food festivals');

-- Seed events
INSERT INTO events (title, description, ticket_price, category_id, is_active, event_date, created_at, updated_at)
VALUES ('Spring Boot Workshop 2025', 'A hands-on workshop covering Spring Boot 3.x', 49.99, 3, true, '2025-06-15 09:00:00', NOW(), NOW());

INSERT INTO events (title, description, ticket_price, category_id, is_active, event_date, created_at, updated_at)
VALUES ('Rock Night Live', 'An evening of rock classics with local bands', 25.00, 1, true, '2025-07-20 19:00:00', NOW(), NOW());

INSERT INTO events (title, description, ticket_price, category_id, is_active, event_date, created_at, updated_at)
VALUES ('DevConf 2025', 'Annual developer conference with keynotes and sessions', 199.00, 2, true, '2025-09-10 08:00:00', NOW(), NOW());

INSERT INTO events (title, description, ticket_price, category_id, is_active, event_date, created_at, updated_at)
VALUES ('Free Coding Bootcamp', 'Introduction to programming, completely free', 0.00, 3, true, '2025-08-01 10:00:00', NOW(), NOW());

INSERT INTO events (title, description, ticket_price, category_id, is_active, event_date, created_at, updated_at)
VALUES ('Summer Music Festival', 'Three days of music across multiple stages', 75.00, 5, true, '2025-07-04 12:00:00', NOW(), NOW());
