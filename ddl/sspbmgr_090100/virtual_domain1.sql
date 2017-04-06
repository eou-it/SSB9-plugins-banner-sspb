--
-- virtual_domain1.sql
--
-- audit trail: pb9.0.0                      init       date
-- 1.                                          hvt      2016-06-08
--    project: pb
--    object:  virtual_domain
--    type:    table
--    description:
--    table with pagebuilder virtual domains
--    purpose:
--    this table stores the pagebuilder virtual domains
--    todo review/correct detailed description and purpose of object
-- audit trail end
--

  create table virtual_domain
   (
    service_name                    varchar2(60 char)        not null ,
    type_of_code                    varchar2(1 char),
    code_get                        clob                     not null ,
    code_delete                     clob,
    code_post                       clob,
    code_put                        clob,
    id                              number(19,0)             not null ,
    version                         number(19,0)             not null ,
                                    date_created timestamp (6),
    last_updated timestamp (6),
    file_timestamp timestamp (6)
   )
  storage (initial     &mmedtab_initial_extent
           next        &mmedtab_next_extent
           minextents  &mmedtab_min_extents
           maxextents  &mmedtab_max_extents
           pctincrease &mmedtab_pct_increase)
  tablespace           &mmedtab_tablespace_name
  pctfree              &mmedtab_pct_free
  pctused              &mmedtab_pct_used
;
