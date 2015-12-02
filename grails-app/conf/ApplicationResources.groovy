/*******************************************************************************
Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
*******************************************************************************/

modules = {
    'angular' {
        resource url:[plugin: 'banner-ui-ss', dir: 'js/angular/', file: 'angular.js']
        resource url:[plugin: 'banner-ui-ss', dir: 'js/angular/', file: 'angular-resource.js']
        resource url:[plugin: 'banner-ui-ss', dir: 'js/angular/', file: 'angular-sanitize.min.js']
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
        resource url:[plugin: 'banner-sspb', dir: 'js', file: "pbRunApp.js"]
        //Load newer version of jquery required for date picker
//        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/jquery', file:  'jquery-1.8.2.js'], disposition: 'head'
        //TODO jquery 1.9.1 is not supported by Aurora - update Aurora
//        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/jquery', file:  'jquery-1.9.1.min.js'], disposition: 'head'
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/jquery', file:  'jquery-ui-1.8.24.custom.js'] , disposition: 'head'
    }

    'pageBuilderLTR' {
        dependsOn "min-bannerWebLTR, pageBuilder"
        defaultBundle environment == "development" ? false : "pageBuilderLTR"

        // below file (jquery-ui.css) fixes datepicker display issue
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'jquery-ui.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'ng-grid.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'angular-ui.css']
        resource url:[plugin: 'banner-ui-ss', dir: 'bootstrap/css', file: 'bootstrap.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/xe-components/css', file: "xe-ui-components.min.css"]
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'main.css']
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'pbDefault.css']
    }

    'pageBuilderRTL' {
        dependsOn "min-bannerWebRTL, pageBuilder"
        defaultBundle environment == "development" ? false : "pageBuilderRTL"

        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'jquery-ui-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'ng-grid-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'angular-ui-rtl.css']
        resource url:[plugin: 'banner-ui-ss', dir: 'bootstrap/css', file: 'bootstrap-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/xe-components/css', file: "xe-ui-components.min.css"]
        resource url:[plugin: 'banner-sspb', file: 'css/banner-sspb-rtl-patch.css']
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'main-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'pbDefault-rtl.css']
    }

    // A reduced set of dependencies compared to the resources in banner-ui-ss
    'min-bannerWeb' {
        dependsOn "jquery, i18n-core"

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
        // Removed resources not needed in PB
    }

    'min-bannerWebLTR' {
        dependsOn "min-bannerWeb, min-auroraLTR"
        defaultBundle environment == "development" ? false : "bannerSelfServiceLTR"

        resource url:[plugin: 'banner-ui-ss', file: 'css/banner-ui-ss.css'],             attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/notification-center.css'],      attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/jquery/jquery.ui.tooltip.css'], attrs:[media:'screen, projection']

        // used by Pagination options in PageBuilder Grid
        resource url:[plugin: 'banner-ui-ss', file: 'css/backbone.pagingcontrols.css'],  attrs:[media:'screen, projection']
    }

    'min-bannerWebRTL' {
        dependsOn "min-bannerWeb, min-auroraRTL"
        defaultBundle environment == "development" ? false : "bannerSelfServiceRTL"

        resource id: 'themeRTL', url:[plugin:'banner-ui-ss', dir:'css/themeroller/jquery-ui-1.8.13-lt.gry.ov/css/custom-theme', file:'jquery-ui-1.8.13.custom-rtl.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/banner-ui-ss-rtl.css'],             attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/notification-center-rtl.css'],      attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/jquery/jquery.ui.tooltip-rtl.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/banner-ui-ss-rtl-patch.css'],       attrs:[media:'screen, projection']
        resource url:[plugin: 'i18n-core', file: 'css/multiCalendar-rtl-patch.css']
    }

    'min-auroraLTR' {
        dependsOn "auroraCommon"
        defaultBundle environment == "development" ? false : "aurora"

        resource url:[plugin: 'sghe-aurora', file: 'css/common-controls.css'], attrs:[media:'screen, projection']
//        resource url:[plugin: 'sghe-aurora', file: 'css/common-platform.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-header.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-breadcrumb.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-menu.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-tools.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-profile.css'], attrs:[media:'screen, projection']
    }

    'min-auroraRTL' {
        dependsOn "auroraCommon"
        resource url:[plugin: 'sghe-aurora', file: 'css/common-controls-rtl.css'], attrs:[media:'screen, projection']
//        resource url:[plugin: 'sghe-aurora', file: 'css/common-platform-rtl.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/rtl.css'],       attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-header-rtl.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-header-rtl-patch.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-menu-rtl.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-tools-rtl.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-profile-rtl.css'], attrs:[media:'screen, projection']
        resource url:[plugin: 'sghe-aurora', file: 'css/aurora-breadcrumb-rtl.css'], attrs:[media:'screen, projection']
     }

    application {
        resource url:'js/application.js'
    }
}
