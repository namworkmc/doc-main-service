SET SEARCH_PATH TO doc_main;

ALTER TABLE user_role
DROP CONSTRAINT user_role_role_name_check,
ADD CONSTRAINT user_role_role_name_check_1 CHECK ( role_name IN ('DIRECTOR', 'EXPERT', 'MANAGER', 'STAFF', 'GIAM_DOC', 'CHUYEN_VIEN', 'TRUONG_PHONG', 'VAN_THU') );

UPDATE user_role
SET role_name = 'GIAM_DOC'
WHERE role_name = 'DIRECTOR';

UPDATE user_role
SET role_name = 'CHUYEN_VIEN'
WHERE role_name = 'EXPERT';

UPDATE user_role
SET role_name = 'TRUONG_PHONG'
WHERE role_name = 'MANAGER';

UPDATE user_role
SET role_name = 'VAN_THU'
WHERE role_name = 'STAFF';

ALTER TABLE user_role
DROP CONSTRAINT user_role_role_name_check_1,
ADD CHECK ( role_name IN ('GIAM_DOC', 'CHUYEN_VIEN', 'TRUONG_PHONG', 'VAN_THU') );
