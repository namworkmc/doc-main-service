SET SEARCH_PATH TO "doc_main";

INSERT INTO "document_type" ("type", "created_by", "updated_by")
VALUES ('CONTRACT', 1, 1),
       ('INVOICE', 1, 1),
       ('PAYMENT', 1, 1),
       ('RECEIPT', 1, 1),
       ('OTHER', 1, 1);

INSERT INTO "sending_level" ("level", "created_by", "updated_by")
VALUES ('CITY', 1, 1),
       ('DISTRICT', 1, 1),
       ('SCHOOL', 1, 1);

INSERT INTO "distribution_organization" ("name", "symbol", "created_by", "updated_by")
VALUES ('University of Science', 'HCMUS', 1, 1),
       ('University of Technology', 'HCMUT', 1, 1),
       ('University of Education', 'HCMUE', 1, 1);

INSERT INTO "incoming_document"
(incoming_number,
 document_type_id,
 original_symbol_number,
 distribution_org_id,
 distribution_date,
 arriving_date,
 arriving_time,
 comment,
 urgency,
 confidentiality,
 folder,
 sending_level_id,
 created_by,
 updated_by)
VALUES ('1', 1, '1', 1, NOW(), '2018-01-01', '19:00:00', 'comment', 'LOW', 'LOW', 'FOLDER', 1, 1,
        1),
       ('2', 2, '2', 2, NOW(), '2018-01-01', '19:00:00', 'comment', 'LOW', 'LOW', 'FOLDER', 2, 1,
        1),
       ('3', 3, '3', 3, NOW(), '2018-01-01', '19:00:00', 'comment', 'LOW', 'LOW', 'FOLDER', 3, 1,
        1),
       ('4', 4, '4', 1, NOW(), '2018-01-01', '19:00:00', 'comment', 'LOW', 'LOW', 'FOLDER', 1, 1,
        1),
       ('5', 5, '5', 2, NOW(), '2018-01-01', '19:00:00', 'comment', 'MEDIUM', 'MEDIUM', 'FOLDER', 2,
        1, 1),
       ('6', 1, '6', 3, NOW(), '2018-01-01', '19:00:00', 'comment', 'MEDIUM', 'MEDIUM', 'FOLDER', 3,
        1, 1),
       ('7', 2, '7', 1, NOW(), '2018-01-01', '19:00:00', 'comment', 'MEDIUM', 'MEDIUM', 'FOLDER', 1,
        1, 1),
       ('8', 3, '8', 2, NOW(), '2018-01-01', '19:00:00', 'comment', 'MEDIUM', 'MEDIUM', 'FOLDER', 2,
        1, 1),
       ('9', 4, '9', 3, NOW(), '2018-01-01', '19:00:00', 'comment', 'HIGH', 'HIGH', 'FOLDER', 3, 1,
        1),
       ('10', 5, '10', 1, NOW(), '2018-01-01', '19:00:00', 'comment', 'HIGH', 'HIGH', 'FOLDER', 1,
        1, 1),
       ('11', 1, '11', 2, NOW(), '2018-01-01', '19:00:00', 'comment', 'HIGH', 'HIGH', 'FOLDER', 2,
        1, 1),
       ('12', 2, '12', 3, NOW(), '2018-01-01', '19:00:00', 'comment', 'HIGH', 'HIGH', 'FOLDER', 3,
        1, 1);

INSERT INTO "processed_document" (incoming_doc_id, created_by, updated_by)
VALUES (2, 1, 1),
       (4, 1, 1),
       (6, 1, 1),
       (8, 1, 1),
       (10, 1, 1),
       (12, 1, 1);

INSERT INTO "processing_document"
(incoming_doc_id,
 status,
 is_opened,
 processing_request,
 created_by,
 updated_by)
VALUES (1, 'IN_PROGRESS', FALSE, 'processing_request', 1, 1),
       (3, 'IN_PROGRESS', FALSE, 'processing_request', 1, 1),
       (5, 'IN_PROGRESS', FALSE, 'processing_request', 1, 1),
       (7, 'IN_PROGRESS', FALSE, 'processing_request', 1, 1),
       (9, 'IN_PROGRESS', FALSE, 'processing_request', 1, 1),
       (11, 'IN_PROGRESS', FALSE, 'processing_request', 1, 1),
       (2, 'CLOSED', TRUE, 'processing_request', 1, 1),
       (4, 'CLOSED', TRUE, 'processing_request', 1, 1),
       (6, 'CLOSED', TRUE, 'processing_request', 1, 1),
       (8, 'CLOSED', TRUE, 'processing_request', 1, 1),
       (10, 'CLOSED', TRUE, 'processing_request', 1, 1),
       (12, 'CLOSED', TRUE, 'processing_request', 1, 1);

INSERT INTO "outgoing_document"
("document_type_id",
 "urgency",
 "confidentiality",
 "summary",
 "status",
 "created_by",
 "updated_by")
VALUES (1, 'LOW', 'LOW', 'TEST', 'UNPROCESSED', 1, 1),
       (1, 'LOW', 'LOW', 'TEST', 'UNPROCESSED', 1, 1),
       (1, 'LOW', 'LOW', 'TEST', 'UNPROCESSED', 1, 1),
       (1, 'LOW', 'LOW', 'TEST', 'UNPROCESSED', 1, 1),
       (1, 'LOW', 'LOW', 'TEST', 'UNPROCESSED', 1, 1),
       (1, 'LOW', 'LOW', 'TEST', 'UNPROCESSED', 1, 1);

INSERT INTO "linked_document" (incoming_doc_id, outgoing_doc_id, created_by, updated_by)
VALUES (1, 1, 1, 1),
       (3, 2, 1, 1),
       (5, 3, 1, 1),
       (7, 4, 1, 1),
       (9, 5, 1, 1),
       (11, 6, 1, 1);

INSERT INTO "feedback" (processing_doc_id, content, created_by, updated_by)
VALUES (1, 'content', 1, 1),
       (2, 'content', 1, 1),
       (3, 'content', 1, 1),
       (4, 'content', 1, 1),
       (5, 'content', 1, 1),
       (6, 'content', 1, 1),
       (7, 'content', 1, 1),
       (8, 'content', 1, 1),
       (9, 'content', 1, 1),
       (10, 'content', 1, 1),
       (11, 'content', 1, 1),
       (12, 'content', 1, 1);

INSERT INTO "processing_document_role" ("name", created_by, updated_by)
VALUES ('APPROVER', 1, 1),
       ('REVIEWER', 1, 1),
       ('SUBMITTER', 1, 1),
       ('COLLABORATOR', 1, 1);

INSERT INTO "processing_user" (user_id, processing_doc_id, step, created_by, updated_by)
VALUES (1, 1, 1, 1, 1),
       (1, 1, 2, 1, 1),
       (1, 1, 3, 1, 1),
       (1, 1, 4, 1, 1),
       (1, 1, 5, 1, 1),
       (2, 2, 1, 1, 1),
       (2, 2, 2, 1, 1),
       (2, 2, 3, 1, 1),
       (1, 9, 1, 1, 1),
       (2, 10, 1, 1, 1),
       (1, 11, 1, 1, 1),
       (2, 12, 1, 1, 1);

INSERT INTO "processing_user_role"
(user_id,
 processing_doc_id,
 step,
 processing_role_id,
 created_by,
 updated_by)
VALUES (1, 1, 1, 1, 1, 1),
       (1, 1, 2, 2, 1, 1),
       (1, 1, 3, 3, 1, 1),
       (1, 1, 4, 4, 1, 1),
       (1, 1, 5, 4, 1, 1),
       (2, 2, 1, 1, 1, 1),
       (2, 2, 2, 2, 1, 1),
       (2, 2, 3, 3, 1, 1),
       (1, 9, 1, 1, 1, 1),
       (2, 10, 1, 1, 1, 1),
       (1, 11, 1, 1, 1, 1),
       (2, 12, 1, 1, 1, 1);

INSERT INTO "return_request"
(user_id,
 processing_doc_id,
 step,
 reason,
 status,
 created_by,
 updated_by)
VALUES (1, 1, 1, 'reason', 'PENDING', 1, 1),
       (1, 1, 2, 'reason', 'PENDING', 1, 1),
       (1, 1, 3, 'reason', 'PENDING', 1, 1),
       (1, 1, 4, 'reason', 'PENDING', 1, 1),
       (1, 1, 5, 'reason', 'APPROVED', 1, 1),
       (2, 2, 1, 'reason', 'APPROVED', 1, 1),
       (2, 2, 2, 'reason', 'APPROVED', 1, 1),
       (2, 2, 3, 'reason', 'APPROVED', 1, 1),
       (1, 9, 1, 'reason', 'REJECTED', 1, 1),
       (2, 10, 1, 'reason', 'REJECTED', 1, 1),
       (1, 11, 1, 'reason', 'REJECTED', 1, 1),
       (2, 12, 1, 'reason', 'REJECTED', 1, 1);

INSERT INTO "processing_flow" (flow_version, doc_type_id, flow, created_by, updated_by)
VALUES (1, 1, '{APPROVER, SUBMITTER, REVIEWER}', 1, 1),
       (1, 2, '{APPROVER, SUBMITTER, REVIEWER}', 1, 1),
       (1, 3, '{APPROVER, SUBMITTER, REVIEWER}', 1, 1),
       (1, 4, '{APPROVER, SUBMITTER, REVIEWER}', 1, 1),
       (1, 5, '{APPROVER, SUBMITTER, REVIEWER}', 1, 1),
       (2, 1, '{APPROVER, REVIEWER, SUBMITTER}', 1, 1),
       (2, 2, '{SUBMITTER, REVIEWER, APPROVER}', 1, 1);
