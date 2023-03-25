SET SEARCH_PATH TO doc_main;

CREATE TABLE "folder"
(
    "id"    SERIAL       NOT NULL,
    "folder_name" VARCHAR(255) NOT NULL,
    "year" INTEGER      NOT NULL DEFAULT date_part('year', CURRENT_DATE),
    "next_number" BIGINT     NOT NULL DEFAULT 1,
    CONSTRAINT "folder_name_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

INSERT INTO "folder" ("folder_name", "next_number")
VALUES ('Sổ văn bản đến - 2023', 2),
       ('Sổ văn bản nội bộ - 2023', 2),
       ('Sổ văn bản đi - 2023', 2);