DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (userid, datetime, description, calories)
VALUES (100001, '2020-06-01 14:00:00', 'Админ ланч', 510),
       (100001, '2020-06-01 21:00:00', 'Админ ланч', 1500),
       (100000, '2020-06-01 09:00:00', 'Завтрак', 300),
       (100000, '2020-06-01 13:00:00', 'Обед', 1000),
       (100000, '2020-06-01 18:00:00', 'Ужин', 200);

