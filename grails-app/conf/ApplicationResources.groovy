/*******************************************************************************
Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
*******************************************************************************/

modules = {
    'pageBuilder' {
        //dependsOn "bannerSelfService" //modify to avoid extensibility
        dependsOn "bannerSelfServiceWithoutAurora, aurora"

        defaultBundle environment == "development" ? false : "pageBuilder"


        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'ng-grid.css']
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'angular-ui.css']

        // don't add angular.js twice (it is in the layout page) some button handler will be called twice!
        //resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular', file: "angular.js"]
        //resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular', file: "angular-resource.js"]


        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/ng-grid', file: "ng-grid.js"]
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular-ui', file: "angular-ui.js"]
        // file upload
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/ng-upload', file: "ng-upload.js"]
        // modal dialog
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular-ui', file: "ui-bootstrap-tpls-0.3.0.js"]
        resource url:[plugin: 'banner-sspb', dir: 'js', file: "pbRunDirectives.js"]

        // TODO bootstrap interferes with navigation bar  - caused by breadcrumb class in bootstrap.css. also ss_ui uses a later version of bootstrap
        // TODO use common-control.css breadcrumb instead

        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/css', file: "bootstrap_custom.css"]
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/bootstrap', file: "bootstrap.js"]
        //TODO: duplicate below
        resource url:[plugin: 'banner-sspb', dir: 'BannerXE/lib/angular-ui', file: "ui-bootstrap-tpls-0.3.0.js"]

        // TODO loading order in page does not work --> cause angular module not found
        //resource url:[plugin: 'banner-sspb', dir: 'js', file: "pbRunApp.js"]
        resource url:[plugin: 'banner-sspb', file: 'js/misc/es5-shim.js'],
                disposition: 'head',
                wrapper: { s -> "<!--[if lt IE 9]>$s<![endif]-->" }
    }


    'pageBuilderRTL' {
        dependsOn "bannerSelfServiceRTL"
        dependsOn "pageBuilder"
        defaultBundle environment == "development" ? false : "pageBuilderRTL"
        resource url:[plugin: 'banner-sspb', file: 'BannerXE/css/ng-grid-rtl.css']
        resource url:[plugin: 'banner-ui-ss', file: 'css/backbone.pagingcontrols-rtl.css']
        resource url:[plugin: 'banner-sspb', file: 'css/banner-sspb-rtl-patch.css']
    }
	
    application {
        resource url:'js/application.js'
    }


}
