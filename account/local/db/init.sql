CREATE TABLE Car (
    id INTEGER PRIMARY KEY,
    user_email VARCHAR(255),
    car_number VARCHAR(255),
    car_type VARCHAR(255),
    car_brand VARCHAR(255),
    car_model VARCHAR(255)
);

CREATE TABLE UserFile (
    id INTEGER PRIMARY KEY,
    car_id INTEGER,
    file_key VARCHAR(255),
    presigned_url VARCHAR(255),
    FOREIGN KEY (car_id) REFERENCES Car(id)
);