/*******************************************************************************
Copyright 2009-2015 Ellucian Company L.P. and its affiliates.
*******************************************************************************/

modules = {
//    'angular' {
//        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular', file: "angular.js"]
//        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular', file: "angular-resource.js"]
//        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular', file: "angular-sanitize.js"]
//    }

    'pageBuilder' {
//        dependsOn "angular"
        defaultBundle environment == "development" ? false : "pageBuilder"

        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/ng-grid', file: "ng-grid.js"]
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular-ui', file: "angular-ui.js"]
        // file upload
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/ng-upload', file: "ng-upload.js"]
        // modal dialog
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular-ui', file: "ui-bootstrap-tpls-0.3.0.js"]
        // page builder
        resource url:[plugin: 'banner-sspb', dir: 'js', file: "pbRunDirectives.js"]
        // uxd components
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/uxd/js', file: "xe-ui-components.js"]
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/uxd/css', file: "xe-ui-components.min.css"]


        // TODO bootstrap interferes with navigation bar  - caused by breadcrumb class in bootstrap.css. also ss_ui uses a later version of bootstrap
        // TODO use common-control.css breadcrumb instead

        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/bootstrap', file: "bootstrap.js"]
        //TODO: duplicate below
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular-ui', file: "ui-bootstrap-tpls-0.3.0.js"]

        // TODO loading order in page does not work --> cause angular module not found
        //resource url:[plugin: 'banner-sspb', dir: 'js', file: "pbRunApp.js"]
        resource url:[plugin: 'banner-sspb', file: 'js/misc/es5-shim.js'],
                disposition: 'head',
                wrapper: { s -> "<!--[if lt IE 9]>$s<![endif]-->" }
    }

    'pageBuilderLTR' {
        dependsOn "bannerSelfServiceCommonLTR, pageBuilder"
        defaultBundle environment == "development" ? false : "pageBuilderLTR"

        // below file (jquery-ui.css) fixes datepicker display issue
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'jquery-ui.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'ng-grid.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'angular-ui.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: "bootstrap_custom.css"]
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'main.css']
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'pbDefault.css']
    }

    'pageBuilderRTL' {
        dependsOn "bannerSelfServiceCommonRTL, pageBuilder"
        defaultBundle environment == "development" ? false : "pageBuilderRTL"

        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'jquery-ui-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'ng-grid-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'angular-ui-rtl.css']
        resource url:[plugin: 'banner-ui-ss', file: 'css/backbone.pagingcontrols-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: "bootstrap_custom-rtl.css"]
        resource url:[plugin: 'banner-sspb', file: 'css/banner-sspb-rtl-patch.css']
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'main-rtl.css']
        resource url:[plugin: 'banner-sspb', dir: 'css', file: 'pbDefault-rtl.css']
    }
    application {
        resource url:'js/application.js'
    }
}
