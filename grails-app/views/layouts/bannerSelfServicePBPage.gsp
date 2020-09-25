<%--
Copyright 2013-2020 Ellucian Company L.P. and its affiliates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="net.hedtech.banner.sspb.PBUser;" contentType="text/html;charset=UTF-8" %>
<%@ page import="net.hedtech.banner.security.DeveloperSecurityService" contentType="text/html;charset=UTF-8" %>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder" %>

<!DOCTYPE html>
<html lang="${message(code: 'default.language.locale')}" dir="${message(code:'default.language.direction')}">
<head>
    <title><g:layoutTitle default="Banner Page Builder"/></title>
    <%
        def infoService = grailsApplication.classLoader.loadClass('net.hedtech.extensibility.InfoService').newInstance()
        def extensibilityInfo = (infoService.getJSON(controllerName, resource(plugin: 'web-app-extensibility', dir: 'html')))
    %>

    <g:set var="mep" value="${org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()?.request?.session?.getAttribute('ssbMepDesc')}"/>
    <g:set var="hideSSBHeaderComps" value="${session.hideSSBHeaderComps ? session.hideSSBHeaderComps : (Boolean.parseBoolean(params?.hideSSBHeaderComps)) ? params.hideSSBHeaderComps : false}" scope="session"/>

    <meta charset="${message(code: 'default.character.encoding')}"/>
    <meta name="dir" content="${message(code:'default.language.direction')}"/>
    <meta name="synchronizerToken" content="${org.grails.web.servlet.mvc.SynchronizerTokensHolder.store( session ).generateToken(request.forwardURI)}" />

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
    <meta name="loginEndpoint" content="${grails.util.Holders.config.loginEndpoint}"/>
    <meta name="logoutEndpoint" content="${grails.util.Holders.config.logoutEndpoint}"/>
    <meta name="guestLoginEnabled" content="${grails.util.Holders.config.guestLoginEnabled}"/>
    <meta name="userLocale" content="${LocaleContextHolder.getLocale()}"/>
    <meta name="footerFadeAwayTime" content="${grails.util.Holders.config.footerFadeAwayTime}"/>
    <meta name="hideSSBHeaderComps" content="${session?.hideSSBHeaderComps}">
    <meta name="menuEndPoint" content="${request.contextPath}/ssb/menu"/>
    <meta name="menuBaseURL" content="${request.contextPath}/ssb"/>
    <g:set var="aboutServiceUrl" value="${net.hedtech.banner.controllers.ControllerUtils.aboutServiceUrl()}" />
    <g:set var="privacyPolicyUrl" value="${net.hedtech.banner.controllers.ControllerUtils.privacyPolicyUrl()}"/>
    <meta name="aboutUrl" content="${!aboutServiceUrl ? '' : aboutServiceUrl}"/>
    <meta name="aboutUrlContextPath" content="${request.contextPath}/ssb"/>
    <meta name="contextPath" content="${request.contextPath}"/>
    <meta name="headerAttributes" content=""/>
    <meta name="privacyPolicyUrl" content="${!privacyPolicyUrl ? '' : privacyPolicyUrl}"/>

    <asset:link rel="apple-touch-icon" sizes="57x57" href="eds/apple-touch-icon-57x57.png"/>
    <asset:link rel="apple-touch-icon" sizes="60x60" href="eds/apple-touch-icon-60x60.png"/>
    <asset:link rel="apple-touch-icon" sizes="72x72" href="eds/apple-touch-icon-72x72.png"/>
    <asset:link rel="apple-touch-icon" sizes="76x76" href="eds/apple-touch-icon-76x76.png"/>
    <asset:link rel="apple-touch-icon" sizes="114x114" href="eds/apple-touch-icon-114x114.png"/>
    <asset:link rel="apple-touch-icon" sizes="120x120" href="eds/apple-touch-icon-120x120.png"/>
    <asset:link rel="apple-touch-icon" sizes="144x144" href="eds/apple-touch-icon-144x144.png"/>
    <asset:link rel="apple-touch-icon" sizes="152x152" href="eds/apple-touch-icon-152x152.png"/>
    <asset:link rel="apple-touch-icon" sizes="180x180" href="eds/apple-touch-icon-180x180.png"/>
    <asset:link rel="shortcut icon" type="image/png" href="eds/favicon-32x32.png" sizes="32x32"/>
    <asset:link rel="shortcut icon" type="image/png" href="eds/android-chrome-192x192.png" sizes="192x192"/>
    <asset:link rel="shortcut icon" type="image/png" href="eds/favicon-96x96.png" sizes="96x96"/>
    <asset:link rel="shortcut icon" type="image/png" href="eds/favicon-16x16.png" sizes="16x16"/>
    <asset:link rel="shortcut icon" href="eds/favicon.ico" type="image/x-icon"/>

    <g:set var="localeLanguage"    value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" scope="page" />
    <g:set var="localeBrowserFull" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).toString().replace('_','-')}" scope="page" />

    <asset:deferredScripts/>

    <g:customStylesheetIncludes/>

    <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
        <asset:stylesheet href="modules/pageBuilderRTL-mf.css"/>
    </g:if>

    <g:else>
        <asset:stylesheet href="modules/pageBuilderLTR-mf.css"/>
    </g:else>
    <asset:javascript src="modules/jquery-mf.js"/>
    <asset:javascript src="modules/pageBuilderJQuery-mf.js"/>

    <asset:script>
        var extensibilityInfo = ${extensibilityInfo.encodeAsRaw()}
        window.mepCode='${session.mep}';
    </asset:script>

    <g:javascript>
        var rootWebApp = "${createLink(uri: '/')}";
        var resourceBase = "${createLink(uri: '/') + grails.util.Holders.config.sspb.apiPath +'/' }";
        var templatesLocation = "${assetPath(src: 'template')}";
        var user = ${raw(PBUser.getTrimmed().encodeAsJSON().decodeHTML())};
        var isImportPrevented = ${DeveloperSecurityService?.getImportConfigValue()};

        var gridLocale = '${localeBrowserFull.toLowerCase()}';
        var params = ${raw(params?.encodeAsJSON().decodeHTML())};
        if (!window.console) {
            console = {log: function() {}};
        }
        // inject services and controller modules to be registered with the global ng-app
        var myCustomServices = ['ngResource', 'ui.grid', 'ui.grid.i18n','ui.grid.edit','ui.grid.selection','ui.grid.resizeColumns', 'ui.grid.cellNav','ui', 'pbrun.directives', 'ngSanitize', 'xe-ui-components','ui.grid.autoResize'];
        var pageControllers = {};

        var transactionTimeoutMeta    = $( "meta[name=transactionTimeout]" ),
            transactionTimeoutSeconds = ( transactionTimeoutMeta.length == 1 ? parseInt( transactionTimeoutMeta.attr( "content" ) ) : 30 ),
            transactionTimeoutPadding = 10 * 1000,
            transactionTimeoutMilli   = ( transactionTimeoutSeconds * 1000 ) + transactionTimeoutPadding;

        $.ajaxSetup( { timeout: transactionTimeoutMilli } );

          document.getElementsByName('headerAttributes')[0].content = JSON.stringify({
            "pageTitle": "<g:layoutTitle/>"
        });

        var dateTimeLocale = "${localeBrowserFull}" ;
    </g:javascript>


    <g:layoutHead />

    <g:theme />


</head>
    <body>
        <div id="dialogAppDiv" role="main"></div>
        <div id="popupContainerDiv"></div>
        <div ng-app="BannerOnAngular">
            <div id="splash"></div>
            <div id="spinner" class="spinner spinner-img" style="display:none;">
        </div>

        <g:analytics/>
        
        <asset:javascript src="modules/pageBuilder-mf.js"/>

        <asset:script type="text/javascript">
            %{--window.ui.grid.i18n[gridLocale] = {
                ngAggregateLabel:          '${message(code: 'nggrid.ngAggregateLabel'         , encodeAs: 'JavaScript')}',
                ngGroupPanelDescription:   '${message(code: 'nggrid.ngGroupPanelDescription'  , encodeAs: 'JavaScript')}',
                ngSearchPlaceHolder:       '${message(code: 'nggrid.ngSearchPlaceHolder'      , encodeAs: 'JavaScript')}',
                ngMenuText:                '${message(code: 'nggrid.ngMenuText'               , encodeAs: 'JavaScript')}',
                ngShowingItemsLabel:       '${message(code: 'nggrid.ngShowingItemsLabel'      , encodeAs: 'JavaScript')}',
                ngTotalItemsLabel:         '${message(code: 'nggrid.ngTotalItemsLabel'        , encodeAs: 'JavaScript')}',
                ngSelectedItemsLabel:      '${message(code: 'nggrid.ngSelectedItemsLabel'     , encodeAs: 'JavaScript')}',
                ngPageSizeLabel:           '${message(code: 'nggrid.ngPageSizeLabel'          , encodeAs: 'JavaScript')}',
                ngPagerFirstTitle:         '${message(code: 'nggrid.ngPagerFirstTitle'        , encodeAs: 'JavaScript')}',
                ngPesheetInclude:          '${message(code: 'nggrid.ngPagerNextTitle'         , encodeAs: 'JavaScript')}',
                ngPagerPrevTitle:          '${message(code: 'nggrid.ngPagerPrevTitle'         , encodeAs: 'JavaScript')}',
                ngPagerLastTitle:          '${message(code: 'nggrid.ngPagerLastTitle'         , encodeAs: 'JavaScript')}',
                direction:                 '${message(code:'default.language.direction')}',
                styleLeft:                 '${message(code:'default.language.direction')=='ltr'?'left':'right'}',
                styleRight:                '${message(code:'default.language.direction')=='ltr'?'right':'left'}',
                maxPageLabel:              '${message(code:'nggrid.maxPageLabel'             , encodeAs: 'JavaScript')}',
                pageLabel:                 '${message(code:'nggrid.pageLabel'                , encodeAs: 'JavaScript')}'
            };--}%
            yepnope({
                test : window.JSON,
                nope : '${assetPath(src: 'json2.js')}'
            });

            $(window).load(function() {
                _.defer( function() {
                    $( "#splash" ).remove();
                });
                $('#content').find('button, a, input:not([class~="pb-boolean"]), select, textarea, label, [tabindex]:not([tabindex="-1"])').eq(0).focus();
            });
            <g:i18nJavaScript/>
            <g:pageAccessAudit/>
        </asset:script>
            <script type="text/javascript">
                var mepcodeChanged='${request.mepcodeChanged}';
                if(null !== mepcodeChanged && undefined !== mepcodeChanged && mepcodeChanged == "true"){
                    $(document).bind('notification-use-ready', function (e) {
                        mepcodeNotificationAddition(window.mepCode)
                    });
                }
            </script>
        <g:layoutBody/>
        <asset:deferredScripts/>
        <g:customJavaScriptIncludes/>
        <asset:javascript src="lib/jquery/i18n/jquery.ui.datepicker-${localeBrowserFull.toLowerCase()}.js" />
        <asset:javascript src="lib/angular/i18n/angular-locale_${localeBrowserFull.toLowerCase()}.js" />
        <asset:javascript src="modules/pageBuilderDate-mf.js"/>

    </body>
</html>

