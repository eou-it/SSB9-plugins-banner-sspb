--ALTER SESSION SET CURRENT_SCHEMA=SSPBMGR;

-- Using readable names for now 
--- TODO: use Banner naming standards if this has to be Banner specific

Prompt Virtual Domain Role

  CREATE TABLE VIRTUAL_DOMAIN_ROLE 
   (ID NUMBER(19,0) NOT NULL , 
	VERSION NUMBER(19,0) NOT NULL , 
	ALLOW_DELETE NUMBER(1,0) NOT NULL , 
	ALLOW_GET NUMBER(1,0) NOT NULL , 
	ALLOW_POST NUMBER(1,0) NOT NULL , 
	ALLOW_PUT NUMBER(1,0) NOT NULL , 
	ROLE_NAME VARCHAR2(30 CHAR) NOT NULL , 
	VIRTUAL_DOMAIN_ID NUMBER(19,0) NOT NULL , 
	 PRIMARY KEY (ID), 
	 CONSTRAINT VIRTUAL_DOMAIN_ROLE_FK1 FOREIGN KEY (VIRTUAL_DOMAIN_ID)
	  REFERENCES VIRTUAL_DOMAIN (ID) 
   );
   
   CREATE OR REPLACE PUBLIC SYNONYM VIRTUAL_DOMAIN_ROLE FOR VIRTUAL_DOMAIN_ROLE;
   GRANT ALL ON VIRTUAL_DOMAIN_ROLE TO BANINST1;   