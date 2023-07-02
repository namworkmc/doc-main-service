--liquibase formatted sql
--changeset doc:043

SET SEARCH_PATH TO doc_main;

CREATE TABLE "processing_method"
(
    "id"                SERIAL                   NOT NULL,
    "name"              VARCHAR(255)             NOT NULL UNIQUE,
    CONSTRAINT "processing_method_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

INSERT INTO "processing_method" ("name") VALUES ('Báo cáo kết quả thực hiện');
INSERT INTO "processing_method" ("name") VALUES ('Lưu tham khảo');
INSERT INTO "processing_method" ("name") VALUES ('Soạn văn bản trả lời');

ALTER TABLE "processing_user"
    ADD COLUMN "processing_method_id" BIGINT
        CONSTRAINT "processing_user_processing_method_fk" REFERENCES "processing_method" (id);

UPDATE "processing_user" SET "processing_method_id" = 1 WHERE "process_method" = 'Báo cáo kết quả thực hiện';
UPDATE "processing_user" SET "processing_method_id" = 2 WHERE "process_method" = 'Lưu tham khảo';
UPDATE "processing_user" SET "processing_method_id" = 3 WHERE "process_method" = 'Soạn văn bản trả lời';

ALTER TABLE "processing_user"
    DROP COLUMN "process_method";
