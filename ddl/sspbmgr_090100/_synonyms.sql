-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

-- This script installs public synonyms for PageBuilder specific database objects

create or replace public synonym css for sspbmgr.css;
create or replace public synonym page for sspbmgr.page;
create or replace public synonym page_role for sspbmgr.page_role;
create or replace public synonym virtual_domain for sspbmgr.virtual_domain;
create or replace public synonym virtual_domain_role for sspbmgr.virtual_domain_role; 
create or replace public synonym requestmap for sspbmgr.requestmap;
create or replace public synonym hibernate_sequence for sspbmgr.hibernate_sequence;
