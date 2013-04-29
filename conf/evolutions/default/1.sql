# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table dioptrie (
  id                        bigint not null,
  details                   varchar(255),
  date                      timestamp,
  user_login                varchar(255),
  constraint pk_dioptrie primary key (id))
;

create table exam (
  id                        bigint not null,
  findings                  varchar(255),
  create                    timestamp,
  user_login                varchar(255),
  constraint pk_exam primary key (id))
;

create table order_table (
  id                        bigint not null,
  date                      timestamp,
  user_login                varchar(255),
  constraint pk_order_table primary key (id))
;

create table user (
  login                     varchar(255) not null,
  name                      varchar(255),
  surname                   varchar(255),
  titul                     varchar(255),
  address                   varchar(255),
  password                  varchar(255),
  role                      integer,
  constraint ck_user_role check (role in (0,1)),
  constraint pk_user primary key (login))
;

create sequence dioptrie_seq;

create sequence exam_seq;

create sequence order_table_seq;

create sequence user_seq;

alter table dioptrie add constraint fk_dioptrie_user_1 foreign key (user_login) references user (login) on delete restrict on update restrict;
create index ix_dioptrie_user_1 on dioptrie (user_login);
alter table exam add constraint fk_exam_user_2 foreign key (user_login) references user (login) on delete restrict on update restrict;
create index ix_exam_user_2 on exam (user_login);
alter table order_table add constraint fk_order_table_user_3 foreign key (user_login) references user (login) on delete restrict on update restrict;
create index ix_order_table_user_3 on order_table (user_login);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists dioptrie;

drop table if exists exam;

drop table if exists order_table;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists dioptrie_seq;

drop sequence if exists exam_seq;

drop sequence if exists order_table_seq;

drop sequence if exists user_seq;

