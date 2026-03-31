-- V3__add_indexes.sql
-- Add indexes for performance optimization

CREATE INDEX idx_events_category    ON events (category_id);
CREATE INDEX idx_events_is_active   ON events (is_active);
CREATE INDEX idx_events_event_date  ON events (event_date);
CREATE INDEX idx_events_price       ON events (ticket_price);

CREATE INDEX idx_registrations_user   ON registrations (user_id);
CREATE INDEX idx_registrations_status ON registrations (status);
CREATE INDEX idx_registrations_date   ON registrations (registration_date);

CREATE INDEX idx_reg_items_registration ON registration_items (registration_id);
CREATE INDEX idx_reg_items_event        ON registration_items (event_id);
