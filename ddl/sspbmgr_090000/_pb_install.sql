-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

-- This script installs SSPBMGR with PageBuilder specific database objects


SET define ON

col SELECTED for a40
-- bind a substitution variable SELECTION to a SQL column
col SELECTED new_val SELECTION 
col SELECTED for a40

connect sys/&&sys_password as sysdba

-- Check if user SSPBMGR exists

select
  decode(cnt,0,'_create_user','_skip') selected
from (
  select count(*) cnt
  from all_users where username='SSPBMGR'
);

@@ &selection SSPBMGR &&SSPBMGR_PASSWORD

grant dba to SSPBMGR;

prompt Connect SSPBMGR
connect SSPBMGR/&&SSPBMGR_PASSWORD

--Enable the dba role
set role all;

prompt
prompt Table CSS
-- Check if CSS table exists
select
  decode(cnt,0,'_run','_skip') selected
from (
  select count(*) cnt
  from all_tables where owner=user and table_name='CSS'
);

@@ &selection css1.sql
@@ &selection css2.sql
@@ &selection css3.sql

prompt
prompt Table PAGE
-- Check if PAGE table exists
select
  decode(cnt,0,'_run','_skip') selected
from (
  select count(*) cnt
  from all_tables where owner=user and table_name='PAGE'
);

@@ &selection page1.sql
@@ &selection page2.sql
@@ &selection page3.sql
@@ &selection page4.sql

prompt
prompt Table PAGE_ROLE
-- Check if PAGE_ROLE table exists
select
  decode(cnt,0,'_run','_skip') selected
from (
  select count(*) cnt
  from all_tables where owner=user and table_name='PAGE_ROLE'
);

@@ &selection page_role1.sql
@@ &selection page_role2.sql
@@ &selection page_role3.sql
@@ &selection page_role4.sql
@@ &selection page_role5.sql

prompt
prompt Table REQUESTMAP
-- Check if REQUESTMAP table exists
select
  decode(cnt,0,'_run','_skip') selected
from (
  select count(*) cnt
  from all_tables where owner=user and table_name='REQUESTMAP'
);

@@ &selection requestmap1.sql
@@ &selection requestmap2.sql
@@ &selection requestmap3.sql

prompt
prompt Table VIRTUAL_DOMAIN
-- Check if VIRTUAL_DOMAIN table exists
select
  decode(cnt,0,'_run','_skip') selected
from (
  select count(*) cnt
  from all_tables where owner=user and table_name='VIRTUAL_DOMAIN'
);

@@ &selection virtual_domain1.sql
@@ &selection virtual_domain2.sql
@@ &selection virtual_domain3.sql

prompt
prompt Table VIRTUAL_DOMAIN_ROLE
-- Check if VIRTUAL_DOMAIN_ROLE table exists
select
  decode(cnt,0,'_run','_skip') selected
from (
  select count(*) cnt
  from all_tables where owner=user and table_name='VIRTUAL_DOMAIN_ROLE'
);

@@ &selection virtual_domain_role1.sql
@@ &selection virtual_domain_role2.sql
@@ &selection virtual_domain_role3.sql
@@ &selection virtual_domain_role4.sql
@@ &selection virtual_domain_role5.sql

prompt
prompt Sequence HIBERNATE_SEQUENCE
-- Check if hibernate_sequence exists
select
  decode(cnt,0,'_run','_skip') selected
from (
  select count(*) cnt
  from all_sequences where sequence_owner=user and sequence_name='HIBERNATE_SEQUENCE'
);

@@ &selection hibernate_sequence.sql

prompt
prompt Synonyms
-- Create public synonyms
@@ _synonyms.sql

prompt
prompt Grants
-- Grant privileges on SSPBMGR objects to Banner users 
@@ _grants_pb.sql

-- Grant privileges on Banner objects needed to run PageBuilder
connect sys/&&sys_password as sysdba
@@ _grants_banner.sql

-- Revoke dba and grant any required privileges to sspbmgr
@@ _grants_sspbmgr.sql


Prompt Installing developer security
@@ _pb_security.sql



Prompt Creating Web Tailor Menu
-- If a non default Menu exists for PageBuilder we skip installing to avoid overwriting customized menu's
select
  decode(cnt,0,'_run','_skip') selected
from (
  select count(*) cnt
  from TWGRMENU where TWGRMENU_NAME = 'bmenu.P_PBMainMnu' and TWGRMENU_URL NOT LIKE '&pagebuilder_home.%'
);

@@_pb_webtailor_menudata.sql
