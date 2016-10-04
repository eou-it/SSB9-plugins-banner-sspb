-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

SET define ON veri OFF trimsp OFF HEAD OFF FEEDB 2

-- Define placeholder variables needing customization

--The home page url defined below may need to be customized, depending on the deployment options.
define pagebuilder_home=/BannerExtensibility/

prompt "The value entered for the SYS password will not display."
accept sys_password char prompt "Enter the password for SYS: " hide

accept pb_tablespace_name char  default DEVELOPMENT prompt "Enter the tablespace name for PageBuilder objects (DEVELOPMENT is default):"

-- Define sizing values to run table script directly in sqlplus

define MMEDTAB_INITIAL_EXTENT    = 65536
define MMEDTAB_NEXT_EXTENT       = 1048576
define MMEDTAB_MIN_EXTENTS       = 1
define MMEDTAB_MAX_EXTENTS       = 2147483645
define MMEDTAB_PCT_INCREASE      = 0
define MMEDTAB_TABLESPACE_NAME   = '&&pb_tablespace_name'
define MMEDTAB_PCT_FREE          = 10
define MMEDTAB_PCT_USED          = 40

define MMEDINX_INITIAL_EXTENT    = 65536
define MMEDINX_NEXT_EXTENT       = 1048576
define MMEDINX_MIN_EXTENTS       = 1
define MMEDINX_MAX_EXTENTS       = 2147483645
define MMEDINX_PCT_INCREASE      = 0
define MMEDINX_TABLESPACE_NAME   = '&&pb_tablespace_name'
define MMEDINX_PCT_FREE          = 5


--  The install begins
connect baninst1/&&BANINST1_PASSWORD
spool &&splpref.banner_upgrade_pb.lis
define pb_installdir = 'sspbmgr_090000'
@@ &&pb_installdir/_pb_install.sql
spool off
exit
