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
        <g:set var="localeLanguage"    value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" scope="page" />
        <g:set var="localeBrowserFull" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).toString().replace('_','-')}" scope="page" />


        <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
            <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'anish-app-rtl.css')}">
        </g:if>
        <g:else>
            <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'BannerXE/css', file: 'anish-app.css')}">
        </g:else>

        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular" file="angular.js" />"></script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular" file="angular-resource.js"/>"></script>

        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/jquery" file="jquery.ui.datepicker-${localeLanguage}.js" />"> </script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/jquery" file="jquery.ui.datepicker-${localeBrowserFull}.js" />"> </script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular/i18n" file="angular-locale_${localeLanguage}.js" />"> </script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/lib/angular/i18n" file="angular-locale_${localeBrowserFull.toLowerCase()}.js" />"> </script>
        <!-- end from Harry -->

        <script type="text/javascript">
            var rootWebApp = ${createLink(uri: '/')};  //use in controller restful interface
            var templatesLocation = "<g:resource plugin="banner-sspb" dir="template" />";
            var user = ${PBUser.get()?.encodeAsJSON()};
        </script>

        <!-- layout head contains angular module declaration and need to be placed before app.js -->
        <g:layoutHead />

        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/js" file="controllers.js" />"> </script>
        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/js" file="services.js" />"> </script>

        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/js" file="directives.js" />"> </script>

        <script src="<g:resource plugin="banner-sspb" dir="BannerXE/js" file="app.js" />"> </script>

        <g:customStylesheetIncludes/>
        <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'main.css')}">

    </head>
    <body>
        <div id="splash"></div>
        <div id="spinner" class="spinner spinner-img" style="display:none;">

        </div>

        <g:layoutBody />

        <r:layoutResources/>

        <g:customJavaScriptIncludes/>
    </body>
</html>

