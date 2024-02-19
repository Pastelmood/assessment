-- Drop tables if they exist
DROP TABLE IF EXISTS user_ticket CASCADE;
DROP TABLE IF EXISTS lottery CASCADE;

-- Create lottery table
CREATE TABLE lottery (
    id SERIAL PRIMARY KEY,
    ticket VARCHAR(6) NOT NULL,
    price INTEGER NOT NULL,
    amount INTEGER NOT NULL
);

-- Create user_ticket table
CREATE TABLE user_ticket (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    ticket_id INTEGER NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES lottery(id)
);