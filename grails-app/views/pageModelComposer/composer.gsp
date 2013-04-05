<%@ page import="net.hedtech.banner.sspb.Page" %>
<!DOCTYPE html>
<html>
<head>
<title>Composer</title>

<meta name="layout" content="customAngularJSSelfServicePage"/>
<meta name="menuEndPoint" content="/StudentRegistrationSsb/ssb/selfServiceMenu/data"/>
<meta name="menuBaseURL" content="/StudentRegistrationSsb/ssb"/>
<meta name="menuDefaultBreadcrumbId" content="Banner Self-Service_Student_Registration"/>


<g:set var="pageSource" value="test test" scope="page" />
</head>



<body>

<table>
<td>
<g:form name="ComposeForm" action="compose">
<label>Page Model View</label>
<g:textArea name="modelView" value="${pageInstance?.modelView}" rows="32" cols="60" style="width:80%" required="true"/>
<br/>
<g:actionSubmit value="compile" />
</g:form>
</td>

<td>

<g:form name="RenderForm" action="render">
<label>Compiled Page View</label>
<g:textArea name="compiledView" readonly="true" value="${pageInstance?.compiledView}" rows="15" cols="60" style="width:80%" />
    <br/>
<label>Compiled JavaScript</label>
<g:textArea name="compiledController" readonly="true" value="${pageInstance?.compiledController}" rows="13" cols="60" style="width:80%" />
        <br/>

<g:actionSubmit value="render" />
</g:form>

    </td>
</table>

</body>
</html>