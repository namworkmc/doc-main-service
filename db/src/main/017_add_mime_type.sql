--liquibase formatted sql
--changeset doc:017

SET SEARCH_PATH TO doc_main;

CREATE TYPE "file_type" AS ENUM ('application/pdf', 'image/png', 'image/jpeg');

-- change from varchar to enum
ALTER TABLE "attachment" ALTER COLUMN "file_type" TYPE "file_type" USING "file_type"::"file_type";
