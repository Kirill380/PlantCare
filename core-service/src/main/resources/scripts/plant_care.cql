DROP KEYSPACE IF EXISTS plat_care;
CREATE KEYSPACE plant_care WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE plant_care.endpoint_data (
  endpoint_id varchar,
  data_type varchar,
  value int,
  log_time timestamp,
  PRIMARY KEY ((endpoint_id), log_time)
);

CREATE INDEX endpoint_data_value ON plant_care.endpoint_data(value);


