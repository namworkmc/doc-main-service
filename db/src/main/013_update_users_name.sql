--liquibase formatted sql
--changeset doc:013

SET SEARCH_PATH TO doc_main;

ALTER TABLE "user" ADD COLUMN "full_name" VARCHAR(255);
ALTER TABLE "user" DROP COLUMN "first_name";
ALTER TABLE "user" DROP COLUMN "last_name";

UPDATE "user" SET "full_name" = 'John Doe' WHERE id = 1;
UPDATE "user" SET "full_name" = 'Jane Doe' WHERE id = 2;
UPDATE "user" SET "full_name" = 'Jack Doe' WHERE id = 3;
UPDATE "user" SET "full_name" = 'Jill Doe' WHERE id = 4;
