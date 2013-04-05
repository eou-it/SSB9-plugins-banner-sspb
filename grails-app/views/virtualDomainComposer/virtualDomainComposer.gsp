<%@ page import="sspb.VirtualDomainPage" %>
<!DOCTYPE html>
<html>
<head>
<title>Virtual Domain Composer</title>

<meta name="layout" content="customAngularJSSelfServicePage"/>
<meta name="menuEndPoint" content="/StudentRegistrationSsb/ssb/selfServiceMenu/data"/>
<meta name="menuBaseURL" content="/StudentRegistrationSsb/ssb"/>
<meta name="menuDefaultBreadcrumbId" content="Banner Self-Service_Student_Registration"/>


<%
  if (pageInstance?.submitted) {
    out << "<script>"
    if (pageInstance?.saveSuccess)
        out << """alert('Saving virtual domain succeeded. Virtual domain is ${pageInstance.updated?"updated":"created"}. ID=${pageInstance.id}. version=${pageInstance.version}');
        """
    else
        out << """alert('Saving virtual domain failed. Error is ${pageInstance?.error?.replaceAll('[\r\n]','')}');
        """
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
<h3>Define or modify virtual domain definition</h3>
<br><br>
<g:form name="LoadVDForm" action="loadVirtualDomain">
    <label>Load Virtual Domain Definition</label>
    <input type="text" name="loadVdName" value="${pageInstance?.loadVdName}" required />
    <g:actionSubmit action="loadVirtualDomain" value="Load Virtual Domain Definition"/>
</g:form>

<br><br>
<g:form name="ComposeVDForm" action="saveVirtualDomain">
    <label>Save Virtual Domain Definition</label>
    <input type="text" name="vdName" value="${pageInstance?.vdName}" required />
<br>
<br>
<table>
<tr>
    <th align = left>Query Statement</th> <th align = left>Save Statement</th> <th align = left>Delete Statement</th>
</tr>
<tr>
    <td>
    <g:textArea name="vdQueryView" value="${pageInstance?.vdQueryView}" rows="20" cols="60" style="width:90%" required="true" />
    </td>
    <td>
    <g:textArea name="vdSaveView" value="${pageInstance?.vdSaveView}" rows="20" cols="60" style="width:90%"  />
    </td>
    <td>
    <g:textArea name="vdDeleteView" value="${pageInstance?.vdDeleteView}" rows="20" cols="60" style="width:90%" />
    </td>
</tr>
</table>

<g:actionSubmit action="saveVirtualDomain" value="Save Virtual Domain Definition"/>

</g:form>

</body>
</html>