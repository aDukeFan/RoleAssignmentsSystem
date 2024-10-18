DROP TABLE IF EXISTS roles;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1024)
);