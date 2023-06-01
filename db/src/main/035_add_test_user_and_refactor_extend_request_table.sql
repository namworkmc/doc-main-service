SET SEARCH_PATH TO doc_main;

INSERT INTO "user"(created_by, updated_by, username, password, email, full_name, role,
                   department_id)
VALUES ('DOC_ADMIN', 'DOC_ADMIN', 'vanthu',
        '$2a$10$KfYxLt96tapla3wo1d7Hb.4hdArwj2bxauJTlmqdxF/E81y7HaH9q', 'vanthu@gmail.com',
        'vanthu', 'VAN_THU', 1),
       ('DOC_ADMIN', 'DOC_ADMIN', 'giamdoc',
        '$2a$10$.vtgHWpJq.lq4sHrv6FbSeJSNlXbkPiCjcWnhtSLDiJrgGOqoJ/EK', 'giamdoc@gmail.com',
        'giamdoc', 'GIAM_DOC', 1),
       ('DOC_ADMIN', 'DOC_ADMIN', 'truongphong',
        '$2a$10$Hd/iXUa8dx9lAb9lmramVeAsgSP2mtFSP9cjAD9QeayLq79XhRJSa', 'truongphong@gmail.com',
        'truongphong', 'TRUONG_PHONG', 1),
       ('DOC_ADMIN', 'DOC_ADMIN', 'chuyenvien',
        '$2a$10$jwSQZq/ar8PqmdzflzfhlOrdHq/JZjqT3t/GPAYmIrZ39.RgqFDBa', 'chuyenvien@gmail.com',
        'chuyenvien', 'CHUYEN_VIEN', 1);

ALTER TABLE "extension_request"
    RENAME TO "extend_request";
ALTER TABLE "extend_request" RENAME COLUMN "old_expired_date" TO "old_deadline";
ALTER TABLE "extend_request" RENAME COLUMN "extended_until" TO "new_deadline";
