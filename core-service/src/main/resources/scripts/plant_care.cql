DROP KEYSPACE IF EXISTS plant_care;
CREATE KEYSPACE plant_care WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

#TODO make log_time as partitioning key because one unable to select by from to dates
CREATE TABLE IF NOT EXISTS plant_care.plant_data (
  plant_id bigint,
  data_type varchar,
  value int,
  log_time timestamp,
  PRIMARY KEY ((plant_id), log_time)
);


CREATE TABLE IF NOT EXISTS plant_care.token_blacklist (
  auth_token text PRIMARY KEY
);


CREATE TABLE IF NOT EXISTS plant_care.invalidated_tokens (
    user_id bigint PRIMARY KEY,
    timestamp bigint
);

