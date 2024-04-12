CREATE TABLE car (
    id serial PRIMARY KEY,
    user_email VARCHAR(255) UNIQUE,
    car_number VARCHAR(255) UNIQUE,
    car_type VARCHAR(255),
    car_brand VARCHAR(255),
    car_model VARCHAR(255)
);

CREATE TABLE user_file (
    id serial PRIMARY KEY,
    car_id INTEGER,
    file_key VARCHAR(255),
    presigned_url VARCHAR(500),
    FOREIGN KEY (car_id) REFERENCES Car(id)
);