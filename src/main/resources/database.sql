CREATE DATABASE  IF NOT EXISTS jm_secure_task;
USE jm_secure_task;

DROP TABLE IF EXISTS jm_secure_task.user_roles;
DROP TABLE IF EXISTS jm_secure_task.users;
DROP TABLE IF EXISTS jm_secure_task.roles;


-- Table: users
CREATE TABLE users (
  id       bigint          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
)
  ENGINE = InnoDB;

-- Table: roles
CREATE TABLE roles (
  id   bigint    NOT NULL AUTO_INCREMENT PRIMARY KEY,
  role VARCHAR(100) NOT NULL
)
  ENGINE = InnoDB;

-- Table for mapping user and roles: user_roles
CREATE TABLE user_roles (
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,

  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (role_id) REFERENCES roles (id),

  UNIQUE (user_id, role_id)
)
  ENGINE = InnoDB;
