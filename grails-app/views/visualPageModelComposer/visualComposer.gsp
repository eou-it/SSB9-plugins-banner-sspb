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
     var myCustomServices = ['ngResource'];

    // remove additional properties added by Angular resource when pretty print page source
    function JSONFilter(key, value) {
      if (key == "\$resolved" || key == "\$\$hashKey") {
        return undefined;
      }
      return value;
    }


     // define angular controller
     function VisualPageComposerController( $scope, $http, $resource, $parse) {
        $scope.pageName = "${pageModel.pageInstance?.constantName}";
        $scope.pageSource = null;
        // data holder to from reference in child scopes
        $scope.dataHolder = {};

        // load the model definition from deployed app
         var PageModelDef = $resource(rootWebApp+'visualPageModelComposer/pageModelDef');
         PageModelDef.get(null, function(data) {
            $scope.pageModelDef = data.definitions.componentTypeDefinition;
            console.log($scope.pageModelDef);
          });

        // return all required attributes for a given component type
        $scope.findRequiredAttrs = function(type) {
            var attrs = [];
            // add required attribute for all components
            angular.forEach($scope.pageModelDef, function(componentDef) {
                if (componentDef.componentType.indexOf("all") !=-1)
                    attrs = attrs.concat(componentDef.requiredAttributes);
                if (componentDef.componentType.indexOf(type) !=-1)
                    attrs = attrs.concat(componentDef.requiredAttributes);
            });
            return attrs;
        }
        // return all required attributes for a given component type
        $scope.findOptionalAttrs = function(type) {
            var attrs = [];
            // add required attribute for all components
            angular.forEach($scope.pageModelDef, function(componentDef) {
                if (componentDef.componentType.indexOf("all") !=-1)
                    attrs = attrs.concat(componentDef.optionalAttributes);
                if (componentDef.componentType.indexOf(type) !=-1)
                    attrs = attrs.concat(componentDef.optionalAttributes);
            });
            return attrs;
        }

        $scope.findRequiredChildren = function(type) {
            var children = [];
            // add required attribute for all components
            angular.forEach($scope.pageModelDef, function(componentDef) {
                if (componentDef.componentType.indexOf(type) !=-1)
                    children = children.concat(componentDef.requiredChildren);
            });
            return children;
        }

        $scope.findOptionalChildren = function(type) {
            var children = [];
            // add required attribute for all components
            angular.forEach($scope.pageModelDef, function(componentDef) {
                if (componentDef.componentType.indexOf(type) !=-1)
                    children = children.concat(componentDef.optionalChildren);
            });
            return children;
        }

        $scope.getPageSource = function() {
            this.Resource=$resource(rootWebApp+'visualPageModelComposer/page');
            $scope.pageSource = this.Resource.get({pageName:$scope.pageName}, function (){
                $scope.handlePageTreeChange();
            });
        };

        $scope.handlePageTreeChange = function() {
            $scope.pageSourceView = JSON.stringify($scope.pageSource, JSONFilter, 6);
        }

        $scope.deleteComponent = function(parent, index) {
            parent.splice(index, 1);
            $scope.handlePageTreeChange();
        };

        $scope.deleteChildren = function(data) {
            data.components = [];
            $scope.handlePageTreeChange();
        };

        $scope.addChild = function(data) {
            var post = data.components.length + 1;
            var newName = data.name + '-' + post;
            data.components.push({name: newName,components: []});
            $scope.handlePageTreeChange();
        };
     }

    </script>

</head>
<body>

<div ng-controller="VisualPageComposerController">

    <label>Load Page</label>
    <g:select name="constantName"
              from="${Page.list().sort {it.constantName}}"
              value="${pageModel.pageInstance?.constantName}"
              noSelection="${['null': 'Select One...']}"
              optionKey="constantName"
              optionValue="constantName"
                ng-model="pageName"
                ng-change="getPageSource()"/>


<br/>


    <label>Unique Page Name</label>
    <input type="text" name="constantName" ng-model="pageName" required/>

    <input type="button" ng-click="getPageSource()" value="Reload Page Source" />
    <input type="hidden" name="id" value="${pageModel.pageInstance?.id}"/>
    <table>
        <tr>
            <th align = left style="width:30%">Page Source View</th>
            <th align = left style="width:40%">Component Tree View</th>
            <th align = left style="width:30%">Component Property View</th>
        </tr>
        <tr height="90%">
            <td>
                <g:textArea name="modelView" ng-model="pageSourceView"
                            rows="32" cols="60" style="width:100%; height:100%" required="true"/>

            </td>

            <td>
                <script type="text/ng-template"  id="tree_item_renderer.html">
                    <div>
                    <input type="checkbox" ng-model="showChildren" ng-show="data.components!=undefined"> {{data.name}} ({{data.type}})
                    <button ng-click="deleteComponent(c, $index)">Delete</button>
                    <button ng-click="addChild(data)">Add child</button>
                    <button ng-click="deleteChildren(data)" ng-show="data.components.length > 0">Delete child(ren)</button>
                    <ul ng-show="showChildren">
                        <li ng-repeat="data in data.components" ng-init="c = data.components" ng-click="dataHolder.selectedComponent = data"  ng-include="'tree_item_renderer.html'"></li>
                    </ul>
                    </div>
                </script>
                <div style="width:100%; height:50%" ng-show="pageSource != undefined">
                    <input type="checkbox" ng-model="showChildren"> {{pageSource.name}} ({{pageSource.type}})
                <ul ng-show="showChildren">
                    <li ng-repeat="data in pageSource.components" ng-init="c = pageSource.components" ng-click="dataHolder.selectedComponent = data" ng-include="'tree_item_renderer.html'"></li>
                </ul>
                </div>
            </td>
            <td>
                <div ng-show="dataHolder.selectedComponent!=undefined">
                    <div ng-repeat="attrName in findRequiredAttrs(dataHolder.selectedComponent.type)">
                        <label>{{attrName}}</label></s></label><input type="text" ng-model="dataHolder.selectedComponent[attrName]"/>
                    </div>
                    <div ng-repeat="attrName in findOptionalAttrs(dataHolder.selectedComponent.type)">
                        <label>{{attrName}}</label></s></label><input type="text" ng-model="dataHolder.selectedComponent[attrName]"/>
                    </div>
                </div>
            </td>
        </tr>
    </table>
    <g:textArea name="statusMessage" readonly="true" value="${pageModel.status}"
                rows="3" cols="120" style="width:99%; height:50%"/>

</div>
</body>

</body>
</html>