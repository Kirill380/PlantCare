DROP KEYSPACE IF EXISTS plat_care;
CREATE KEYSPACE plant_care WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE plant_care.endpoint_data (
  endpoint_id varchar PRIMARY KEY,
  data_type varchar,
  value int,
  log_time timestamp
);



