--/******************************************************************************
-- *  Copyright 2009-2016 Ellucian Company L.P. and its affiliates.             *
-- ******************************************************************************/

-- login.sql
-- Customized for install of PageBuilder

--TODO For release: replace u_pick_it with '#UPDATEME#'
define DBEU_PASSWORD     = 'u_pick_it'
define SYSTEM_PASSWORD   = 'u_pick_it'
define WTAILOR_PASSWORD  = 'u_pick_it'
define BANINST1_PASSWORD = 'u_pick_it'
define GENERAL_PASSWORD  = 'u_pick_it'
define SATURN_PASSWORD   = 'u_pick_it'
define FIMSMGR_PASSWORD  = 'u_pick_it'
define TAISMGR_PASSWORD  = 'u_pick_it'
define BANSECR_PASSWORD  = 'u_pick_it'
define SSPBMGR_PASSWORD  = 'u_pick_it'

define DEFAULT_SPOOL_DIR = './temp_output'
define META_DIR = '.\meta'
define ID_COLUMN_SUFFIX = 'surrogate_id'


--Define sizing values to run table script directly in sqlplus

define MMEDTAB_INITIAL_EXTENT    = 65536
define MMEDTAB_NEXT_EXTENT       = 1048576
define MMEDTAB_MIN_EXTENTS       = 1
define MMEDTAB_MAX_EXTENTS       = 2147483645
define MMEDTAB_PCT_INCREASE      = 0
define MMEDTAB_TABLESPACE_NAME   = 'DEVELOPMENT'
define MMEDTAB_PCT_FREE          = 10
define MMEDTAB_PCT_USED          = 40

define MMEDINX_INITIAL_EXTENT    = 65536
define MMEDINX_NEXT_EXTENT       = 1048576
define MMEDINX_MIN_EXTENTS       = 1
define MMEDINX_MAX_EXTENTS       = 2147483645 
define MMEDINX_PCT_INCREASE      = 0 
define MMEDINX_TABLESPACE_NAME   = 'DEVELOPMENT'
define MMEDINX_PCT_FREE          = 5
