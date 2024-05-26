CREATE TABLE parking (
    id serial PRIMARY KEY,
    info VARCHAR(255)
);

CREATE TABLE parking_order (
    id serial PRIMARY KEY,
    status VARCHAR(255)
);

CREATE TABLE parking_space (
    id SERIAL PRIMARY KEY,
    parking_id INTEGER,
    car_id INTEGER,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    is_occupied BOOLEAN
);

INSERT INTO Parking (info) VALUES ('Example info');

INSERT INTO parking_space (parking_id, car_id, start_time, end_time, is_occupied)
VALUES (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false),
    (1, NULL, NULL, NULL, false);