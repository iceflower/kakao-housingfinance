--Add initial role
INSERT INTO ROLES (ROLE_NAME) VALUES ('ROLE_USER');
INSERT INTO ROLES (ROLE_NAME) VALUES ('ROLE_ADMIN');


--INSERT INTO USERS(USER_ID, CREATED_AT, UPDATED_AT, IS_ACTIVE, EMAIL, FIRST_NAME, IS_EMAIL_VERIFIED, LAST_NAME, "password", USERNAME)
--VALUES(1, '2019-06-17 23:23:58.116', '2019-06-17 23:23:58.116', true, 'iceflower01@gmail.com', '영근', true, '김', '$2a$10$zEPXABl/o9LxBDJc3BOtdO733U/NdLB5v4/06AJFcDUq1D57PsxPa', 'iceflower01@gmail.com');
--
--
--INSERT INTO USER_AUTHORITY (user_id, role_id) VALUES(1, 1);