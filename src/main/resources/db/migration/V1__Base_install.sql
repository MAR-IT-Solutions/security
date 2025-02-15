create table if not exists users (
    user_id SERIAL primary key,
    client_code VARCHAR(10),
    username VARCHAR(50) not null unique,
    enabled bool,
    name VARCHAR(100),
    email VARCHAR(100) not null,
    contact_number VARCHAR(15),
    address text,
    password VARCHAR not null,
    created_date TIMESTAMP default CURRENT_TIMESTAMP,
    modified_date TIMESTAMP default CURRENT_TIMESTAMP
);


CREATE OR REPLACE FUNCTION update_modified_date_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.modified_date = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER update_users_modified_time
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE PROCEDURE update_modified_date_column();

CREATE TABLE if not exists roles(
    role_id SERIAL primary key,
    "role" varchar(255) NOT NULL UNIQUE
);
INSERT INTO roles ("role")
VALUES
    ('ADMIN'),
    ('MANAGER'),
    ('CHECKER'),
    ('MAKER'),
    ('APPROVER'),
    ('REPORTER'),
    ('USER');


CREATE TABLE users_roles (
    role_id int8 NOT NULL,
    user_id int8 NOT NULL
);

ALTER TABLE users_roles ADD CONSTRAINT users_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(user_id);
ALTER TABLE users_roles ADD CONSTRAINT users_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES roles(role_id);