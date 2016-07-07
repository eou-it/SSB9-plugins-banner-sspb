--
-- css2.sql
--
-- AUDIT TRAIL: PB9.0.0                      INIT       DATE
-- 1.                                          HvT      2016-06-08
--    PROJECT: PB
--    OBJECT:  CSS
--    TYPE:    COMMENT
--    DESCRIPTION:
--    Table with PageBuilder CSS
--    PURPOSE:
--    This table stores the PageBuilder CSS code available for import in pages
--    TODO Review/Correct Detailed Description and purpose of Object
-- AUDIT TRAIL END
--
COMMENT ON TABLE CSS  IS
'Table with PageBuilder CSS';
COMMENT ON COLUMN CSS.CONSTANT_NAME IS
'Name of CSS';
COMMENT ON COLUMN CSS.CSS IS
'CSS code';
COMMENT ON COLUMN CSS.DESCRIPTION IS
'Description  of CSS';
COMMENT ON COLUMN CSS.ID IS
'Id generated from sequence';
COMMENT ON COLUMN CSS.VERSION IS
'Version';
COMMENT ON COLUMN CSS.DATE_CREATED IS
'Date Created';
COMMENT ON COLUMN CSS.LAST_UPDATED IS
'Date Updated';
COMMENT ON COLUMN CSS.FILE_TIMESTAMP IS
'Timestamp of file - used with export and import';
