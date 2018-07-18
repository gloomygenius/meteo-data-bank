CREATE SEQUENCE update_process_id_seq;
CREATE TABLE update_process (
  update_process_id BIGINT DEFAULT NEXTVAL('update_process_id_seq') PRIMARY KEY,
  user_id           BIGINT REFERENCES users (user_id)     NOT NULL,
  date              TIMESTAMP                             NOT NULL,
  status            VARCHAR(15)                           NOT NULL,
  description       TEXT                                  NOT NULL
);