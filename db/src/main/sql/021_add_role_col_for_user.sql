--liquibase formatted sql
--changeset doc:021

SET SEARCH_PATH TO doc_main;

ALTER TABLE "user"
    ADD COLUMN "role" VARCHAR(255) CHECK ( "role" IN
                                           ('GIAM_DOC', 'CHUYEN_VIEN', 'TRUONG_PHONG',
                                            'VAN_THU') );

UPDATE "user" SET "role" = 'GIAM_DOC' WHERE "id" = 1;
UPDATE "user" SET "role" = 'CHUYEN_VIEN' WHERE "id" = 2;
UPDATE "user" SET "role" = 'TRUONG_PHONG' WHERE "id" = 3;
UPDATE "user" SET "role" = 'VAN_THU' WHERE "id" = 4;
UPDATE "user" SET "role" = 'GIAM_DOC' WHERE "id" = 5;
UPDATE "user" SET "role" = 'CHUYEN_VIEN' WHERE "id" = 6;
UPDATE "user" SET "role" = 'TRUONG_PHONG' WHERE "id" = 7;
UPDATE "user" SET "role" = 'VAN_THU' WHERE "id" = 8;
UPDATE "user" SET "role" = 'GIAM_DOC' WHERE "id" = 9;
UPDATE "user" SET "role" = 'CHUYEN_VIEN' WHERE "id" = 10;

ALTER TABLE "user"
    ALTER COLUMN "role" SET NOT NULL;
