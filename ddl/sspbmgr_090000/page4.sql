--
-- page4.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  EXTENDS_PAGE_FK1
--    TYPE:    REF_CONSTRAINT FK
--    DESCRIPTION:
--    Table with PageBuilder Pages
--    PURPOSE:
--    This table stores the PageBuilder Pages
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--

  ALTER TABLE PAGE
    ADD CONSTRAINT EXTENDS_PAGE_FK1 FOREIGN KEY (EXTENDS_PAGE_ID)
      REFERENCES PAGE (ID) ENABLE
;
