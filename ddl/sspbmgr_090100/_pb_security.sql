-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

-- This script creates and Admin Object and a Role to be used for PageBuilder Admins and Developers
set define on veri off head off lines 1000

define object_name = 'GPBADMN'
define object_desc = 'Page Builder Developer Tools'
define pb_role  = 'BAN_DEFAULT_PAGEBUILDER_M'
define pb_module = 'EXTZ'
define pb_module_name = 'Extensibility'
define pb_page = 'GPBADMN'

define delete_option = 'n'
define create_option = 'y'

Prompt Create Object &&object_name (PageBuilder developers need to be granted access to this object) and associated objects.
connect general/&&GENERAL_PASSWORD

select 'Delete existing records for Page Builder from GUBPAGE, GUBMODU and GUBOBJS.'
from dual where '&&delete_option' = 'y';
delete from gubpage where gubpage_code='&&object_name' and '&&delete_option' = 'y';
delete from gubmodu where gubmodu_code='&&pb_module' and '&&delete_option' = 'y';
delete from gubobjs where gubobjs_name = '&&object_name' and '&&delete_option'='y';
-------------------------------------------------
prompt Create GUBMODU record for Module &&pb_module
insert into gubmodu (
gubmodu_code,
gubmodu_name,
gubmodu_url,
gubmodu_version,
gubmodu_user_id,
gubmodu_activity_date,
gubmodu_data_origin,
gubmodu_plat_code
)
select '&&pb_module', '&&pb_module_name',
'Not needed',
0, user, sysdate, 'BASELINE', 'ADMZK'
from dual
where not exists (select 1 from gubmodu where gubmodu_code = '&&pb_module' and '&&create_option'='y');
-------------------------------------------------
prompt Create GUBOBJS object &&object_name
insert into gubobjs (
gubobjs_name     ,
gubobjs_desc       ,
gubobjs_objt_code ,
gubobjs_sysi_code ,
gubobjs_user_id    ,
gubobjs_activity_date ,
gubobjs_help_ind  ,
gubobjs_extract_enabled_ind ,
gubobjs_data_origin ,
gubobjs_ui_version)
select  '&&object_name', '&&object_desc',
   'FORM', 'G', User, Sysdate, 'N', 'D', 'Banner', 'C'
from dual
where not exists ( select 1 from gubobjs where gubobjs_name = '&&object_name' and '&&create_option'='y');
-------------------------------------------------
prompt Create GUBPAGE record for Page &&pb_page
insert into gubpage(
gubpage_code,
gubpage_name,
gubpage_gubmodu_code,
gubpage_version,
gubpage_user_id,
gubpage_activity_date,
gubpage_data_origin
)
select '&&object_name', '&&pb_page', '&&pb_module',
0, user, sysdate, 'BASELINE'
from dual
where not exists (select 1 from gubpage where gubpage_code = '&&object_name' and '&&create_option'='y');
commit;

connect bansecr/&&BANSECR_PASSWORD

--Enable the dba role
set role dba;

Prompt Delete record for Page Builder V 9.0 in GURAOBJ.

delete from Guraobj where guraobj_object = '&&object_name' and Guraobj_Current_Version = '9.0';

prompt Create Guraobj record. Object: &&object_name and Default Role: &&pb_role

Insert Into Guraobj
( Guraobj_Object,
  Guraobj_Default_Role,
  Guraobj_Current_Version,
  Guraobj_Sysi_Code,
  Guraobj_Activity_Date,
  Guraobj_owner)
select '&&object_name',
   '&&pb_role' ,
   '9.1',
   'G',
   Sysdate,
   'PUBLIC'
From dual
where not exists ( select 1 from guraobj where guraobj_object = '&&object_name');

commit;

prompt Setting up developer role
-- Drop the role
set serveroutput on size unlimited;
begin
  if '&&delete_option'='y' then
    execute immediate 'drop role &&pb_role';
    dbms_output.put_line('Dropped role &&pb_role');
  end if;
exception
  when others then
    if sqlcode=-1919 then
      dbms_output.put_line('Role &&pb_role does not exist');
	else
	  raise;
	end if;
end;
/

declare
  l_role_count integer := 0;
begin
  select count(*) into l_role_count
  from dba_roles where role = '&&pb_role';

  if l_role_count = 0 and '&&create_option'='y' then
    execute immediate 'create role &&pb_role';
    dbms_output.put_line('Created role &&pb_role');
  end if;
end;
/

declare
  l_cnt number;
begin
  l_cnt := g$_security_pkg.g$_set_password_fnc('N');
  dbms_output.put_line('Passwords set: '||l_cnt);
end;
/

--Prompt Granting privileges to the
@@_grants_developer

--Generate an overview of security
prompt Overview of security objects
prompt
set serveroutput on size unlimited;
declare
  l_count integer := 0;
  l_message varchar2(256);
begin
  l_message :='Missing GUBMODU record with gubmodu_code=&&pb_module';
  select '. GUBMODU record with gubmodu_code=&&pb_module'
  into l_message
  from gubmodu where gubmodu_code='&&pb_module';
  dbms_output.put_line(l_message);

  l_message :='Missing GUBPAGE record with gubpage_code=&&object_name';
  select '. GUBPAGE record with gubpage_code=&&object_name'
  into l_message
  from gubpage where gubpage_code='&&object_name';
  dbms_output.put_line(l_message);

  l_message :='Missing GUBOBJS record with gubobjs_name=&&object_name';
  select '. GUBOBJS record with gubobjs_name=&&object_name'
  into l_message
  from gubobjs where gubobjs_name='&&object_name';
  dbms_output.put_line(l_message);

  l_message :='Missing GURAOBJ record with guraobj_object=&&object_name and guraobj_current_version = 9.1';
  select '. GURAOBJ record with guraobj_object=&&object_name and guraobj_current_version = 9.1'
  into l_message
  from guraobj where guraobj_object='&&object_name' and guraobj_current_version = '9.1';
  dbms_output.put_line(l_message);

  select '. Default Role for Object &&object_name='||guraobj_default_role
  into l_message
  from guraobj where guraobj_object='&&object_name' and guraobj_current_version = '9.1';
  dbms_output.put_line(l_message);

  l_message :='Missing Role &&pb_role';
  select '. Role &&pb_role has been created.'
  into l_message
  from dba_roles where role = '&&pb_role';
  dbms_output.put_line(l_message);
  -- When not branched in the exception handler,
  dbms_output.put_line('Security set up is correct for PageBuilder');

exception
  when no_data_found then
  dbms_output.put_line(chr(10)||'*WARNING* Security is not correctly set up for PageBuilder');
  dbms_output.put_line(l_message);
end;
/

prompt Overview of users with object and role relevant to PageBuilder
select govurol_userid,govurol_object,govurol_role from govurol
where govurol_object = '&&object_name'
/
