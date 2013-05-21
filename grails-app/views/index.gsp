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
  <title>Index of Self Service Page Builder pages</title>

</head>
<body>
    <li>
        <g:link controller="virtualDomainComposer" >  Virtual Domain Composer</g:link>
    </li>
    <li>
        <g:link controller="pageModelComposer" >  Page Composer</g:link>
    </li>
<li>
    <g:link controller="visualPageModelComposer" > Visual Page Composer</g:link>
</li>

<g:each in="${Page.list()}">
        <li>
            <g:link controller="customPage" id="${it.constantName}" >Run ${it.constantName}</g:link>
        </li>
    </g:each>

</body>
</html>