CREATE TABLE user_roles (
    id SERIAL,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_role_user (
    id SERIAL,
    user_id INTEGER NOT NULL,
    user_role_id INTEGER NOT NULL
);