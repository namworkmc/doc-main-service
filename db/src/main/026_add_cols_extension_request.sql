SET SEARCH_PATH TO doc_main;

ALTER TABLE "extension_request"
    ADD COLUMN "validated_by" BIGINT;
ALTER TABLE "extension_request"
    ADD COLUMN "old_expired_date" DATE NOT NULL DEFAULT CURRENT_DATE;
ALTER TABLE "extension_request"
    RENAME COLUMN "processing_doc_id" TO "processing_user_id";
ALTER TABLE "extension_request"
    DROP CONSTRAINT "extension_request_processing_document_fk";
ALTER TABLE "extension_request"
    ADD CONSTRAINT "extension_request_processing_user_fk" FOREIGN KEY ("processing_user_id") REFERENCES "processing_user" ("id");
