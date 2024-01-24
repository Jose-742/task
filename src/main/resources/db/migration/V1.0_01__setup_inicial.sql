CREATE TABLE user_account (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    CONSTRAINT username_length_check CHECK (LENGTH(username) >= 2 AND LENGTH(username) <= 100),
    CONSTRAINT password_length_check CHECK (LENGTH(password) >= 8 AND LENGTH(password) <= 60)
);

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    user_account_id BIGINT REFERENCES user_account(id) ON DELETE CASCADE,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT description_length_check CHECK (LENGTH(description) >= 1 AND LENGTH(description) <= 255)
);
