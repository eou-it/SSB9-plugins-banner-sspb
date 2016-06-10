-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

prompt CREATE USER &1 
CREATE USER &1 IDENTIFIED BY &2;

ALTER USER &1
DEFAULT TABLESPACE "DEVELOPMENT"
TEMPORARY TABLESPACE "TEMP"
ACCOUNT UNLOCK ;

-- ROLES
GRANT CONNECT,DBA TO &1;
ALTER USER &1 DEFAULT ROLE "CONNECT","DBA";

-- SYSTEM PRIVILEGES

-- QUOTAS
ALTER USER &1 QUOTA UNLIMITED ON DEVELOPMENT;