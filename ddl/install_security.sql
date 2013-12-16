connect baninst1/&&BANINST1_PASSWORD

prompt Grant privileges
grant all on css to ban_ss_user;
grant all on page to ban_ss_user;
grant all on page_role to ban_ss_user;
grant all on requestmap to ban_ss_user;
grant all on virtual_domain to ban_ss_user;
grant all on virtual_domain_role to ban_ss_user;

grant all on css to banproxy;
grant all on page to banproxy;
grant all on page_role to banproxy;
grant all on requestmap to banproxy;
grant all on virtual_domain to banproxy;
grant all on virtual_domain_role to banproxy;

-- these privileges may be present already
grant SELECT on BANSECR.GURLOGN to BANPROXY;
grant SELECT on WTAILOR.TWGRMENU to BANPROXY;
grant SELECT on WTAILOR.TWGRROLE to BANPROXY;
grant SELECT on WTAILOR.TWGRWMRL to BANPROXY;
grant EXECUTE on BANINST1.GB_COMMON to BANPROXY;
grant EXECUTE on BANINST1.F_FORMAT_NAME to BANPROXY;
grant SELECT on BANSECR.GOVUROL to BANPROXY;
grant SELECT on BANSECR.GOVUROL1 to BANPROXY;
grant SELECT on BANSECR.GOVUROL2 to BANPROXY;
grant SELECT on BANSECR.GOVUROLFILTER to BANPROXY;
grant EXECUTE on BANSECR.GSPPRXY to BANPROXY;
grant SELECT on GENERAL.GOBEACC to BANPROXY;
grant SELECT on GENERAL.GOBEACC to BANPROXY;
grant SELECT on GENERAL.GOBUMAP to BANPROXY;
grant SELECT on GENERAL.GOBUMAP to BANPROXY;
grant SELECT on BANINST1.GOVROLE to BANPROXY;
grant SELECT on GENERAL.GUBINST to BANPROXY;
grant SELECT on SATURN.SPRIDEN to BANPROXY;