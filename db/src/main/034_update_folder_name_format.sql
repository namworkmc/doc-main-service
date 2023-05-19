SET SEARCH_PATH TO doc_main;

UPDATE "folder" SET "folder_name" = 'Sổ văn bản đi - %s' WHERE "id" = 1;
UPDATE "folder" SET "folder_name" = 'Sổ văn bản đến - %s' WHERE "id" = 2;
UPDATE "folder" SET "folder_name" = 'Sổ văn bản nội bộ - %s' WHERE "id" = 3;
