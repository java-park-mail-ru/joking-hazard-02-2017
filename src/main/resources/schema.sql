create table if not exists users (
  login    VARCHAR(50) UNIQUE,
  email    VARCHAR(50) UNIQUE,
  password VARCHAR(50) UNIQUE
);