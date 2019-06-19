/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/* global notifications */
$(function(){
    'use strict';
    if (window.extensibilityInfo.admin) {
        try {
            var extensibilityMenu = $($('#extensibility_title'), $('#toolsList'));
            var requestContext = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2))
            var url;
            if (extensibilityMenu.length === 0) {
                ToolsMenu.addSection("extensibility", $.i18n.prop("xe.menu.section.extensibility"));
            }
            ToolsMenu.addItem("uploadProperties", $.i18n.prop("xe.menu.extensibility.uploadProperties"), "extensibility",
                function () {
                    if(location.href.indexOf('/ssb/') > -1) {
                        url =  requestContext + '/ssb/uploadProperties';
                    } else {
                        url =  requestContext + '/uploadProperties';
                    }
                    return location.href = url;
                }
            );
            ToolsMenu.addItem("themeEditor", $.i18n.prop("xe.menu.extensibility.themeEditor"), "extensibility",
                function () {
                    if(location.href.indexOf('/ssb/') > -1) {
                        url =  requestContext + '/ssb/themeEditor';
                    } else {
                        url =  requestContext + '/themeEditor';
                    }
                    return location.href = url;
                }
            );
        } catch (e) {
            log.error('Failed to initiate Upload Properties and Theme Editor Tools menu. Exception: ' + e);
        }
    }
});
