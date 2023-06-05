SET SEARCH_PATH TO doc_main;

ALTER TABLE "outgoing_document"
    ADD COLUMN "name" VARCHAR(255);

ALTER TABLE "incoming_document"
    ADD COLUMN "name" VARCHAR(255);

update outgoing_document
set name = 'outgoing_name' where id is not null;

update incoming_document
set name = 'incoming_name' where id is not null;