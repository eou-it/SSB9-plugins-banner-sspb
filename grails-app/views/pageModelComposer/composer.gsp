<%@ page import="net.hedtech.banner.sspb.Page" %>
<!DOCTYPE html>
<html>
<head>
    <title>Composer</title>

    <meta name="layout" content="BannerXECustomPage"/>
    <meta name="menuEndPoint" content="/StudentRegistrationSsb/ssb/selfServiceMenu/pageModel"/>
    <meta name="menuBaseURL" content="/StudentRegistrationSsb/ssb"/>
    <meta name="menuDefaultBreadcrumbId" content="Banner Self-Service_Student_Registration"/>


    <!-- g:set var="pageSource" value="test test" scope="page" /-->
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
        left:0;	/* rtl fix for ie */    }
    </style>
</head>


<body>
<div class="customPage" >
<g:form name="SelectForm" action="loadPageModel">
    <label>Load Virtual Domain Definition</label>
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
    <label>Save Virtual Domain Definition</label>
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
            <g:textArea name="modelView" value="${pageModel.pageInstance?.modelView}"
                        rows="32" cols="60" style="width:100%; height:100%" required="true"/>

        </td>
        <td>
            <g:textArea name="compiledView" readonly="true" value="${pageModel.pageInstance?.compiledView}"
                        rows="16" cols="60" style="width:100%; height:50%"/>
            <br/>
            <g:textArea name="compiledController" readonly="true" value="${pageModel.pageInstance?.compiledController}"
                        rows="16" cols="60" style="width:100%; height:50%"/>
        </td>
        </tr>
    </table>
    <g:textArea name="statusMessage" readonly="true" value="${pageModel.status}"
                rows="3" cols="120" style="width:99%; height:50%"/>
</g:form>
</div>
</body>
</html>