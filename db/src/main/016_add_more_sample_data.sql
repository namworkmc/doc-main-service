SET SEARCH_PATH TO doc_main;

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
 "sending_level_id",
 "created_by",
 "updated_by")
SELECT SUBSTR(MD5(RANDOM()::TEXT), 1, 10),
       1,
       SUBSTR(MD5(RANDOM()::TEXT), 1, 10),
       1,
       NOW() - '1 day'::INTERVAL * (RANDOM()::INT * 100),
       NOW() - '1 day'::INTERVAL * (RANDOM()::INT * 100),
       NOW() - '1 day'::INTERVAL * (RANDOM()::INT * 100 + 100),
       'summary',
       'MEDIUM',
       'MEDIUM',
       1,
       1,
       1
FROM GENERATE_SERIES(1, 1000) id;

INSERT INTO "distribution_organization" ("name", "symbol", "created_by", "updated_by")
SELECT 'University of Science',
       SUBSTR(MD5(RANDOM()::TEXT), 1, 10),
       1,
       1
FROM GENERATE_SERIES(1, 50) id;

-- Loop through all incoming documents and update distribution_org_id
-- with a random distribution organization id
DO $$
DECLARE
  doc_id integer;
  dist_org_id integer;
BEGIN
    FOR doc_id IN SELECT id FROM incoming_document
    LOOP
        dist_org_id := (SELECT id FROM distribution_organization ORDER BY random() LIMIT 1);
        UPDATE incoming_document SET distribution_org_id = dist_org_id WHERE id = doc_id;
    END LOOP;
    END;
$$;

UPDATE "document_type" SET "type" = 'Hợp đồng' WHERE "type" = 'CONTRACT';
UPDATE "document_type" SET "type" = 'Hoá đơn' WHERE "type" = 'INVOICE';
UPDATE "document_type" SET "type" = 'Phiếu thu' WHERE "type" = 'PAYMENT';
UPDATE "document_type" SET "type" = 'Phiếu chi' WHERE "type" = 'RECEIPT';
UPDATE "document_type" SET "type" = 'Khác' WHERE "type" = 'OTHER';
