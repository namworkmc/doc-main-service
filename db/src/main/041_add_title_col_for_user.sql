--liquibase formatted sql
--changeset doc:041

set search_path to doc_main;

-- update GIAM_DOC TO HIEU_TRUONG
alter type doc_user_role rename value 'GIAM_DOC' to 'HIEU_TRUONG';

alter table "user" add column "role_title" varchar(255);
update "user" set role_title = 'Hiệu trưởng' where role = 'HIEU_TRUONG';
update "user" set role_title = 'Chuyên viên' where role = 'CHUYEN_VIEN';
update "user" set role_title = 'Trưởng phòng' where role = 'TRUONG_PHONG';
update "user" set role_title = 'Văn thư' where role = 'VAN_THU';
update "user" set role_title = 'Adminitrator' where role = 'DOC_ADMIN';
alter table "user" alter column "role_title" set not null;
