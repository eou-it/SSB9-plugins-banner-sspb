<%--
  Created by IntelliJ IDEA.
  User: jzhong
  Date: 4/4/13
  Time: 6:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="net.hedtech.banner.sspb.Page" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title><g:message code="sspb.page.index.pagetitle" /></title>

</head>
<body>
    <li>
        <g:link controller="virtualDomainComposer" >  <g:message code="sspb.page.virtualdomain.pagetitle" /></g:link>
    </li>
    <li>
        <g:link controller="pageModelComposer" >  <g:message code="page.composer" /></g:link>
    </li>
<li>
    <g:link controller="visualPageModelComposer" > <g:message code="visualpage.composer" /></g:link>
</li>

    <g:each in="${Page.list(sort:'constantName')}">
        <li>
            <g:link controller="customPage" id="${it.constantName}" >Run ${it.constantName}</g:link>
        </li>
    </g:each>

</body>
</html>