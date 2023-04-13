SET SEARCH_PATH TO "doc_main";

INSERT INTO "user" ("username", "password", "email", "full_name", created_by, updated_by)
VALUES ('user5', '$2a$10$fXwepveGHZt1poxgYBO7hecLyScDmnlV5c933nTGgN7bsUSk5RPb6', 'user5@gmail.com', 'Maya Patel', 1, 1),
       ('user6', '$2a$10$/Kwg1KTowgmtaFtatV6ljuxnI5Gk9AivN21tMgr/s25p0TPPf/RU2', 'user6@gmail.com', 'Liam Cooper', 1, 1),
       ('user7', '$2a$10$eilqmQpAFBxreqDswnbrnOJ6Oxk2thKVOfNX0nuOqItztazfiL2fK', 'user7@gmail.com', 'Bianca Rodriguez', 1, 1),
       ('user8', '$2a$10$n0htghT1k.wKSyDsqRh9tOG2Fb6AOMJXkYMAl6WjBYXagyosr.cOG', 'user8@gmail.com', 'Marcus Nguyen', 1, 1),
       ('user9', '$2a$10$n0htghT1k.wKSyDsqRh9tOG2Fb6AOMJXkYMAl6WjBYXagyosr.cOG', 'user9@gmail.com', 'Sofia Ramirez', 1, 1),
       ('user10', '$2a$10$n0htghT1k.wKSyDsqRh9tOG2Fb6AOMJXkYMAl6WjBYXagyosr.cOG', 'user10@gmail.com', 'Alexander Lee', 1, 1);

INSERT INTO "user_role" ("user_id", "role_name", created_by, updated_by)
VALUES (5, 'DIRECTOR', 1, 1),
       (6, 'MANAGER', 1, 1),
       (7, 'STAFF', 1, 1),
       (8, 'EXPERT', 1, 1),
       (9, 'DIRECTOR', 1, 1),
       (10, 'MANAGER', 1, 1);