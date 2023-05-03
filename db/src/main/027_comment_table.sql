SET SEARCH_PATH TO doc_main;

CREATE TABLE "comment"
(
    "id"                   SERIAL
        CONSTRAINT "comment_pk" PRIMARY KEY,
    "content"              TEXT   NOT NULL,
    "incoming_document_id" BIGINT NOT NULL
        CONSTRAINT "comment_incoming_document_id_fk" REFERENCES "incoming_document" ("id")
) INHERITS ("doc_base_table");
