/*******************************************************************************
 Copyright 2019-2021 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/* global notifications */
$(function(){
    'use strict';
    if (user && user.isSuperUser) {
        try {
            var extensibilityMenu = $($('#extensibility_title'), $('#toolsList'));
            var url;
            if (extensibilityMenu.length === 0) {
                ToolsMenu.addSection("extensibility", $.i18n.prop("xe.menu.section.extensibility"));
            }

            ToolsMenu.addItem("securityConfiguration", $.i18n.prop("xe.menu.extensibility.SecurityConfiguration"), "extensibility",
                function () {
                    url =  $('meta[name=requestContext]').attr("content") + '/customPage/page/pbadm.AdminSecurity';
                    return location.href = url;
                }
            );
        } catch (e) {
            log.error('Failed to add Admin Security Configuration option into Tools menu: ' + e);
        }
    }
});
