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
    ADD COLUMN processing_user_role_id BIGINT
        CONSTRAINT processing_user_processing_user_role_fk REFERENCES processing_user_role (id);
ALTER TABLE processing_user
    ADD COLUMN return_request_id BIGINT
        CONSTRAINT processing_user_return_request_fk REFERENCES return_request (id);

-- Migrating data from processing_user_role to processing_user
UPDATE processing_user
SET processing_user_role_id = processing_user_role.id
FROM processing_user_role
WHERE processing_user_role.user_id = processing_user.user_id
  AND processing_user_role.processing_doc_id = processing_user.processing_doc_id
  AND processing_user_role.step = processing_user.step;

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