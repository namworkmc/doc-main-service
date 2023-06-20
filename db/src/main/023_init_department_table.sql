--liquibase formatted sql
--changeset doc:023

SET SEARCH_PATH TO doc_main;

CREATE TABLE "department"
(
    "id"              SERIAL       NOT NULL,
    "department_name" VARCHAR(255) NOT NULL,
    CONSTRAINT "department_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

INSERT INTO "department" ("department_name", "created_by")
VALUES ('Ban Chỉ Huy', 'docadmin'),
       ('Phòng kế hoạch đầu tư', 'docadmin');

ALTER TABLE "user"
    ADD COLUMN "department_id" BIGINT;
ALTER TABLE "user"
    ADD CONSTRAINT "user_department_fk" FOREIGN KEY ("department_id") REFERENCES "department" ("id");

UPDATE "user" SET department_id = 1;

UPDATE "user"
SET department_id = 1
WHERE username = 'user3';

UPDATE "user"
SET department_id = 2
WHERE username = 'user7';

UPDATE "user" SET department_id = 2 WHERE id = 1;
UPDATE "user" SET department_id = 2 WHERE id = 2;
UPDATE "user" SET department_id = 2 WHERE id = 3;
UPDATE "user" SET department_id = 2 WHERE id = 4;
