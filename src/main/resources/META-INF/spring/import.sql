--SET SCHEMA = public;

-- Clear Down the Databas
DELETE FROM LOGBOOKUSER_CERTIFICATION;
DELETE FROM LOGBOOKUSER_LOGBOOK;
DELETE FROM LOGBOOKUSER;
DELETE FROM CERTIFICATION;
DELETE FROM ORGANISATION_COUNTRY;
DELETE FROM ORGANISATION;
DELETE FROM LOGBOOK_PAGE;
DELETE FROM LOGBOOK;
DELETE FROM SEC_USER_ROLE;
DELETE FROM SEC_ROLE;
DELETE FROM SEC_USER;
DELETE FROM COUNTRY;

INSERT INTO COUNTRY (id, VERSION, ansi_code) values(1,0,'ZA');
INSERT INTO COUNTRY (id, VERSION, ansi_code) values(2,0,'GB');

-- Insert Roles
INSERT INTO SEC_ROLE (ID, VERSION, NAME, DESCRIPTION, ACTIVE, LAST_UPDATE_DATE) values (1, 1, 'REGISTEREDUSER', 'Logbook guest', true, CURRENT_TIMESTAMP);
INSERT INTO SEC_ROLE (ID, VERSION, NAME, DESCRIPTION, ACTIVE, LAST_UPDATE_DATE) values (2, 1, 'LOGBOOKUSER', 'Logbook user', true, CURRENT_TIMESTAMP);
INSERT INTO SEC_ROLE (ID, VERSION, NAME, DESCRIPTION, ACTIVE, LAST_UPDATE_DATE) values (3, 1, 'LOGBOOKADMIN', 'Logbook administrator', true, CURRENT_TIMESTAMP);
INSERT INTO SEC_ROLE (ID, VERSION, NAME, DESCRIPTION, ACTIVE, LAST_UPDATE_DATE) values (4, 1, 'USERADMIN', 'User administrator', true, CURRENT_TIMESTAMP);
INSERT INTO SEC_ROLE (ID, VERSION, NAME, DESCRIPTION, ACTIVE, LAST_UPDATE_DATE) values (5, 1, 'ADMIN', 'Administrator', true, CURRENT_TIMESTAMP);


--insert SecUsers
INSERT INTO SEC_USER (active, username, first_name, last_name, version, password, email, ID, locale) values(true, 'peterneil', 'Peter', 'Neil', 1, 'password', 'peterneil_world@yahoo.co.uk', 0, 'en_GB');
INSERT INTO SEC_USER (active, username, first_name, last_name, version, password, email, ID, locale) values(true, 'peterneiladmin', 'Peter', 'Neil', 1, 'password', 'saffadiver@yahoo.co.uk', 1, 'en_GB');

--peterneil
INSERT INTO SEC_USER_ROLE (user_id, role_id) values(0,2);
INSERT INTO SEC_USER_ROLE (user_id, role_id) values(0,1); 

--peterneiladmin
INSERT INTO SEC_USER_ROLE (user_id, role_id) values(1,1);
INSERT INTO SEC_USER_ROLE (user_id, role_id) values(1,2);
INSERT INTO SEC_USER_ROLE (user_id, role_id) values(1,5);

-- insert LOGBOOKUSERs
INSERT INTO LOGBOOKUSER (id, version, sec_user_id, country_id) values(1,1,0,1);
INSERT INTO LOGBOOKUSER (id, version, sec_user_id, country_id) values(2,1,1,2);

-- insert logbooks
INSERT INTO LOGBOOK (id, VERSION,CODE, NAME) values (1,1,'dm_logbook', 'Divemaster Logbook');
INSERT INTO LOGBOOK (id, VERSION,CODE, NAME) values (5,1,'coarse_fishing_logbook', 'Coarse Fishing Logbook');

INSERT INTO LOGBOOKUSER_LOGBOOK (id, version, logbook_id, LOGBOOKUSER_id, name) values(1,1,1,1,'Scuba Diving Logbook');
INSERT INTO LOGBOOKUSER_LOGBOOK (id, version, logbook_id, LOGBOOKUSER_id, name) values(2,1,5,1,'Carp Fishing Logbook');

INSERT INTO LOGBOOKUSER_LOGBOOK (id, version, logbook_id, LOGBOOKUSER_id, name) values(3,1,1,2,'Scuba Diving Logbook');

INSERT INTO ORGANISATION (id, VERSION, name, code, name_or_number, street, province, postal_code, email, country_id) values(1, 0, 'Professional Association of Diving Instructors', 'PADI','ho1', 'ho2', 'ho3', 'pc2', 'head-contact@padi.com',1);
INSERT INTO ORGANISATION_COUNTRY (id, version, phone, fax, organisation_id, country_id) values(1, 0, '0044020710810', '0044020710810', 1, 1);
