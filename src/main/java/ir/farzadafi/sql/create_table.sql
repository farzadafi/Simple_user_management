USE `user-management`;

CREATE TABLE IF NOT EXISTS user
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    firstname     VARCHAR(255),
    lastname      VARCHAR(255),
    national_code VARCHAR(255),
    email         VARCHAR(255),
    password      VARCHAR(255),
    birthdate     DATE,
    created_in    DATETIME,
    enabled       TINYINT,
    deleted       TINYINT,
    CONSTRAINT UK_national_code UNIQUE (national_code),
    CONSTRAINT UK_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS location_hierarchy
(
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(255),
    location_hierarchy_id INT
);

CREATE TABLE IF NOT EXISTS address
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    street      VARCHAR(255),
    province_id INT REFERENCES location_hierarchy (id),
    county_id   INT REFERENCES location_hierarchy (id),
    city_id     INT REFERENCES location_hierarchy (id)
)