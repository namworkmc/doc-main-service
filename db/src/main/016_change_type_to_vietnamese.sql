--liquibase formatted sql
--changeset doc:016

SET SEARCH_PATH TO doc_main;

UPDATE "document_type" SET "type" = 'Hợp đồng' WHERE "type" = 'CONTRACT';
UPDATE "document_type" SET "type" = 'Hoá đơn' WHERE "type" = 'INVOICE';
UPDATE "document_type" SET "type" = 'Phiếu thu' WHERE "type" = 'PAYMENT';
UPDATE "document_type" SET "type" = 'Phiếu chi' WHERE "type" = 'RECEIPT';
UPDATE "document_type" SET "type" = 'Khác' WHERE "type" = 'OTHER';
