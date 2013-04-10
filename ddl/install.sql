connect baninst1/&&BANINST1_PASSWORD

DROP USER SSPBMGR CASCADE;

CREATE USER SSPBMGR IDENTIFIED BY &&SSPBMGR_PASSWORD;

ALTER USER SSPBMGR 
DEFAULT TABLESPACE "DEVELOPMENT"
TEMPORARY TABLESPACE "TEMP"
ACCOUNT UNLOCK ;

-- ROLES
GRANT CONNECT,DBA TO SSPBMGR;
ALTER USER SSPBMGR DEFAULT ROLE "CONNECT","DBA";

-- SYSTEM PRIVILEGES

-- QUOTAS
ALTER USER SSPBMGR QUOTA UNLIMITED ON DEVELOPMENT;

connect SSPBMGR/&&SSPBMGR_PASSWORD

prompt Install Hibernate Sequence

CREATE SEQUENCE SSPBMGR.HIBERNATE_SEQUENCE INCREMENT BY 1 START WITH 10000;

prompt Install SSPBMGR tables
@tab_sspb_virt_dom
@tab_sspb_page