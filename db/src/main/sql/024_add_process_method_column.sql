--liquibase formatted sql
--changeset doc:024

SET SEARCH_PATH TO doc_main;

CREATE TYPE "process_method" AS ENUM ('Báo cáo kết quả thực hiện', 'Lưu tham khảo', 'Soạn văn bản trả lời');

ALTER TABLE doc_main.processing_user ADD "process_method" doc_main."process_method";

