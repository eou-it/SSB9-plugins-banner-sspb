<%@ page import="net.hedtech.banner.sspb.PBUser;" contentType="text/html;charset=UTF-8" %>
<%--
/*********************************************************************************
 Copyright 2009-2012 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
--%>
<!DOCTYPE html>
<html ng-app="BannerOnAngular" lang="${message(code: 'default.language.locale')}" dir="${message(code:'default.language.direction')}">
    <head>
        <r:require module="pageBuilder"/>

        <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
           <r:require module="pageBuilderRTL"/>
        </g:if>
        <g:set var="mep" value="${org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()?.request?.session?.getAttribute('ssbMepDesc')}"/>

        <meta charset="${message(code: 'default.character.encoding')}"/>
        <meta name="dir" content="${message(code:'default.language.direction')}"/>
        <meta name="synchronizerToken" content="${org.codehaus.groovy.grails.web.servlet.mvc.SynchronizerTokensHolder.store( session ).generateToken(request.forwardURI)}"/>

        <meta name="maxInactiveInterval" content="${session.maxInactiveInterval}"/>
        <meta name="transactionTimeout" content="${session.getServletContext().transactionTimeout}"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <link rel="apple-touch-icon" href="images/applicationIcon.png" />
        <link rel="apple-touch-startup-image" href="images/applicationStartup.png">
        <meta name="keepAliveURL" content="${createLink(controller:'keepAlive')}"/>
        <meta name="ssbMepDesc" content="${!mep ? '' : mep}"/>
        <meta name="fullName" content="${g.fullName()}"/>


        <title><g:layoutTitle default="Banner Page Builder"/></title>

        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon"/>

        <r:script>
            <g:i18nJavaScript/>

            var transactionTimeoutMeta    = $( "meta[name=transactionTimeout]" ),
                transactionTimeoutSeconds = ( transactionTimeoutMeta.length == 1 ? parseInt( transactionTimeoutMeta.attr( "content" ) ) : 30 ),
                transactionTimeoutPadding = 10 * 1000,
                transactionTimeoutMilli   = ( transactionTimeoutSeconds * 1000 ) + transactionTimeoutPadding;

            $.ajaxSetup( { timeout: transactionTimeoutMilli } );

            yepnope({
               test : window.JSON,
               nope : '${resource(plugin: 'banner-ui-ss', file: 'js/json2.js')}'
            });

            $(window).load(function() {
                _.defer( function() {
                    $( "#splash" ).remove();
                });
            });
        </r:script>

        <r:layoutResources/>

        <!-- TODO begin from Harry -->

        <!--  below file fixes datepicker display issue -->
        <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'jquery-ui.css')}" type="text/css">
        <!--  below file fixes Error: $digest already in progress issue -->
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/jquery" file="jquery-ui-1.8.24.custom.js" />"> </script>

        <!-- above are 2 duplicate imports of newer versions than in ui-ss-->

        <g:set var="localeLanguage"    value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" scope="page" />
        <g:set var="localeBrowserFull" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).toString().replace('_','-')}" scope="page" />

        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular" file="angular.js" />"></script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular" file="angular-resource.js"/>"></script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular" file="angular-sanitize.js"/>"></script>

        <g:if test="${localeLanguage!='en'}">
            <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/jquery" file="jquery.ui.datepicker-${localeLanguage}.js" />"> </script>
        </g:if>
        <g:if test="${localeBrowserFull!='en-US'}">
            <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/jquery" file="jquery.ui.datepicker-${localeBrowserFull}.js" />"> </script>
        </g:if>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular/i18n" file="angular-locale_${localeLanguage}.js" />"> </script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular/i18n" file="angular-locale_${localeBrowserFull.toLowerCase()}.js" />"> </script>
        <!-- end from Harry -->

        <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
            <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'main-rtl.css')}">
        </g:if>
        <g:else>
            <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'main.css')}">
        </g:else>

        <script type="text/javascript">
            var rootWebApp = ${createLink(uri: '/')};  //use in controller restful interface
            var templatesLocation = "<g:resource plugin="banner-sspb" dir="template" />";
            var user = ${PBUser.get()?.encodeAsJSON()};
            var gridLocale = '${localeBrowserFull.toLowerCase()}';
            var params = ${params?.encodeAsJSON()};
        </script>

        <!-- layout head contains angular module declaration and need to be placed before app.js -->
        <g:layoutHead />
        <%--  next resources seem not needed by pagebuilder - comment out and remove if no issues occur.
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/js" file="controllers.js" />"> </script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/js" file="services.js" />"> </script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/js" file="directives.js" />"> </script>
        --%>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/js" file="app.js" />"> </script>

        <g:customStylesheetIncludes/>

        <style>
            *.margin
            {
                margin-top: 10px;
                margin-left:10px;
                margin-right:10px;
                overflow-y:scroll;
                overflow-x:auto;
            }
            div.customPage {
                text-align:start;
                overflow-x: auto;
                overflow-y: auto;
                margin: 4px;
                padding: 0;
                width:99%;

                position: absolute;
                top: 110px;
                bottom: 30px;
                left:0;	/* rtl fix for ie */
            }
        </style>

    </head>
    <body>
        <div id="splash"></div>
        <div id="spinner" class="spinner spinner-img" style="display:none;">

        </div>

        <g:layoutBody />

        <r:layoutResources/>

        <g:customJavaScriptIncludes/>

    <script type="text/javascript">
        window.ngGrid.i18n[gridLocale] = {
            ngAggregateLabel:          '${message(code: 'nggrid.ngAggregateLabel'         , encodeAs: 'JavaScript')}',
            ngGroupPanelDescription:   '${message(code: 'nggrid.ngGroupPanelDescription'  , encodeAs: 'JavaScript')}',
            ngSearchPlaceHolder:       '${message(code: 'nggrid.ngSearchPlaceHolder'      , encodeAs: 'JavaScript')}',
            ngMenuText:                '${message(code: 'nggrid.ngMenuText'               , encodeAs: 'JavaScript')}',
            ngShowingItemsLabel:       '${message(code: 'nggrid.ngShowingItemsLabel'      , encodeAs: 'JavaScript')}',
            ngTotalItemsLabel:         '${message(code: 'nggrid.ngTotalItemsLabel'        , encodeAs: 'JavaScript')}',
            ngSelectedItemsLabel:      '${message(code: 'nggrid.ngSelectedItemsLabel'     , encodeAs: 'JavaScript')}',
            ngPageSizeLabel:           '${message(code: 'nggrid.ngPageSizeLabel'          , encodeAs: 'JavaScript')}',
            ngPagerFirstTitle:         '${message(code: 'nggrid.ngPagerFirstTitle'        , encodeAs: 'JavaScript')}',
            ngPagerNextTitle:          '${message(code: 'nggrid.ngPagerNextTitle'         , encodeAs: 'JavaScript')}',
            ngPagerPrevTitle:          '${message(code: 'nggrid.ngPagerPrevTitle'         , encodeAs: 'JavaScript')}',
            ngPagerLastTitle:          '${message(code: 'nggrid.ngPagerLastTitle'         , encodeAs: 'JavaScript')}',
            direction:                 '${message(code:'default.language.direction')}',
            styleLeft:                 '${message(code:'style.left')}',
            styleRight:                '${message(code:'style.right')}',
            maxPageLabel:              '${message(code:'nggrid.maxPageLabel'             , encodeAs: 'JavaScript')}',
            pageLabel:                 '${message(code:'nggrid.pageLabel'                , encodeAs: 'JavaScript')}'
        };
    </script>
    </body>
</html>
