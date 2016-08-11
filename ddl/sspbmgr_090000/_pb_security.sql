-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

-- This script creates and Admin Object and a Role to be used for PageBuilder Admins and Developers
set define on veri off

define object_name = 'GPBADMN'
define object_desc = 'Page Builder Developer Tools'
define pb_role  = 'BAN_PAGEBUILDER_M'
define delete_option = 'y'
define create_option = 'y'

Prompt Create Object &&object_name (PageBuilder developers need to be granted access to this objects)
connect general/&&GENERAL_PASSWORD


delete from gubobjs where gubobjs_name = '&&object_name' and '&&delete_option'='y';

prompt Create GUBOBJS object &&object_name
Insert Into Gubobjs (
GUBOBJS_NAME     ,
GUBOBJS_DESC       ,
GUBOBJS_OBJT_CODE ,
GUBOBJS_SYSI_CODE ,
GUBOBJS_USER_ID    ,
GUBOBJS_ACTIVITY_DATE ,
GUBOBJS_HELP_IND  ,
Gubobjs_Extract_Enabled_Ind ,
Gubobjs_Data_Origin ,
GUBOBJS_UI_VERSION)
select  '&&object_name', '&&object_desc',
   'FORM', 'G', User, Sysdate, 'N', 'D', 'Banner', 'C'
from dual
where not exists ( select 1 from gubobjs where gubobjs_name = '&&object_name' and '&&create_option'='y');

commit;

connect bansecr/&&BANSECR_PASSWORD

delete from Guraobj where guraobj_object = '&&object_name' and '&&delete_option'='y';

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
   '9.0',
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

begin
  if '&&create_option'='y' then
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