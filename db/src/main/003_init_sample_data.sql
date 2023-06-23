--liquibase formatted sql
--changeset doc:003

SET SEARCH_PATH TO "doc_main";

INSERT INTO "document_type" ("type", "created_by", "updated_by")
VALUES ('CONTRACT', 'docadmin', 'docadmin'),
       ('INVOICE', 'docadmin', 'docadmin'),
       ('PAYMENT', 'docadmin', 'docadmin'),
       ('RECEIPT', 'docadmin', 'docadmin'),
       ('OTHER', 'docadmin', 'docadmin');

INSERT INTO "sending_level" ("level", "created_by", "updated_by")
VALUES ('CITY', 1, 1),
       ('DISTRICT', 1, 1),
       ('SCHOOL', 1, 1);

INSERT INTO "distribution_organization" ("name", "symbol", "created_by", "updated_by")
VALUES ('University of Science', 'HCMUS', 1, 1),
       ('University of Technology', 'HCMUT', 1, 1),
       ('University of Education', 'HCMUE', 1, 1);
