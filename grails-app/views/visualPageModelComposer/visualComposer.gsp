<%--
  Created by IntelliJ IDEA.
  User: jzhong
  Date: 5/17/13
  Time: 4:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="net.hedtech.banner.sspb.PageComponent; net.hedtech.banner.sspb.Page" %>
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
        // page command execution status
        $scope.pageStatus = {};

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
            $scope.sourceRenderDef = data.definitions.sourceRenderDefinitions;
            $scope.setAttributeRenderProperties();
            //console.log($scope.sourceRenderDef);
          });

         // create a map of {attribute type -> rendering property} for editing a component attribute
         // can not do this dynamically when ng-switch is called because it will keep calling the function to monitor the change
         $scope.setAttributeRenderProperties = function() {
            $scope.attrRenderProps = {};
            //var defaultRenderProp = {renderType:"text"};
            //var found = false;
            for(var i=0; i < $scope.sourceRenderDef.length; i++ ){
                var attrDef = $scope.sourceRenderDef[i];
                for (var j = 0; j < attrDef.AttributeType.length; j++) {
                    $scope.attrRenderProps[attrDef.AttributeType[j]] = attrDef.renderProperty;
                }
            }
            //console.log("attrRenderProps = " + $scope.attrRenderProps);
         }

         /* return the default value for an attribute
          if the value is undefined and there is a default value then return the default value
          if the value is undefined and there is a default value then return undefined
          if the value is defined then return the current value
          */
         $scope.setDefaultValue = function (attributeName, value) {
            if (value == undefined && $scope.attrRenderProps[attributeName].default!=undefined)
                return  $scope.attrRenderProps[attributeName].default;
            else
                return value;

         };

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

        $scope.i18nGet  = function (key,args) {
            var tr = [];
            tr['attribute.type'             ]="${message(code:'sspb.model.attribute.type')}";
            tr['attribute.name'             ]="${message(code:'sspb.model.attribute.name')}";
            tr['attribute.documentation'    ]="${message(code:'sspb.model.attribute.documentation')}";
            tr['attribute.title'            ]="${message(code:'sspb.model.attribute.title')}";
            tr['attribute.scriptingLanguage']="${message(code:'sspb.model.attribute.scriptingLanguage')}";
            tr['attribute.label'            ]="${message(code:'sspb.model.attribute.label')}";
            tr['attribute.style'            ]="${message(code:'sspb.model.attribute.style')}";
            tr['attribute.submit'           ]="${message(code:'sspb.model.attribute.submit')}";
            tr['attribute.submitLabel'      ]="${message(code:'sspb.model.attribute.submitLabel')}";
            tr['attribute.model'            ]="${message(code:'sspb.model.attribute.model')}";
            tr['attribute.value'            ]="${message(code:'sspb.model.attribute.value')}";
            tr['attribute.validation'       ]="${message(code:'sspb.model.attribute.validation')}";
            tr['attribute.placeholder'      ]="${message(code:'sspb.model.attribute.placeholder')}";
            tr['attribute.onUpdate'         ]="${message(code:'sspb.model.attribute.onUpdate')}";
            tr['attribute.onClick'          ]="${message(code:'sspb.model.attribute.onClick')}";
            tr['attribute.onLoad'           ]="${message(code:'sspb.model.attribute.onLoad')}";
            tr['attribute.labelKey'         ]="${message(code:'sspb.model.attribute.labelKey')}";
            tr['attribute.valueKey'         ]="${message(code:'sspb.model.attribute.valueKey')}";
            tr['attribute.sourceModel'      ]="${message(code:'sspb.model.attribute.sourceModel')}";
            tr['attribute.booleanTrueValue' ]="${message(code:'sspb.model.attribute.booleanTrueValue')}";
            tr['attribute.booleanFalseValue']="${message(code:'sspb.model.attribute.booleanFalseValue')}";
            tr['attribute.sequence'         ]="${message(code:'sspb.model.attribute.sequence')}";
            tr['attribute.binding'          ]="${message(code:'sspb.model.attribute.binding')}";
            tr['attribute.resource'         ]="${message(code:'sspb.model.attribute.resource')}";
            tr['attribute.nextButtonLabel'  ]="${message(code:'sspb.model.attribute.nextButtonLabel')}";
            tr['attribute.showInitially'    ]="${message(code:'sspb.model.attribute.showInitially')}";
            tr['attribute.allowNew'         ]="${message(code:'sspb.model.attribute.allowNew')}";
            tr['attribute.allowModify'      ]="${message(code:'sspb.model.attribute.allowModify')}";
            tr['attribute.allowDelete'      ]="${message(code:'sspb.model.attribute.allowDelete')}";
            tr['attribute.allowReload'      ]="${message(code:'sspb.model.attribute.allowReload')}";
            tr['attribute.required'         ]="${message(code:'sspb.model.attribute.required')}";
            tr['attribute.readonly'         ]="${message(code:'sspb.model.attribute.readonly')}";
            tr['attribute.loadInitially'    ]="${message(code:'sspb.model.attribute.loadInitially')}";
            tr['attribute.activated'        ]="${message(code:'sspb.model.attribute.activated')}";
            tr['attribute.parameters'       ]="${message(code:'sspb.model.attribute.parameters')}";
            tr['attribute.sourceParameters' ]="${message(code:'sspb.model.attribute.sourceParameters')}";
            tr['attribute.pageSize'         ]="${message(code:'sspb.model.attribute.pageSize')}";
            tr['attribute.imageUrl'         ]="${message(code:'sspb.model.attribute.imageUrl')}";
            tr['attribute.url'              ]="${message(code:'sspb.model.attribute.url')}";
            tr['attribute.sourceValue'      ]="${message(code:'sspb.model.attribute.sourceValue')}";
            tr['attribute.default'          ]="${message(code:'sspb.model.attribute.default')}";
            tr['attribute.description'      ]="${message(code:'sspb.model.attribute.description')}";

            tr['type.page'        ]="${message(code:'sspb.model.type.page'     )}";
            tr['type.flow'        ]="${message(code:'sspb.model.type.flow'     )}";
            tr['type.form'        ]="${message(code:'sspb.model.type.form'     )}";
            tr['type.block'       ]="${message(code:'sspb.model.type.block'    )}";
            tr['type.grid'        ]="${message(code:'sspb.model.type.grid'     )}";
            tr['type.select'      ]="${message(code:'sspb.model.type.select'   )}";
            tr['type.radio'       ]="${message(code:'sspb.model.type.radio'    )}";
            tr['type.list'        ]="${message(code:'sspb.model.type.list'     )}";
            tr['type.detail'      ]="${message(code:'sspb.model.type.detail'   )}";
            tr['type.data'        ]="${message(code:'sspb.model.type.data'     )}";
            tr['type.resource'    ]="${message(code:'sspb.model.type.resource' )}";
            tr['type.literal'     ]="${message(code:'sspb.model.type.literal'  )}";
            tr['type.display'     ]="${message(code:'sspb.model.type.display'  )}";
            tr['type.text'        ]="${message(code:'sspb.model.type.text'     )}";
            tr['type.textArea'    ]="${message(code:'sspb.model.type.textArea' )}";
            tr['type.number'      ]="${message(code:'sspb.model.type.number'   )}";
            tr['type.datetime'    ]="${message(code:'sspb.model.type.datetime' )}";
            tr['type.email'       ]="${message(code:'sspb.model.type.email'    )}";
            tr['type.tel'         ]="${message(code:'sspb.model.type.tel'      )}";
            tr['type.link'        ]="${message(code:'sspb.model.type.link'     )}";
            tr['type.boolean'     ]="${message(code:'sspb.model.type.boolean'  )}";
            tr['type.button'      ]="${message(code:'sspb.model.type.button'   )}";


            tr['sspb.page.visualbuilder.edit.map.title' ] = "${message(code:'sspb.page.visualbuilder.edit.map.title',encodeAs: 'JavaScript')}";

            var res=tr[key];
            if ( res )  {
                if (args) {
                    args.forEach(function (arg,index)  {
                        //note undefined parameters will show as undefined
                        res=res.replace("{"+index+"}",arg);
                    } );
                }
                return res;
            } else {
                return key ;
            }
        }

        $scope.attributeIsTranslatable = function (attr) {
            var attributes = ${PageComponent.translatableAttributes.encodeAsJSON()};
            return (attributes.indexOf(attr) != -1); // seems not to work in IE8
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

          /* page operations */
          $scope.newPageSource = function() {
            // TODO generate a unique page name
            var r=confirm(${message(code: 'sspb.page.visualbuilder.unsaved.changes.message')});
            if (!r)
                return;

            $scope.pageName= "${message( code:'sspb.page.visualbuilder.newpage.default', encodeAs: 'JavaScript')}";
            $scope.pageSource[0] = {"type": "page", "name": $scope.pageName};
            $scope.resetSelected();
            $scope.handlePageTreeChange();
          };

          $scope.submitPageSource = function() {
            this.Resource1=$resource(rootWebApp+'visualPageModelComposer/compilePage');
            // send the page source as text as expected by the compiler
            $scope.pageOneSource = this.Resource1.save({pageName:$scope.pageName, source:$scope.pageSourceView }, function(response) {
                //console.log("save response = " + response.statusCode + ", " +response.statusMessage);
                if (response.statusCode == 0)
                    $scope.pageStatus.message = response.statusMessage;
                else
                    $scope.pageStatus.message = response.statusMessage + " Page Validation Error:\n" + response.pageValidationResult.errors;

                alert($scope.pageStatus.message);
            });

          }

          $scope.previewPageSource = function() {

          }

          $scope.exportPageSource = function() {

          }

          $scope.importPageSource = function () {

          }

          $scope.deletePageSource = function () {

          }


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

    <label><g:message code="sspb.page.visualbuilder.load.label" /></label>
    <g:select name="constantName"
              from="${Page.list().sort {it.constantName}}"
              value="${pageModel.pageInstance?.constantName}"
              noSelection="${['null': 'Select One...']}"
              optionKey="constantName"
              optionValue="constantName"
                ng-model="pageName"
                ng-change="getPageSource()"/>


<br/>


    <label><g:message code="sspb.page.visualbuilder.name.label" /></label>
    <input type="text" name="constantName" ng-model="pageName" required/>

    <button ng-click='newPageSource()'><g:message code="sspb.page.visualbuilder.new.page.label" /></button>
    <button ng-click='submitPageSource()'><g:message code="sspb.page.visualbuilder.compile.save.label" /></button>
    <button ng-click="getPageSource()"><g:message code="sspb.page.visualbuilder.reload.label" /></button>
    <button ng-click="previewPageSource()"><g:message code="sspb.page.visualbuilder.preview.label" /></button>
    <button ng-click='exportPageSource()'><g:message code="sspb.page.visualbuilder.export.label" /></button>
    <button ng-click='importPageSource()'><g:message code="sspb.page.visualbuilder.import.label" /></button>
    <button ng-click='deletePageSource()'><g:message code="sspb.page.visualbuilder.delete.label" /></button>


    <input type="hidden" name="id" value="${pageModel.pageInstance?.id}"/>
    <table style="height:80%;">
        <tr>
            <th align = left style="width:30%"><g:message code="sspb.page.visualbuilder.page.sourceview.label" /></th>
            <th align = left style="width:30%"><g:message code="sspb.page.visualbuilder.component.treeview.label" /></th>
            <th align = left style="width:40%"><g:message code="sspb.page.visualbuilder.component.propertyview.label" /></th>
        </tr>
        <tr height="99%">
            <td>
                <g:textArea name="modelView" ng-model="pageSourceView"
                            cols="60" rows="30" style="width:100%; height:auto;" required="true"/>

            </td>

            <td>

                <script type="text/ng-template"  id="tree_item_renderer.html">
                    <span  ng-show="data.components!=undefined && data.components.length>0">
                        <button title="${message(code:'sspb.page.visualbuilder.collapsetree.title')}" style="background:none;border:none; font-size:100%; color:gray;" ng-click="showChildren=!showChildren;"  ng-show="showChildren">&#x229f;</button>
                        <button title="${message(code:'sspb.page.visualbuilder.expandtree.title')}" style="background:none;border:none; font-size:100%; color:gray;" ng-click="showChildren=!showChildren;"  ng-show="!showChildren">&#x229e;</button>
                    </span>
                    <!-- align text if there is no expand/collapse button-->
                    <span  ng-show="data.components==undefined || data.components.length==0" style="background:none;border:none; font-size:100%">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </span>
                    <!--input type="checkbox" ng-model="showChildren" ng-show="data.components!=undefined && data.type!=undefined"/-->
                    <span ng-init="index=nextIndex()" ng-click="selectData(data, index)" style="{{componentLabelStyle(index == statusHolder.selectedIndex)}}">{{data.name}} [{{i18nGet('type.'+data.type)}}]</span>
                    <button  title="${message(code:'sspb.page.visualbuilder.insert.sibling.title')}" class="btn btn-mini" style="background:none;" ng-click="insertSibling($parent.$parent.data, $index)" ng-show="data.type!='page'">&larr;</button>
                    <button  title="${message(code:'sspb.page.visualbuilder.append.child.title')}" class="btn btn-mini" style="background:none;" ng-click="addChild(data)" ng-show="findAllChildrenTypes(data.type).length>0">+</button>
                    <button  title="${message(code:'sspb.page.visualbuilder.delete.component.title')}" class="btn btn-mini" style="background:none;" ng-click="deleteComponent($parent.$parent.data, $index, index)"  ng-show="data.type!='page'">-</button>
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
                        <label style="text-align:right; width: 30%">{{i18nGet('attribute.'+attr.name)}}<span ng-show="attr.required">*</span> <span ng-show="attributeIsTranslatable(attr.name)">⊙</span></label>
                        <span ng-switch on="attrRenderProps[attr.name].inputType" >
                            <pb-Map ng-switch-when="map" label="{{i18nGet('sspb.page.visualbuilder.edit.map.title' , [i18nGet('attribute.'+attr.name),dataHolder.selectedComponent.name])}}"
                                    map='dataHolder.selectedComponent[attr.name]' pb-parent="dataHolder.selectedComponent" pb-attrname="attr.name"
                                    pb-change="handlePageTreeChange()"></pb-Map>
                            <input ng-switch-when="text" style="text-align:left;" type="text" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-change="handlePageTreeChange()" ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>
                            <input ng-switch-when="number" style="text-align:left;" type="number" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-change="handlePageTreeChange()" ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>
                            <input ng-switch-when="url" style="text-align:left;" type="url" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-change="handlePageTreeChange()" ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>
                            <pb-Arrayofmap ng-switch-when="arrayOfMap" label="{{i18nGet('sspb.page.visualbuilder.edit.map.title' , [i18nGet('attribute.'+attr.name),dataHolder.selectedComponent.name])}}"
                                           pb-change="handlePageTreeChange()" array='dataHolder.selectedComponent[attr.name]'
                                           pb-parent="dataHolder.selectedComponent" pb-attrname="attr.name"></pb-Arrayofmap>
                            <input ng-switch-when="boolean" style="text-align:left;" type="checkbox" ng-change="handlePageTreeChange()" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>

                            <!-- TODO default type is set in the model defintion - not mapped here  -->
                            <input ng-switch-default style="text-align:left;" type="text" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-change="handlePageTreeChange()" ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>
                        </span>
                    </div>
                </div>
             </td>
        </tr>



    </table>

    <!-- type selection modal body-->
    <div modal="shouldBeOpen"  options="typeSelectionModalOpts">
        <div class="modal-header">
            <h4><g:message code="sspb.page.visualbuilder.select.type.prompt" /></h4>
        </div>
        <div class="modal-body">

            <select  ng-model="$parent.selectedType" ng-options="i18nGet('type.'+type) for type in validChildTypes"></select>
        </div>
        <div class="modal-footer">
            <button class="btn btn-success ok" ng-click="closeTypeSelectionModal()"><g:message code="sspb.page.visualbuilder.create.component.label" /></button>
            <button class="btn btn-warning cancel" ng-click="cancelTypeSelectionModal()"><g:message code="sspb.page.visualbuilder.create.cancel.label" /></button>
        </div>
    </div>


    <textArea name="statusMessage" readonly="true" ng-model="pageStatus.message"
              rows="3" cols="120" style="width:99%; height:10%"/>

</div>
</body>

</body>
</html>