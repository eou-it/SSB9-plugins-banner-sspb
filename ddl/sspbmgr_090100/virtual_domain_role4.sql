--
-- virtual_domain_role4.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  VIRTUAL_DOMAIN_ROLE_FK1
--    TYPE:    REF_CONSTRAINT FK
--    DESCRIPTION:
--    Table with PageBuilder Virtual Domain Roles
--    PURPOSE:
--    This table stores the roles and privileges associated with PageBuilder Virtual Domains
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--

  ALTER TABLE VIRTUAL_DOMAIN_ROLE
    ADD CONSTRAINT VIRTUAL_DOMAIN_ROLE_FK1 FOREIGN KEY (VIRTUAL_DOMAIN_ID)
      REFERENCES VIRTUAL_DOMAIN (ID) ENABLE
;
