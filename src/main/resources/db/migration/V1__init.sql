CREATE TABLE IF NOT EXISTS users (
  login    VARCHAR(50) UNIQUE,
  email    VARCHAR(50) UNIQUE,
  password TEXT,
  score    INTEGER DEFAULT 0
);