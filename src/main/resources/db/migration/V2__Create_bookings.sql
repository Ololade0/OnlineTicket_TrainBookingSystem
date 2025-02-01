CREATE TABLE bookings (
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    train_class_id BIGINT,
    fare_amount DOUBLE,
    passenger_type VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);