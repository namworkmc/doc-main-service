--liquibase formatted sql
--changeset doc:029

SET SEARCH_PATH TO doc_main;

DROP TABLE "user_role";

INSERT INTO "department"("department_name", "created_by") VALUES ('RA', 'docadmin');

ALTER TABLE "user" DROP CONSTRAINT "user_role_check";
CREATE TYPE "doc_user_role" AS ENUM ('GIAM_DOC', 'CHUYEN_VIEN', 'TRUONG_PHONG', 'VAN_THU', 'DOC_ADMIN');
ALTER TABLE "user" ALTER COLUMN role TYPE "doc_user_role" USING role::"doc_user_role";
INSERT INTO "user"(username, password, email, full_name, role, department_id)
VALUES ('docadmin', '$2a$10$hUcP7Mhb15kVhpDP0nldcOasxMiGmjmjiyJ/qzG67rBbSSIeJV6eq', 'admin@localhost',
        'Administrator', 'DOC_ADMIN', 3);
