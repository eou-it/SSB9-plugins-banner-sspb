--
-- page_role1.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  PAGE_ROLE
--    TYPE:    TABLE
--    DESCRIPTION:
--    Table with PageBuilder Page roles
--    PURPOSE:
--    This table stores the roles associated with PageBuilder Pages
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--

  CREATE TABLE PAGE_ROLE
   (
    ID                              NUMBER(19,0)             NOT NULL ,
    PAGE_ID                         NUMBER(19,0)             NOT NULL ,
    VERSION                         NUMBER(19,0)             NOT NULL ,
    ALLOW                           NUMBER(1,0)              NOT NULL ,
    ROLE_NAME                       VARCHAR2(30 CHAR)        NOT NULL
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
