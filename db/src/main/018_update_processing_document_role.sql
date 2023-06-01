SET SEARCH_PATH TO doc_main;

ALTER TABLE processing_user_role
DROP CONSTRAINT processing_user_role_role_name_check,
ADD CONSTRAINT processing_user_role_role_name_check_1 CHECK ( role_name IN ('APPROVER', 'REVIEWER', 'SUBMITTER', 'COLLABORATOR', 'ASSIGNEE', 'REPORTER') );

UPDATE processing_user_role
SET role_name = 'ASSIGNEE'
WHERE role_name = 'APPROVER' OR role_name = 'REVIEWER';

UPDATE processing_user_role
SET role_name = 'REPORTER'
WHERE role_name = 'SUBMITTER';

ALTER TABLE processing_user_role
DROP CONSTRAINT processing_user_role_role_name_check_1,
ADD CHECK ( role_name IN ('ASSIGNEE', 'REPORTER', 'COLLABORATOR') );
