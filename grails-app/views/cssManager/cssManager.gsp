<%--
  Created by IntelliJ IDEA.
  User: jzhong
  Date: 7/24/13
  Time: 10:14 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
<head>

    <meta name="layout" content="bannerSelfServicePBPage"/>

  <title>CSS Stylesheet Manager</title>

    <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'pageComposer.css')}">

    <script src="<g:resource plugin="banner-sspb" dir="js" file="pbDirectives.js" />"> </script>

    <script type="text/javascript">
        var myCustomServices = ['ngResource', 'ui.bootstrap', 'pagebuilder.directives', 'ngUpload'];

        function CssManagerController( $scope, $http, $resource, $parse) {
            // upload status callback
            $scope.foo = "Hello!";
            $scope.bar = function(content) {
                //console.log(content.length);
                $scope.uploadResponse = content;
                $scope.cssStatus.message = content.statusMessage;
            };

            $scope.i18nGet = function(key,args) {
                var tr = [];
                tr['sspb.css.cssManager.upload.label'             ]="${message(code:'sspb.css.cssManager.upload.label')}";
                tr['pb.template.upload.ok.label'             ]="${message(code:'pb.template.upload.ok.label')}";

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
            var Css = $resource(rootWebApp+'internal/csses/:constantName',{},{
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
            $scope.loadCssNames();

            $scope.getCssSource = function() {
                //TODO prompt for unsaved data
                if ($scope.cssSource != undefined)   {
                    var r=confirm("${message(code: 'sspb.page.visualbuilder.loadpage.unsaved.changes.message', encodeAs: 'JavaScript')}");
                    if (!r)
                        return;
                }
                Css.get({constantName:$scope.cssName}, function (data){
                    try {
                        //$scope.cssSource = JSON.parse(data.css);
                        $scope.cssSource = data.css;
                        $scope.description = data.description;
                    } catch(ex) {
                        alert($scope.i18nGet("${message(code:'sspb.page.visualbuilder.parsing.error.message')}",[ex]));
                    }
                }, function(response) {
                    var msg  = "${message(code: 'sspb.page.visualcomposer.page.load.failed.message', encodeAs: 'JavaScript')}";
                    if (response.data != undefined && response.data.errors!=undefined)
                        msg = $scope.i18nGet(msg, [response.data.errors[0].errorMessage]);
                    else
                        msg = $scope.i18nGet(msg, ['']);

                    alert(msg);
                });
            };

            /* page operations */
            $scope.newCssSource = function() {
                if ($scope.cssSource != undefined) {
                    var r=confirm("${message(code: 'sspb.page.visualbuilder.newpage.unsaved.changes.message', encodeAs: 'JavaScript')}");
                    if (!r)
                        return;
                }

                $scope.cssName= "${message( code:'sspb.css.cssManager.newcss.default', encodeAs: 'JavaScript')}";
                $scope.cssSource = "";
                $scope.description="";
            };


            $scope.submitCssSource = function() {
                //check if page name is set
                if ($scope.cssName== undefined || $scope.cssName == '') {
                    alert("${message(code:'sspb.page.visualbuilder.page.name.prompt.message')}");
                    return;
                }

                Css.save({cssName:$scope.cssName, source:$scope.cssSource, description:$scope.description }, function(response) {
                    //console.log("save response = " + response.statusCode + ", " +response.statusMessage);
                    if (response.statusCode == 0)
                        $scope.cssStatus.message = response.statusMessage;
                    else {
                        var msg="${message(code:'sspb.page.validation.error.message')}";
                        if (response.cssValidationResult != undefined)
                            $scope.cssStatus.message = $scope.i18nGet(msg,[response.statusMessage,response.cssValidationResult.errors]);
                        else
                            $scope.cssStatus.message = $scope.i18nGet(msg,[response.statusMessage, ""]);
                    }

                    alert($scope.cssStatus.message);

                    // refresh the page list in case new page is added
                    $scope.loadCssNames();
                }, function(response) {
                    var msg ="${message(code: 'sspb.page.visualcomposer.page.submit.failed.message', encodeAs: 'JavaScript')}";;
                    if (response.data != undefined && response.data.errors!=undefined)
                        msg =  $scope.i18nGet(msg, [response.data.errors[0].errorMessage]);
                    else
                        msg = $scope.i18nGet(msg, ['']);

                    alert(msg);
                });

            }

            $scope.deleteCssSource = function () {
                //check if page name is set
                if ($scope.cssName== undefined || $scope.cssName == '') {
                    alert("${message(code:'sspb.page.visualbuilder.page.name.prompt.message')}");

                    return;
                }

                Css.delete({constantName:$scope.cssName }, function() {
                    // on success
                    alert("${message(code:'sspb.page.visualbuilder.deletion.success.message')}");
                    // clear the page name field and page source
                    $scope.cssName = "";
                    $scope.description = "";
                    $scope.cssSource= undefined;
                    $scope.loadCssNames();

                }, function(response) {
                    var msg="${message(code:'sspb.page.visualbuilder.deletion.error.message')}";

                    if (response.data != undefined && response.data.errors != undefined)
                        msg = $scope.i18nGet(msg,[response.data.errors[0].errorMessage]);
                    else
                        msg = $scope.i18nGet(msg, ['']);

                    alert(msg);

                });

            }

        }
    </script>
</head>
<body>
    <div id="content" ng-controller="CssManagerController" class="customPage">

        <label><g:message code="sspb.css.cssManager.load.label" /></label>
        <select name="constantName"
                ng-options="css.css.constantName as css.css.constantName for css in cssList"
                ng-model="cssName"
                ng-change="getCssSource()"></select>

        <button ng-click='loadCssNames()'><g:message code="sspb.css.cssManager.reload.pages.label" /></button>
        <div>
        <button ng-click='newCssSource()'><g:message code="sspb.css.cssManager.new.css.label" /></button>
        <button ng-click='submitCssSource()'><g:message code="sspb.css.cssManager.save.label" /></button>
        <pb-Upload label='Upload Stylesheet' status='cssStatus' pb-change='loadCssNames()'></pb-Upload>
        <button ng-click="getCssSource()"><g:message code="sspb.css.cssManager.reload.label" /></button>
        <button ng-click='deleteCssSource()'><g:message code="sspb.css.cssManager.delete.label" /></button>

        </div>
        <div class="form-horizontal">
            <div class="control-group">
                <label class="control-label"  for='cssName'>Select a name for publishing:</label>
                <input name="cssName" id='cssName' ng-model='cssName'/>
            </div>
            <div class="control-group">
                <label class="control-label"  for='desc'>Enter a description:</label>
                <input name="description" id='desc' ng-model='description'/>
            </div>

            <div class="control-group">
                <label class="control-label" for='source'>Stylesheet Source</label>
                <textarea ng-model='cssSource' id='source'></textarea>
            </div>
        </div>
        <textArea name="statusMessage" readonly="true" ng-model="cssStatus.message"
                  rows="3" cols="120" style="width:99%; height:10%"></textArea>
    </div>

</body>
</html>