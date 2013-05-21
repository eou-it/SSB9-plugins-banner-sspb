<%--
  Created by IntelliJ IDEA.
  User: jzhong
  Date: 5/17/13
  Time: 4:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="net.hedtech.banner.sspb.Page" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Banner Page Builder Visual Composer</title>

    <meta name="layout" content="BannerXECustomPage"/>
    <meta name="menuEndPoint" content="/sspb/selfServiceMenu/pageModel"/>
    <meta name="menuBaseURL" content="/sspb/sspb"/>
    <meta name="menuDefaultBreadcrumbId" content=""/>

    <script type="text/javascript">

    </script>

</head>
<body>
<g:form name="SelectForm" action="loadPageModel">
    <label>Load Page</label>
    <g:select name="constantName"
              from="${Page.list().sort {it.constantName}}"
              value="${pageModel.pageInstance?.constantName}"
              noSelection="${['null': 'Select One...']}"
              optionKey="constantName"
              optionValue="constantName"
              onChange="this.form.submit()"/>
</g:form>

<br/>

<g:form name="ComposeForm" action="compile">
    <label>Unique Page Name</label>
    <input type="text" name="constantName" value="${pageModel.pageInstance?.constantName}" required/>
    <g:actionSubmit action="compile" value="Compile & Save"/>
    <input type="hidden" name="id" value="${pageModel.pageInstance?.id}"/>
    <table>
        <tr>
            <th align = left style="width:50%">Page Model View</th>
            <th align = left style="width:50%">Compiled Page View/Javascript</th>
        </tr>
        <tr height="90%">
            <td>
                <g:textArea name="modelView" value="${pageModel?.modelView}"
                            rows="32" cols="60" style="width:100%; height:100%" required="true"/>

            </td>
            <td>
                <g:textArea name="compiledView" readonly="true" value="${pageModel?.compiledView}"
                            rows="16" cols="60" style="width:100%; height:50%"/>
                <br/>
                <g:textArea name="compiledController" readonly="true" value="${pageModel?.compiledController}"
                            rows="16" cols="60" style="width:100%; height:50%"/>
            </td>
        </tr>
    </table>
    <g:textArea name="statusMessage" readonly="true" value="${pageModel.status}"
                rows="3" cols="120" style="width:99%; height:50%"/>
</g:form>

</body>

</body>
</html>