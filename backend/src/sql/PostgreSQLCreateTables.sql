DROP SEQUENCE IF EXISTS user_id_seq CASCADE;
DROP SEQUENCE IF EXISTS database_id_seq CASCADE;
DROP SEQUENCE IF EXISTS type_id_seq CASCADE;
DROP SEQUENCE IF EXISTS task_id_seq CASCADE;
DROP SEQUENCE IF EXISTS log_id_seq CASCADE;

DROP TABLE IF EXISTS UserTable CASCADE;
DROP TABLE IF EXISTS DatabaseTable CASCADE;
DROP TABLE IF EXISTS DatabaseType CASCADE;
DROP TABLE IF EXISTS Task CASCADE;
DROP TABLE IF EXISTS Log CASCADE;
DROP TABLE IF EXISTS EntityTable CASCADE;

CREATE SEQUENCE user_id_seq;
CREATE SEQUENCE database_id_seq;
CREATE SEQUENCE type_id_seq;
CREATE SEQUENCE task_id_seq;
CREATE SEQUENCE log_id_seq;

CREATE TABLE UserTable(
    id BIGINT DEFAULT nextval('user_id_seq'),
    username VARCHAR(60) NOT NULL,
    password VARCHAR(300) NOT NULL,
    email VARCHAR(60) NOT NULL,
    role INT NOT NULL,
    CONSTRAINT User_PK PRIMARY KEY (id),
    CONSTRAINT UserNameUnique UNIQUE (username)
);

CREATE INDEX UserIndexByUserName ON UserTable (username);

CREATE TABLE DatabaseType(
     id BIGINT DEFAULT nextval('type_id_seq'),
     typeName VARCHAR(60) NOT NULL,
     CONSTRAINT Type_PK PRIMARY KEY (id),
     CONSTRAINT TypeNameUnique UNIQUE (typeName)
);

CREATE TABLE DatabaseTable(
     id BIGINT DEFAULT nextval('database_id_seq'),
     database VARCHAR(60) NOT NULL,
     host VARCHAR(60) NOT NULL,
     port int NOT NULL,
     userdb VARCHAR(60) NOT NULL,
     password VARCHAR(300) NOT NULL,
     userId BIGINT NOT NULL,
     typeId BIGINT NOT NULL,
     CONSTRAINT DatabaseTable_PK PRIMARY KEY (id),
     CONSTRAINT DatabaseUserIdFK FOREIGN KEY (userId) REFERENCES UserTable(id),
     CONSTRAINT DatabaseTypeIdFK FOREIGN KEY (typeId) REFERENCES DatabaseType(id)
);

CREATE TABLE Task(
    id BIGINT DEFAULT nextval('task_id_seq'),
    taskname CHARACTER VARYING NOT NULL,
    state CHARACTER VARYING NOT NULL,
    taskString CHARACTER VARYING NOT NULL,
    creationDate TIMESTAMP NOT NULL,
    endDate TIMESTAMP,
    error CHARACTER VARYING,
    userId BIGINT NOT NULL,
    databaseId BIGINT,
    CONSTRAINT Task_PK PRIMARY KEY (id),
    CONSTRAINT TaskUserIdFK FOREIGN KEY (userId) REFERENCES UserTable(id),
    CONSTRAINT TaskDatabaseIdFK FOREIGN KEY (databaseId) REFERENCES DatabaseTable(id)
);

CREATE TABLE EntityTable(
    databaseId BIGINT NOT NULL,
    entityName CHARACTER VARYING NOT NULL,
    CONSTRAINT Entity_PK PRIMARY KEY (databaseId, entityName),
    CONSTRAINT EntityFKDatabase FOREIGN KEY (databaseId) REFERENCES DatabaseTable (id)
);

CREATE TABLE Log(
    id BIGINT DEFAULT nextval('log_id_seq'),
    log CHARACTER VARYING NOT NULL,
    logType CHARACTER VARYING NOT NULL,
    taskId BIGINT NOT NULL,
    CONSTRAINT Log_PK PRIMARY KEY (id),
    CONSTRAINT LogTaskIdFK FOREIGN KEY (taskId) REFERENCES Task(id)
);

INSERT INTO DatabaseType (typeName) VALUES ('PostgreSQL');
INSERT INTO DatabaseType (typeName) VALUES ('MySQL');

