DROP SCHEMA IF EXISTS doc_main CASCADE;

CREATE SCHEMA doc_main;
SET SEARCH_PATH TO doc_main;

CREATE TYPE "request_status" AS ENUM ('PENDING', 'APPROVED', 'REJECTED');
CREATE TYPE "urgency" AS ENUM ('LOW', 'MEDIUM', 'HIGH');
CREATE TYPE "confidentiality" AS ENUM ('LOW', 'MEDIUM', 'HIGH');
CREATE TYPE "processing_status" AS ENUM ('IN_PROGRESS', 'CLOSED');
CREATE TYPE "outgoing_document_status" AS ENUM ('UNPROCESSED', 'IN_PROGRESS', 'WAITING_FOR_OUTGOING_NUMBER', 'READY_TO_RELEASE', 'RELEASED');

CREATE TABLE "doc_base_table"
(
    "version"      BIGINT    NOT NULL DEFAULT 0,
    "created_date" TIMESTAMP NOT NULL DEFAULT NOW(),
    "created_by"   VARCHAR(255),
    "updated_date" TIMESTAMP NOT NULL DEFAULT NOW(),
    "updated_by"   VARCHAR(255)
);

CREATE TABLE "processing_document_role"
(
    "id"   SERIAL      NOT NULL,
    "name" VARCHAR(20) NOT NULL UNIQUE CHECK ( "name" IN ('APPROVER', 'REVIEWER', 'SUBMITTER',
                                                          'COLLABORATOR') ),
    CONSTRAINT "processing_document_role_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "doc_system_role"
(
    "id"   SERIAL      NOT NULL,
    "name" VARCHAR(20) NOT NULL UNIQUE CHECK ( "name" IN ('DIRECTOR', 'EXPERT', 'MANAGER', 'STAFF') ),
    CONSTRAINT "doc_system_role_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "user"
(
    "id"         SERIAL       NOT NULL,
    "first_name" VARCHAR(255),
    "last_name"  VARCHAR(255),
    "username"   VARCHAR(255) NOT NULL UNIQUE,
    "password"   VARCHAR(255) NOT NULL,
    "email"      VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT "user_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "user_role"
(
    "role_id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    CONSTRAINT "user_role_pk" PRIMARY KEY ("role_id", "user_id")
) INHERITS ("doc_base_table");

CREATE TABLE "incoming_document"
(
    "id"                     SERIAL            NOT NULL,
    "incoming_number"        VARCHAR(255)      NOT NULL,
    "document_type_id"       BIGINT            NOT NULL,
    "original_symbol_number" VARCHAR(255)      NOT NULL,
    "distribution_org_id"    BIGINT            NOT NULL,
    "distribution_date"      DATE              NOT NULL,
    "arriving_date"          DATE              NOT NULL,
    "arriving_time"          TIME              NOT NULL,
    "comment"                VARCHAR(255)      NOT NULL,
    "urgency"                "urgency"         NOT NULL,
    "confidentiality"        "confidentiality" NOT NULL,
    "folder"                 VARCHAR(255)      NOT NULL,
    "sending_level_id"       BIGINT            NOT NULL,
    "is_deleted"             BOOL              NOT NULL DEFAULT FALSE,
    CONSTRAINT "incoming_document_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "document_type"
(
    "id"   SERIAL       NOT NULL,
    "type" VARCHAR(255) NOT NULL,
    CONSTRAINT "document_type_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "processing_document"
(
    "id"                  SERIAL              NOT NULL,
    "incoming_doc_id"     BIGINT              NOT NULL,
    "status"              "processing_status" NOT NULL,
    "is_opened"           BOOL                NOT NULL,
    "processing_duration" DATE,
    "processing_request"  VARCHAR(255)        NOT NULL,
    CONSTRAINT "processing_document_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "outgoing_document"
(
    "id"               SERIAL                     NOT NULL,
    "document_type_id" BIGINT                     NOT NULL,
    "original_symbol_number" VARCHAR(255)         NOT NULL,
    "urgency"          "urgency"                  NOT NULL,
    "confidentiality"  "confidentiality"          NOT NULL,
    "summary"          VARCHAR(255)               NOT NULL,
    "outgoing_number"  VARCHAR(255),
    "recipient"        VARCHAR(255),
    "release_date"     DATE,
    "status"           "outgoing_document_status" NOT NULL,
    CONSTRAINT "outgoing_document_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "linked_document"
(
    "incoming_doc_id" BIGINT NOT NULL,
    "outgoing_doc_id" BIGINT NOT NULL,
    CONSTRAINT "linked_document_pk" PRIMARY KEY ("incoming_doc_id", "outgoing_doc_id")
) INHERITS ("doc_base_table");

CREATE TABLE "distribution_organization"
(
    "id"     SERIAL       NOT NULL,
    "name"   VARCHAR(255) NOT NULL,
    "symbol" VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT "distribution_organization_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "sending_level"
(
    "id"    SERIAL       NOT NULL,
    "level" VARCHAR(255) NOT NULL,
    CONSTRAINT "sending_level_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "processing_user"
(
    "user_id"           BIGINT NOT NULL,
    "processing_doc_id" BIGINT NOT NULL,
    "step"              INT    NOT NULL CHECK ( "step" > 0 ),
    CONSTRAINT "processing_user_pk" PRIMARY KEY ("user_id", "processing_doc_id", "step")
) INHERITS ("doc_base_table");

CREATE TABLE "processed_document"
(
    "id"              SERIAL NOT NULL,
    "incoming_doc_id" BIGINT NOT NULL,
    CONSTRAINT "processed_document_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "return_request"
(
    "id"                SERIAL           NOT NULL,
    "user_id"           BIGINT           NOT NULL,
    "processing_doc_id" BIGINT           NOT NULL,
    "step"              INT              NOT NULL CHECK ( "step" > 0 ),
    "reason"            VARCHAR(200)     NOT NULL,
    "status"            "request_status" NOT NULL,
    CONSTRAINT "return_request_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "extension_request"
(
    "id"                SERIAL           NOT NULL,
    "processing_doc_id" BIGINT           NOT NULL,
    "reason"            VARCHAR(200)     NOT NULL,
    "extended_until"    DATE             NOT NULL,
    "status"            "request_status" NOT NULL,
    CONSTRAINT "extension_request_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

CREATE TABLE "processing_user_role"
(
    "user_id"            BIGINT NOT NULL,
    "processing_doc_id"  BIGINT NOT NULL,
    "step"               INT    NOT NULL CHECK ( "step" > 0 ),
    "processing_role_id" BIGINT NOT NULL,
    CONSTRAINT "processing_user_role_pk" PRIMARY KEY ("user_id", "processing_doc_id", "step",
                                                      "processing_role_id")
) INHERITS ("doc_base_table");

CREATE TABLE "processing_flow"
(
    "flow_version" BIGINT         NOT NULL CHECK ( "flow_version" > 0 ),
    "doc_type_id"  BIGINT         NOT NULL,
    "flow"         VARCHAR(255)[] NOT NULL,
    CONSTRAINT "processing_flow_pk" PRIMARY KEY ("flow_version", "doc_type_id")
) INHERITS ("doc_base_table");

CREATE TABLE "feedback"
(
    "id"                SERIAL       NOT NULL,
    "processing_doc_id" BIGINT       NOT NULL,
    "content"           VARCHAR(200) NOT NULL,
    CONSTRAINT "feedback_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

-- user_role table
ALTER TABLE "user_role"
    ADD CONSTRAINT "user_role_doc_system_role_fk" FOREIGN KEY ("role_id") REFERENCES "doc_system_role" ("id");
ALTER TABLE "user_role"
    ADD CONSTRAINT "user_role_user_fk" FOREIGN KEY ("user_id") REFERENCES "user" ("id");

-- incoming_document table
ALTER TABLE "incoming_document"
    ADD CONSTRAINT "incoming_document_document_type_fk" FOREIGN KEY ("document_type_id") REFERENCES "document_type" ("id");
ALTER TABLE "incoming_document"
    ADD CONSTRAINT "incoming_document_distribution_organization_fk" FOREIGN KEY ("distribution_org_id") REFERENCES "distribution_organization" ("id");
ALTER TABLE "incoming_document"
    ADD CONSTRAINT "incoming_document_sending_level_fk" FOREIGN KEY ("sending_level_id") REFERENCES "sending_level" ("id");

-- processing_document table
ALTER TABLE "processing_document"
    ADD CONSTRAINT "processing_document_incoming_document_fk" FOREIGN KEY ("incoming_doc_id") REFERENCES "incoming_document" ("id");

-- linked_document table
ALTER TABLE "linked_document"
    ADD CONSTRAINT "linked_document_incoming_document_fk" FOREIGN KEY ("incoming_doc_id") REFERENCES "incoming_document" ("id");
ALTER TABLE "linked_document"
    ADD CONSTRAINT "linked_document_outgoing_document_fk" FOREIGN KEY ("outgoing_doc_id") REFERENCES "outgoing_document" ("id");

-- processing_user table
ALTER TABLE "processing_user"
    ADD CONSTRAINT "processing_user_user_fk" FOREIGN KEY ("user_id") REFERENCES "user" ("id");
ALTER TABLE "processing_user"
    ADD CONSTRAINT "processing_user_processing_document_fk" FOREIGN KEY ("processing_doc_id") REFERENCES "processing_document" ("id");

-- processed_document table
ALTER TABLE "processed_document"
    ADD CONSTRAINT "processed_document_incoming_document_fk" FOREIGN KEY ("incoming_doc_id") REFERENCES "incoming_document" ("id");

-- return_request table
ALTER TABLE "return_request"
    ADD CONSTRAINT "return_request_processing_user_fk"
        FOREIGN KEY ("user_id", "processing_doc_id", "step")
            REFERENCES "processing_user" ("user_id", "processing_doc_id", "step");

-- extension_request table
ALTER TABLE "extension_request"
    ADD CONSTRAINT "extension_request_processing_document_fk" FOREIGN KEY ("processing_doc_id") REFERENCES "processing_document" ("id");

-- processing_user_role table
ALTER TABLE "processing_user_role"
    ADD CONSTRAINT "processing_user_role_processing_user_fk"
        FOREIGN KEY ("user_id", "processing_doc_id", "step")
            REFERENCES "processing_user" ("user_id", "processing_doc_id", "step");
ALTER TABLE "processing_user_role"
    ADD CONSTRAINT "processing_user_role_processing_document_role_fk"
        FOREIGN KEY ("processing_role_id")
            REFERENCES "processing_document_role" ("id");

-- processing_flow table
ALTER TABLE "processing_flow"
    ADD CONSTRAINT "processing_flow_document_type_fk" FOREIGN KEY ("doc_type_id") REFERENCES "document_type" ("id");

-- feedback table
ALTER TABLE "feedback"
    ADD CONSTRAINT "feedback_processing_document_fk" FOREIGN KEY ("processing_doc_id") REFERENCES "processing_document" ("id");
