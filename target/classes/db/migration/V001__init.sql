create table user_entity (
  id bigint not null,
  external_id uuid not null,
  email character varying(60),
  created_at timestamp without time zone,
  last_name character varying(30),
  firstname character varying(30) not null,
  user_password character varying(750) not null,
  updated_at timestamp without time zone,
  username character varying(20) not null,
  constraint user_entity_pkey primary key (id),
  constraint uk_username UNIQUE (username),
  constraint uk_external_id UNIQUE (external_id)
);

create table phone (
  id bigint not null,
  phone_number character varying(20) not null,
  phone_type character varying(20) not null,
  user_id bigint not null,
  constraint phone_pkey primary key (id),
  constraint fk_user_id foreign key (user_id) references  user_entity (id),
  constraint uk_phone_number unique (phone_number)
);

create table role_entity (
  id bigint not null,
  authority character varying(15),
  constraint role_pkey primary key (id)
);

create table user_role (
  user_id bigint not null,
  role_id bigint not null,
  constraint fk_role foreign key (role_id) references role_entity (id),
  constraint fk_user foreign key (user_id) references user_entity (id),
  constraint uk_user_role unique (user_id, role_id)
);

