create sequence hibernate_sequence start 1 increment 1;
create table honey
(
    id         int8 not null,
    uuid       varchar(255),
    version    int8 not null,
    amount     int4,
    available  int8,
    cover_id   int8,
    created_at timestamp,
    name       varchar(255),
    price      numeric(19, 2),
    updated_at timestamp,
    primary key (id)
);
create table honey_manufacturers
(
    honeys_id        int8 not null,
    manufacturers_id int8 not null,
    primary key (honeys_id, manufacturers_id)
);
create table manufacturer
(
    id         int8 not null,
    uuid       varchar(255),
    version    int8 not null,
    created_at timestamp,
    first_name varchar(255),
    last_name  varchar(255),
    primary key (id)
);
create table order_item
(
    id         int8 not null,
    uuid       varchar(255),
    version    int8 not null,
    created_at timestamp,
    quantity   int4 not null,
    updated_at timestamp,
    honey_id   int8,
    order_id   int8,
    primary key (id)
);
create table orders
(
    id           int8 not null,
    uuid         varchar(255),
    version      int8 not null,
    created_at   timestamp,
    delivery     varchar(255),
    status       varchar(255),
    updated_at   timestamp,
    recipient_id int8,
    primary key (id)
);
create table recipient
(
    id       int8 not null,
    uuid     varchar(255),
    version  int8 not null,
    city     varchar(255),
    email    varchar(255),
    name     varchar(255),
    phone    varchar(255),
    street   varchar(255),
    zip_code varchar(255),
    primary key (id)
);
create table upload
(
    id           int8 not null,
    uuid         varchar(255),
    version      int8 not null,
    content_type varchar(255),
    created_at   timestamp,
    file         bytea,
    filename     varchar(255),
    primary key (id)
);
create table user_roles
(
    user_id int8 not null,
    role    varchar(255)
);
create table users
(
    id         int8 not null,
    uuid       varchar(255),
    version    int8 not null,
    created_at timestamp,
    password   varchar(255),
    updated_at timestamp,
    username   varchar(255),
    primary key (id)
);
alter table if exists honey
    add constraint UK_9f08tpgultynopirae2sw3nno unique (name);
alter table if exists honey_manufacturers
    add constraint FKrgjy5bnwfyplraf4c1htnkypf foreign key (manufacturers_id) references manufacturer;
alter table if exists honey_manufacturers
    add constraint FK4vjr481bxya46ghouoh12huu3 foreign key (honeys_id) references honey;
alter table if exists order_item
    add constraint FKq8vkyomo8j2yk8d8tb45j32k5 foreign key (honey_id) references honey;
alter table if exists order_item
    add constraint FKt4dc2r9nbvbujrljv3e23iibt foreign key (order_id) references orders;
alter table if exists orders
    add constraint FKcxwo1jbmo15jih4b5qjclvye8 foreign key (recipient_id) references recipient;
alter table if exists user_roles
    add constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users;
