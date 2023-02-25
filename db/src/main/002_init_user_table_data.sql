SET SEARCH_PATH TO "doc_main";

INSERT INTO "user" ("username", "password", "email", created_by, updated_by)
VALUES ('user1', '$2a$10$fXwepveGHZt1poxgYBO7hecLyScDmnlV5c933nTGgN7bsUSk5RPb6', 'user1', 1, 1),
       ('user2', '$2a$10$/Kwg1KTowgmtaFtatV6ljuxnI5Gk9AivN21tMgr/s25p0TPPf/RU2', 'user2', 1, 1);

INSERT INTO "doc_system_role" ("name", created_by, updated_by)
VALUES ('DIRECTOR', 1, 1),
       ('EXPERT', 1, 1),
       ('MANAGER', 1, 1),
       ('STAFF', 1, 1);

INSERT INTO "user_role" ("user_id", "role_id", created_by, updated_by)
VALUES (1, 1, 1, 1),
       (1, 2, 1, 1),
       (1, 3, 1, 1),
       (2, 1, 1, 1),
       (2, 2, 1, 1);