SET SEARCH_PATH TO doc_main;

UPDATE "user"
SET password = '$2a$10$fXwepveGHZt1poxgYBO7hecLyScDmnlV5c933nTGgN7bsUSk5RPb6'
WHERE username IN ('user5', 'user6', 'user7', 'user8', 'user9', 'user10');