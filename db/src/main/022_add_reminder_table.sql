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

INSERT INTO "incoming_document"(created_by, updated_by, incoming_number, document_type_id,
                                original_symbol_number, distribution_org_id, distribution_date,
                                arriving_date, arriving_time, summary, urgency, confidentiality,
                                sending_level_id, folder_id)
VALUES (1, 1, '0987654321', 1, '0987654321', 1, NOW() + INTERVAL '30 DAY', NOW(), NOW(), 'test',
        'LOW', 'LOW', 1, 1);

INSERT INTO "processing_document"(created_by, updated_by, incoming_doc_id, status, is_opened,
                                  processing_duration, processing_request)
VALUES (1, 1, 1025, 'IN_PROGRESS', TRUE, NOW() + INTERVAL '7 DAY', 'test');

INSERT INTO "processing_user"(created_by, updated_by, user_id, processing_doc_id, step,
                              return_request_id)
VALUES (1, 1, 1, 13, 1, NULL);

INSERT INTO "processing_user"(created_by, updated_by, user_id, processing_doc_id, step,
                              return_request_id)
VALUES (1, 1, 1, 13, 2, NULL);

INSERT INTO "processing_user"(created_by, updated_by, user_id, processing_doc_id, step,
                              return_request_id)
VALUES (1, 1, 1, 13, 3, NULL);

INSERT INTO "processing_user_role"(created_by, updated_by, role_name, processing_user_id)
VALUES (1, 1, 'COLLABORATOR', 13);

INSERT INTO "processing_user_role"(created_by, updated_by, role_name, processing_user_id)
VALUES (1, 1, 'COLLABORATOR', 14);

INSERT INTO "processing_user_role"(created_by, updated_by, role_name, processing_user_id)
VALUES (1, 1, 'COLLABORATOR', 15);

INSERT INTO "document_reminder"(created_by, updated_by, processing_doc_id, execution_time,
                                expiration_date, status, is_opened)
VALUES (1, 1, 13, NOW() + INTERVAL '7 DAY', NOW() + INTERVAL '7 DAY', 'ACTIVE', false);

INSERT INTO "document_reminder"(created_by, updated_by, processing_doc_id, execution_time,
                                expiration_date, status, is_opened)
VALUES (1, 1, 13, NOW() + INTERVAL '7 DAY', NOW() + INTERVAL '7 DAY', 'CLOSE_TO_EXPIRATION', false);

INSERT INTO "document_reminder"(created_by, updated_by, processing_doc_id, execution_time,
                                expiration_date, status, is_opened)
VALUES (1, 1, 13, NOW() + INTERVAL '8 DAY', NOW() + INTERVAL '8 DAY', 'ACTIVE', false);

INSERT INTO "document_reminder"(created_by, updated_by, processing_doc_id, execution_time,
                                expiration_date, status, is_opened)
VALUES (1, 1, 13, NOW() + INTERVAL '8 DAY', NOW() + INTERVAL '8 DAY', 'CLOSE_TO_EXPIRATION', false);

INSERT INTO "document_reminder"(created_by, updated_by, processing_doc_id, execution_time,
                                expiration_date, status, is_opened)
VALUES (1, 1, 13, NOW() + INTERVAL '9 DAY', NOW() + INTERVAL '9 DAY', 'ACTIVE', false);

INSERT INTO "document_reminder"(created_by, updated_by, processing_doc_id, execution_time,
                                expiration_date, status, is_opened)
VALUES (1, 1, 13, NOW() + INTERVAL '9 DAY', NOW() + INTERVAL '9 DAY', 'EXPIRED', false);

INSERT INTO "document_reminder"(created_by, updated_by, processing_doc_id, execution_time,
                                expiration_date, status, is_opened)
VALUES (1, 1, 13, NOW() + INTERVAL '10 DAY', NOW() + INTERVAL '10 DAY', 'ACTIVE', false);

INSERT INTO "document_reminder"(created_by, updated_by, processing_doc_id, execution_time,
                                expiration_date, status, is_opened)
VALUES (1, 1, 13, NOW() + INTERVAL '10 DAY', NOW() + INTERVAL '10 DAY', 'EXPIRED', false);
