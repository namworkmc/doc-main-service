SET SEARCH_PATH TO doc_main;

ALTER TABLE processing_user_role
    RENAME COLUMN processing_role_id TO role_name;
ALTER TABLE processing_user_role
    DROP CONSTRAINT processing_user_role_processing_document_role_fk;
ALTER TABLE processing_user_role
    ALTER COLUMN role_name TYPE VARCHAR(20) USING role_name::VARCHAR(20);
UPDATE processing_user_role
SET role_name = (SELECT name
                 FROM processing_document_role
                 WHERE processing_user_role.role_name::BIGINT = processing_document_role.id);
ALTER TABLE processing_user_role
    ADD CHECK ( role_name IN ('APPROVER', 'REVIEWER', 'SUBMITTER', 'COLLABORATOR') );

DROP TABLE processing_document_role;

ALTER TABLE user_role
    RENAME COLUMN role_id TO role_name;
ALTER TABLE user_role
    DROP CONSTRAINT user_role_doc_system_role_fk;
ALTER TABLE user_role
    ALTER COLUMN role_name TYPE VARCHAR(20) USING role_name::VARCHAR(20);
UPDATE user_role
SET role_name = (SELECT name
                 FROM doc_system_role
                 WHERE user_role.role_name::BIGINT = doc_system_role.id);
ALTER TABLE user_role
    ADD CHECK ( role_name IN ('DIRECTOR', 'EXPERT', 'MANAGER', 'STAFF') );

DROP TABLE doc_system_role;