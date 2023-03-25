SET SEARCH_PATH TO doc_main;

CREATE TABLE "work_folder"
(
    "id"    SERIAL       NOT NULL,
    "work_folder_number" BIGINT NOT NULL,
    "work_folder_name" VARCHAR(255) NOT NULL,
    "user_id" BIGINT NOT NULL,
    CONSTRAINT "work_folder_pk" PRIMARY KEY ("id"),
    CONSTRAINT "work_folder_user_id_fk" FOREIGN KEY ("user_id")
        REFERENCES "user" ("id") MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) INHERITS ("doc_base_table");