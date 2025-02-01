CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    gender ENUM('MALE', 'FEMALE'),
    password VARCHAR(255),
    phone_number VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    id_number VARCHAR(50)
);