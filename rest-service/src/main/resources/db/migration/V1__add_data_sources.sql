
CREATE SEQUENCE user_id_seq START 1 INCREMENT 1;
CREATE TABLE app_users (
user_id INT8 PRIMARY KEY,
email VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL
);

CREATE SEQUENCE data_source_info_id_seq START 1 INCREMENT 1;
CREATE TABLE data_source_info (
data_source_info_id INT8 PRIMARY KEY,
data VARCHAR(255),
data_set VARCHAR(255),
server VARCHAR(255)
);

CREATE SEQUENCE data_meta_info_id_seq START 1 INCREMENT 1;
CREATE TABLE data_meta_info (
data_meta_info_id INT8 PRIMARY KEY,
description VARCHAR(255),
parameter_name VARCHAR(255), 
resolution INT4 NOT NULL, 
data_source_info_id INT8 NOT NULL REFERENCES DATA_SOURCE_INFO
);

CREATE SEQUENCE fetch_data_task_id_seq START 1 INCREMENT 1;
CREATE TABLE fetch_data_task (
fetch_data_task_id INT8 NOT NULL, 
date date NOT NULL, 
status VARCHAR(255), 
data_meta_info_id INT8 NOT NULL, 
PRIMARY KEY(fetch_data_task_id)
);

CREATE SEQUENCE position_id_seq START 1 INCREMENT 1;
CREATE TABLE position (
position_id INT8 NOT NULL,
altitude float8 NOT NULL,
latitude float8 NOT NULL,
longitude float8 NOT NULL,
PRIMARY KEY(position_id),
CONSTRAINT latitude_longitude_index UNIQUE (latitude, longitude)
);

CREATE SEQUENCE local_data_id_seq START 1 INCREMENT 1;
CREATE TABLE local_data (
local_data_id INT8 NOT NULL,
payload float[],
year INT4 NOT NULL,
data_meta_info_id INT8 NOT NULL,
position_id INT8 NOT NULL,
PRIMARY KEY(local_data_id),
CONSTRAINT FK_local_data_TO_data_meta_info FOREIGN KEY(data_meta_info_id) references data_meta_info,
CONSTRAINT FK_local_data_TO_position FOREIGN KEY(position_id) references position
);

CREATE TABLE user_roles (
user_id INT8 NOT NULL, 
role_name VARCHAR(255) NOT NULL,
CONSTRAINT role_id_unique_index UNIQUE (user_id, role_name),
CONSTRAINT FK_user_roles_TO_app_users FOREIGN KEY(user_id) references app_users
);

INSERT INTO data_source_info(data_source_info_id, data, data_set, server)
  VALUES(nextval('data_source_info_id_seq'), 'MERRA2_400.tavg1_2d_rad_Nx', 'M2T1NXRAD.5.12.4', 'goldsmr4');

INSERT INTO data_source_info(data_source_info_id, data, data_set, server)
  VALUES(nextval('data_source_info_id_seq'), 'MERRA2_400.tavg1_2d_slv_Nx', 'M2T1NXSLV.5.12.4', 'goldsmr4');

INSERT INTO data_meta_info(data_meta_info_id, description, parameter_name, resolution, data_source_info_id)
  VALUES (nextval('data_meta_info_id_seq'), 'Shortwave solar radiation', 'SWGDN', 60,
    (SELECT ds.data_source_info_id FROM data_source_info ds WHERE ds.data='MERRA2_400.tavg1_2d_rad_Nx'));