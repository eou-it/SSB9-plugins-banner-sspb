<%--
  Created by IntelliJ IDEA.
  User: jzhong
  Date: 5/17/13
  Time: 4:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="net.hedtech.banner.sspb.PageComponent" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Banner Page Builder Visual Composer</title>

    <meta name="layout" content="bannerSelfServicePBPage"/>

    <meta name="menuEndPoint" content="${request.contextPath}/ssb/menu"/>
    <meta name="menuBaseURL" content="${request.contextPath}/ssb"/>

    <g:if test="${message(code: 'default.language.direction')  == 'rtl'}">
        <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'pbDeveloper-rtl.css')}">
    </g:if>
    <g:else>
        <link rel="stylesheet" href="${resource(plugin: 'banner-sspb', dir: 'css', file: 'pbDeveloper.css')}">
    </g:else>


    <r:require modules="pageBuilderDev"/>

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
     function VisualPageComposerController( $scope, $http, $resource, $parse, $filter) {

        var noteType = {
            success: "success",
            warning: "warning",
            error:   "error"
        };
        $scope.alertNote = function(note) {
            note.type = note.type||noteType.success;
            note.flash = note.flash||true;
            notifications.addNotification(new Notification(note));
        };

         $scope.alertError = function(msg, stay) {
             var note = {type: noteType.error, message: msg};
             if (!stay){
                 note.flash = true;
             }
             notifications.addNotification(new Notification(note));
         };

        $scope.component_entry_style = ["componentEntry", "componentEntry_selected"];

        $scope.pageName = "";
        $scope.pageCurName = $scope.pageName;
        $scope.extendsPage = {};
        // top level page source container must be an array for tree view rendering consistency
        $scope.pageSource = [];
         // saved pageSource for dirty detection
         //$scope.savedPageSource = {};

        // data holder for reference in child scopes
        $scope.dataHolder = {selectedContext:{}};

        // status holder to remember which component property is shown, and if page has been modified
         // noDirtyCheck is set by page loading, new page function to tell the watch function not to set the dirty flag
         // right after a page is loaded (which causes the PageSource to change)
        $scope.statusHolder = {selectedIndex:0, newIndex:-1, pageIsDirty:false, noDirtyCheck:true};
        // page command execution status
        $scope.pageStatus = {};

        // index is a unique number assigned to each component in its own scope
        $scope.index = 0;
        $scope.nextIndex = function () {
            $scope.index = $scope.index + 1;
            return $scope.index;
        };

        // used to highlight the selected component in the component tree
        // TODO this does not work with IE 9
        $scope.componentLabelStyle = function(selected) {
        if (selected)
            //return "text-decoration:underline;";
            return "border-style:ridge; border-width:2px; color:red";
        else
            return "";
        };


        // load the model definition from deployed app
         var PageModelDef = $resource(rootWebApp+'visualPageModelComposer/pageModelDef');
         PageModelDef.get(null, function(data) {
            $scope.pageModelDef = data.definitions.componentTypeDefinition;
            $scope.sourceRenderDef = data.definitions.sourceRenderDefinitions;
            $scope.dropdownsDef = data.definitions.dropdowns;
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
         };

         $scope.getDropdown = function(attribute) {
             console.log('return dropdown for attribute');
             //return ['text','number','email','date'];
             return $scope.dropdownsDef[attribute.name];
         }

         /* return the default value for an attribute
          if the value is undefined and there is a default value then return the default value
          if the value is undefined and there is a default value then return undefined
          if the value is defined then return the current value
          */
         $scope.setDefaultValue = function (attributeName, value) {
            if (value == undefined && $scope.attrRenderProps[attributeName].defaultValue!=undefined)
                return  $scope.attrRenderProps[attributeName].defaultValue;
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
        };
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
        };

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
        };

        $scope.findRequiredChildrenTypes = function(type) {
            var children = [];
            // add required attribute for all components
            angular.forEach($scope.pageModelDef, function(componentDef) {
                if (componentDef.componentType.indexOf(type) !=-1)
                    children = children.concat(componentDef.requiredChildren);
            });
            return children;
        };

        $scope.findOptionalChildrenTypes = function(type) {
            var children = [];
            // add required attribute for all components
            angular.forEach($scope.pageModelDef, function(componentDef) {
                if (componentDef.componentType.indexOf(type) !=-1)
                    children = children.concat(componentDef.optionalChildren);
            });
            return children;
        };

        $scope.findAllChildrenTypes = function(type) {
            return $scope.findRequiredChildrenTypes(type).concat($scope.findOptionalChildrenTypes(type));
        };

        $scope.handlePageTreeChange = function() {
            $scope.pageSourceView = JSON.stringify($scope.pageSource[0], JSONFilter, 6);
        };

        //
        $scope.resetSelected = function() {
            $scope.dataHolder.selectedComponent = undefined;
            $scope.statusHolder.selectedIndex = 0;
            $scope.dataHolder.allAttrs = [];
            $scope.dataHolder.selectedContext = {};
        };

        $scope.i18nGet = function(key,args) {
            var tr = [];
            tr['attribute.type'             ]="${message(code:'sspb.model.attribute.type')}";
            tr['attribute.subType'          ]="${message(code:'sspb.model.attribute.subtype')}";
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
            tr['attribute.onError'          ]="${message(code:'sspb.model.attribute.onError')}";
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
            tr['attribute.asHtml'           ]="${message(code:'sspb.model.attribute.asHtml')}";
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
            tr['attribute.staticData'       ]="${message(code:'sspb.model.attribute.staticData')}";
            tr['attribute.default'          ]="${message(code:'sspb.model.attribute.default')}";
            tr['attribute.description'      ]="${message(code:'sspb.model.attribute.description')}";
            tr['attribute.importCSS'        ]="${message(code:'sspb.model.attribute.importCSS')}";
            tr['attribute.replaceView'      ]="${message(code:'sspb.model.attribute.replaceView')}";
            tr['attribute.fractionDigits'   ]="${message(code:'sspb.model.attribute.fractionDigits')}";
            tr['attribute.labelStyle'       ]="${message(code:'sspb.model.attribute.labelStyle')}";
            tr['attribute.valueStyle'       ]="${message(code:'sspb.model.attribute.valueStyle')}";

            tr['attribute.newRecordLabel'   ]="${message(code:'sspb.model.attribute.newRecordLabel')}";
            tr['attribute.deleteRecordLabel']="${message(code:'sspb.model.attribute.deleteRecordLabel')}";
            tr['attribute.saveDataLabel'    ]="${message(code:'sspb.model.attribute.saveDataLabel')}";
            tr['attribute.refreshDataLabel' ]="${message(code:'sspb.model.attribute.refreshDataLabel')}";


            tr['type.page'        ]="${message(code:'sspb.model.type.page'     )}";
            tr['type.flow'        ]="${message(code:'sspb.model.type.flow'     )}";
            tr['type.form'        ]="${message(code:'sspb.model.type.form'     )}";
            tr['type.block'       ]="${message(code:'sspb.model.type.block'    )}";
            tr['type.grid'        ]="${message(code:'sspb.model.type.grid'     )}";
            tr['type.htable'      ]="${message(code:'sspb.model.type.htable'   )}";
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
            tr['type.hidden'      ]="${message(code:'sspb.model.type.hidden'   )}";
            tr['type.style'       ]="${message(code:'sspb.model.type.style'   )}";
            tr['type.xeTextBox'   ]="${message(code:'sspb.model.type.xeTextBox'  )}";
            tr['type.xeTextArea'  ]="${message(code:'sspb.model.type.xeTextArea'  )}";
            tr['type.xeButton'  ]="${message(code:'sspb.model.type.xeButton' )}";
            tr['type.xeDropdown'  ]="${message(code:'sspb.model.type.xeDropdown' )}";
            tr['type.xeCheckbox'  ]="${message(code:'sspb.model.type.xeCheckbox' )}";
            tr['subType.text'     ]="${message(code:'sspb.model.subType.text'    )}";
            tr['subType.number'   ]="${message(code:'sspb.model.subType.number'  )}";
            tr['subType.email'    ]="${message(code:'sspb.model.subType.email'   )}";
            tr['subType.tel'      ]="${message(code:'sspb.model.subType.tel'     )}";
            tr['subType.primaryButton'  ]="${message(code:'sspb.model.subType.primaryButton')}";
            tr['subType.secondaryButton'  ]="${message(code:'sspb.model.subType.secondaryButton')}";

            tr['sspb.page.visualbuilder.edit.map.title' ] = "${message(code:'sspb.page.visualbuilder.edit.map.title',encodeAs: 'JavaScript')}";
            tr['sspb.page.visualbuilder.edit.textarea.title' ] = "${message(code:'sspb.page.visualbuilder.edit.textarea.title',encodeAs: 'JavaScript')}";
            tr['sspb.page.visualbuilder.invalidCopyType.error.message' ] = "${message(code:'sspb.page.visualbuilder.invalidCopyType.error.message',encodeAs: 'JavaScript')}";
            tr['pb.template.map.new.key.label'          ] = "${message(code:'pb.template.map.new.key.label',encodeAs: 'JavaScript')}";
            tr['pb.template.map.new.value.label'        ] = "${message(code:'pb.template.map.new.value.label',encodeAs: 'JavaScript')}";
            tr['pb.template.map.name.label'             ] = "${message(code:'pb.template.map.name.label',encodeAs: 'JavaScript')}";
            tr['pb.template.map.value.label'            ] = "${message(code:'pb.template.map.value.label',encodeAs: 'JavaScript')}";
            tr['pb.template.map.value.select.label'     ] = "${message(code:'pb.template.map.value.select.label',encodeAs: 'JavaScript')}";
            tr['pb.template.map.value.enter.label'      ] = "${message(code:'pb.template.map.value.enter.label',encodeAs: 'JavaScript')}";
            tr['pb.template.map.add.label'              ] = "${message(code:'pb.template.map.add.label',encodeAs: 'JavaScript')}";
            tr['pb.template.map.ok.label'               ] = "${message(code:'pb.template.map.ok.label',encodeAs: 'JavaScript')}";
            tr['pb.template.arraymap.add.label'         ] = "${message(code:'pb.template.arraymap.add.label',encodeAs: 'JavaScript')}";
            tr['pb.template.arraymap.ok.label'          ] = "${message(code:'pb.template.arraymap.ok.label',encodeAs: 'JavaScript')}";
            tr['pb.template.arraymap.new.value.label'   ] = "${message(code:'pb.template.arraymap.new.value.label',encodeAs: 'JavaScript')}";
            tr['pb.template.textarea.ok.label'          ] = "${message(code:'pb.template.textarea.ok.label',encodeAs: 'JavaScript')}";
            tr['pb.template.combo.loadsource.label'     ] = "${message(code:'pb.template.combo.loadsource.label',encodeAs: 'JavaScript')}";
            tr['pb.template.combo.edit.label'           ] = "${message(code:'pb.template.combo.edit.label',encodeAs: 'JavaScript')}";
            tr['pb.template.combo.select.label'         ] = "${message(code:'pb.template.combo.select.label',encodeAs: 'JavaScript')}";


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
        };

        $scope.attributeIsTranslatable = function (attr) {
            var attributes = ${PageComponent.translatableAttributes.encodeAsJSON()};
            return (attributes.indexOf(attr) != -1); // seems not to work in IE8 - indexOf is added by one of the JS libraries
        };

        // recursively check if 'component1' is a direct or indirect child of 'component'
        $scope.isChild = function(component, component1) {
            // reached a leaf node
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
        };

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
            //$scope.handlePageTreeChange();
        };

        $scope.moveUpComponent = function(parent, index, gIndex) {
            //var isChildSelected = $scope.dataHolder.selectedComponent!= undefined && $scope.isChild(parent.components[index], $scope.dataHolder.selectedComponent);
            // TODO selected data gIndex may have been changed
            var prev = parent.components[index-1];
            parent.components[index-1] = parent.components[index];
            parent.components[index] = prev;
            //$scope.handlePageTreeChange();
        };

        $scope.moveDownComponent = function(parent, index, gIndex) {
            //var isChildSelected = $scope.dataHolder.selectedComponent!= undefined && $scope.isChild(parent.components[index], $scope.dataHolder.selectedComponent);
            // TODO selected data gIndex may have been changed
            var next = parent.components[index+1];
            parent.components[index+1] = parent.components[index];
            parent.components[index] = next;
            //$scope.handlePageTreeChange();
        };

        $scope.copyComponent = function(data) {
            // make a deep copy of the data so when the source changes the copy won't get changed un-intentionally
            $scope.dataHolder.copy = JSON.parse(JSON.stringify(data));
        };

        // paste a component as a new child of 'data'
        $scope.pasteComponent = function(data) {
            // check if the copied component is allowed for the parent component
            if ($scope.findAllChildrenTypes(data.type).indexOf($scope.dataHolder.copy.type) == -1) {
                $scope.alertError( $scope.i18nGet('sspb.page.visualbuilder.invalidCopyType.error.message', [$scope.dataHolder.copy.type, data.type]));
            } else {
                if (data.components==undefined)
                    data.components=[];
                // make a new (deep) copy of the copied component so each pasted instance is unique
                var newCopy = JSON.parse(JSON.stringify($scope.dataHolder.copy));
                data.components.push(newCopy);
                $scope.handlePageTreeChange();
            }

        };

        $scope.deleteChildren = function(data) {
            data.components = [];
            //$scope.$apply('data');
            //$scope.handlePageTreeChange();
        };


        $scope.addChild = function(parent) {
            //console.log("addChild");
            $scope.validChildTypes = $scope.findAllChildrenTypes(parent.type);
            // TODO make sure the children are expanded if appending a new component
            //$scope.showChildren = true;
            $scope.openTypeSelectionModal(parent, -1);
            // delay adding node until the type selection is made
        };

        /*
            Insert a new child component to 'data' at location 'index'
         */
        $scope.insertSibling = function(parent, index) {
            //console.log("addChild");
            $scope.validChildTypes = $scope.findAllChildrenTypes(parent.type);
            $scope.openTypeSelectionModal(parent, index);
        };


        $scope.selectData = function(data, index, parent) {
            //alert("scope = " + $scope.$id + ", data = " + data.type);
            $scope.dataHolder.selectedComponent = data;
            $scope.statusHolder.selectedIndex = index;
            $scope.dataHolder.selectedContext = parent;
            $scope.dataHolder.selectedType = data.type;
            // set the components types that are valid for the selected component's parent
            // used when component type is changed
            if (parent != undefined) {
                $scope.dataHolder.selectedCompatibleTypes = $scope.findAllChildrenTypes(parent.type);
            }
            else
                $scope.dataHolder.selectedCompatibleTypes = ["page"];
            // update the current selected component's property list
            $scope.findAllAttrs(data.type);

            //console.log("scope = " + $scope.$id);
        };

        // handle type switch for a component
        $scope.handleAttrChange= function() {
            // set all valid attributes for the new component type
            $scope.findAllAttrs($scope.dataHolder.selectedComponent.type);
            var newAttrs = $scope.findRequiredAttrs($scope.dataHolder.selectedComponent.type).
                    concat($scope.findOptionalAttrs($scope.dataHolder.selectedComponent.type));
            // first remove existing attributes that are not allowed for the new type
            var oldAttrs= _.keys($scope.dataHolder.selectedComponent);
            for (var i =0; i < oldAttrs.length; i++ ){
                if (oldAttrs[i] != "components" && newAttrs.indexOf(oldAttrs[i]) == -1)
                    delete $scope.dataHolder.selectedComponent[oldAttrs[i]];
            }
            // check if any existing children is not allowed for the new type
            var oldChildren = $scope.dataHolder.selectedComponent.components;
            if (oldChildren != undefined && oldChildren.length > 0 ) {
                var allowedChildrenTypes = $scope.findAllChildrenTypes($scope.dataHolder.selectedComponent.type);

                for (var i=0; i < oldChildren.length; i++) {
                    if (allowedChildrenTypes==undefined || !_.contains(allowedChildrenTypes, oldChildren[i].type))
                        $scope.dataHolder.selectedComponent.components = _.without($scope.dataHolder.selectedComponent.components,
                                oldChildren[i]);
                }
            }

            //$scope.handlePageTreeChange();

         };

        $scope.toggleShowChildren = function() {
            $scope.showChildren = !$scope.showChildren;
        };

        // type selection modal dialog functions
        /*
        Note! use console.log() immediately before, during & after modal dialog is displayed will prevent the dialog to show on IE 9
         unless the developer tool window is opened.
         */
          $scope.openTypeSelectionModal = function (parent, index) {
            $scope.shouldBeOpen = true;
            $scope.newParent = parent;
            $scope.newIndex = index;
          };

          $scope.closeTypeSelectionModal = function () {
            //$scope.closeMsg = 'I was closed at: ' + new Date();
            $scope.shouldBeOpen = false;
            // add the child component
            var parent = $scope.newParent;
            if (parent.components==undefined)
                parent.components=[];
            var post = parent.components.length + 1;
            var newName = parent.name + '_child_' + post;
            //console.log("Adding child =" + newName);
            var newComp = {name: newName, type: $scope.selectedType};
            if ($scope.newIndex==-1)
                parent.components.push(newComp);
            else
                parent.components.splice($scope.newIndex, 0, newComp);
            // open the new component for editing - the new component always get an incremented index number
            $scope.selectData(newComp, $scope.index+1, parent);
            // modal dialog is associated with parent scope
            //$scope.handlePageTreeChange();

          };

          $scope.cancelTypeSelectionModal = function() {
            $scope.shouldBeOpen = false;
          };

          $scope.typeSelectionModalOpts = {
            backdropFade: true,
            dialogFade:true
          };

         // declare the virtual domain lookup resource  and functions for loading the resourse combo box
         var VDList = $resource(rootWebApp+'internal/virtualDomains.pbadmVirtualDomainLookup',{},{
             list: {
                 method:"GET",
                 isArray:true,
                 headers:{'Content-Type':'application/json', 'Accept':'application/json'}
             }
         });
         $scope.vdlist = [];
         $scope.loadVdList = function() {
             console.log("==== In loadVDList====");
             VDList.list({}, function(data) {
                 // only need the service_name
                 $scope.vdlist = [];

                  //console.log("vdList = " + data);
                  angular.forEach(data, function(vd){
                    $scope.vdlist.push("virtualDomains."+ vd.SERVICE_NAME);
                    //console.log("VD = " + vd.SERVICE_NAME);
                  });
             });
         };
         $scope.loadVdList();


         // declare the Page resource
         var Page = $resource(rootWebApp+'internal/pages/:constantName',{},{
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
             remove: {   // cannot use delete as method name in IE8
                 method:"DELETE",
                 isArray:false,
                 headers:{'Content-Type':'application/json', 'Accept':'application/json'}
             }
         });

         // load the list of pages
         $scope.pageList = [];
         $scope.loadPageNames = function() {
             Page.list({}, function(data) {
                 $scope.pageList = data;
                 /*
                  console.log("pageList = " + data);
                  angular.forEach($scope.pageList, function(page){
                  console.log("Page = " + page.page.constantName);
                  });*/
             });
         };
         // populate the page list initially
         $scope.loadPageNames();

         // determine if current loaded page has changed since it was load
         $scope.isPageModified = function () {
             return $scope.statusHolder.isPageModified;
         };

         // handle page model changes
         $scope.$watch('pageOneSource', function() {
             //console.log("-- changed --");
             if (!$scope.statusHolder.noDirtyCheck)
                $scope.statusHolder.isPageModified = true;
             else
                 $scope.statusHolder.noDirtyCheck = false; // we still want to check dirty state after the initial loading/new page
             // re-format the page source text
             $scope.handlePageTreeChange();
         }, true);

         $scope.getPageSource = function() {
             //TODO prompt for unsaved data
             if ($scope.isPageModified())   {
                 var r=confirm("${message(code: 'sspb.page.visualbuilder.loadpage.unsaved.changes.message', encodeAs: 'JavaScript')}");
                 if (!r)  {
                     $scope.pageName = $scope.pageCurName;
                     return;
                 }
             }
             Page.get({constantName:$scope.pageName}, function (data){
                 try {
                     $scope.statusHolder.noDirtyCheck = true;
                     $scope.pageOneSource = JSON.parse(data.modelView);
                     $scope.pageSource[0] = $scope.pageOneSource;
                     $scope.resetSelected();
                     //$scope.handlePageTreeChange();
                     $scope.pageCurName= $scope.pageName;
                     $scope.extendsPage = data.extendsPage;
                     $scope.statusHolder.isPageModified = false;
                 } catch(ex) {
                     $scope.alertError( $scope.i18nGet("${message(code:'sspb.page.visualbuilder.parsing.error.message')}",[ex]));
                 }
             }, function(response) {
                 var note={type: noteType.error, message: "${message(code: 'sspb.page.visualcomposer.page.load.failed.message', encodeAs: 'JavaScript')}"};
                 if (response.data != undefined && response.data.errors!=undefined) {
                     note.message = $scope.i18nGet(note.message, [response.data.errors[0].errorMessage]);
                 } else {
                     note.message = $scope.i18nGet(note.message, ['']);
                 }
                 $scope.alertNote( note);
             });
         };

         /* page operations */
          $scope.newPageSource = function() {
            // TODO generate a unique page name
              if ($scope.isPageModified())   {
                var r=confirm("${message(code: 'sspb.page.visualbuilder.newpage.unsaved.changes.message', encodeAs: 'JavaScript')}");
                if (!r)
                    return;
            }

            $scope.pageName= "${message( code:'sspb.page.visualbuilder.newpage.default', encodeAs: 'JavaScript')}";
            $scope.pageCurName= $scope.pageName;
            $scope.extendsPage = {};
            $scope.statusHolder.noDirtyCheck = true;
            $scope.pageSource[0] = {"type": "page", "name": $scope.pageName};
            $scope.pageOneSource =  $scope.pageSource[0];
            $scope.resetSelected();
            //$scope.handlePageTreeChange();
            $scope.statusHolder.isPageModified = false;
          };

          $scope.submitPageSource = function () {
             var saveErrorId = "saveErrorId";
             var note = {type: noteType.success, id: saveErrorId, flash: true};

             //Remove error from previous compilation if exists
             var saveError =  notifications.get(saveErrorId);
             if (saveError) {
                 notifications.remove(saveError);
             }

             //check if page name is set
             if ($scope.pageCurName == undefined || $scope.pageCurName == '') {
                 $scope.alertError("${message(code:'sspb.page.visualbuilder.page.name.prompt.message')}");
                 return;
             }

              Page.save({pageName:$scope.pageCurName, source:$scope.pageSourceView, extendsPage:$scope.extendsPage},
                  function(response) {
                     if (response.statusCode == 0) {
                         $scope.pageStatus.message = response.statusMessage;
                         if (response.pageValidationResult.warn) {
                             $scope.pageStatus.message += response.pageValidationResult.warn;
                             note.type = noteType.warning;
                         }
                         $scope.statusHolder.isPageModified = false;
                     }
                     else {
                         var msg = "${message(code:'sspb.page.validation.error.message')}";
                         var err = response.pageValidationResult && response.pageValidationResult.errors?response.pageValidationResult.errors:"";
                         note.type  = noteType.error;
                         $scope.pageStatus.message = $scope.i18nGet(msg, [response.statusMessage, err]);
                     }
                     note.message = $scope.pageStatus.message
                     $scope.alertNote(note);
                     $scope.pageStatus.message = $filter('date')(new Date(), 'medium') + ': ' + $scope.pageStatus.message;
                     // refresh the page list in case new page is added
                     $scope.loadPageNames();
                 },
                 function (response) {
                     var msg = "${message(code: 'sspb.page.visualcomposer.page.submit.failed.message', encodeAs: 'JavaScript')}";
                     var err = response.data && response.data.errors ? response.data.errors[0].errorMessage : "";
                     msg = $scope.i18nGet(msg, [err]);
                     note.message = msg;
                     note.type = noteType.success;
                     $scope.alertNote(note);
                 }
             );

          };

          $scope.previewPageSource = function() {
              //check if page name is set
              if ($scope.pageCurName== undefined || $scope.pageCurName == '') {
                  $scope.alertError( "${message(code:'sspb.page.visualbuilder.page.name.prompt.message')}");
                  return;
              }
              window.open(rootWebApp+'customPage/page/'+ $scope.pageCurName, '_blank');

          };


          $scope.deletePageSource = function () {
              //check if page name is set
              if ($scope.pageCurName== undefined || $scope.pageCurName == '') {
                  $scope.alertError("${message(code:'sspb.page.visualbuilder.page.name.prompt.message')}");
                  return;
              }

              Page.remove({constantName:$scope.pageCurName }, function() {
                  // on success
                  $scope.alertNote({message: "${message(code:'sspb.page.visualbuilder.deletion.success.message')}"});

                  // clear the page name field and page source
                  $scope.pageCurName = "";
                  $scope.pageName = "";
                  $scope.extendsPage = {};
                  $scope.resetSelected();
                  $scope.statusHolder.noDirtyCheck = true;
                  $scope.pageSource[0] = {};
                  $scope.pageOneSource= undefined;
                  $scope.statusHolder.isPageModified = false;
                  // refresh the page list after a page is deleted
                  $scope.loadPageNames();
              }, function(response) {
                  var note={type: noteType.error, message: "${message(code:'sspb.page.visualbuilder.deletion.error.message')}",flash:true};
                  if (response.data != undefined && response.data.errors != undefined) {
                      note.message = $scope.i18nGet(note.message, [response.data.errors[0].errorMessage]);
                  } else {
                      note.message = $scope.i18nGet(note.message, ['']);
                  }
                  $scope.alertNote(note);
              });

          };

         /* tab controls */
         // show tree view initially
         $scope.showTree= true;
         //$scope.toggleSourceLabel = "Show Page Source";
         $scope.toggleSourceView = function() {
             $scope.showTree = !$scope.showTree;
         };

         $scope.sourceEditEnabled = false;

         $scope.enableSourceEdit = function() {
             $scope.resetSelected();
             $scope.sourceEditEnabled = true;
         };

         $scope.applySourceEdit = function() {
            // parse the source
            try {
                var newPage = JSON.parse($scope.pageSourceView);
                $scope.pageOneSource = newPage;
                $scope.pageSource[0] = $scope.pageOneSource;
                $scope.sourceEditEnabled = false;
            } catch(ex) {
                $scope.alertError( $scope.i18nGet("${message(code:'sspb.page.visualbuilder.parsing.error.message')}",[ex]));
            }
         };

         $scope.discardSourceEdit = function() {
             $scope.handlePageTreeChange(); // This stringifies the tree and thus undo's the text modification if any
             $scope.sourceEditEnabled = false;
         }

     }

    </script>


</head>
<body>

<div id="content" ng-controller="VisualPageComposerController" class="customPage" ng-form="pagemodelform">

    <label><g:message code="sspb.page.visualbuilder.load.label" /></label>
    <select name="constantName"
            ng-options="page.constantName as page.constantName for page in pageList"
                ng-model="pageName"
                ng-change="getPageSource()"></select>

    <button ng-click='loadPageNames()'><g:message code="sspb.page.visualbuilder.reload.pages.label" /></button>
<br/>


    <label><g:message code="sspb.page.visualbuilder.name.label" /></label>
    <input type="text" name="constantName" ng-model="pageCurName" required ng-maxlength="60" ng-pattern="/^[a-zA-Z]+[a-zA-Z0-9\._-]*$/">

    <label><g:message code="sspb.page.visualbuilder.extends.label" /></label>
    <select name="extendsPage"
            ng-options="page as page.constantName for page in pageList track by page.id"
                ng-model="extendsPage">
    </select>

    <button ng-click='newPageSource()'><g:message code="sspb.page.visualbuilder.new.page.label" /></button>
    <button ng-click='submitPageSource()' ng-disabled='sourceEditEnabled || !pagemodelform.$valid'><g:message code="sspb.page.visualbuilder.compile.save.label" /></button>
    <button ng-click="getPageSource()"><g:message code="sspb.page.visualbuilder.reload.label" /></button>
    <button ng-click="previewPageSource()"><g:message code="sspb.page.visualbuilder.preview.label" /></button>
    <button ng-click='deletePageSource()'><g:message code="sspb.page.visualbuilder.delete.label" /></button>

    <table style="height:80%; min-width: 60em">
        <tr>
            <th style="width:50%"><g:message code="sspb.page.visualbuilder.page.view.label" /></th>
            <th style="width:50%"><g:message code="sspb.page.visualbuilder.component.propertyview.label" /></th>
        </tr>
        <tr height="99%">
            <td>
                <span ng-show="pageCurName != '' && pageCurName != 'null'">
                    <div>
                        <button class="btn btn-mini" ng-click='toggleSourceView()' ng-disabled='showTree || sourceEditEnabled'><g:message code="sspb.page.visualbuilder.page.treeview.label" /></button>
                        <button class="btn btn-mini" ng-click='toggleSourceView()' ng-disabled='!showTree'><g:message code="sspb.page.visualbuilder.page.sourceview.label" /></button>
                        <span ng-show="!showTree" class="alignRight">
                            <button class="btn btn-mini" ng-click='enableSourceEdit()' ng-disabled='sourceEditEnabled'><g:message code="sspb.page.visualbuilder.page.enable.edit.label" /></button>
                            <button class="btn btn-mini" ng-click='applySourceEdit()' ng-disabled='!sourceEditEnabled'><g:message code="sspb.page.visualbuilder.page.apply.change.label" /></button>
                            <button class="btn btn-mini" ng-click='discardSourceEdit()' ng-disabled='!sourceEditEnabled'><g:message code="sspb.page.visualbuilder.page.discard.change.label" /></button>
                        </span>
                    </div>
                    <div class="tabs-below">
                        <div class='tab-content' ng-show='!showTree'>
                            <textArea name="modelView" ng-model="pageSourceView"
                                        cols="60" rows="30" style="width:90%; height:auto;" required="true" ng-readonly="!sourceEditEnabled" > </textArea>
                        </div>

                        <div class='tab-content' ng-show="showTree">
                            <div style="width:100%;  overflow-y: auto; overflow-x: auto; white-space:nowrap;" >
                                <ul ng-init="showChildren=true;">
                                    <li ng-repeat="data in pageSource"   ng-include="'tree_item_renderer.html'"></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </span>
            </td>
            <td>
                <div ng-show="dataHolder.selectedComponent!=undefined">
                    <!--
                    <div>Selected Component = {{dataHolder.selectedComponent.type}}</div>
                    -->

                    <div ng-repeat="attr in dataHolder.allAttrs">
                        <label style="text-align:end; width: 30%">{{i18nGet('attribute.'+attr.name)}}<span ng-show="attr.required">*</span> <span ng-show="attributeIsTranslatable(attr.name)">⊙</span></label>
                        <span ng-switch on="attrRenderProps[attr.name].inputType" >
                            <pb-Map ng-switch-when="map" label="{{i18nGet('sspb.page.visualbuilder.edit.map.title' , [i18nGet('attribute.'+attr.name),dataHolder.selectedComponent.name])}}"
                                    map='dataHolder.selectedComponent[attr.name]' pb-parent="dataHolder.selectedComponent" pb-attrname="attr.name"
                                    ></pb-Map>
                            <pb-Textarea ng-switch-when="textarea" label="{{i18nGet('sspb.page.visualbuilder.edit.textarea.title' , [i18nGet('attribute.'+attr.name),dataHolder.selectedComponent.name])}}"
                                    value='dataHolder.selectedComponent[attr.name]' pb-Parent="dataHolder.selectedComponent" pb-Attrname="attr.name"
                                    ></pb-Textarea>
                            <pb-Combo ng-switch-when="combo"
                                   value='dataHolder.selectedComponent[attr.name]' pb-Parent="dataHolder.selectedComponent" pb-Attrname="attr.name"
                                   pb-loadsourcelist="loadVdList()" load-source-label="{{i18nGet('pb.template.combo.loadsource.label')}}" edit-value-label="{{i18nGet('pb.template.combo.edit.label')}}"
                                   select-label="{{i18nGet('pb.template.combo.select.label')}}" source-list="vdlist"></pb-Combo>
                            <select ng-switch-when="select" ng-options="type as i18nGet('type.'+type) for type in dataHolder.selectedCompatibleTypes"
                                ng-model="dataHolder.selectedComponent[attr.name]" ng-change="handleAttrChange()"></select>
                            <%--HvT re-introduced ng-change in previous line because changing type may cause issues. Seems to help. Not sure why it was removed?--%>
                            <input ng-switch-when="text" style="text-align:start;" type="text" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-model="dataHolder.selectedComponent[attr.name]"/>
                            <input ng-switch-when="nameText" style="text-align:start;" type="text" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-model="dataHolder.selectedComponent[attr.name]" ng-pattern="/^[a-zA-Z]\w*$/"/>
                            <input ng-switch-when="number" style="text-align:start;" type="number" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>
                            <input ng-switch-when="url" style="text-align:start;" type="url" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>
                            <pb-Arrayofmap ng-switch-when="arrayOfMap" label="{{i18nGet('sspb.page.visualbuilder.edit.map.title' , [i18nGet('attribute.'+attr.name),dataHolder.selectedComponent.name])}}"
                                   array='dataHolder.selectedComponent[attr.name]'
                                   pb-parent="dataHolder.selectedComponent" pb-attrname="attr.name"></pb-Arrayofmap>
                            <input ng-switch-when="boolean" style="text-align:start;" type="checkbox" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>
                            <!-- Added for xe-text-box so we can handle subtypes -->
                            <select ng-switch-when="dropdown" ng-options="type as i18nGet('subType.'+type) for type in getDropdown(attr)"
                                    ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                    ng-model="dataHolder.selectedComponent[attr.name]" ng-change="handleAttrChange()"></select>
                            <!-- TODO default type is set in the model defintion - not mapped here  -->
                            <input ng-switch-default style="text-align:start;" type="text" ng-init='dataHolder.selectedComponent[attr.name]=setDefaultValue(attr.name, dataHolder.selectedComponent[attr.name])'
                                   ng-readonly="attr.name=='type'" ng-model="dataHolder.selectedComponent[attr.name]"/>

                        </span>
                    </div>
                </div>
             </td>
        </tr>

    </table>
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
        <span ng-init="index=nextIndex()" ng-click="selectData(data, index, $parent.$parent.data)"
              style="{{componentLabelStyle(index == statusHolder.selectedIndex)}}">{{data.name}} &lrm;[{{i18nGet('type.'+data.type)}}]&lrm;</span>

        <button  title="${message(code:'sspb.page.visualbuilder.insert.sibling.title')}" class="button_insert button_edit" ng-click="insertSibling($parent.$parent.data, $index)" ng-show="data.type!='page'"></button>
        <button  title="${message(code:'sspb.page.visualbuilder.append.child.title')}" class="button_edit button_add" ng-click="addChild(data)" ng-show="findAllChildrenTypes(data.type).length>0"></button>
        <button  title="${message(code:'sspb.page.visualbuilder.moveup.component.title')}" class="button_sort_asc button_edit" ng-click="moveUpComponent($parent.$parent.data, $index, index)"  ng-show="!$first"></button>
        <button  title="${message(code:'sspb.page.visualbuilder.movedown.component.title')}" class="button_sort_desc button_edit" ng-click="moveDownComponent($parent.$parent.data, $index, index)"  ng-show="!$last"></button>
        <button  title="${message(code:'sspb.page.visualbuilder.delete.component.title')}" class="button_delete button_edit" ng-click="deleteComponent($parent.$parent.data, $index, index)"  ng-show="data.type!='page'"></button>
        <button  title="${message(code:'sspb.page.visualbuilder.copy.component.title')}"  class="button_copy button_edit" ng-click="copyComponent(data)" ng-show="data.type!='page'"></button>
        <button  title="${message(code:'sspb.page.visualbuilder.paste.component.title')}"  class="button_paste button_edit" ng-click="pasteComponent(data)"  ng-show="dataHolder.copy!=undefined" ></button>


        <!--button  class="btn btn-mini" ng-click="deleteChildren(data)" ng-show="data.components.length > 0">--</button-->
        <!--input type="checkbox" ng-model="(index == statusHolder.selectedIndex)" ng-init="index=index+1" /-->

        <ul ng-show="showChildren" style="list-style: none;">
            <li ng-repeat="data in data.components"   ng-include="'tree_item_renderer.html'"></li>
        </ul>
    </script>


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
              style="width:99.7%; height: 10%;"></textArea>

</div>

</body>
</html>
