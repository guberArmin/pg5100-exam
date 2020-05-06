create sequence hibernate_sequence start with 1 increment by 1;
create table copy
(
    number_of_copies bigint       not null,
    user_username    varchar(255) not null,
    item_id          bigint       not null,
    primary key (item_id, user_username)
);
create table item
(
    id          bigint       not null,
    attack      integer      not null,
    defense     integer      not null,
    description varchar(255),
    is_golden   boolean      not null,
    rarity      varchar(255) not null,
    title       varchar(128),
    primary key (id)
);
create table user_roles
(
    user_username varchar(255) not null,
    roles         varchar(255)
);
create table users
(
    username             varchar(255) not null,
    balance              double       not null,
    email                varchar(255),
    enabled              boolean      not null,
    hashed_password      varchar(255),
    last_name            varchar(128),
    name                 varchar(128),
    number_of_loot_boxes bigint,
    primary key (username)
);
create table users_owned_items
(
    owned_by_username varchar(255) not null,
    owned_items_id    bigint       not null,
    primary key (owned_by_username, owned_items_id)
);
alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table copy
    add constraint FKnxxn2qcft2l23elybmuiufni7 foreign key (user_username) references users;
alter table copy
    add constraint FKhdwwcwd5iff37fjek6cgdtxni foreign key (item_id) references item;
alter table user_roles
    add constraint FKs9rxtuttxq2ln7mtp37s4clce foreign key (user_username) references users;
alter table users_owned_items
    add constraint FKp0cfga0rr2lp2dj17c4vru2r3 foreign key (owned_items_id) references item;
alter table users_owned_items
    add constraint FK3j69vlj89rebofh1mjskypplh foreign key (owned_by_username) references users;