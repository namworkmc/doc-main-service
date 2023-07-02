--liquibase formatted sql
--changeset doc:044

SET SEARCH_PATH TO doc_main;

ALTER TABLE "transfer_history"
    ADD COLUMN "processing_method_id" BIGINT
        CONSTRAINT "transfer_history_processing_method_fk" REFERENCES "transfer_history" (id);

UPDATE "transfer_history" SET "processing_method_id" = 1 WHERE "process_method" = 'Báo cáo kết quả thực hiện';
UPDATE "transfer_history" SET "processing_method_id" = 2 WHERE "process_method" = 'Lưu tham khảo';
UPDATE "transfer_history" SET "processing_method_id" = 3 WHERE "process_method" = 'Soạn văn bản trả lời';

ALTER TABLE "transfer_history"
    DROP COLUMN "process_method";
