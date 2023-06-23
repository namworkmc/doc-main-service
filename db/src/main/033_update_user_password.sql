--liquibase formatted sql
--changeset doc:033

SET SEARCH_PATH TO doc_main;

-- user's password is their username
UPDATE "user" SET password = '$2a$10$QOlZokTstn.leX/kkpDJw.iak9YlaSrvE8hmKb8Bv.RaBMJRliZJ6' WHERE username = 'user5';
update "user" set password = '$2a$10$HToUQ4s6c7QV/9aOi5HlK.t3/monYp/7iiCpSaOp8KgiMvkzLLvau' where username = 'user6';
update "user" set password = '$2a$10$VzRn3fD7MWFb8HgqztApf.ZyOKrDrRVM6.QhqINnn.dAMFNIIF8lq' where username = 'user7';
update "user" set password = '$2a$10$VlSaLlWFDFX78B/GC2FcZ.okai.TO9s8TODP4BjR9/zVPQ.Dszf6S' where username = 'user8';
update "user" set password = '$2a$10$K.pzMTqOlRQ4X4MJthwlRu26oEdQdYyoprZ7h6vl2FfEUKMao/mrq' where username = 'user9';
update "user" set password = '$2a$10$J.1KlR02eB/UJSwOkUlNZOMDQEofP0tYvWWtgJckX7MGLRE21PAja' where username = 'user10';
