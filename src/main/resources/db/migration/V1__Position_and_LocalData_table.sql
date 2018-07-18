CREATE SEQUENCE position_position_id_seq;
CREATE TABLE position (
  position_id BIGINT DEFAULT NEXTVAL('position_position_id_seq') PRIMARY KEY,
  latitude    REAL NOT NULL,
  longitude   REAL NOT NULL,
  altitude    REAL   DEFAULT 0,
  CONSTRAINT u_constraint UNIQUE (latitude, longitude)
);

CREATE SEQUENCE local_data_local_data_id_seq;
CREATE TABLE local_data (
  local_data_id BIGINT DEFAULT NEXTVAL('local_data_local_data_id_seq') PRIMARY KEY,
  parameter     VARCHAR(15)                              NOT NULL,
  year          INTEGER                                  NOT NULL,
  position_id   BIGINT REFERENCES position (position_id) NOT NULL,
  arr           REAL []                                  NOT NULL
);

CREATE SEQUENCE user_user_id_seq;
CREATE TABLE users (
  user_id  BIGINT DEFAULT NEXTVAL('user_user_id_seq') PRIMARY KEY,
  email    VARCHAR(40) NOT NULL,
  password VARCHAR(70) NOT NULL,
  role     VARCHAR(10) DEFAULT 'USER'
);

INSERT INTO users (email, password)
VALUES ('user@mail.com', '$2a$11$0kj8NQUWhDNnR61BfWvDG.US20O9fQFLsBO2EXeC6jMxDUPYRdeyu')
ON CONFLICT DO NOTHING;