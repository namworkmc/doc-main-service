--liquibase formatted sql
--changeset doc:025

SET SEARCH_PATH TO doc_main;

ALTER TABLE processing_user
    ADD processing_duration date NULL;

ALTER TABLE processing_document
    DROP COLUMN processing_duration;
