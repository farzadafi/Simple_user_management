USE `user-management`;

CREATE TABLE user
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    firstname     VARCHAR(255),
    lastname      VARCHAR(255),
    national_code VARCHAR(255),
    email         VARCHAR(255),
    password      VARCHAR(255),
    birthdate     DATE,
    created_in    DATETIME,
    enabled     TINYINT,
    deleted       TINYINT,
    CONSTRAINT UK_national_code UNIQUE (national_code),
    CONSTRAINT UK_email UNIQUE (email)
);