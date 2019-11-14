DELETE FROM users;
DELETE FROM user_roles;
DELETE FROM user_role_user;

INSERT INTO user_roles(name)
VALUES ('ROLE_USER');