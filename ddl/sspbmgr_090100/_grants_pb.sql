-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

-- This script grants privileges on PageBuilder specific database objects to Banner Users

prompt Grant privileges on SSPBMGR tables to Banner users

grant all on css to baninst1;
grant all on page to baninst1;
grant all on page_role to baninst1;
grant all on requestmap to baninst1;
grant all on virtual_domain to baninst1;
grant all on virtual_domain_role to baninst1;
grant select on sspbmgr.hibernate_sequence to baninst1;

grant all on css to ban_ss_user;
grant all on page to ban_ss_user;
grant all on page_role to ban_ss_user;
grant all on requestmap to ban_ss_user;
grant all on virtual_domain to ban_ss_user;
grant all on virtual_domain_role to ban_ss_user;
grant select on sspbmgr.hibernate_sequence to ban_ss_user;

grant all on css to banproxy;
grant all on page to banproxy;
grant all on page_role to banproxy;
grant all on requestmap to banproxy;
grant all on virtual_domain to banproxy;
grant all on virtual_domain_role to banproxy;
grant select on sspbmgr.hibernate_sequence to banproxy;
