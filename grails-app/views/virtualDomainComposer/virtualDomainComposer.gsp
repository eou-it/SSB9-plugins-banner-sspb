<%@ page import="net.hedtech.banner.virtualDomain.VirtualDomain" %>
<!DOCTYPE html>
<html>
<head>
<title><g:message code="sspb.page.virtualdomain.pagetitle" /></title>

    <meta name="layout" content="bannerSelfServicePBPage"/>
    <meta name="menuEndPoint" content="${request.contextPath}/ssb/menu"/>
    <meta name="menuBaseURL" content="${request.contextPath}/ssb"/>
    <meta name="menuDefaultBreadcrumbId" content=""/>


<style>
    div.customPage {
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
<div id="content"  class="customPage" >
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
    <input type="text" name="vdServiceName" value="${pageInstance?.vdServiceName}" required />
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
<g:actionSubmit action="saveVirtualDomain" value="${message(code:"sspb.page.virtualdomain.save.label")}" />
<br/>
</g:form>
<g:form name="TestVDForm" action="loadVirtualDomain">
<%
    if (pageInstance?.vdServiceName)  {
    out << """
    <br/>
    <h4>${message(code:"sspb.page.virtualdomain.query.result.label")} </h4>
    ${message(code:"sspb.page.virtualdomain.query.result.parameters.label")} <input type="text" name="vdTestParameters" value="${pageInstance?.vdTestParameters}" />
    ${message(code:"sspb.page.virtualdomain.query.result.parameters.enter")}
    <input type="hidden"  name="vdServiceName" value="${pageInstance?.vdServiceName}" />
    <br/>
    <iframe width="95%" height="20%" src="${createLink(uri: '/')}virt/${pageInstance?.vdServiceName}?debug=true&${pageInstance?.vdTestParameters?.replace('%','%25')}">
    </iframe>
    """
//It should not be very difficult to write an AngularJS page to submit the JSON shown in the iframe above.
//When debug=true, the service should add a rollback statement to the DML statement...
//This way a round trip test can be done making sure the SQL is valid.
}
%>
</g:form>
</div> <!--class customPage-->
</body>
</html>