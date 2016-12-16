--
-- virtual_domain_role1.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  VIRTUAL_DOMAIN_ROLE
--    TYPE:    TABLE
--    DESCRIPTION:
--    Table with PageBuilder Virtual Domain Roles
--    PURPOSE:
--    This table stores the roles and privileges associated with PageBuilder Virtual Domains
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--

  CREATE TABLE VIRTUAL_DOMAIN_ROLE
   (
    ID                              NUMBER(19,0)             NOT NULL ,
    VERSION                         NUMBER(19,0)             NOT NULL ,
    ALLOW_DELETE                    NUMBER(1,0)              NOT NULL ,
    ALLOW_GET                       NUMBER(1,0)              NOT NULL ,
    ALLOW_POST                      NUMBER(1,0)              NOT NULL ,
    ALLOW_PUT                       NUMBER(1,0)              NOT NULL ,
    ROLE_NAME                       VARCHAR2(30 CHAR)        NOT NULL ,
    VIRTUAL_DOMAIN_ID               NUMBER(19,0)             NOT NULL
   )
  STORAGE (INITIAL     &MMEDTAB_INITIAL_EXTENT
           NEXT        &MMEDTAB_NEXT_EXTENT
           MINEXTENTS  &MMEDTAB_MIN_EXTENTS
           MAXEXTENTS  &MMEDTAB_MAX_EXTENTS
           PCTINCREASE &MMEDTAB_PCT_INCREASE)
  TABLESPACE           &MMEDTAB_TABLESPACE_NAME
  PCTFREE              &MMEDTAB_PCT_FREE
  PCTUSED              &MMEDTAB_PCT_USED
;
