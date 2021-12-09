DROP TABLE game IF EXISTS;

CREATE TABLE game  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(30),
    console VARCHAR(30),
    release INTEGER
);