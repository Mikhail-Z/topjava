DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users;
drop table if exists meals;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT now() NOT NULL,
    enabled          BOOL                DEFAULT TRUE  NOT NULL,
    calories_per_day INTEGER             DEFAULT 2000  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE meals
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    description varchar not null,
    calories integer not null,
    date_time timestamp not null,
    user_id integer not null,
    constraint date_time_user_idx unique (date_time, user_id),
    constraint user_fk foreign key (user_id) references users(id) on delete cascade
);