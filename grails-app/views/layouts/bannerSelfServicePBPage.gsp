<%--
Copyright 2013-2018 Ellucian Company L.P. and its affiliates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="net.hedtech.banner.sspb.PBUser;" contentType="text/html;charset=UTF-8" %>
<%@ page import="net.hedtech.banner.tools.i18n.LocaleResource;" contentType="text/html;charset=UTF-8" %>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder" %>

<!DOCTYPE html>
<html ng-app="BannerOnAngular" lang="${message(code: 'default.language.locale')}" dir="${message(code:'default.language.direction')}">
    <head>
        <script>
            var extensibilityInfo =
                    ${raw(net.hedtech.extensibility.InfoService.getJSON(controllerName, resource(plugin:'web-app-extensibility', dir:'html')))};
            window.mepCode='${session.mep}';
        </script>
        <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
            <r:require modules="pageBuilderRTL"/>
        </g:if>
        <g:else>
            <r:require modules="pageBuilderLTR"/>
        </g:else>

        <g:set var="mep" value="${org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()?.request?.session?.getAttribute('ssbMepDesc')}"/>
        <g:set var="hideSSBHeaderComps" value="${session.hideSSBHeaderComps?session.hideSSBHeaderComps: params?.hideSSBHeaderComps? params.hideSSBHeaderComps:false} " scope="session" />

        <meta charset="${message(code: 'default.character.encoding')}"/>
        <meta name="dir" content="${message(code:'default.language.direction')}"/>
        <meta name="synchronizerToken" content="${org.codehaus.groovy.grails.web.servlet.mvc.SynchronizerTokensHolder.store( session ).generateToken(request.forwardURI)}"/>
        <meta name="logLevel" content="${g.logLevel()}"/>
        <meta name="maxInactiveInterval" content="${session.maxInactiveInterval}"/>
        <meta name="transactionTimeout" content="${session.getServletContext().transactionTimeout}"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <link rel="apple-touch-icon" href="images/applicationIcon.png" />
        <link rel="apple-touch-startup-image" href="images/applicationStartup.png">
        <meta name="keepAliveURL" content="${createLink(controller:'keepAlive')}"/>
        <meta name="ssbMepDesc" content="${!mep ? '' : mep}"/>
        <meta name="fullName" content="${g.fullName()}"/>
        <meta name="loginEndpoint" content="${session.getServletContext().loginEndpoint}"/>
        <meta name="logoutEndpoint" content="${session.getServletContext().logoutEndpoint}"/>
        <meta name="guestLoginEnabled" content="${session.getServletContext().guestLoginEnabled}"/>
        <meta name="userLocale" content="${LocaleContextHolder.getLocale()}"/>
        <meta name="footerFadeAwayTime" content="${grails.util.Holders.config.footerFadeAwayTime}"/>
        <meta name="hideSSBHeaderComps" content="${session?.hideSSBHeaderComps?.trim()}"/>
        <meta name="menuEndPoint" content="${request.contextPath}/ssb/menu"/>
        <meta name="menuBaseURL" content="${request.contextPath}/ssb"/>


        <meta name="headerAttributes" content=""/>
        <script type="text/javascript">
        document.getElementsByName('headerAttributes')[0].content = JSON.stringify({
            "pageTitle": "<g:layoutTitle/>"
          });
        </script>

        <title><g:layoutTitle default="Banner Page Builder"/></title>

        <link rel="shortcut icon" href="${resource(plugin: 'banner-ui-ss', dir:'images',file:'favicon.ico')}" type="image/x-icon"/>


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

        <g:set var="localeLanguage"    value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" scope="page" />
        <g:set var="localeBrowserFull" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).toString().replace('_','-')}" scope="page" />



        <script type="text/javascript">
            var rootWebApp = "${createLink(uri: '/')}";
            var resourceBase = "${createLink(uri: '/') + grails.util.Holders.config.sspb.apiPath +'/' }";
            var templatesLocation = "<g:resource plugin="banner-sspb" dir="template" />";
            var user;
            var gridLocale = '${localeBrowserFull.toLowerCase()}';
            var params = ${params?.encodeAsJSON()};
            if (!window.console) {
                console = {log: function() {}};
            }
            // inject services and controller modules to be registered with the global ng-app
            var myCustomServices = ['ngResource','ngGrid','ui', 'pbrun.directives', 'ngSanitize', 'xe-ui-components', 'extensibility'];
            var pageControllers = {};

        </script>

        <g:customStylesheetIncludes/>
        <!-- layout head contains angular module declaration and need to be placed before pbRunApp.js -->
        <g:layoutHead />
        <g:theme />
    </head>
    <body>
        <div id="splash"></div>
        <div id="spinner" class="spinner spinner-img" style="display:none;">

        </div>
        <g:layoutBody />

        <r:layoutResources/>

        <g:customJavaScriptIncludes/>

        ${LocaleResource.importExisting(plugin:'banner-sspb', dir: 'BannerXE/lib/jquery/i18n', file: 'jquery.ui.datepicker-{locale}.js',
                                        locale: localeBrowserFull, html: '<script src="{resource}" ></script>' )}
        ${LocaleResource.importExisting(plugin:'banner-sspb', dir: 'BannerXE/lib/angular/i18n', file: 'angular-locale_{locale}.js',
                                        locale: localeBrowserFull.toLowerCase(), html: '<script src="{resource}" ></script>' )}



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
                styleLeft:                 '${message(code:'default.language.direction')=='ltr'?'left':'right'}',
                styleRight:                '${message(code:'default.language.direction')=='ltr'?'right':'left'}',
                maxPageLabel:              '${message(code:'nggrid.maxPageLabel'             , encodeAs: 'JavaScript')}',
                pageLabel:                 '${message(code:'nggrid.pageLabel'                , encodeAs: 'JavaScript')}'
            };
        </script>
    </body>
</html>
