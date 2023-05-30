--liquibase formatted sql
--changeset doc:006

SET SEARCH_PATH TO doc_main;

ALTER TABLE "incoming_document" RENAME "comment" TO "summary";
