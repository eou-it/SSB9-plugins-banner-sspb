--ALTER SESSION SET CURRENT_SCHEMA=SSPBMGR;

-- Using readable names for now 
--- TODO: use Banner naming standards if this has to be Banner specific

Prompt Virtual Domain

  CREATE TABLE VIRTUAL_DOMAIN  (
    SERVICE_NAME VARCHAR2(60 CHAR) NOT NULL,
	TYPE_OF_CODE VARCHAR2(1 CHAR),
	DATA_SOURCE VARCHAR2(1 CHAR) DEFAULT 'B',
	CODE_GET CLOB NOT NULL, 
	CODE_DELETE CLOB, 
	CODE_POST CLOB, 
	CODE_PUT CLOB,
	ID NUMBER(19,0) NOT NULL, 
	VERSION NUMBER(19,0) NOT NULL, 
    --	 
    PRIMARY KEY (ID),
    UNIQUE(SERVICE_NAME)
   ); 
   
   CREATE OR REPLACE PUBLIC SYNONYM VIRTUAL_DOMAIN FOR VIRTUAL_DOMAIN;
   GRANT ALL ON VIRTUAL_DOMAIN TO BANINST1;
   
/* MIGRATE
insert into virtual_domain (id,version,service_name,type_of_code,code_get,code_delete,code_post,code_put)
select rownum,1,name,'S',code_get,code_delete,code_post,code_put
from SCOTT.virtual_domain
*/   