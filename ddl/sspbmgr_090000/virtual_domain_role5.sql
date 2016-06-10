--
-- virtual_domain_role5.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  VIRTUAL_DOMAIN_ROLE_FK1_IDX
--    TYPE:    INDEX
--    DESCRIPTION:
--    Table with PageBuilder Virtual Domain Roles
--    PURPOSE:
--    This table stores the roles and privileges associated with PageBuilder Virtual Domains
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--


CREATE UNIQUE INDEX VIRTUAL_DOMAIN_ROLE_FK1_IDX ON VIRTUAL_DOMAIN_ROLE (VIRTUAL_DOMAIN_ID, ROLE_NAME)
  STORAGE (INITIAL     &MMEDINX_INITIAL_EXTENT
           NEXT        &MMEDINX_NEXT_EXTENT
           MINEXTENTS  &MMEDINX_MIN_EXTENTS
           MAXEXTENTS  &MMEDINX_MAX_EXTENTS
           PCTINCREASE &MMEDINX_PCT_INCREASE)
  TABLESPACE           &MMEDINX_TABLESPACE_NAME
  PCTFREE              &MMEDINX_PCT_FREE;
