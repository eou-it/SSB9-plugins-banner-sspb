<%@ page contentType="text/html;charset=UTF-8" %>
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
<html ng-app="BannerOnAngular" lang="${message(code:'default.language.locale')}" dir="${message(code:'default.language.direction')}">
    <head>
        <title><g:layoutTitle default="Banner Self Service Custom Page"/></title>

        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'bootstrap.css')}">
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'jquery.dataTables.css')}">
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'jquery.dataTables_themeroller.css')}">
<!--
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'demo_page.css')}">
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'demo_table.css')}">
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'demo_table_jui.css')}">
-->


<g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
    <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'anish-app-rtl.css')}">
</g:if>
<g:else>
    <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'anish-app.css')}">
</g:else>


        <link rel="stylesheet" href="${resource(dir: 'BannerXE/lib/jquery/plugins/jstree/themes/classic', file: 'style.css')}">
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'common-controls.css')}">
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'common-platform.css')}">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}">

<!--
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'select2.css')}">
-->
        <%-- Added Harry --%>
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'ng-grid.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'angular-ui.css')}" type="text/css">
        <link rel="stylesheet" href="${resource(dir: 'BannerXE/css', file: 'jquery-ui.css')}" type="text/css">
        <script src="/banner-sspb/BannerXE/lib/jquery/jquery-1.8.2.js"></script>
        <script src="/banner-sspb/BannerXE/lib/jquery/jquery-ui-1.8.24.custom.js"></script>
        <%-- end Added Harry --%>


        <!-- load angular JS here -->
        <!-- TODO utilize Grails Resource Management here -->

        <script src="/banner-sspb/BannerXE/lib/angular/angular.js"></script>
        <script src="/banner-sspb/BannerXE/lib/angular/angular-resource.js"></script>

        <%-- Added Harry --%>
        <script src="/banner-sspb/BannerXE/lib/ng-grid/ng-grid.js"></script>
        <script src="/banner-sspb/BannerXE/lib/angular-ui/angular-ui.js"></script>
        <!-- for modal dialog -->
        <script src="/banner-sspb/BannerXE/lib/angular-ui/ui-bootstrap-tpls-0.3.0.js"></script>
        <!--
        <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
            -->
        <%--    <g:javascript src="sspbCommon.js"/>
        end Added Harry --%>


<%
 /* TODO integrate with Banner Session

  if (user) {
    out << "<script>"
       out << "console.log('user.name = ' + $user.Username);"
    out << "</script>"
    } else {         */
      out << """
        <script>

         //function to avoid undefined
            function nvl(val,def){
                if ( (val == undefined) || (val == null ) ) {
                    return def;
                }
                return val;
            }

         // TODO retrieve user login information from XE session
            var __isUserAuthenticated = true;
            var __userFullName = 'Catherine S. Miller';
            var __userFirstName = 'Catherine';
            var __userLastName = 'Miller';
            var __pidm = 7;
            var __userRoles = [ 'ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M','ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M','ROLE_SELFSERVICE_BAN_DEFAULT_M' ];
        </script>

      """
  //}

%>

        <script type="text/javascript">
            var rootWebApp = ${createLink(uri: '/')};  //use in controller restful interface
        </script>
        <!-- r:layoutResources/ -->

        <g:layoutHead />

        <script src="/banner-sspb/BannerXE/js/controllers.js"></script>
        <script src="/banner-sspb/BannerXE/js/services.js"></script>

        <script src="/banner-sspb/BannerXE/js/directives.js"></script>

        <script src="/banner-sspb/BannerXE/js/app.js"></script>
        <script src="/banner-sspb/BannerXE/lib/jquery/jquery-1.8.2.js"></script>
        <script src="/banner-sspb/BannerXE/lib/jquery/jquery-ui-1.8.24.custom.js"></script>
        <script src="/banner-sspb/BannerXE/lib/jquery/jquery.dataTables.js"></script>
        <script src="/banner-sspb/BannerXE/lib/bootstrap/bootstrap.js"></script>
        <script src="/banner-sspb/BannerXE/lib/jquery/plugins/jstree/jquery.jstree.js"></script>

        <script src="/banner-sspb/BannerXE/js/aurora/serviceProperties.js"></script>
        <script src="/banner-sspb/BannerXE/js/aurora/application.js"></script>
        <script src="/banner-sspb/BannerXE/js/aurora/common-controls.js"></script>
        <script src="/banner-sspb/BannerXE/js/aurora/common-integration.js"></script>
        <script src="/banner-sspb/BannerXE/js/aurora/common-navigation.js"></script>
        <script src="/banner-sspb/BannerXE/js/aurora/common-platform.js"></script>
        <script src="/banner-sspb/BannerXE/lib/select2/select2.js"></script>


        <!--g:customStylesheetIncludes/-->


    </head>
    <body>

    <div>
        <div ng-controller="MainCtrl">
            <div id="header" role="banner">
                <div id="globalNav">
                    <div>
                        <ul>
                            <li class="userIdentityText bold">Catherine S. Miller</li>
                            <li><a class="signOutText pointer signIn" title="">Sign Out</a></li>
                            <li><a class="preferenceText pointer">Preferences</a></li>
                            <li><a class="helpText pointer" title="Help Alt+F1">Help</a></li>
                        </ul>
                    </div>
                </div>
                <a target="_parent" href="#"><span class="institutionalBranding"></span></a>
                <div style=" left: 300px;padding: 11px;position: absolute;">
                            <checkbox value="extensibilityenabled" label="Extensibility" style="float:right;padding-left:20px;color:#fff;"></checkbox>

                        </div>
                <div id="areas">
                    <div id="browseButtonState" style="right: 181.867px;">
                        <div class="homeButton" id="homeButton" title="Home Ctrl+Home">
                            <div>
                                <div><a href="javascript:void(0)" class="homeButtonDownArrow" id="homeArrow"></a></div>
                            </div>
                        </div>
                        <div class="browseButton" id="browseButton">
                            <div class="menuWrap">
                                <div><a tabindex="-1" href="javascript:void(0)" class="browseButtonDownArrow"
                                        id="browseArrow"></a></div>
                                <div tabindex="0" id="menuArrow" title="Browse Alt+M"></div>
                            </div>
                        </div>
                        <div class="breadcrumb" id="breadcrumb" title="Browse Alt+M">
                            <div>
                                <div role="listbox" id="breadcrumbHeader"></div>
                            </div>
                        </div>
                    </div>
                    <div id="buttonContainer">
                        <div id="openedItemsButtonState">
                            <div title="Go To Alt+G" class="headerButton" id="openedButton">
                                <div>
                                    <div><a href="javascript:void(0)" class="headerButtonDownArrow" id="openedArrow">Go
                                        To...</a></div>
                                </div>
                            </div>
                        </div>
                        <div id="toolsButtonState">
                            <div class="headerButton" id="toolsButton" title="Tools Alt+L">
                                <div>
                                    <div><a href="javascript:void(0)" class="headerButtonDownArrow"
                                            id="toolsArrow">Tools</a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div role="application" id="browseMenuContainer">
                    <div role="tree" id="browseMenu">
                        <div role="presentation" class="browseMenuShadow">
                            <div role="presentation" id="scrollableListContainer">
                                <div role="presentation" class="btn-l" id="btn-l" style="visibility: hidden;"></div>
                                <div role="group" id="columnsContainer">
                                    <div role="presentation" id="columnsContainerTrack" style="width: 201px;">
                                        <div role="presentation" class="columns"><span role="presentation"
                                                                                       class="scrollUpButton upButtonDisabled"></span>

                                            <div role="presentation" class="scrollContainer">
                                                <ul role="presentation" class="navList"></ul>
                                            </div>
                                            <span role="presentation" class="scrollDownButton downButtonDisabled"></span>
                                        </div>
                                    </div>
                                </div>
                                <div role="presentation" class="btn-r visible" id="btn-r"
                                     style="display: block; visibility: hidden;"></div>
                            </div>
                        </div>
                    </div>
                    <span class="browseButton" id="browseButtonBottom"></span><span class="bottomDropShadow"></span></div>
                <div id="openedItemsContainer">
                    <div id="openedItemsMenu">
                        <div class="browseMenuShadow">
                            <div id="openedItemsCanvas"></div>
                        </div>
                    </div>
                </div>
                <div id="toolsContainer">
                    <div id="toolsMenu">
                        <div class="browseMenuShadow">
                            <div id="toolsCanvas"></div>
                        </div>
                    </div>
                </div>
            </div>


            <notificationcenter class="notificationcenter" message="message" theme="theme"></notificationcenter>

            <div ng-view>

            </div>   <!-- ng-view -->

        </div>     <!--  ng-controller="MainCtrl" -->



        <g:layoutBody />
        </div>

     <div class="footer"></div>

        <!-- r:layoutResources/ -->

        <!--g:customJavaScriptIncludes/-->
    </body>
</html>

