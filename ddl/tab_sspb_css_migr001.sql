declare
  data_type VARCHAR2(30);
  data_len  INTEGER;
begin
  select c.data_type, c.char_length
  into data_type,data_len
  from all_tab_columns c
  where owner='SSPBMGR'
    and table_name='CSS'
    and column_name='DESCRIPTION';

  if data_type <> 'VARCHAR2' or data_len <> 255 then
    execute immediate 'alter table css rename column description to clob_desc';
    execute immediate 'alter table css add (description varchar2(255 char))';
    execute immediate
      'update css '||
      'set description = substr(clob_desc,1,255)';
    commit;
    execute immediate 'alter table css drop column clob_desc';
  end if;

end;
/

select '***WARNING*** Migration 001 failed'
from all_tab_columns
where owner='SSPBMGR'
  and table_name='CSS'
  and column_name='DESCRIPTION'
  and (data_type<>'VARCHAR2' or char_length<>255);
