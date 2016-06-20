-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************
SET DEFINE ON VERI OFF;
--This script creates a Self Service Menu for PageBuilder

delete
from twgrmenu
where twgrmenu_name = 'bmenu.P_PBMainMnu'
or twgrmenu_url = 'bmenu.P_PBMainMnu';

delete
from twgrwmrl
where TWGRWMRL_NAME = 'bmenu.P_PBMainMnu';

delete
from twgbwmnu
where twgbwmnu_name = 'bmenu.P_PBMainMnu';

delete
from twtvmodu
where TWTVMODU_CODE = 'PB';

-- Create the page builder module
Insert into TWTVMODU
   (TWTVMODU_CODE, TWTVMODU_DESC, TWTVMODU_HEADER_CAPS_ON, TWTVMODU_DISPLAY_EXIT_IND, TWTVMODU_ACTIVITY_DATE)
 Values
   ('PB', 'Page Builder', 'N', 'N', SYSDATE);

-- Create the menu page
Insert into TWGBWMNU
   (TWGBWMNU_NAME, TWGBWMNU_DESC, TWGBWMNU_PAGE_TITLE, TWGBWMNU_HEADER, TWGBWMNU_TOP_RIGHT_IMAGE, TWGBWMNU_TOP_LEFT_IMAGE, TWGBWMNU_L_MARGIN_WIDTH, TWGBWMNU_R_MARGIN_WIDTH, TWGBWMNU_BACK_URL, TWGBWMNU_BACK_LINK, TWGBWMNU_BACK_MENU_IND, TWGBWMNU_MODULE, TWGBWMNU_ENABLED_IND, TWGBWMNU_INSECURE_ALLOWED_IND, TWGBWMNU_ACTIVITY_DATE, TWGBWMNU_CSS_URL, TWGBWMNU_CACHE_OVERRIDE, TWGBWMNU_SOURCE_IND, TWGBWMNU_ADM_ACCESS_IND)
 Values
   ('bmenu.P_PBMainMnu', 'Page Builder', 'Page Builder', 'Page Builder', 'TopRightBanner2000', 'TopLeftBanner2000', '30', '40', 'bmenu.P_MainMnu', 'Return to Menu', 'Y', 'PB', 'Y', 'N', SYSDATE, '/css/web_defaultmenu.css', 'S', 'B', 'N');

-- Grant page access to web tailor admin
Insert into TWGRWMRL
   (TWGRWMRL_NAME, TWGRWMRL_ROLE, TWGRWMRL_ACTIVITY_DATE, TWGRWMRL_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 'WTAILORADMIN', SYSDATE, 'B');

-- Create menu items
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_MainMnu', (select (nvl(max(twgrmenu_sequence),0) + 1) from twgrmenu where twgrmenu_name = 'bmenu.P_MainMnu'), 'XE Page Builder', 'bmenu.P_PBMainMnu', 'XE Page Builder Menu', 'Y', 'Y', 'Y', SYSDATE, 'B');


 Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 1, 'Page Builder Home Page', '&&pagebuilder_home.', 'Got to home page on xe self-service page builder application', 'Y', 'N', 'N', SYSDATE, 'B');
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 2, 'Virtual Domain Composer', '&&pagebuilder_home.virtualDomainComposer/loadVirtualDomain', 'Create and maintain virtual domains', 'Y', 'N', 'N', SYSDATE, 'B');
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 3, 'Visual Page Composer', '&&pagebuilder_home.visualPageModelComposer/loadComposerPage', 'Create and maintain virtual pages', 'Y', 'N', 'N', SYSDATE, 'B');
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 4, 'CSS Stylesheet Manager', '&&pagebuilder_home.cssManager/loadCssManagerPage', 'Create and maintain css stylesheets', 'Y', 'N', 'N', SYSDATE, 'B');
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 5, 'Page Roles', '&&pagebuilder_home.customPage/page/pbadm.PageRoles', 'Maintain access to virtual pages', 'Y', 'N', 'N', SYSDATE, 'B');
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 6, 'Virtual Domain Roles', '&&pagebuilder_home.customPage/page/pbadm.VirtualDomainRoles', 'Maintain access to virtual domains', 'Y', 'N', 'N', SYSDATE, 'B');
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 7, 'Export Pages', '&&pagebuilder_home.customPage/page/pbadm.ExportPages', 'Export virtual page metadata to a file', 'Y', 'N', 'N', SYSDATE, 'B');
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 8, 'Export Virtual Domains', '&&pagebuilder_home.customPage/page/pbadm.ExportVirtualDomains', 'Export virtual domain metadata to a file', 'Y', 'N', 'N', SYSDATE, 'B');
Insert into TWGRMENU
   (TWGRMENU_NAME, TWGRMENU_SEQUENCE, TWGRMENU_URL_TEXT, TWGRMENU_URL, TWGRMENU_URL_DESC, TWGRMENU_ENABLED, TWGRMENU_DB_LINK_IND, TWGRMENU_SUBMENU_IND, TWGRMENU_ACTIVITY_DATE, TWGRMENU_SOURCE_IND)
 Values
   ('bmenu.P_PBMainMnu', 9, 'Export CSS Stylesheet', '&&pagebuilder_home.customPage/page/pbadm.ExportCss', 'Export CSS Stylesheet metadata to a file', 'Y', 'N', 'N', SYSDATE, 'B');
COMMIT;
