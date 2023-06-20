--liquibase formatted sql
--changeset doc:037

SET SEARCH_PATH TO doc_main;

UPDATE "incoming_document" SET "folder_id" = 1
