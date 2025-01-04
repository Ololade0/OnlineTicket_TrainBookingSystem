-- V2__Create_Tables.sql

-- Creating users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    password VARCHAR(255),
    confirm_password VARCHAR(255),
    phone_number VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    id_number VARCHAR(15),
    identification_type VARCHAR(50) NOT NULL
);

-- Creating roles table
CREATE TABLE role (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_type VARCHAR(50)
);

-- Creating notifications table
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Creating trains table
CREATE TABLE trains (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    train_name VARCHAR(255),
    train_code VARCHAR(50)
);

-- Creating trainclass table
CREATE TABLE trainclass (
    train_class_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(50),
    train_id BIGINT,
    total_seat INT,
    FOREIGN KEY (train_id) REFERENCES trains(id) ON DELETE CASCADE
);

-- Creating seats table
CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_number INT,
    status VARCHAR(50),
    train_class_id BIGINT,
    booking_id BIGINT,
    FOREIGN KEY (train_class_id) REFERENCES trainclass(train_class_id) ON DELETE SET NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET NULL
);

-- Creating bookings table
CREATE TABLE bookings (
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    train_class_id BIGINT,
    schedule_id BIGINT NOT NULL,
    fare_amount DOUBLE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (train_class_id) REFERENCES trainclass(train_class_id) ON DELETE SET NULL,
    FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE
);

-- Creating payments table
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DOUBLE,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50),
    booking_id BIGINT,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

-- Creating schedules table
CREATE TABLE schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    departure_time TIME,
    arrival_time TIME,
    departure_date DATE,
    arrival_date DATE,
    duration INTERVAL,
    distance VARCHAR(50),
    schedule_type VARCHAR(50),
    route VARCHAR(50),
    fare_adult_prices DOUBLE,
    fare_minor_prices DOUBLE,
    train_id BIGINT,
    FOREIGN KEY (train_id) REFERENCES trains(id) ON DELETE SET NULL
);

-- Creating stations table
CREATE TABLE stations (
    station_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    station_name VARCHAR(255) NOT NULL,
    station_code VARCHAR(50) NOT NULL,
    station_tag VARCHAR(50)
);

-- Creating schedule_station junction table for many-to-many relationship between schedules and stations
CREATE TABLE schedule_station (
    schedule_id BIGINT,
    station_id BIGINT,
    PRIMARY KEY (schedule_id, station_id),
    FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE,
    FOREIGN KEY (station_id) REFERENCES stations(station_id) ON DELETE CASCADE
);

CREATE TABLE test_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);
