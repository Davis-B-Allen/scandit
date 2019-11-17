CREATE TABLE comments (
    id SERIAL,
    text VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    post_id BIGINT NOT NULL
);