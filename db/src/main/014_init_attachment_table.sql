SET SEARCH_PATH TO doc_main;

CREATE TABLE "attachment"
(
    "id"                 SERIAL       NOT NULL,
    "incoming_doc_id"    BIGINT       NOT NULL,
    "alfresco_file_id"   VARCHAR(255) NOT NULL,
    "alfresco_folder_id" VARCHAR(255) NOT NULL,
    "file_type"          VARCHAR(255) NOT NULL,
    CONSTRAINT "attachment_pk" PRIMARY KEY ("id"),
    CONSTRAINT "attachment_incoming_document_fk" FOREIGN KEY ("incoming_doc_id")
        REFERENCES "incoming_document" ("id")
) INHERITS ("doc_base_table");
