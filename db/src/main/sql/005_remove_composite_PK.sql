--liquibase formatted sql
--changeset doc:005

SET SEARCH_PATH TO doc_main;

-- processing_user_role table
ALTER TABLE processing_user_role
    DROP CONSTRAINT processing_user_role_pk;
ALTER TABLE processing_user_role
    DROP CONSTRAINT processing_user_role_processing_user_fk;
ALTER TABLE processing_user_role
    ADD COLUMN id SERIAL
        CONSTRAINT processing_user_role_pk PRIMARY KEY;

-- return_request table
ALTER TABLE return_request
    DROP CONSTRAINT return_request_processing_user_fk;

-- processing_user table
ALTER TABLE processing_user
    DROP CONSTRAINT processing_user_pk;
ALTER TABLE processing_user
    ADD COLUMN id SERIAL
        CONSTRAINT processing_user_pk PRIMARY KEY;
ALTER TABLE processing_user
    ADD COLUMN return_request_id BIGINT
        CONSTRAINT processing_user_return_request_fk REFERENCES return_request (id);

ALTER TABLE processing_user_role
    ADD COLUMN processing_user_id BIGINT
        CONSTRAINT processing_user_role_processing_user_fk REFERENCES processing_user (id);

-- Migrating data from processing_user to processing_user_role
UPDATE processing_user_role
SET processing_user_id = processing_user.id
FROM processing_user
WHERE processing_user.user_id = processing_user_role.user_id
  AND processing_user.processing_doc_id = processing_user_role.processing_doc_id
  AND processing_user.step = processing_user_role.step;

-- Migrating data from return_request to processing_user
UPDATE processing_user
SET return_request_id = return_request.id
FROM return_request
WHERE return_request.user_id = processing_user.user_id
  AND return_request.processing_doc_id = processing_user.processing_doc_id
  AND return_request.step = processing_user.step;

ALTER TABLE processing_user_role
    DROP user_id,
    DROP processing_doc_id,
    DROP step;
ALTER TABLE return_request
    DROP user_id,
    DROP processing_doc_id,
    DROP step;

-- user_role table
ALTER TABLE user_role
    DROP CONSTRAINT user_role_pk;
ALTER TABLE user_role ADD COLUMN id SERIAL
    CONSTRAINT user_role_pk PRIMARY KEY;

-- processing_flow table
ALTER TABLE processing_flow
    DROP CONSTRAINT processing_flow_pk;
ALTER TABLE processing_flow
    ADD COLUMN id SERIAL
        CONSTRAINT processing_flow_pk PRIMARY KEY;

-- linked_document table
ALTER TABLE linked_document
    DROP CONSTRAINT linked_document_pk;
ALTER TABLE linked_document
    ADD COLUMN id SERIAL
        CONSTRAINT linked_document_pk PRIMARY KEY;
