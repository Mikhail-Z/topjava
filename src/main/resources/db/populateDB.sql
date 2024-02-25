DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (description, calories, date_time, user_id)
VALUES
('Завтрак USER', 500, '2020-01-30 10:00', 100000),
('Обед USER', 1000, '2020-01-30 13:00', 100000),
('Ужин USER', 500, '2020-01-30 20:00', 100000),
('Еда на граничное значение USER', 100, '2020-01-31 00:00', 100000),
('Завтрак USER', 1000, '2020-01-31 10:00', 100000),
('Обед USER', 500, '2020-01-31 13:00', 100000),
('Ужин USER', 410, '2020-01-31 20:00', 100000),

('Завтрак ADMIN', 500, '2020-02-01 10:00', 100001),
('Обед ADMIN', 1000, '2020-02-01 13:00', 100001),
('Ужин ADMIN', 500, '2020-02-01 20:00', 100001),
('Еда на граничное значение ADMIN', 100, '2020-02-02 00:00', 100001),
('Завтрак ADMIN', 1000, '2020-02-02 10:00', 100001),
('Обед ADMIN', 500, '2020-02-02 13:00', 100001),
('Ужин ADMIN', 410, '2020-02-02 20:00', 100001);