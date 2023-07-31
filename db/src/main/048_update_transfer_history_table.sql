--liquibase formatted sql
--changeset doc:048

SET SEARCH_PATH TO doc_main;

ALTER TABLE "transfer_history"
    ADD COLUMN "is_read" BOOLEAN NOT NULL DEFAULT FALSE;


