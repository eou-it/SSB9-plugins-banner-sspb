-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

-- This script establishes privileges for SSPBMGR post install

REVOKE DBA FROM SSPBMGR;

ALTER USER SSPBMGR DEFAULT ROLE "CONNECT";

-- SYSTEM PRIVILEGES

-- QUOTAS
ALTER USER SSPBMGR QUOTA UNLIMITED ON &&MMEDTAB_TABLESPACE_NAME;
