SET SEARCH_PATH TO doc_main;

CREATE TABLE "department"
(
    "id"    SERIAL       NOT NULL,
    "department_name" VARCHAR(255) NOT NULL,
    CONSTRAINT "department_pk" PRIMARY KEY ("id")
) INHERITS ("doc_base_table");

INSERT INTO "department" ("department_name", created_by, updated_by)
VALUES ('Ban Chỉ Huy', 1, 1),
       ('Phòng kế hoạch đầu tư', 1, 1);

ALTER TABLE "user" ADD COLUMN "department_id" BIGINT;
ALTER TABLE "user" ADD CONSTRAINT "user_department_fk" FOREIGN KEY ("department_id") REFERENCES "department" ("id");

UPDATE "user"
SET department_id = 1
WHERE username = 'user3';

UPDATE "user"
SET department_id = 2
WHERE username = 'user7';
