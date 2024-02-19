-- Drop tables if they exist
DROP TABLE IF EXISTS user_ticket CASCADE;
DROP TABLE IF EXISTS lottery CASCADE;

-- Create lottery table
CREATE TABLE lottery (
    ticket_id VARCHAR(6) PRIMARY KEY,
    price INTEGER NOT NULL,
    amount INTEGER NOT NULL
);

-- Create user_ticket table
CREATE TABLE user_ticket (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(10)  NOT NULL,
    ticket_id VARCHAR(6)  NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES lottery(ticket_id)

);