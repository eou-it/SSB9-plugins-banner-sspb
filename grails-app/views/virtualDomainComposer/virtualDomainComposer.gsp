<%--
Copyright 2013-2020 Ellucian Company L.P. and its affiliates.
--%>
<%@ page import="net.hedtech.banner.virtualDomain.VirtualDomain" %>
<!DOCTYPE html>
<html>
<head>
    <title><g:message code="sspb.page.virtualdomain.pagetitle" /></title>

    <meta name="layout" content="bannerSelfServicePBPage"/>

    <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
        <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'pbDeveloper-rtl.css')}">
    </g:if>
    <g:else>
        <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'pbDeveloper.css')}">
    </g:else>

    <%
        if (pageInstance?.submitted) {
            out << "<script>"
            if (pageInstance?.saveSuccess) {
                out << """alert('${message(code:"sspb.page.virtualdomain.save.ok.message", args:[pageInstance.id,pageInstance.version])}');"""
            } else {
                out << """alert('${message(code:"sspb.page.virtualdomain.save.fail.message", args:[pageInstance?.error?.replaceAll('[\r\n]','')])}');"""
            }
            out << "</script>"
        }
    %>
    <%
        if (pageInstance?.loadSubmitted) {
            out << "<script>"
            if (!pageInstance?.loadSuccess)
                out << """alert('${message(code:"sspb.page.virtualdomain.load.fail.message", args:[pageInstance?.error?.replaceAll('[\r\n]','')])}');"""
            out << "</script>"
        }
    %>

    <Script type="text/javascript">
        function showDomainRoles(id, name){
            var allowModify ="${!pageInstance?.allowModify}"

            window.localStorage['allowModify'] = allowModify;

            updateLocalStorage(name,  id)
            window.open(rootWebApp+'customPage/page/'+ 'pbadm.VirtualDomainRoles', '_self');

        }
        function getDeveloperSecurityPage(id, name){
            //updateLocalStorage(name,  id)
            window.open(rootWebApp+'customPage/page/'+ 'pbadm.DeveloperPageSecurity', '_self');
        }


    </Script>
    <!-- g:set var="pageSource" value="test test" scope="page" /-->
</head>
<body>
<div id="content"  class="customPage container-fluid vdPage" role="main">
    <h3><g:message code="sspb.page.virtualdomain.heading" /></h3>
    <div class="vd-section">
        <g:form name="LoadVDForm" action="loadVirtualDomain" aria-label="Load Virtual Domain">
            <label for="vdServiceName"><g:message code="sspb.page.virtualdomain.select.label" /></label>

            <g:select id="vdServiceName" name="vdServiceName" from="${[['serviceName': (pageInstance?.vdServiceName?:'')]]}"
                      class="popupSelectBox pbPopupDataGrid:{'serviceNameType':'virtualdomains','id':'vdServiceName'}"
                      value="${pageInstance?.vdServiceName}"
                      noSelection="${['null':message(code:"sspb.page.virtualdomain.select.noselection.label")]}"
                      optionKey="serviceName"
                      optionValue="serviceName"
                      onChange="this.form.submit()"
            />
        </g:form>


    </div>

    <g:form name="ComposeVDForm" action="saveVirtualDomain">
        <div class="vd-section-2">
            <label for="pbid-saveVD"><g:message code="sspb.page.virtualdomain.servicename.label" /></label>
            <input id="pbid-saveVD" maxlength="60"  pattern="^[a-zA-Z]+[a-zA-Z0-9_-]*$" type="text" name="vdServiceName" value="${pageInstance?.vdServiceName}" required />

            <g:actionSubmit action="saveVirtualDomain" role="button" ng-disabled="${!isProductionReadOnlyMode || !(pageInstance?.allowModify==null ? true: pageInstance?.allowModify)}" class="primary" value="${message(code:"sspb.page.virtualdomain.save.label")}" />
            <g:actionSubmit action="deleteVirtualDomain" role="button" ng-disabled="${!isProductionReadOnlyMode || !(pageInstance?.allowModify==null ? true: pageInstance?.allowModify)}"  class="secondary" value="${message(code:"sspb.page.virtualdomain.delete.label")}" />
            <g:if test="${pageInstance?.vdServiceName}">
                <input type="button" class="secondary" role="button" value="${message(code:"sspb.page.virtualdomain.roles.label")}" onclick="showDomainRoles('${pageInstance?.id}','${pageInstance?.vdServiceName}')"/>
                <input type="button" class="secondary" role="button" value="${message(code:"sspb.css.cssManager.developer.label")}" onclick="getDeveloperSecurityPage('${pageInstance?.id}','${pageInstance?.vdServiceName}')"/>
                <span class="alignRight">
                    <label class="vpc-name-label dispInline" for="vdOwner"><g:message code="sspb.vd.visualbuilder.vdowner.label" /></label>
                    <g:select id="vdOwner" class="owner-select alignRight" name="owner" ng-disabled="${!(pageInstance?.allowUpdateOwner==null ? true: pageInstance?.allowUpdateOwner)}" noSelection="${['null':'']}"
                              value="${pageInstance?.owner?:''}" from="${userDetailsInList}" optionKey="USER_ID" optionValue="USER_ID">
                    </g:select>
                   %{-- <select class="owner-select vd-select-width" id="pageOwner" onchange="onChangeOfOwner()">
                    </select>--}%
                </span>
%{--
                <g:textField name="owner" id="vdowner"  style="display:none;" />
--}%
            </g:if>
        </div>


        <table role="grid">
            <thead role="rowgroupw">
                <tr role="row">
                    <th align = left id="vdQueryStatement" ><g:message code="sspb.page.virtualdomain.query.heading"/></th> <th align = left id="vdDeleteStatement"><g:message code="sspb.page.virtualdomain.delete.heading" /></th>
                </tr>
            </thead>
            <tbody role="rowgroup">
                <tr role="row">
                    <td role="gridcell">
                        <g:textArea name="vdQueryView" aria-labelledby="vdQueryStatement" value="${pageInstance?.vdQueryView}" rows="20" cols="60" style="width:100%" required="true" tabindex="-1"/>
                    </td>
                    <td role="gridcell">
                        <g:textArea name="vdDeleteView" aria-labelledby="vdDeleteStatement" value="${pageInstance?.vdDeleteView}" rows="20" cols="60" style="width:100%" tabindex="-1"/>
                    </td>
                </tr>
            </tbody>
        </table>
        <table role="grid">
            <thead role="rowgroup">
                <tr role="row">
                    <th align = left id="vdPostStatement"><g:message code="sspb.page.virtualdomain.post.heading" /></th> <th align = left id="vdPutStatement"><g:message code="sspb.page.virtualdomain.put.heading" /></th>
                </tr>
            </thead>
            <tbody role="rowgroup">
                <tr role="row">
                    <td role="gridcell">
                        <g:textArea name="vdPostView" aria-labelledby="vdPostStatement" value="${pageInstance?.vdPostView}" rows="20" cols="60" style="width:100%"  tabindex="-1"/>
                    </td>
                    <td role="gridcell">
                        <g:textArea name="vdPutView" aria-labelledby="vdPutStatement" value="${pageInstance?.vdPutView}" rows="20" cols="60" style="width:100%" tabindex="-1"/>
                    </td>
                </tr>
            </tbody>
        </table>
        <br/>

        <br/>
    </g:form>
    <g:form name="TestVDForm" action="loadVirtualDomain">
        <%
            if (pageInstance?.vdServiceName)  {
                out << """
    <h4 id="vdQueryResult">${message(code:"sspb.page.virtualdomain.query.result.label")} </h4>
    <label for"vdTestParameters"> ${message(code:"sspb.page.virtualdomain.query.result.parameters.label")}</label>
    <input type="text" id="vdTestParameters" name="vdTestParameters" value="${pageInstance.vdTestParameters?pageInstance.vdTestParameters:""}" />
    <input type="hidden"  name="vdServiceName" value="${pageInstance?.vdServiceName}" />
    <input type="button" class="primary" role="button" aria-label="${message(code:"sspb.page.virtualdomain.query.result.test.label")}" value="${message(code:"sspb.page.virtualdomain.query.result.test.label")}" id="getDataButton"/>
    <br/>
    <br/>
    <textarea id="testarea1" rows="5" readonly style="width:98%" aria-labelledby="vdQueryResult"></textarea>
    """
//It should not be very difficult to write an AngularJS page to submit the JSON shown in the texarea above.
//When debug=true, the service should add a rollback statement to the DML statement...
//This way a round trip test can be done making sure the SQL is valid.

            }
        %>
    </g:form>
    <%
        if (pageInstance?.vdServiceName)  {
            out << """
    <script>
        function testApi() {
            var param = \$("#vdTestParameters").val();
            param =  param.replace(/%/g,'%25');
            const Url = "${createLink(uri: '/')}${grailsApplication.config.sspb.adminApiPath}/virtualDomains.${pageInstance?.vdServiceName}?debug=true&"+param;
            \$.ajax( {
                    url : Url,
                    type : "GET",
                    success : function(result) {
                             \$('#testarea1').val(JSON.stringify(result));
                    },
                    error : function(jqXHR) {
                           var errorResponse = \$.parseJSON(jqXHR.responseText);
                           var errorMsg = errorResponse.errors.errorMessage;
                           var msg = \$.i18n.prop("js.net.hedtech.banner.ajax.error.message", [ errorMsg ]);
                           var n = new Notification( {
                                message: msg,
                                type:"error",
                                promptMessage: \$.i18n.prop("js.net.hedtech.banner.ajax.reload.prompt")
                           });

                           n.addPromptAction( \$.i18n.prop("js.net.hedtech.banner.ajax.reload.button"),
                           function() { window.location.reload() });
                           n.addPromptAction( \$.i18n.prop("js.net.hedtech.banner.ajax.continue.button"),
                           function() { notifications.remove( n ); });
                           notifications.addNotification( n );
                    }
            });
            return false;
        };
        \$("#getDataButton").click(function() {
            testApi();
        });
    </script>
    """
        }
    %>
</div> <!--class customPage-->
</body>
</html>
