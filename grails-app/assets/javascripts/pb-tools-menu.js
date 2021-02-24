/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/* global notifications */
$(function(){
    'use strict';
    if (user && user.isSuperUser) {
        try {
            var extensibilityMenu = $($('#extensibility_title'), $('#toolsList'));
            if (extensibilityMenu.length === 0) {
                ToolsMenu.addSection("extensibility", $.i18n.prop("xe.menu.section.extensibility"));
            }

            ToolsMenu.addItem("themeEditor", $.i18n.prop("xe.menu.extensibility.SecurityConfiguration"), "extensibility",
                function () {
                    return location.href =  window.location.pathname.substring(0, window.location.pathname.indexOf("/",2)) + '/customPage/page/pbadm.AdminSecurity';
                }
            );
        } catch (e) {
            log.error('Failed to add Admin Security Configuration option into Tools menu: ' + e);
        }
    }
});
