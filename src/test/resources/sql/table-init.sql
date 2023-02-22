create schema if not exists test authorization test;

create table if not exists test.user_entity(
  id serial primary key ,
  name varchar,
  email varchar ,
  password varchar,
  logo varchar,
  role_permission_entities_id integer,
  created_by varchar,
  update_by varchar,
  created_date timestamp,
  updated_date timestamp
);