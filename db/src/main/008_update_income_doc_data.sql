SET SEARCH_PATH TO "doc_main";

UPDATE "incoming_document" SET "arriving_date" = CURRENT_DATE + "id";
UPDATE "incoming_document" SET "arriving_time" = CURRENT_TIME + '1 hour'::INTERVAL * "id";

INSERT INTO "incoming_document"
("incoming_number",
 "document_type_id",
 "original_symbol_number",
 "distribution_org_id",
 "distribution_date",
 "arriving_date",
 "arriving_time",
 "summary",
 "urgency",
 "confidentiality",
 "folder",
 "sending_level_id",
 "created_by",
 "updated_by")
VALUES ('13', 1, '4f81', 1, NOW(), '2018-01-01', '19:00:00', 'pio8rXJ', 'LOW', 'LOW', 'FOLDER', 1, 1, 1),
       ('14', 2, 'q9uv5Xne2', 2, NOW(), '2018-01-01', '19:00:00', 'vSxpk', 'LOW', 'LOW', 'FOLDER', 2, 1, 1),
       ('15', 3, 'sy6Y3', 3, NOW(), '2018-01-01', '19:00:00', 'xN8', 'LOW', 'LOW', 'FOLDER', 3, 1, 1),
       ('16', 4, '1Plkv4', 1, NOW(), '2018-01-01', '19:00:00', 'Ua1DYQc2', 'LOW', 'LOW', 'FOLDER', 1, 1, 1),
       ('17', 5, 'jw17D9pF5', 2, NOW(), '2018-01-01', '19:00:00', 'fvICk', 'MEDIUM', 'MEDIUM', 'FOLDER', 2, 1, 1),
       ('18', 1, 'IJ736', 3, NOW(), '2018-01-01', '19:00:00', 'hALNC', 'MEDIUM', 'MEDIUM', 'FOLDER', 3, 1, 1),
       ('19', 2, 'lCHh8a87', 1, NOW(), '2018-01-01', '19:00:00', 'Y0S0n81E', 'MEDIUM', 'MEDIUM', 'FOLDER', 1, 1, 1),
       ('20', 3, 'duw9u78', 2, NOW(), '2018-01-01', '19:00:00', 'x6q', 'MEDIUM', 'MEDIUM', 'FOLDER', 2, 1, 1),
       ('21', 4, '5q34uu4Z9', 3, NOW(), '2018-01-01', '19:00:00', 'GkOEm65', 'HIGH', 'HIGH', 'FOLDER', 3, 1, 1),
       ('22', 5, 'UmRFP410', 1, NOW(), '2018-01-01', '19:00:00', '2Iq', 'HIGH', 'HIGH', 'FOLDER', 1, 1, 1),
       ('23', 1, 'Cfg11', 2, NOW(), '2018-01-01', '19:00:00', 'cb72FY', 'HIGH', 'HIGH', 'FOLDER', 2, 1, 1),
       ('24', 2, 'dNpS4s12', 3, NOW(), '2018-01-01', '19:00:00', 'pUF6', 'HIGH', 'HIGH', 'FOLDER', 3, 1, 1);
