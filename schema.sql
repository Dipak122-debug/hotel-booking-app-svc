-- User Service Database Schema

-- Create database
CREATE DATABASE IF NOT EXISTS user_service_db;
USE user_service_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(50) DEFAULT 'USER',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create index on email for faster lookups
CREATE INDEX idx_users_email ON users(email);

-- Optional: Insert sample data for testing
-- INSERT INTO users (first_name, last_name, email, password, phone, role, status)
-- VALUES ('John', 'Doe', 'john@example.com', 'hashed_password_here', '+1234567890', 'USER', 'ACTIVE');

