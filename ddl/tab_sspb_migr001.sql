set serveroutput on size unlimited;
Prompt add timestamp columns to main SSPB tables
declare
  function has_column(tab_name varchar2, col_name varchar2, col_type varchar2) return boolean is
  begin
    for c in (
      select 1
      from all_tab_columns c
      where owner='SSPBMGR'
        and table_name=tab_name
        and column_name=col_name
        and data_type like col_type||'%'
    ) loop
      return true;
    end loop;
    return false;  
  end;
  
  procedure add_timestamp_column(tab_name varchar2, col_name varchar2) is
  begin
    if has_column(tab_name, col_name, 'TIMESTAMP') then
      dbms_output.put_line('Skipping '|| tab_name ||' - already has '||col_name);
    else
      dbms_output.put_line('Adding '||col_name ||' to '|| tab_name );
      execute immediate 'alter table '||tab_name||' add ('||col_name||' TIMESTAMP )';
    end if;
  end;
  
  procedure process_table(tab_name VARCHAR2) is
  begin
    dbms_output.put_line('Table: '|| tab_name);
    add_timestamp_column(tab_name,'DATE_CREATED');
    add_timestamp_column(tab_name,'LAST_UPDATED');
    add_timestamp_column(tab_name,'FILE_TIMESTAMP');
  end;
  

begin
  process_table('CSS');
  process_table('PAGE');
  process_table('VIRTUAL_DOMAIN');
end;
/