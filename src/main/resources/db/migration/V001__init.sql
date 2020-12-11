create table profession (
  id bigint not null,
  description character varying(255) not null,
  CONSTRAINT profession_pkey primary key (id)
);

create table user_entity (
  id bigint not null,
  date_of_birth date,
  user_uuid uuid not null,
  email character varying(60),
  profession_id bigint,
  salary numeric (19, 2),
  created timestamp without time zone,
  last_name character varying(30),
  name_user character varying(30) not null,
  password_user character varying(750) not null,
  updated timestamp without time zone,
  username character varying(20) not null,
  constraint user_entity_pkey primary key (id),
  constraint uk_username UNIQUE (username),
  constraint uk_user_uuid UNIQUE (user_uuid),
  constraint fk_profession foreign key (profession_id) references profession (id)
);

create table phone (
  id bigint not null,
  number_user character varying(255) not null,
  type_number character varying(255) not null,
  owner_id bigint,
  constraint phone_pkey primary key (id),
  constraint fk_owner_id foreign key (owner_id) references  user_entity (id),
  constraint uk_number_user unique (number_user)
);

create table role (
  id bigint not null,
  authority character varying(255),
  constraint role_pkey primary key (id)
);

create table user_entity_role (
  user_entity_id bigint not null,
  role_id bigint not null,
  constraint role_fk foreign key (role_id) references role (id),
  constraint user_entity_fk foreign key (user_entity_id) references user_entity (id),
  constraint uk_role_id unique (role_id),
  constraint unique_role_user_entity unique (user_entity_id, role_id)
);

