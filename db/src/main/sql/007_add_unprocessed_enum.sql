--liquibase formatted sql
--changeset doc:007

SET SEARCH_PATH TO doc_main;

ALTER TYPE "processing_status" ADD VALUE 'UNPROCESSED';
