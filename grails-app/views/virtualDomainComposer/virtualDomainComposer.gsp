<%--
Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
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
<!-- g:set var="pageSource" value="test test" scope="page" /-->
</head>

<body>
<div id="content"  class="customPage container-fluid" >
<h3><g:message code="sspb.page.virtualdomain.heading" /></h3>
<br/>
<g:form name="LoadVDForm" action="loadVirtualDomain">
    <label><g:message code="sspb.page.virtualdomain.select.label" /></label>
    <g:select name="vdServiceName"
              from="${VirtualDomain.list().sort{it.serviceName}}"
              value="${pageInstance?.vdServiceName}"
              noSelection="${['null':message(code:"sspb.page.virtualdomain.select.noselection.label")]}"
              optionKey="serviceName"
              optionValue="serviceName"
              onChange="this.form.submit()"
    />
</g:form>

<br/>
<g:form name="ComposeVDForm" action="saveVirtualDomain">
    <label><g:message code="sspb.page.virtualdomain.servicename.label" /></label>
    <input maxlength="60"  pattern="^[a-zA-Z]+[a-zA-Z0-9_-]*$" type="text" name="vdServiceName" value="${pageInstance?.vdServiceName}" required />
    <g:actionSubmit action="saveVirtualDomain" value="${message(code:"sspb.page.virtualdomain.save.label")}" />
    <g:actionSubmit action="deleteVirtualDomain" value="${message(code:"sspb.page.virtualdomain.delete.label")}" />
<br/>

<table>
<tr>
    <th align = left><g:message code="sspb.page.virtualdomain.query.heading" /></th> <th align = left><g:message code="sspb.page.virtualdomain.delete.heading" /></th>
</tr>
<tr>
    <td>
    <g:textArea name="vdQueryView" value="${pageInstance?.vdQueryView}" rows="20" cols="60" style="width:100%" required="true" />
    </td>
    <td>
     <g:textArea name="vdDeleteView" value="${pageInstance?.vdDeleteView}" rows="20" cols="60" style="width:100%" />
    </td>
</tr>
</table>
    <table>
        <tr>
            <th align = left><g:message code="sspb.page.virtualdomain.post.heading" /></th> <th align = left><g:message code="sspb.page.virtualdomain.put.heading" /></th>
        </tr>
        <tr>
    <td>
    <g:textArea name="vdPostView" value="${pageInstance?.vdPostView}" rows="20" cols="60" style="width:100%"  />
    </td>
    <td>
    <g:textArea name="vdPutView" value="${pageInstance?.vdPutView}" rows="20" cols="60" style="width:100%" />
    </td>
</tr>

</table>
<br/>

<br/>
</g:form>
<g:form name="TestVDForm" action="loadVirtualDomain">
<%
    if (pageInstance?.vdServiceName)  {
    out << """
    <h4>${message(code:"sspb.page.virtualdomain.query.result.label")} </h4>
    ${message(code:"sspb.page.virtualdomain.query.result.parameters.label")}
    <input type="text" id="vdTestParameters" name="vdTestParameters" value="${pageInstance.vdTestParameters?pageInstance.vdTestParameters:""}" />
    <input type="hidden"  name="vdServiceName" value="${pageInstance?.vdServiceName}" />
    <input type="button" value="${message(code:"sspb.page.virtualdomain.query.result.test.label")}" id="getDataButton"/>
    <br/>
    <textarea id="testarea1" rows="5" readonly style="width:98%"></textarea>
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
            \$.get("${createLink(uri: '/')}${grailsApplication.config.sspb.adminApiPath}/virtualDomains.${pageInstance?.vdServiceName}?debug=true&"+param, {},
                    function(data) {
                        \$('#testarea1').val(JSON.stringify(data));
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
