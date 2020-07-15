<%--
Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
<head>

    <meta name="layout" content="bannerSelfServicePBPage"/>

    <title><g:message code="sspb.css.cssManager.pagetitle" /></title>

    <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
        <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'pbDeveloper-rtl.css')}">
    </g:if>
    <g:else>
        <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'pbDeveloper.css')}">
    </g:else>


    <script type="text/javascript">
        var myCustomServices = ['ngResource', 'ui.bootstrap', 'pagebuilder.directives', 'ngUpload', 'ngMessages'];
        pageControllers["CssManagerController"] = function ( $scope, $http, $resource, $parse) {
            // upload status callback
            $scope.foo = "Hello!";
            $scope.bar = function(content) {
                //console.log(content.length);
                $scope.uploadResponse = content;
                $scope.cssStatus.message = content.statusMessage;
            };
            $scope.i18nGet = function(key,args) {
                var tr = [];
                tr['sspb.css.cssManager.stylesheet.submit.failed.message']  =   "${message(code:'sspb.css.cssManager.stylesheet.submit.failed.message',encodeAs: 'JavaScript')}";
                tr['sspb.css.cssManager.parsing.error.message']  =   "${message(code:'sspb.css.cssManager.parsing.error.message',encodeAs: 'JavaScript')}";
                tr['sspb.css.cssManager.Stylesheet.load.failed.message']  =   "${message(code:'sspb.css.cssManager.Stylesheet.load.failed.message',encodeAs: 'JavaScript')}";
                tr['sspb.css.cssManager.validation.error.message']  =   "${message(code:'sspb.css.cssManager.validation.error.message',encodeAs: 'JavaScript')}";
                tr['sspb.css.cssManager.deletion.success.message']  =   "${message(code:'sspb.css.cssManager.deletion.success.message',encodeAs: 'JavaScript')}";
                tr['sspb.css.cssManager.deletion.error.message']  =   "${message(code:'sspb.css.cssManager.deletion.error.message',encodeAs: 'JavaScript')}";

                var res=tr[key];
                if ( !res )  {
                    res=key;
                }
                if (args) {
                    args.forEach(function (arg,index)  {
                        //note undefined parameters will show as undefined
                        res=res.replace("{"+index+"}",arg);
                    } );
                }
                return res;
            }
            //
            $scope.cssName = "";
            // css command execution status
            $scope.cssStatus = {};

            // declare the Css resource
            var Css = $resource(resourceBase+'csses/:constantName',{},{
                save:{
                    method:"POST",
                    isArray:false,
                    headers:{'Content-Type':'application/json', 'Accept':'application/json'}
                },
                list: {
                    method:"GET",
                    isArray:true,
                    headers:{'Content-Type':'application/json', 'Accept':'application/json'}
                },
                get: {
                    method:"GET",
                    isArray:false,
                    headers:{'Content-Type':'application/json', 'Accept':'application/json'}
                },
                delete: {
                    method:"DELETE",
                    isArray:false,
                    headers:{'Content-Type':'application/json', 'Accept':'application/json'}
                }
            });

            $scope.cssList = [];
            $scope.loadCssNames = function() {
                Css.list({}, function(data) {
                    $scope.cssList = data;
                });
            };
            // populate the css list initially
            //  $scope.loadCssNames();

            $scope.getCssSource = function() {
                //TODO prompt for unsaved data
                if ($scope.cssSource != undefined)   {
                    var r=confirm("${message(code: 'sspb.css.cssManager.loadStylesheet.unsaved.changes.message', encodeAs: 'JavaScript')}");
                    if (!r)
                        return;
                }

                if(!$scope.cssName){
                    $scope.cssName = document.getElementById('cssConstantName').value;
                }
                Css.get({constantName:$scope.cssName}, function (data){
                    try {
                        //$scope.cssSource = JSON.parse(data.css);
                        $scope.cssSource = data.css;
                        $scope.description = data.description;
                        $scope.cssOwner = data.owner;
                        $scope.allowUpdateOwner = data.allowUpdateOwner;
                        $scope.allowModify = data.allowModify;
                        $scope.resetCssNameData();
                    } catch(ex) {
                        alert($scope.i18nGet("${message(code:'sspb.css.cssManager.parsing.error.message')}",[ex]),{type:"error"});
                    }
                }, function(response) {
                    var msg  = "${message(code: 'sspb.css.cssManager.Stylesheet.load.failed.message', encodeAs: 'JavaScript')}";
                    if (response.data != undefined && response.data.errors!=undefined)
                        msg = $scope.i18nGet(msg, [response.data.errors[0].errorMessage]);
                    else
                        msg = $scope.i18nGet(msg, ['']);

                    alert(msg, {type:"error"});
                });
            };

            // Fetch Page ownersList
            // declare the virtual domain lookup resource  and functions for loading the resourse combo box
            var VDOwnersList = $resource(resourceBase+'virtualDomains.pbadmUserDetails',{},{
                list: {
                    method:"GET",
                    isArray:true,
                    headers:{'Content-Type':'application/json', 'Accept':'application/json'}
                }
            });
            $scope.pbUserList = [];
            $scope.loadVdOwnersList = function() {
                console.log("==== In loadVDOwnerList====");
                VDOwnersList.list({}, function(data) {
                    // only need the service_name
                    $scope.pbUserList = [];

                    //console.log("vdList = " + data);
                    angular.forEach(data, function(vd){
                        $scope.pbUserList.push(vd.USER_ID);
                    });
                });
            };
            $scope.loadVdOwnersList();

            /* page operations */
            $scope.newCssSource = function() {
                if ($scope.cssSource != undefined) {
                    var r=confirm("${message(code: 'sspb.css.cssManager.newStylesheet.unsaved.changes.message', encodeAs: 'JavaScript')}");
                    if (!r)
                        return;
                }

                $scope.cssName= "${message( code:'sspb.css.cssManager.newCss.default', encodeAs: 'JavaScript')}";
                $scope.cssSource = "";
                $scope.description="";
                $scope.cssOwner = user.oracleUserName;
                $scope.allowUpdateOwner = true;
                $scope.allowModify = true;
            };


            $scope.submitCssSource = function() {
                //check if page name is set
                if ($scope.cssName== undefined || $scope.cssName == '') {
                    alert("${message(code:'sspb.css.cssManager.cssName.prompt.message', encodeAs: 'JavaScript')}");
                    return;
                }

                Css.save({cssName:$scope.cssName, source:$scope.cssSource, description:$scope.description,
                    owner:$scope.cssOwner?$scope.cssOwner:user.oracleUserName }, function(response) {
                    //console.log("save response = " + response.statusCode + ", " +response.statusMessage);
                    var note = {type:"error"};
                    if (response.statusCode == 0) {
                        $scope.cssOwner=$scope.cssOwner?$scope.cssOwner:user.oracleUserName;
                        $scope.cssStatus.message = $scope.i18nGet(response.statusMessage);
                        note = {type: "success", flash: true};
                        $scope.allowUpdateOwner = response.allowUpdateOwner;
                        $scope.allowModify = response.allowModify;
                        $scope.resetCssNameData();
                    } else {
                        var msg="${message(code:'sspb.css.cssManager.validation.error.message', encodeAs: 'JavaScript')}";
                        if (response.cssValidationResult != undefined)
                            $scope.cssStatus.message = $scope.i18nGet(msg, [$scope.i18nGet(response.statusMessage), response.cssValidationResult.errors]);
                        else
                            $scope.cssStatus.message = $scope.i18nGet(msg, [$scope.i18nGet(response.statusMessage), ""]);
                    }
                    alert($scope.cssStatus.message, note );

                    // refresh the page list in case new page is added
                    //  $scope.loadCssNames();
                }, function(response) {
                    if(response && response.status == 403){
                        var err = response.data && response.data.errors ? response.data.errors.errorMessage : "";
                        alert(err,{type: "error",flash: true});
                    }else {
                        var msg = "${message(code: 'sspb.css.cssManager.stylesheet.submit.failed.message', encodeAs: 'JavaScript')}";
                        ;
                        if (response.data != undefined && response.data.errors != undefined)
                            msg = $scope.i18nGet(msg, [response.data.errors[0].errorMessage]);
                        else
                            msg = $scope.i18nGet(msg, ['']);

                        alert(msg, {type: "error"});
                    }
                });

            }

            $scope.deleteCssSource = function () {
                //check if page name is set
                if ($scope.cssName== undefined || $scope.cssName == '') {
                    alert("${message(code:'sspb.css.cssManager.cssName.prompt.message')}",{type: "warning"});
                    return;
                }

                Css.delete({constantName:$scope.cssName }, function() {
                    // on success
                    alert("${message(code:'sspb.css.cssManager.deletion.success.message')}",{flash:true});
                    // clear the page name field and page source
                    $scope.cssName = "";
                    $scope.description = "";
                    $scope.cssSource= undefined;
                    $scope.resetCssNameData();
                    // $scope.loadCssNames();

                }, function(response) {
                    if(response && response.status == 403){
                        var err = response.data && response.data.errors ? response.data.errors.errorMessage : "";
                        alert(err,{type: "error",flash: true});
                    }else {
                    var msg="${message(code:'sspb.css.cssManager.deletion.error.message')}";

                    if (response.data != undefined && response.data.errors != undefined)
                        msg = $scope.i18nGet(msg,[response.data.errors[0].errorMessage]);
                    else
                        msg = $scope.i18nGet(msg, ['']);

                    alert(msg,{type: "error"});
                    }
                });

            }

            $scope.getDeveloperSecurityPage = function(){
                window.open(rootWebApp+'customPage/page/'+ 'pbadm.DeveloperPageSecurity', '_self');

            }

            $scope.pageNamePoppup = function(params) {
                console.log("Model Popup");
                initlizePopUp(params);

            }

            $scope.resetCssNameData = function(){
                $("#cssConstantName option").each(function() {
                    $(this).text($scope.cssName);
                    $(this).val($scope.cssName);
                    $(this).attr('label', $scope.cssName);
                    $(this).attr('selected', 'selected');

                });
            }

        }
    </script>
</head>
<body>
    <asset:javascript src="modules/pageBuilderDev-mf.js"/>
    <div id="content" ng-controller="CssManagerController" class="customPage container-fluid cssPage" role="main">
    <div class="btn-section">
        <label for="cssConstantName"><g:message code="sspb.css.cssManager.load.label" /></label>
        <select id="cssConstantName" name="constantName" class="popupSelectBox vpc-name-input pbPopupDataGrid:{'serviceNameType':'csses','id':'cssConstantName'}"
                ng-model="cssName"
                ng-change="getCssSource()">
        </select>

        <button class="secondary" ng-click='loadCssNames()' ng-show="false" ><g:message code="sspb.css.cssManager.reload.pages.label" /></button>
    </div>
    <div class="btn-section-2">
        <button class="primary" ng-click='newCssSource();resetCssNameData();' ng-disabled="${!isProductionReadOnlyMode}" ><g:message code="sspb.css.cssManager.newCss.label" /></button>
        <button class="secondary" ng-click='submitCssSource()' ng-disabled="${!isProductionReadOnlyMode} || !(allowModify == null ? true :allowModify)"><g:message code="sspb.css.cssManager.save.label" /></button>
        <span ng-show="${isProductionReadOnlyMode}">
            <pb-Upload label='Upload Stylesheet' status='cssStatus' pb-change=''></pb-Upload>
        </span>
        <button class="secondary" ng-click="getCssSource()"><g:message code="sspb.css.cssManager.reload.label" /></button>
        <button class="secondary" ng-click='deleteCssSource()' ng-disabled="${!isProductionReadOnlyMode} || !(allowModify == null ? true :allowModify) "><g:message code="sspb.css.cssManager.delete.label" /></button>
        <span  ng-show="cssName.length>0">
            <button class="secondary" ng-click='getDeveloperSecurityPage()'><g:message code="sspb.css.cssManager.developer.label" /></button>
        </span>

        <span ng-show="cssName" class="alignRight">
            <label class="vpc-name-label dispInline" for="pbid-cssOwner"><g:message code="sspb.css.visualbuilder.cssowner.label" /></label>
            <input style="display: none" ng-model="allowUpdateOwner" aria-label="Allow Update Owner"/>
            <select id="pbid-cssOwner" class="owner-select alignRight" ng-model="cssOwner"  ng-disabled="!allowUpdateOwner">
                <option ng-repeat="owner in pbUserList" value="{{owner}}" ng-selected="{{owner == pageOwner}}">{{owner}}</option>
            </select>
        </span>
    </div>
    <div class="form-horizontal" ng-form="cssform">
        <div class="control-group">
            <label class="col-sm-3 control-label"  for='cssName'><g:message code="sspb.css.cssManager.cssName.label" /></label>
            <div class="col-sm-9">
                <input name="cssName" id='cssName' class="form-control" ng-model='cssName' required maxlength="60" ng-pattern="/^[a-zA-Z]+[a-zA-Z0-9\._-]*$/" />
                <span ng-messages="cssform.cssName.$error" role="alert" class="fieldValidationMessage">
                    <span ng-message="pattern" ><g:message code="sspb.page.visualbuilder.name.invalid.pattern.message" /></span>
                    <span ng-message="required" > <g:message code="sspb.page.visualbuilder.name.required.message" /></span>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="col-sm-3 control-label"  for='desc'><g:message code="sspb.css.cssManager.description.label" /></label>
            <div class="col-sm-9">
                <input name="description" id='desc' class="form-control" ng-model='description' maxlength="255"/>
            </div>
        </div>

        <div class="control-group">
            <label class="col-sm-3 control-label" for='source'><g:message code="sspb.css.cssManager.cssSource.label" /></label>
            <div class="col-sm-9">
                <textarea ng-model='cssSource' class="form-control" rows="9" id='source'></textarea>
            </div>
        </div>
    </div>
    <textArea name="statusMessage" readonly="true" ng-model="cssStatus.message" aria-label="Status Message"
              rows="3" cols="120" style="width:99%; height:10%"></textArea>
</div>

</body>
</html>
