SET SEARCH_PATH TO doc_main;

-- Update user5 to user10 password to 'user1'
UPDATE "user"
SET password = '$2a$10$QOlZokTstn.leX/kkpDJw.iak9YlaSrvE8hmKb8Bv.RaBMJRliZJ6' -- user
WHERE username IN ('user5', 'user6', 'user7', 'user8', 'user9', 'user10');
