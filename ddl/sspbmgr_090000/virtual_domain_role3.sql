--
-- virtual_domain_role3.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  PK_VIRTUAL_DOMAIN_ROLE
--    TYPE:    CONSTRAINT PK
--    DESCRIPTION:
--    Table with PageBuilder Virtual Domain Roles
--    PURPOSE:
--    This table stores the roles and privileges associated with PageBuilder Virtual Domains
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--

  ALTER TABLE VIRTUAL_DOMAIN_ROLE
    ADD CONSTRAINT PK_VIRTUAL_DOMAIN_ROLE
    PRIMARY KEY (
      ID
               )
  USING INDEX
  STORAGE (INITIAL     &MMEDINX_INITIAL_EXTENT
           NEXT        &MMEDINX_NEXT_EXTENT
           MINEXTENTS  &MMEDINX_MIN_EXTENTS
           MAXEXTENTS  &MMEDINX_MAX_EXTENTS
           PCTINCREASE &MMEDINX_PCT_INCREASE)
  TABLESPACE           &MMEDINX_TABLESPACE_NAME
  PCTFREE              &MMEDINX_PCT_FREE
;
