<%@ page import="net.hedtech.banner.virtualDomain.VirtualDomain" %>
<!DOCTYPE html>
<html>
<head>
<title>Virtual Domain Composer</title>

<meta name="layout" content="BannerXECustomPage"/>
<meta name="menuEndPoint" content="/StudentRegistrationSsb/ssb/selfServiceMenu/data"/>
<meta name="menuBaseURL" content="/StudentRegistrationSsb/ssb"/>
<meta name="menuDefaultBreadcrumbId" content="Banner Self-Service_Student_Registration"/>


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
    if (pageInstance?.saveSuccess)
        out << """alert('Saving virtual domain succeeded. Virtual domain is ${pageInstance.updated?"updated":"created"}. ID=${pageInstance.id}. version=${pageInstance.version}');
        """
    else
        out << """alert('Saving virtual domain failed. Error is ${pageInstance?.error?.replaceAll('[\r\n]','')}');"""
    out << "</script>"
    }

%>
<%
  if (pageInstance?.loadSubmitted) {
    out << "<script>"
    if (!pageInstance?.loadSuccess)
        out << """alert('Loading virtual domain failed. Error is ${pageInstance?.error?.replaceAll('[\r\n]','')}');
        """
    out << "</script>"
    }

%>
<!-- g:set var="pageSource" value="test test" scope="page" /-->
</head>

<body>
<div class="customPage" >
<h3>Define or modify virtual domain definition</h3>
<br/>
<g:form name="LoadVDForm" action="loadVirtualDomain">
    <label>Load Virtual Domain Definition</label>
    <g:select name="vdServiceName"
              from="${VirtualDomain.list().sort{it.serviceName}}"
              value="${pageInstance?.vdServiceName}"
              noSelection="${['null':'Select One...']}"
              optionKey="serviceName"
              optionValue="serviceName"
              onChange="this.form.submit()"
    />
</g:form>

<br/>
<g:form name="ComposeVDForm" action="saveVirtualDomain">
    <label>Save Virtual Domain Definition</label>
    <input type="text" name="vdServiceName" value="${pageInstance?.vdServiceName}" required />
<br/>

<table>
<tr>
    <th align = left>Query Statement</th> <th align = left>Delete Statement</th>
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
            <th align = left>Post/Create/Save Statement</th> <th align = left>Put/Update Statement</th>
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
Note: Self Service Page Builder pages currently use the Post/Create/Save Statement for both Create and Update.
The Put/Update Statement can be provided if required.
<br/>
<g:actionSubmit action="saveVirtualDomain" value="Save Virtual Domain Definition"/>
<br/>
</g:form>
<g:form name="TestVDForm" action="loadVirtualDomain">
<%
    if (pageInstance?.vdServiceName)  {
    out << """
    <br/>
    <h4> Result Query statement</h4>
    Parameters: <input type="text" name="vdTestParameters" value="${pageInstance?.vdTestParameters}" />
    [Return]
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