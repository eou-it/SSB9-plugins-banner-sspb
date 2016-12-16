--
-- page_role4.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  PAGE_ROLE_FK1
--    TYPE:    REF_CONSTRAINT FK
--    DESCRIPTION:
--    Table with PageBuilder Page roles
--    PURPOSE:
--    This table stores the roles associated with PageBuilder Pages
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--

  ALTER TABLE PAGE_ROLE
    ADD CONSTRAINT PAGE_ROLE_FK1 FOREIGN KEY (PAGE_ID)
      REFERENCES PAGE (ID) ENABLE
;
