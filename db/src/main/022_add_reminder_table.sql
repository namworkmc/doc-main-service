SET SEARCH_PATH TO doc_main;

ALTER TABLE "doc_base_table"
    ADD COLUMN "is_deleted" BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TYPE document_reminder_status AS ENUM ('ACTIVE','CLOSE_TO_EXPIRATION','EXPIRED');

CREATE TABLE "document_reminder"
(
    "id"                SERIAL                   NOT NULL,
    "processing_doc_id" BIGINT                   NOT NULL REFERENCES "processing_document" ("id"),
    "execution_time"    TIMESTAMP                NOT NULL,
    "expiration_date"   DATE                     NOT NULL,
    "status"            document_reminder_status NOT NULL,
    "is_opened"         BOOL                     NOT NULL DEFAULT FALSE,
    CONSTRAINT "document_reminder_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");
