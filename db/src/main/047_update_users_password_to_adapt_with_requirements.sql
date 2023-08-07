--liquibase formatted sql
--changeset doc:047

set search_path to doc_main;

update "user" set password = '$2a$10$.15cs/uwkRfRjgnRqwC.NOndHHvVXkyDMfi3IrqbMVofJciaijkEW'; -- Password is Testpassword1!
update "user" set password = '$2a$10$sfRdnNAyQCQFewb6d/SqYedOIE9dyZOtthAQoT2HPk8qQrpN9zcye' where username = 'user1'; -- User111!
update "user" set password = '$2a$10$5A.XhJesVS1Pl1fnpWlHgefXTukuA0lH1m5KeBtlvoQFDut6PenO6' where username = 'user2'; -- User222!
update "user" set password = '$2a$10$GvwvBOPTX.MuBEnLNQDnBuYDliX/hPbwIrFIK9kvdpbd7sIK3ghoW' where username = 'user3'; -- User333!
update "user" set password = '$2a$10$alfZs/yBOnNg7Nk7W6oJFePnE0NJH2k7/7G5zYefyZpoXlDtaHyJC' where username = 'user4'; -- User444!
update "user" set password = '$2a$10$s8fDrbXPTJu0.i/jLnDaSe5xemtkV8GYJh087k88En/QA0OGe9892' where username = 'vanthu'; -- Vanthu1!
update "user" set username = 'hieutruong' where username = 'giamdoc';
update "user" set password = '$2a$10$Y9k.vSI6bdAHjU1c0kWoJeknpF.OOu/3c2SoHa/9eVaqdC/4PeAWK' where username = 'hieutruong'; -- Hieutruong1!
update "user" set password = '$2a$10$je4.LUb.x6dnSXDNfQ6W7ug/GCY7.vdlA26RetkA01gq/r4760hQO' where username = 'truongphong'; -- Truongphong1!
update "user" set password = '$2a$10$MlnVNEjjikpl1RufVHQW7OACxZHaBbTjama5besBUSPC08ZvNLx5a' where username = 'chuyenvien'; -- Chuyenvien1!
