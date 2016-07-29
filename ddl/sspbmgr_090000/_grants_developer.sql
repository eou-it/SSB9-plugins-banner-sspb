-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

-- This script grants minimum object privileges required to use PageBuilder Admin/Develop features.
-- To develop and debug virtual domains for Banner, additional grants to Banner tables may be needed.

set define on veri off
define pb_role  = 'BAN_PAGEBUILDER_M'
--Next grants are needed for Admin user to use PageBuilder developer tools
grant execute on GOKFGAC to &&pb_role;
grant execute on GB_COMMON to &&pb_role;
grant all on PAGE      to &&pb_role;   
grant all on PAGE_ROLE to &&pb_role;
grant all on VIRTUAL_DOMAIN to &&pb_role;
grant all on VIRTUAL_DOMAIN_ROLE to &&pb_role;
grant all on CSS  to &&pb_role;
grant select on sspbmgr.hibernate_sequence to &&pb_role;

grant all on REQUESTMAP to &&pb_role;
grant select on TWTVROLE to &&pb_role;
grant select on TWGRMENU to &&pb_role;

--
-- Include Oracle Object grants the developer needs to access to develop and test the virtual domains
-- We do not recommend grant any in production!