/*******************************************************************************
Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
*******************************************************************************/

modules = {
    'angular' {
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/load-angular-locale.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-resource.min.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-route.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-sanitize.min.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-animate.min.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-ui-router.min.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-messages.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-translate.min.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/ui-bootstrap-tpls.min.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/lrInfiniteScroll.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/moment.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-common.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/angular-dateparser.min.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular-components/locale-numeric-input/custom-number-input.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular-components/locale-numeric-input/directives/currency-directive.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular-components/locale-numeric-input/directives/decimal-directive.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular-components/locale-numeric-input/directives/percent-directive.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular-components/locale-numeric-input/directives/only-number.js']
        resource url:[plugin: 'banner-ui-ss',file: 'css/custom-number-input/custom-number-input.css'],     attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular-components/locale-numeric-input/services/readonly-service.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular-components/date-picker/directives/date-picker-directive.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular-components/i18n/i18n-filter.js']
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/hotkeys.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/shortcuts/bannershortcuts.js']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular', file: "angular-messages.js"]
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular', file: "angular-translate.js"]

    }

    'pageBuilderDev' {
        dependsOn "angular"
        // file upload
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/ng-upload', file: "ng-upload.js"]
        // modal dialog
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular-ui', file: "ui-bootstrap-tpls-0.3.0.js"]
        resource url:[plugin: 'banner-sspb', dir: 'js', file: "pbDirectives.js"]
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/hotkeys.js']
    }

    'pageBuilder' {
        dependsOn "angular"
        defaultBundle environment == "development" ? false : "pageBuilder"

        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/ng-grid', file: "ng-grid.js"]
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular-ui', file: "angular-ui.js"]
        // page builder pages at runtime
        resource url:[plugin: 'banner-sspb', dir: 'js', file: "pbRunDirectives.js"]
        // uxd components
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/xe-components/js', file: "xe-ui-components.js"]

        resource url:[plugin: 'banner-ui-ss', dir: 'bootstrap/js', file: "bootstrap.js"]
        resource url:[plugin: 'banner-sspb', dir: 'js', file: "modelPoppup.js"]
        resource url:[plugin: 'banner-sspb', dir: 'js', file: "pbRunApp.js"]
        //Load newer version of jquery required for date picker
//        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/jquery', file:  'jquery-1.8.2.js'], disposition: 'head'
        //TODO jquery 1.9.1 is not supported by Aurora - update Aurora
//        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/jquery', file:  'jquery-1.9.1.min.js'], disposition: 'head'
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/jquery', file:  'jquery-ui-1.8.24.custom.js'] , disposition: 'head'
        resource url:[plugin: 'banner-ui-ss', file: 'js/menu/tools-menu.js']
        resource url: [plugin: 'web-app-extensibility', file: 'js/extensibility-angular/xe-angular.js']
        resource url:[plugin: 'banner-ui-ss', file: 'css/shortcuts/bannershortcuts.css', attrs:[media:'screen, projection']]
        resource url: [plugin: 'banner-ui-ss', file: 'css/preference/userpreference.css', attrs: [media: 'screen, projection']]
        resource url:[plugin: 'banner-ui-ss',file: 'js/angular/hotkeys.js']
        resource url: [plugin: 'banner-ui-ss', file: 'js/about/about.js']
        resource url: [plugin: 'banner-ui-ss', file: 'js/preference/userpreference.js']
        resource url:[plugin: 'banner-sspb', file:'js/pb-tools-menu.js']
    }

    'pageBuilderLTR' {
        dependsOn "min-bannerWebLTR, pageBuilder"
        defaultBundle environment == "development" ? false : "pageBuilderLTR"

        // below file (jquery-ui.css) fixes datepicker display issue
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'jquery-ui.css']
        resource url:[plugin: 'banner-sspb', file: 'BannerXE/css/ng-grid.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'angular-ui.css']
        resource url:[plugin: 'banner-ui-ss', dir: 'bootstrap/css', file: 'bootstrap.css']
        resource url: [plugin: 'banner-ui-ss', file: 'css/preference/userpreference.css', attrs: [media: 'screen, projection']]
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/xe-components/css', file: "xe-ui-components.css"]
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'main.css']
        resource url:[plugin: 'banner-sspb',  file: 'css/pbDefault.css']
        resource url:[plugin: 'banner-ui-ss', file: 'css/shortcuts/bannershortcuts.css', attrs:[media:'screen, projection']]
        resource url:[plugin: 'banner-ui-ss', file: 'js/select2/select2.css'],           attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/eds.css'],             attrs:[media:'screen, projection']
    }

    'pageBuilderRTL' {
        dependsOn "min-bannerWebRTL, pageBuilder"
        defaultBundle environment == "development" ? false : "pageBuilderRTL"

        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'jquery-ui-rtl.css']
        resource url:[plugin: 'banner-sspb', file: 'BannerXE/css/ng-grid-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'angular-ui-rtl.css']
        resource url:[plugin: 'banner-ui-ss', dir: 'bootstrap/css', file: 'bootstrap-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/xe-components/css', file: "xe-ui-components.min-rtl.css"]
        resource url:[plugin: 'banner-sspb', file: 'css/banner-sspb-rtl-patch.css']
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'main-rtl.css']
        resource url:[plugin: 'banner-sspb',  file: 'css/pbDefault-rtl.css']
        resource url: [plugin: 'banner-ui-ss', file: 'css/shortcuts/bannershortcuts-rtl.css', attrs: [media: 'screen, projection']]
        resource url: [plugin: 'banner-ui-ss', file: 'css/preference/userpreference-rtl.css', attrs: [media: 'screen, projection']]
        resource url: [plugin: 'banner-ui-ss', file: 'css/preference/userpreference-rtl-patch.css', attrs: [media: 'screen, projection']]
        resource url:[plugin: 'banner-ui-ss', file: 'js/select2/select2-rtl.css'],           attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/eds-rtl.css'],             attrs:[media:'screen, projection']
    
    }

    // A reduced set of dependencies compared to the resources in banner-ui-ss
    'min-bannerWeb' {
        dependsOn "jquery" //removed i18n-core as multi-calendar is not supported in PageBuilder layout gets messed up

        resource url:[plugin: 'banner-ui-ss', file: 'js/html5shim.js'],
                disposition: 'head',
                wrapper: { s -> "<!--[if lt IE 9]>$s<![endif]-->" }

        resource url:[plugin: 'banner-ui-ss', file: 'js/underscore.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/underscore.string.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/backbone.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/common/backbone-custom.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/backbone.modelbinding.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/backbone.datagridview.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/backbone.pagedcollection.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/jquery-plugins/jquery.i18n.properties.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/yepnope.1.0.1-min.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/log4javascript.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/common/activity-timer.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/modernizr-2.5.3.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/ICanHaz.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/handlebars.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/common/logging.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/common/common.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/jquery-plugins/jquery.sghe.dirtycheck.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/common/notification-center.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/jquery-plugins/jquery.hoverintent.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/jquery-plugins/jquery.jeditable.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/jquery-plugins/jquery.simplemodal-1.4.1.js']
        resource url:[plugin: 'banner-ui-ss', file: 'js/jquery-plugins/jquery.jeditable.datepicker.js']
        resource url: [plugin: 'web-app-extensibility', file: 'js/extensibility-common/xe-common.js']
        // Removed resources not needed in PB
    }


    'min-bannerWebLTR' {
        dependsOn "min-bannerWeb, aurora"
        defaultBundle environment == "development" ? false : "bannerSelfServiceLTR"

        resource url:[plugin: 'banner-ui-ss', file: 'css/banner-ui-ss.css'],             attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/notification-center.css'],      attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/jquery/jquery.ui.tooltip.css'], attrs:[media:'screen, projection']

        // next resource used by Pagination options in PageBuilder Grid
        resource url:[plugin: 'banner-ui-ss', file: 'css/backbone.pagingcontrols.css'],  attrs:[media:'screen, projection']
        resource url: [plugin: 'web-app-extensibility', file: 'css/extensibility-ss.css'], attrs: [media: 'screen, projection']
    }

    'min-bannerWebRTL' {
        dependsOn "min-bannerWeb, auroraRTL, extensibilityCommonRTL"
        defaultBundle environment == "development" ? false : "bannerSelfServiceRTL"

        resource id: 'themeRTL', url:[plugin:'banner-ui-ss', dir:'css/themeroller/jquery-ui-1.8.13-lt.gry.ov/css/custom-theme', file:'jquery-ui-1.8.13.custom-rtl.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/banner-ui-ss-rtl.css'],             attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/banner-ui-ss-rtl-patch.css'],       attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/notification-center-rtl.css'],      attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/jquery/jquery.ui.tooltip-rtl.css'], attrs:[media:'screen, projection']
        // next resource is used by Pagination options in PageBuilder Grid
        resource url:[plugin: 'banner-ui-ss', file: 'css/backbone.pagingcontrols-rtl.css'],  attrs:[media:'screen, projection']
        //resource url:[plugin: 'i18n-core', file: 'css/multiCalendar-rtl-patch.css']
    }

    application {
        resource url:'js/application.js'
    }
}
