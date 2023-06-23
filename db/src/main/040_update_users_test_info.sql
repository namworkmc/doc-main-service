--liquibase formatted sql
--changeset doc:040

set search_path to doc_main;

update "user" set email = "user".username || '@' || 'example.com';
update "user" set full_name = "user".username;
