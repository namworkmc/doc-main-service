--liquibase formatted sql
--changeset doc:011

SET SEARCH_PATH TO doc_main;

ALTER TABLE "incoming_document" DROP COLUMN "folder";

ALTER TABLE "incoming_document" ADD COLUMN "folder_id" bigint;
ALTER TABLE "incoming_document" ADD CONSTRAINT "incoming_document_folder_id_fkey" FOREIGN KEY ("folder_id") REFERENCES "folder" ("id") ON DELETE SET NULL;

UPDATE "incoming_document" SET "folder_id" = 1 WHERE "folder_id" IS NULL;
