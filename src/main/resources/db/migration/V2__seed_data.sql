-- V2__seed_data.sql
-- EventHub seed data

INSERT INTO categories (name, description) VALUES
                                               ('Conference',  'Professional and tech conferences'),
                                               ('Workshop',    'Hands-on learning sessions'),
                                               ('Concert',     'Live music performances'),
                                               ('Sports',      'Athletic and sporting events'),
                                               ('Networking',  'Professional networking meetups');

INSERT INTO events (title, description, ticket_price, category_id, is_active, event_date, created_at, updated_at) VALUES
                                                                                                                      ('Spring Boot Conference 2025',  'A full-day conference on Spring Boot 3 and microservices.', 49.99,  1, TRUE,  '2025-06-15 09:00:00', NOW(), NOW()),
                                                                                                                      ('Java Workshop: Clean Code',    'Hands-on workshop covering SOLID principles.',              29.99,  2, TRUE,  '2025-07-10 10:00:00', NOW(), NOW()),
                                                                                                                      ('Calgary Music Fest',           'Annual outdoor music festival.',                            79.99,  3, TRUE,  '2025-08-20 18:00:00', NOW(), NOW()),
                                                                                                                      ('AI & Machine Learning Summit', 'Explore the latest in AI and ML technologies.',            99.99,  1, TRUE,  '2025-09-05 09:00:00', NOW(), NOW()),
                                                                                                                      ('Tech Networking Night',        'Connect with Calgary tech professionals.',                  15.00,  5, TRUE,  '2025-07-25 18:30:00', NOW(), NOW()),
                                                                                                                      ('Docker & Kubernetes Workshop', 'Learn containerization from scratch.',                     39.99,  2, TRUE,  '2025-08-01 10:00:00', NOW(), NOW()),
                                                                                                                      ('Calgary Stampede Run',         '5K charity run during Stampede week.',                     25.00,  4, TRUE,  '2025-07-08 07:00:00', NOW(), NOW()),
                                                                                                                      ('React Frontend Workshop',      'Build modern UIs with React and TypeScript.',              34.99,  2, FALSE, '2025-05-20 10:00:00', NOW(), NOW());

INSERT INTO users (name, email, phone, created_at) VALUES
                                                       ('Jane Doe',     'jane.doe@example.com',   '+1-403-555-0101', NOW()),
                                                       ('John Smith',   'john.smith@example.com', '+1-403-555-0102', NOW()),
                                                       ('Alice Johnson','alice.j@example.com',    '+1-587-555-0103', NOW());

INSERT INTO registrations (user_id, registration_date, status, total_amount) VALUES
                                                                                 (1, NOW(), 'CONFIRMED', 79.98),
                                                                                 (2, NOW(), 'CONFIRMED', 99.99),
                                                                                 (3, NOW(), 'PENDING',   54.99);

INSERT INTO registration_items (registration_id, event_id, quantity, unit_price) VALUES
                                                                                     (1, 1, 1, 49.99),
                                                                                     (1, 5, 2, 15.00),
                                                                                     (2, 4, 1, 99.99),
                                                                                     (3, 6, 1, 39.99),
                                                                                     (3, 5, 1, 15.00);