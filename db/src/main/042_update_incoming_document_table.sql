--liquibase formatted sql
--changeset doc:042

SET SEARCH_PATH TO doc_main;

ALTER TABLE "incoming_document"
    ADD COLUMN "close_date" DATE;

ALTER TABLE "incoming_document"
    ADD COLUMN "close_username" VARCHAR(255);

