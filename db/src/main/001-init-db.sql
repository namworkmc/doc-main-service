CREATE TABLE "user"
(
    "id"         SERIAL       NOT NULL,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name"  VARCHAR(255) NOT NULL,
    "username"   VARCHAR(255) NOT NULL,
    "password"   VARCHAR(255) NOT NULL,
    "email"      VARCHAR(255) NOT NULL,
    CONSTRAINT "user_pk" PRIMARY KEY ("id")
);

CREATE TABLE "doc_role"
(
    "id"   SERIAL      NOT NULL,
    "name" VARCHAR(10) NOT NULL,
    CONSTRAINT "doc_role_pk" PRIMARY KEY ("id")
);

CREATE TABLE "user_role"
(
    "user_id" BIGINT NOT NULL,
    "role_id" BIGINT NOT NULL,
    CONSTRAINT "user_role_pk" PRIMARY KEY ("user_id", "role_id")
);

INSERT INTO "user" ("first_name", "last_name", "username", "password", "email")
VALUES ('John', 'Doe', 'johndoe', '$2a$10$04cKaybcuJcL18QPgs5UKOSb9gKdPnpv8wWHyTswwg5Q0Vt4GFQFm', 'john@gmail.com'),
       ('Jane', 'Doe', 'janedoe', '$2a$10$.v7r4X6hqzgR5rE8zMu6uOmDhCWxk7MnCPSN8Hjex0E2PD8a7GKWq', 'jane@gmail.com');

INSERT INTO "doc_role" ("name")
VALUES ('APPROVER'), ('REVIEWER'), ('SUBMITTER');

INSERT INTO "user_role" ("user_id", "role_id")
VALUES (1, 1), (1, 2), (1, 3), (2, 1), (2, 2);