--
-- requestmap1.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  REQUESTMAP
--    TYPE:    TABLE
--    DESCRIPTION:
--    Requestmap table for Spring Security
--    PURPOSE:
--    This table stores the RequestMap data for Spring with SecurityConfigType.Requestmap
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--

  CREATE TABLE REQUESTMAP
   (
    ID                              NUMBER(19,0)             NOT NULL ,
    VERSION                         NUMBER(19,0)             NOT NULL ,
    CONFIG_ATTRIBUTE                VARCHAR2(4000 CHAR)      NOT NULL ,
    URL                             VARCHAR2(255 CHAR)       NOT NULL
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
