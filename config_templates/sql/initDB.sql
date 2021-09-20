CREATE SEQUENCE common_seq START 100000;

CREATE TABLE city
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
    ref  TEXT UNIQUE,
    name TEXT NOT NULL
);

CREATE TABLE users
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
    ref  TEXT UNIQUE,
    name TEXT NOT NULL
);

ALTER TABLE users
    ADD COLUMN city_id INTEGER REFERENCES city (id);