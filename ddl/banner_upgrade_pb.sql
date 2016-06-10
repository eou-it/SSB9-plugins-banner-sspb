-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

SET define ON veri OFF trimsp OFF HEAD OFF FEEDB 2

connect baninst1/&&BANINST1_PASSWORD
spool banner_upgrade_pb.lis
define pb_installdir = 'sspbmgr_090000'
@@ &&pb_installdir/_pb_install.sql
spool off
