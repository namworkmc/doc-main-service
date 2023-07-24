--liquibase formatted sql
--changeset doc:045

set search_path to doc_main;

create table firebase_token
(
    id      serial primary key,
    token   varchar(255) not null
        constraint unique_token unique,
    type    varchar(255) not null check ( type in ('FCM') ),
    user_id bigint
) inherits (doc_base_table);
