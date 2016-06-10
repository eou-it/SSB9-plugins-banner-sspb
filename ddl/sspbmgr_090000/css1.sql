--
-- css1.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  CSS
--    TYPE:    TABLE
--    DESCRIPTION:
--    Table with PageBuilder CSS
--    PURPOSE:
--    This table stores the PageBuilder CSS code available for import in pages
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--

  CREATE TABLE CSS
   (
    CONSTANT_NAME                   VARCHAR2(60 CHAR)        NOT NULL ,
    CSS                             CLOB                     NOT NULL ,
    DESCRIPTION                     VARCHAR2(255 CHAR),
    ID                              NUMBER(19,0)             NOT NULL ,
    VERSION                         NUMBER(19,0)             NOT NULL ,
                                    DATE_CREATED TIMESTAMP (6),
    LAST_UPDATED TIMESTAMP (6),
    FILE_TIMESTAMP TIMESTAMP (6)
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
