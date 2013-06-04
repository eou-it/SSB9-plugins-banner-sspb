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


    <script src="/banner-sspb/js/pbDirectives.js"></script>

    <script type="text/javascript">
     var myCustomServices = ['ngResource', 'ui.bootstrap', 'pagebuilder.directives'];

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
        // top level page source container must be an array for tree view rendering consistency
        $scope.pageSource = [];
        // data holder for reference in child scopes
        $scope.dataHolder = {};
        // status holder to remember which component property is shown
        $scope.statusHolder = {selectedIndex:0, newIndex:-1};

        // index is a unique number assigned to each component in its own scope
        $scope.index = 0;
        $scope.nextIndex = function () {
            $scope.index = $scope.index + 1;
            return $scope.index;
        }

        // used to highlight the selected component in the component tree
        // TODO this does not work with IE 9
        $scope.componentLabelStyle = function(selected) {
        if (selected)
            //return "text-decoration:underline;";
            return "border-style:ridge; border-width:2px;";
        else
            return "";
        }


        // load the model definition from deployed app
         var PageModelDef = $resource(rootWebApp+'visualPageModelComposer/pageModelDef');
         PageModelDef.get(null, function(data) {
            $scope.pageModelDef = data.definitions.componentTypeDefinition;
            //console.log($scope.pageModelDef);
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

        // return all attributes (required and optional) for a given component type
        // return a array of map of {name:"", required: isRequired}
        $scope.findAllAttrs = function(type) {
            $scope.dataHolder.allAttrs =[];
            // insert required attributes
            var attrs = $scope.findRequiredAttrs(type);
            angular.forEach(attrs, function(attrName) {
                $scope.dataHolder.allAttrs.push({name:attrName, required:true});
            });

            // insert optional attributes
            var optAttrs = $scope.findOptionalAttrs(type);
            angular.forEach(optAttrs, function(attrName) {
                $scope.dataHolder.allAttrs.push({name:attrName, required: false} );
            });
        }

        $scope.findRequiredChildrenTypes = function(type) {
            var children = [];
            // add required attribute for all components
            angular.forEach($scope.pageModelDef, function(componentDef) {
                if (componentDef.componentType.indexOf(type) !=-1)
                    children = children.concat(componentDef.requiredChildren);
            });
            return children;
        }

        $scope.findOptionalChildrenTypes = function(type) {
            var children = [];
            // add required attribute for all components
            angular.forEach($scope.pageModelDef, function(componentDef) {
                if (componentDef.componentType.indexOf(type) !=-1)
                    children = children.concat(componentDef.optionalChildren);
            });
            return children;
        }

        $scope.findAllChildrenTypes = function(type) {
            return $scope.findRequiredChildrenTypes(type).concat($scope.findOptionalChildrenTypes(type));
        }


        $scope.getPageSource = function() {
            this.Resource=$resource(rootWebApp+'visualPageModelComposer/page');
            $scope.pageOneSource = this.Resource.get({pageName:$scope.pageName}, function (){
                $scope.pageSource[0] = $scope.pageOneSource;
                $scope.resetSelected();
                $scope.handlePageTreeChange();
            });
        };

        $scope.handlePageTreeChange = function() {
            $scope.pageSourceView = JSON.stringify($scope.pageSource[0], JSONFilter, 6);
        }

        //
        $scope.resetSelected = function() {
            $scope.dataHolder.selectedComponent = undefined;
            $scope.statusHolder.selectedIndex = 0;
            $scope.dataHolder.allAttrs = [];
        }

        // recursively check if component1 is a direct or indirect child of component
        $scope.isChild = function(component, component1) {
            // reach a leaf node
            if (component.components==undefined || component.components.length==0)
                return false;

            for(var i= 0; i< component.components.length; i++) {
                // stop search if found a matching child
                if (component.components[i] === component1)
                    return true;
                if ($scope.isChild(component.components[i], component1))
                    return true;
            }
            //console.log("comp = " + component.type + ", found = " + found);
            return false;
        }

        /*
        delete a component from the tree
        parameters:
            parent - parent of the component to be deleted
            index - the index of the component in the context of the parent
            gIndex - the unique global index number assigned to the component to be deleted
         */
        $scope.deleteComponent = function(parent, index, gIndex) {
            var isChildSelected = $scope.dataHolder.selectedComponent!= undefined && $scope.isChild(parent.components[index], $scope.dataHolder.selectedComponent);

            parent.components.splice(index, 1);
            // if the deleted component is open or it has children (which MAY be selected)then unset the currently selected component
            if ($scope.statusHolder.selectedIndex == gIndex || isChildSelected)
               $scope.resetSelected();

            //$scope.$apply('parent');
            $scope.handlePageTreeChange();
        };

        $scope.deleteChildren = function(data) {
            data.components = [];
            //$scope.$apply('data');
            $scope.handlePageTreeChange();
        };

        $scope.addChild = function(data) {
            //console.log("addChild");
            $scope.validChildTypes = $scope.findAllChildrenTypes(data.type);
            // TODO make sure the children are expanded if appending a new component
            //$scope.showChildren = true;
            $scope.openTypeSelectionModal(data, -1);
            // delay adding node until the type selection is made
        };

        $scope.insertSibling = function(data, index) {
            //console.log("addChild");
            $scope.validChildTypes = $scope.findAllChildrenTypes(data.type);
            $scope.openTypeSelectionModal(data, index);
        }

        $scope.selectData = function(data, index) {
            //alert("scope = " + $scope.$id + ", data = " + data.type);
            $scope.dataHolder.selectedComponent = data;
            $scope.statusHolder.selectedIndex = index;
            // update the current selected component's property list
            $scope.findAllAttrs(data.type);

            //console.log("scope = " + $scope.$id);
        };

        $scope.toggleShowChildren = function() {
            $scope.showChildren = !$scope.showChildren;
        }

        // type selection modal dialog functions
        /*
        Note! use console.log() immediately before, during & after modal dialog is displayed will prevent the dialog to show on IE 9
         unless the developer tool window is opened.
         */
          $scope.openTypeSelectionModal = function (data, index) {
            $scope.shouldBeOpen = true;
            $scope.newData = data;
            $scope.newIndex = index;
          };

          $scope.closeTypeSelectionModal = function () {
            //$scope.closeMsg = 'I was closed at: ' + new Date();
            $scope.shouldBeOpen = false;
            // add the child component
            var data = $scope.newData;
            if (data.components==undefined)
                data.components=[];
            var post = data.components.length + 1;
            var newName = data.name + '_child_' + post;
            //console.log("Adding child =" + newName);
            var newComp = {name: newName, type: $scope.selectedType};
            if ($scope.newIndex==-1)
                data.components.push(newComp);
            else
                data.components.splice($scope.newIndex, 0, newComp);
            // open the new component for editing - the new component always get an incremented index number
            $scope.selectData(newComp, $scope.index+1);
            // modal dialog is associated with parent scope
            $scope.handlePageTreeChange();

          };

          $scope.cancelTypeSelectionModal = function() {
            $scope.shouldBeOpen = false;
          }

          $scope.typeSelectionModalOpts = {
            backdropFade: true,
            dialogFade:true
          };

          $scope.tMap={a:'1', b:'2'};
     }

    </script>

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

<div ng-controller="VisualPageComposerController" class="customPage">

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
    <button ng-click='handlePageTreeChange()'>Refresh Tree View</button>
    <input type="hidden" name="id" value="${pageModel.pageInstance?.id}"/>
    <table style="height:80%;">
        <tr>
            <th align = left style="width:30%">Page Source View</th>
            <th align = left style="width:30%">Component Tree View</th>
            <th align = left style="width:40%">Component Property View</th>
        </tr>
        <tr height="99%">
            <td>
                <g:textArea name="modelView" ng-model="pageSourceView"
                            cols="60" rows="30" style="width:100%; height:auto;" required="true"/>

            </td>

            <td>

                <script type="text/ng-template"  id="tree_item_renderer.html">
                    <span  ng-show="data.components!=undefined && data.components.length>0">
                        <button title="Collapse tree" style="background:none;border:none; font-size:100%; color:gray;" ng-click="showChildren=!showChildren;"  ng-show="showChildren">&#x229f;</button>
                        <button title="Expand tree" style="background:none;border:none; font-size:100%; color:gray;" ng-click="showChildren=!showChildren;"  ng-show="!showChildren">&#x229e;</button>
                    </span>
                    <!-- align text if there is no expand/collapse button-->
                    <span  ng-show="data.components==undefined || data.components.length==0" style="background:none;border:none; font-size:100%">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </span>
                    <!--input type="checkbox" ng-model="showChildren" ng-show="data.components!=undefined && data.type!=undefined"/-->
                    <span ng-init="index=nextIndex()" ng-click="selectData(data, index)" style="{{componentLabelStyle(index == statusHolder.selectedIndex)}}">{{data.name}} [{{data.type}}]</span>
                    <button  title="Insert a sibling component before this component" class="btn btn-mini" style="background:none;" ng-click="insertSibling($parent.$parent.data, $index)" ng-show="data.type!='page'">&larr;</button>
                    <button  title="Append a child component" class="btn btn-mini" style="background:none;" ng-click="addChild(data)" ng-show="findAllChildrenTypes(data.type).length>0">+</button>
                    <button  title="Delete this component" class="btn btn-mini" style="background:none;" ng-click="deleteComponent($parent.$parent.data, $index, index)"  ng-show="data.type!='page'">-</button>
                    <!--button  class="btn btn-mini" ng-click="deleteChildren(data)" ng-show="data.components.length > 0">--</button-->
                    <!--input type="checkbox" ng-model="(index == statusHolder.selectedIndex)" ng-init="index=index+1" /-->

                    <ul ng-show="showChildren" style="list-style: none;">
                        <li ng-repeat="data in data.components"   ng-include="'tree_item_renderer.html'"></li>
                    </ul>
                </script>

                <div style="width:100%;  overflow-y: auto; overflow-x: auto; white-space:nowrap;" ng-show="pageName != '' && pageName != 'null'">
                <ul style="list-style: none;" ng-init="showChildren=true;">
                    <li ng-repeat="data in pageSource"   ng-include="'tree_item_renderer.html'"></li>
                </ul>
                </div>
            </td>
            <td>
                <div ng-show="dataHolder.selectedComponent!=undefined">
                    <!--
                    <div>Selected Component = {{dataHolder.selectedComponent.type}}</div>
                    -->

                    <div ng-repeat="attr in dataHolder.allAttrs">
                        <label style="text-align:right; width: 30%">{{attr.name}}<span ng-show="attr.required">*</span></label>
                        <span ng-switch on="attr.name" >
                            <pb-Map ng-switch-when="parameters" label='Edit parameters for {{dataHolder.selectedComponent.name}}' map='dataHolder.selectedComponent[attr.name]' pb-change="handlePageTreeChange()"></pb-Map>
                            <input ng-switch-default style="text-align:left;" type="text" ng-change="handlePageTreeChange()" ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>
                        </span>
                    </div>
                </div>
             </td>
        </tr>



    </table>





    <!-- type selection modal body-->
    <div modal="shouldBeOpen"  options="typeSelectionModalOpts">
        <div class="modal-header">
            <h4>Select a component type</h4>
        </div>
        <div class="modal-body">

            <select  ng-model="$parent.selectedType" ng-options="type for type in validChildTypes"></select>
        </div>
        <div class="modal-footer">
            <button class="btn btn-success ok" ng-click="closeTypeSelectionModal()">Create Component</button>
            <button class="btn btn-warning cancel" ng-click="cancelTypeSelectionModal()">Cancel Creation</button>
        </div>
    </div>

    <g:textArea name="statusMessage" readonly="true" value="${pageModel.status}"
                rows="3" cols="120" style="width:99%; height:10%"/>

</div>
</body>

</body>
</html>