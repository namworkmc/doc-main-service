SET SEARCH_PATH TO doc_main;

ALTER TABLE "document_reminder"
    DROP COLUMN "processing_doc_id";
ALTER TABLE "document_reminder"
    ADD COLUMN "processing_user_id" BIGINT NOT NULL DEFAULT NULL;
ALTER TABLE "document_reminder"
    ADD CONSTRAINT "document_reminder_processing_user_id_fkey" FOREIGN KEY ("processing_user_id") REFERENCES "processing_user" ("id");
ALTER TABLE "document_reminder" DROP COLUMN "execution_time";
