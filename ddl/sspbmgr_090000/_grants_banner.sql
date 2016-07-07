-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

-- This script grants privileges on PageBuilder specific database objects to Banner Users

prompt Grant privileges on Banner objects

-- Privileges may be present already - Errors were observed in PageBuilder without these privileges
grant select on bansecr.gurlogn to banproxy;
grant select on wtailor.twgrmenu to banproxy;
grant select on wtailor.twgrrole to banproxy;
grant select on wtailor.twgrwmrl to banproxy;
grant execute on baninst1.gb_common to banproxy;
grant execute on baninst1.f_format_name to banproxy;
grant select on bansecr.govurol to banproxy;
grant select on bansecr.govurol1 to banproxy;
grant select on bansecr.govurol2 to banproxy;
grant select on bansecr.govurolfilter to banproxy;
grant execute on bansecr.gspprxy to banproxy;
grant select on general.gobeacc to banproxy;
grant select on general.gobeacc to banproxy;
grant select on general.gobumap to banproxy;
grant select on general.gobumap to banproxy;
grant select on baninst1.govrole to banproxy;
grant select on general.gubinst to banproxy;
grant select on saturn.spriden to banproxy;