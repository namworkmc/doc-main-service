--liquibase formatted sql
--changeset doc:032

SET SEARCH_PATH TO doc_main;

ALTER TABLE OUTGOING_DOCUMENT
    ADD COLUMN signer VARCHAR(255);
