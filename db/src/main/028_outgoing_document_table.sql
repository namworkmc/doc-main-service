--liquibase formatted sql
--changeset doc:028

SET SEARCH_PATH TO doc_main;

ALTER TABLE "outgoing_document"
    ADD COLUMN "publishing_department_id" BIGINT
        CONSTRAINT "outgoing_document_publishing_department_fk" REFERENCES "department" ("id");
ALTER TABLE "outgoing_document"
    ADD COLUMN "folder_id" BIGINT
        CONSTRAINT "outgoing_document_folder_fk" REFERENCES "folder" ("id");

ALTER TABLE "incoming_document"
    ADD COLUMN "publishing_department_id" BIGINT
        CONSTRAINT "incoming_document_publishing_department_fk" REFERENCES "department" ("id");

ALTER TABLE "processing_document"
    ADD COLUMN "outgoing_document_id" BIGINT
        CONSTRAINT "processed_document_outgoing_document_fk" REFERENCES "outgoing_document" ("id");
ALTER TABLE "processing_document"
    ALTER COLUMN "incoming_doc_id" DROP NOT NULL;

ALTER TABLE "attachment"
    ADD COLUMN "outgoing_document_id" BIGINT
        CONSTRAINT "attachment_outgoing_document_fk" REFERENCES "outgoing_document" ("id");
ALTER TABLE "attachment"
    ALTER COLUMN "incoming_doc_id" DROP NOT NULL;

ALTER TABLE "comment" ADD COLUMN "outgoing_document_id" BIGINT
    CONSTRAINT "comment_outgoing_document_fk" REFERENCES "outgoing_document" ("id");
ALTER TABLE "comment" ALTER COLUMN "incoming_document_id" DROP NOT NULL;

ALTER TABLE "outgoing_document" ADD CONSTRAINT "outgoing_document_document_type_fk" FOREIGN KEY ("document_type_id") REFERENCES "document_type" ("id");
update outgoing_document
set folder_id = 2 where id is not null;

update outgoing_document
set publishing_department_id = 1 where id is not null;
