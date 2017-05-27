DROP KEYSPACE IF EXISTS plat_care;
CREATE KEYSPACE plant_care WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

#TODO make log_time as partitioning key because one unable to select by from to dates
CREATE TABLE plant_care.sensor_data (
  sensor_id varchar,
  data_type varchar,
  value int,
  log_time timestamp,
  PRIMARY KEY ((endpoint_id), log_time)
);


CREATE TABLE plant_care.token_blacklist (
  token varchar PRIMARY KEY
);

CREATE INDEX endpoint_data_value ON plant_care.endpoint_data(value);


