CREATE TABLE IF NOT EXISTS users (
  id       SERIAL PRIMARY KEY,
  login    VARCHAR(50) UNIQUE,
  email    VARCHAR(50) UNIQUE,
  password TEXT,
  score    INTEGER DEFAULT 0
);

CREATE UNIQUE INDEX users_login_unique_idx
  ON users (login);