
CREATE TABLE IF NOT EXISTS PUBLIC.user (
    id_user INT PRIMARY KEY NOT NULL IDENTITY,
    name VARCHAR(200) NOT NULL UNIQUE,
    email VARCHAR(200) NULL,
    birthday TIMESTAMP NULL,
    CONSTRAINT U0_USER UNIQUE (email)
);

CREATE TABLE PUBLIC.role (
    id_role INT PRIMARY KEY NOT NULL IDENTITY,
    name VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE PUBLIC.user_role (
    id_user INT NOT NULL,
    id_role INT NOT NULL,
    CONSTRAINT PK0_USER_ROLE PRIMARY KEY (id_user, id_role),
    CONSTRAINT FK0_USER FOREIGN KEY (id_user) REFERENCES user (id_user),
    CONSTRAINT FK0_ROLE FOREIGN KEY (id_role) REFERENCES role (id_role)
);
