package net.hedtech.banner.sspb

import groovy.json.JsonSlurper

class CompileService {
    // compile the page
    // accept a normalized page level pageComponent
    // output
    def static compile2page(pageComponent) {
        def pageTxt=pageComponent.compileComponent("")
        //println pageTxt
        return pageTxt
    }


    /*
    Inject the JavaScript controller to the HTML page
    Return the combined page
     */
    def static assembleFinalPage(page, code) {
        def ind = page.indexOf(PageComponent.CONTROLLER_PLACEHOLDER)
        if (ind != -1)
            return page.substring(0, ind-1) + code + page.substring(ind + PageComponent.CONTROLLER_PLACEHOLDER.length())
    }

    /* compile the controller for the given page
    page: top level page component
    output: a string containing all the Java script code goes into the HTML header
     */
    def static compileController(page) {
        // find all data definition in page
        def modelComponents = findDataItems(page)

        // JS code segment to add
        def codeList = []
        // each each model, depending on the type of binding, generate all JS functions or services
        /*
        for all resource:
            - a model instance definition or array per grid
            - initialization function
            - variables for each component that uses the data
            - per grid control
                - pagination variables and functions per grid control,
            - function bind to each button or change event
        for top level REST resource:
            - class declaration to get, query, save, delete/remove
            - per grid - list of retrieved objects from list, modified, added and deleted object list
            - initial load function
            - keys for query
        for top level SQL resource:
            - functions to get, list, update, create and delete
            - per grid - list of retrieved objects from list, modified, added and deleted object list
            - initial load
            - keys for query

         */
        // is order important?
        modelComponents.each {
            codeList << buildCode(it)
        }

        // Add control specific scope variables, functions
        // add visibility control
        // do this for block and form for now
        codeList << buildControlVar(page)

        // add flow control functions
        codeList << buildFlowControl(page)

        // TODO Add each function to result

        def result = codeList.join("\n")
        result = """
    function CustomPageController( \$scope, \$http, \$resource) {
        // copy global var to scope
        \$scope._isUserAuthenticated = __isUserAuthenticated;
        \$scope._userFullName = __userFullName;
        \$scope._userFirstName = __userFirstName;
        \$scope._userLastName = __userLastName;
        \$scope._pidm = __pidm;
        \$scope._userRoles = __userRoles;

    $result
    }

        """
        return result
    }

    /* build JS function and models based on data definition
        input: pageComponent - data definirion
        return a string of the variables and JS function
     */
    def static buildCode(dataComponent) {
        def functions = []


        // first handle data binding
        if (dataComponent.binding == PageComponent.BINDING_REST) {
            // create a class for
            def classDef = """
        var $dataComponent.name = \$resource('$dataComponent.resource');
        """
            functions << classDef
        }

        // generate all scope variables / arrays for each component that uses the model
        // append component name to the variable name
        // TODO recursive to find all children
        def referenceComponents = findUsageComponents(dataComponent.parent, dataComponent)

        // use lower case for all instance definition on page
        def dataVarName = "_"+dataComponent.name.toLowerCase()
        // generate variable declarations for each usage of this model
        referenceComponents.each {component->
            // transform parameters to angular $scope variable
            def paramMap =""
            if (dataComponent.binding == PageComponent.BINDING_REST) {
                    // for each component that uses a model with binding to REST
                    // generate an update function that takes the parameters as query parameters

                    // concatenate all map entries to a string
                    component.parameters.each { key, value->
                        paramMap +=  "$key : $value,"
                    }
                    // remove trailing comma
                    if (paramMap?.endsWith(","))
                        paramMap = paramMap.substring(0, paramMap.length() - 1)
                    paramMap = "{$paramMap}"
                }

            def sourceParamMap =""
            if (dataComponent.binding == PageComponent.BINDING_REST) {
                    // for each component that uses a model with binding to REST
                    // generate an update function that takes the parameters as query parameters

                    // concatenate all map entries to a string
                    component.sourceParameters?.each { key, value->
                        sourceParamMap +=  "$key : $value,"
                    }
                    // remove trailing comma
                    if (sourceParamMap?.endsWith(","))
                        sourceParamMap = sourceParamMap.substring(0, sourceParamMap.length() - 1)
                    sourceParamMap = "{$sourceParamMap}"
                }

            if (component.type==PageComponent.COMP_TYPE_GRID) {
                // for grid use add an array
                //  modified index array, added array, deleted array,
                // pagination data
                // TODO fill in page size etc. from page model
                def arrayName = "${dataVarName}s_${component.ID}"
                def arrayFuncName = "${component.ID}"
                def functionDef = """
        \$scope.$arrayName = [];
        \$scope.${arrayName}_Modified = [];
        \$scope.${arrayName}_Added = [];
        \$scope.${arrayName}_Deleted = [];

        \$scope.${arrayName}_CurrentPage = 0;
        \$scope.${arrayName}_PageSize = $component.pageSize;
        \$scope.${arrayName}_NumberOfPages = function(){
            return Math.ceil(\$scope.${arrayName}.length/\$scope.${arrayName}_PageSize);
        };
        // load items using any query parameters specified
        \$scope.${arrayFuncName}_load = function() {
            \$scope.$arrayName = ${dataComponent.name}.query($paramMap);
            // clear out all other changes made before the load because the data may have been modified on the server
            \$scope.${arrayName}_Modified = [];
            \$scope.${arrayName}_Added = [];
            \$scope.${arrayName}_Deleted = [];
            \$scope.${arrayName}_CurrentPage = 0;
         };

        // load items NOT using any query parameters specified
        \$scope.${arrayFuncName}_loadAll = function() {
            \$scope.$arrayName = ${dataComponent.name}.query();
            // clear out all other changes made before the load because the data may have been modified on the server
            \$scope.${arrayName}_Modified = [];
            \$scope.${arrayName}_Added = [];
            \$scope.${arrayName}_Deleted = [];
            \$scope.${arrayName}_CurrentPage = 0;
         };

        // mark an item as being modified (dirty)
        \$scope.${arrayFuncName}_setModified = function(item) {
            if (\$scope.${arrayName}_Modified.indexOf(item) == -1)
                \$scope.${arrayName}_Modified.push(item);
        };

        // add a new item
        \$scope.${arrayFuncName}_add = function(item) {
            var newItem = new ${dataComponent.name}(item);
            \$scope.${arrayName}_Added.push(newItem);
            // add the new item to the beginning of the array so they show up on the top of the table
            \$scope.${arrayName}.unshift(newItem);

            // TODO - clear the add control content
        };

        //add an item to the Deleted array and remove it from the array for displaying in the table
        // item is the item to be deleted
        \$scope.${arrayFuncName}_delete = function (item) {
            if (\$scope.${arrayName}_Deleted.indexOf(item)==-1)
                \$scope.${arrayName}_Deleted.push(item);
            // remove from display
            \$scope.${arrayName}.splice(\$scope.${arrayName}.indexOf(item),1);
        };

        // this should find the dirty, deleted and added objects and save them to the server
        \$scope.${arrayFuncName}_save = function() {

            // do not save the added item unless they have been modified(otherwise they only have the un-initiated values),
            // which will place them in the modified list
            /*
            angular.forEach(\$scope.${arrayName}_Added, function(item) {
                console.log("Adding $component.model = " +  item);
                item.\$save();
            });
            */

            \$scope.${arrayName}_Added = [];

            // handle modified objects

            angular.forEach(\$scope.${arrayName}_Modified, function(item) {
                console.log("Saving $component.model = " +  item);
                item.\$save();
            });

            \$scope.${arrayName}_Modified = [];

            // handle deleted items
            angular.forEach(\$scope.${arrayName}_Deleted, function(item) {
                console.log("Deleting $component.model = " +  item);
                item.\$delete({id:item.id});
            });
            \$scope.${arrayName}_Deleted = [];
         };

        // when we first start up, populate items
        \$scope.${arrayFuncName}_load();
        """

            functions << functionDef
            } else if (component.type==PageComponent.COMP_TYPE_LIST) {
                // for list, generate load, pagination and click code
                def arrayName = "${dataVarName}s_${component.ID}"
                def arrayFuncName = "${component.ID}"
                def functionDef = """
        \$scope.$arrayName = [];

        \$scope.${arrayName}_CurrentPage = 0;
        \$scope.${arrayName}_PageSize = $component.pageSize;
        \$scope.${arrayName}_NumberOfPages = function(){
            return Math.ceil(\$scope.${arrayName}.length/\$scope.${arrayName}_PageSize);
        };
        // load items using any query parameters specified
        \$scope.${arrayFuncName}_load = function() {
            \$scope.$arrayName = ${dataComponent.name}.query($paramMap);
            // clear out all other changes made before the load because the data may have been modified on the server
            \$scope.${arrayName}_Modified = [];
            \$scope.${arrayName}_Added = [];
            \$scope.${arrayName}_Deleted = [];
            \$scope.${arrayName}_CurrentPage = 0;
         };

        // load items NOT using any query parameters specified
        \$scope.${arrayFuncName}_loadAll = function() {
            \$scope.$arrayName = ${dataComponent.name}.query();
            // clear out all other changes made before the load because the data may have been modified on the server
            \$scope.${arrayName}_Modified = [];
            \$scope.${arrayName}_Added = [];
            \$scope.${arrayName}_Deleted = [];
            \$scope.${arrayName}_CurrentPage = 0;
         };

        // when we first start up, populate items
        \$scope.${arrayFuncName}_load();
        """

            functions << functionDef
            } else if (component.type==PageComponent.COMP_TYPE_SELECT) {

                // Select may have a model and sourceModel that uses the dataComponent
                // only generate storage and function for sourceModel
                // model is only a single value
                def functionDef =""
                // for select generate load storage and function
                if(component.sourceModel) {
                    def arrayName = "${dataVarName}s_${component.name}_source"
                    def arrayFuncName = "${component.ID}"
                    functionDef += """
                    // define an array for select source
            \$scope.$arrayName = [];

            // load items using any query parameters specified
            \$scope.${arrayFuncName}_populateSource = function() {
                \$scope.$arrayName = ${dataComponent.name}.query($sourceParamMap);
            }

            // load items ignoring any query parameters
            \$scope.${arrayFuncName}_populateSourceAll = function() {
                \$scope.$arrayName = ${dataComponent.name}.query();
            }

            // when we first start up, populate items
            \$scope.${arrayFuncName}_populateSource();
            """
                }


                functions << functionDef
            } else if (component.type==PageComponent.COMP_TYPE_DETAIL) {
                // for detail type we only need the first row
                def arrayName = "${dataVarName}s_${component.ID}"
                def arrayFuncName = "${component.ID}"
                def functionDef = """
        \$scope.$arrayName = [];

        // load item using any query parameters specified
        \$scope.${arrayFuncName}_load = function() {
            \$scope.$arrayName = ${dataComponent.name}.query($paramMap);
        }

        // load item using any query parameters specified
        \$scope.${arrayFuncName}_save = function() {
            \$scope.$arrayName[0].\$save();
            // load updated data
            \$scope.${arrayFuncName}_load();
        }

        // when we first start up, populate items
        \$scope.${arrayFuncName}_load();
        """
                functions << functionDef
            } else if (component.type==PageComponent.COMP_TYPE_DATA) {
                // for data type we only need the first row
                def arrayName = "${dataVarName}s_${component.ID}"
                def arrayFuncName = component.ID
                def dataName = component.name
                def functionDef = """
        \$scope.${arrayFuncName}_onLoad = function() {
            ${parseExpression(component.onLoad)}
        }

        \$scope.$arrayName = [];

        // load item using any query parameters specified
        // call the onLoad function after loading is completed
        \$scope.${arrayFuncName}_load = function() {
            \$scope.$arrayName = ${dataComponent.name}.query($paramMap, function(){
            \$scope.$dataName = \$scope.$arrayName[0];
            \$scope.${arrayFuncName}_onLoad(); }
            );
        }

        // when we first start up, populate items if specified
        ${component.loadInitially?"\$scope.${arrayFuncName}_load();":""}
        """
                functions << functionDef
            } else if (dataComponent.binding != "page"){


                    //println "params = $p"
                    // implicitly define a $scope variable  ${dataVarName}_${component.ID}
                    def functionDef = """
            // initialize value

    \$scope.${component.ID}_load = function() {
            \$scope.${dataVarName}_${component.ID} = ${dataComponent.name}.get($paramMap);
    };


            """
                functions << functionDef
            }


        }

        return functions.join("\n")
    }

    // build all data and functions for flow control
    def static buildFlowControl(pageComponent, depth = 0) {
        def code = ""
        def flowArray = "\$scope._flowDefs"
        def activeFlowVar = "\$scope._activeFlow"
        def formSetVar = "\$scope._formSet"

        // first build flow structure in page
        if (pageComponent.type == PageComponent.COMP_TYPE_FLOW) {
            // add flow definition to global flow definition in page
            pageComponent.root.flowDefs << ["name":pageComponent.name, "sequence":pageComponent.sequence.replaceAll("\\s","").tokenize(','),
                    "activated": pageComponent.activated]
            if (pageComponent.activated)
                pageComponent.root.activeFlow = pageComponent.name
            }

        if (pageComponent.type == PageComponent.COMP_TYPE_FORM) {
            // add an variable of _name_visible to the scope and set the initial value
            // determine if a form needs to be shown initially if there is an active flow defined
            // build a form array
            pageComponent.root.formSet << pageComponent.name

            //def showInActiveFlow
           code += """
    \$scope.${pageComponent.name}_visible = $pageComponent.showInitially;
            """
        }

        if (pageComponent.type == PageComponent.COMP_TYPE_BLOCK) {
            // add an variable of _name_visible to the scope and set the initial value
            // determine if a form needs to be shown initially if there is an active flow defined
            // build a form array

           code += """
    \$scope.${pageComponent.name}_visible = $pageComponent.showInitially;
            """
        }

        // loop through all forms and flow controls first before adding code
         pageComponent.components.each { child ->
            code+= buildFlowControl(child, depth+1)
        }

        // now all page flows and forms have been accounted for, generate the data and functions
        if (pageComponent.type == PageComponent.COMP_TYPE_PAGE && pageComponent.flowDefs) {
            // initialize global structures and data
            code += """
    $flowArray = ${groovy.json.JsonOutput.toJson(pageComponent.flowDefs)};
    $activeFlowVar = ${pageComponent.activeFlow?"\"$pageComponent.activeFlow\"":"null"};
    $formSetVar = ${groovy.json.JsonOutput.toJson(pageComponent.formSet)};

    // find the flow definition by flow name
    \$scope._findFlow = function(flowName) {
        var ind;
        for (ind = 0; ind < ${flowArray}.length; ind++) {
            if ($flowArray[ind].name == flowName)
                return $flowArray[ind];
        }
        return null;
    }

    // return the next form of the active flow, or the same form if the form is the last one in the flow
    \$scope._nextForm = function(formName) {
        var activeflow = \$scope._findFlow($activeFlowVar);
        // activeSeq is an array of forms
        var activeSeq = activeflow["sequence"];
        // curIndex is the position of the current form in the active sequence
        var curIndex = activeSeq.indexOf(formName);
        if (curIndex < activeSeq.length - 1) {
            curIndex = curIndex+1;
            return activeSeq[curIndex];
        } else {
            return null;
        }
    }

    // called by form "next" button
    \$scope._activateNextForm = function(formName, hideExistingForm) {
        // set hideExistingForm default to true
        hideExistingForm = (typeof hideExistingForm === 'undefined') ? true : hideExistingForm;
        var nextForm = \$scope._nextForm(formName);

        if (hideExistingForm)
            \$scope[formName + "_visible"] = false;

        if (nextForm != null)
            \$scope[nextForm + "_visible"] = true;
    }

    // return the name of the first form of a flow
    \$scope._findFirstForm = function(flowName) {
        // find the flow definition by name
        var flow = \$scope._findFlow(flowName);
        // seq is an array of forms
        var seq = flow["sequence"];
        return seq[0];
    }

    // called when a flow is activated
    \$scope._activateFlow = function(flowName) {
        // disable all existing forms
        angular.forEach($formSetVar, function(value, index) {
            // value is form name
            \$scope[value+"_visible"] = false;
        })

        // set the active flow name
        $activeFlowVar = flowName;

        // now enable the first form of the active flow
        var fname = \$scope._findFirstForm(flowName);

        \$scope[fname + "_visible"] = true;
    }
    //activate the $activeFlowVar if it is set
    if ($activeFlowVar)
        \$scope._activateFlow($activeFlowVar);
            """
        }

        return code
    }


    def static buildControlVar(pageComponent, depth = 0) {
        // for form or block visibility control
        def code = ""
         if (pageComponent.type == PageComponent.COMP_TYPE_BUTTON) {
            // generate a control function for each button 'click' property
            if (pageComponent.onClick) {
                // handle variable and constant in expression
                def expr = parseExpression(pageComponent.onClick)
                code += """
    \$scope.${pageComponent.name}_onClick = function() {
        $expr
        };
                """
            }

        } else if (pageComponent.type == PageComponent.COMP_TYPE_LIST) {
            // generate a control function for list 'click' property
            if (pageComponent.onClick) {
                // handle variable and constant in expression
                def expr = parseExpression(pageComponent.onClick)
                code += """
    \$scope.${pageComponent.name}_onClick = function($PageComponent.CURRENT_ITEM) {
        $expr
        };
                """
            }

        } else  if (pageComponent.type == PageComponent.COMP_TYPE_SELECT && pageComponent.sourceValue) {
                // handle sourceValue for select
                 // sourceValue is already in javascript format
             // internally assign a source model name as the component name
                pageComponent.sourceModel = pageComponent.name
                 def arrayName = "_${pageComponent.sourceModel?.toLowerCase()}s_${pageComponent.name}_source"
                 code += """
    \$scope.$arrayName = ${groovy.json.JsonOutput.toJson(pageComponent.sourceValue)};
                 """

            } else if (pageComponent.submit) {
             // parse submit action
             // do not need $scope. prefix or {{ }}
             pageComponent.submit = parseVariable(pageComponent.submit)

         } else if (pageComponent.onUpdate) {
            // handle input field update
            // generate a ng-change function
            def  expr = parseExpression(pageComponent.onUpdate)
            def duplicateExpr =""
            if (pageComponent.type == PageComponent.COMP_TYPE_SELECT &&
                    (pageComponent.parent.type == PageComponent.COMP_TYPE_DETAIL || pageComponent.parent.type == PageComponent.COMP_TYPE_GRID) )
                // if a select is used in a grid or detail control its model is bound to parent model, we need to copy the model to $<selectName>
                // in order for it to be referenced by other controls
                duplicateExpr = "\$scope.$pageComponent.name = \$scope._${pageComponent.parent.model.toLowerCase()}s_${pageComponent.parent.name}[0].$pageComponent.model;"

            code += """
    \$scope.${pageComponent.name}_onUpdate = function() {
        $duplicateExpr
        // hanlde undefined value
        if (\$scope.${pageComponent.name} == null || \$scope.${pageComponent.name} ==undefined)
            \$scope.${pageComponent.name} = ""
        $expr
        };
            """
        }
        pageComponent.components.each { child ->
            code+= buildControlVar(child, depth+1)
        }
        return code
    }

    // parse expression defined in page model for use in scope functions
    // $var --> $scope.var
    // $$var --> reserved page model variables
    // custom variable cannot start with '_'
    // var.property will be transformed to var_property
    // var --> constant -> not change
    // to work around an angularJS object property binding issue we need to replace '.' with '_' to use plain scope variables
    def static parseExpression(rawExp) {
        // do a replacement of global $$reservedVar.property with __reservedVar_property?
        // $$reservedVar with __reservedVar
        //def ret = rawExp.replaceAll('\\$\\$(\\w*)\\.(\\w*)', '__$1_$2')
        def ret = rawExp.replaceAll('\\$\\$(\\w*)', '#scope._$1')
        // replace $myVar with $scope.myVar, $myVar.prop with $scope.myVar_prop
        ret = ret.replaceAll('\\$(\\w*)\\.\\$(\\w*)', '#scope.$1_$2')
        ret = ret.replace('$', '$scope.')
        // finalize
        ret = ret.replace('#', '$')

        return ret
    }

    // parse variable defined in page model, allow use of expression in submit, details, display and link
    // $var --> var
    // $$var --> reserved page model variables _
    // custom variable cannot start with '_'
    // var.property will be transformed to var_property
    // var --> constant -> not change
    // to work around an angularJS object property binding issue we need to replace '.' with '_' to use plain scope variables
    def static parseVariable(rawExp) {
        // do a replacement of $$reservedVar.property with $scope._reservedVar_property
        // $$reservedVar with $scope._reservedVar
        //def ret = rawExp.replaceAll('\\$\\$(\\w*)\\.(\\w*)', '#scope._$1_$2')
        //ret = ret.replaceAll('\\$\\$(\\w*)', '#scope._$1')
        // replace $myVar with $scope.myVar, $myVar.prop with $scope.myVar_prop
        //ret = ret.replaceAll('\\$(\\w*)\\.(\\w*)', '#scope.$1_$2')
         def ret = rawExp.replaceAll('\\$\\$([\\w\\.]*)', '_$1')
        ret = ret.replaceAll('\\$(\\w*)\\.\\$(\\w*)', '$1_$2')
        ret = ret.replace('$', '')
        // finalize
        //ret = ret.replace('#', '$')

        return ret
    }
    // allow use of $var in literal string as {{...}} angular value binding
    def static parseLiteral(rawExp) {
        // do a replacement of $$reservedVar.property with $scope._reservedVar_property
        // $$reservedVar with $scope._reservedVar
        //def ret = rawExp.replaceAll('\\$\\$(\\w*)', '__$1')
        //ret = ret.replaceAll('\\$\\$(\\w*)', '#scope._$1')
        // replace $myVar with $scope.myVar, $myVar.prop with $scope.myVar_prop
        //ret = ret.replaceAll('\\$(\\w*)\\.(\\w*)', '#scope.$1_$2')
        def ret = rawExp.replaceAll('\\$\\$([\\w\\.]*)', '{{ _$1 }}')
        ret = ret.replaceAll('\\$([\\w\\.]*)', '{{ $1 }}')
        // finalize
        //ret = ret.replace('#', '$')

        return ret
    }

    // find all component that uses this data component, search child components
    def static findUsageComponents(component, dataComponent) {
        def compList = []
        //println "in findUsageComponents (${component.ID}, ${dataComponent.name})"
        if (component.ID != dataComponent.ID) {
           if (component.model==dataComponent.name || component.model?.startsWith("${dataComponent.name}.")
           || component.sourceModel==dataComponent.name || component.sourceModel?.startsWith("${dataComponent.name}.")) {
               compList.push(component)
               component.binding = dataComponent.binding
               //println "component=${component.name}, dataComponent = $dataComponent.name, compList Size = ${compList.size()}"
           }
        }
        component.components?.each {
            compList += findUsageComponents(it, dataComponent)
        }

        return compList

    }

    // recursively find all component of type "data" and return the list
    // TODO eliminate potential duplicate if validation does not check for it?
    def static findDataItems(pageComponent) {
        def myDataList = []
        if (pageComponent.type==PageComponent.COMP_TYPE_RESOURCE) {
            myDataList << pageComponent
        }
        pageComponent.components.each {
            myDataList += findDataItems(it)
        }
        return myDataList
    }



    // accept a normalized pageComponent
    def static compile2js(page) {
        def codeList = []
        codeList = page.addJsItems(codeList)   //first do items so they are available already when generating controller
        codeList = page.compileComponentJS(codeList)
        String pageJS=finalizeJS(codeList)
        return pageJS
    }


    /* TODO normalize model name etc for code generation
    * model needs to be at least two letters long and camel cased
    * set parent for each component
    * set model to parent model if a child does not have its own model
    * generate a normalized ID using name, replacing space with "_"
    * */
    def static normalizeComponent(pageComponent) {
        // type of input (except button or submit)
        def inputTypes = [PageComponent.COMP_TYPE_BOOLEAN, PageComponent.COMP_TYPE_TEXT, PageComponent.COMP_TYPE_TEXTAREA,
        PageComponent.COMP_TYPE_EMAIL,PageComponent.COMP_TYPE_NUMBER, PageComponent.COMP_TYPE_DATETIME, PageComponent.COMP_TYPE_TEL, PageComponent.COMP_TYPE_SELECT]

        if (pageComponent.type == PageComponent.COMP_TYPE_PAGE) {
            pageComponent.parent = null
            pageComponent.root = pageComponent
        }
        // parse parameters
        if (pageComponent.parameters) {
            def newParam = [:]
            pageComponent.parameters.each{key, value ->
                def v = parseExpression(value)
                newParam.put(key, v)
            }
            pageComponent.parameters = newParam
        }
        // parse source parameters
        if (pageComponent.sourceParameters) {
            def newParam = [:]
            pageComponent.sourceParameters.each{key, value ->
                def v = parseExpression(value)
                newParam.put(key, v)
            }
            pageComponent.sourceParameters = newParam
        }


        pageComponent.components?.eachWithIndex {it, index ->
            it.parent = pageComponent
            it.root = pageComponent.root

            // if a child component does not have a name, assign a name of format parent_child{componentIndex}
            if (!it.name)
                it.name = "${pageComponent.name}_child$index"

            if (!it.model) {
                // generate a model of name_value if it is missing
                // for use in controller functions to be referenced by other components
                if (inputTypes.contains(it.type) ) {
                    it.model = "${it.name}"
                    it.binding = PageComponent.BINDING_PAGE
                } else {
                    // for non-input types inherit parent model?
                    it.model = pageComponent.model
                }
            }
            normalizeComponent(it)
        }
        pageComponent.ID = pageComponent.name?.replaceAll(" ", "_")
        // set root class of the model
        pageComponent.modelRoot = pageComponent.model
        def model = pageComponent.model
        if (model && model.indexOf(".")!=-1) {
           pageComponent.modelRoot = model.substring(0, model.indexOf("."))
            pageComponent.modelComponent = model.substring(model.indexOf(".") + 1, model.length())
        }

        return pageComponent
    }

    //TODO develop page validation
    /*  parse, normalize and validate page model
        check all required field for each component recursively
        check name and data uniqueness
        return validation and a normalized page component
        or error messages with component tree
    */
    def static preparePage(String json) {
        def slurper = new groovy.json.JsonSlurper()
        def page
        def errors=[]
        def valid = true
        def pageValidation = [:]

        try {
            def jsonResult = slurper.parseText(json)
            page = new PageComponent(jsonResult)
            page = normalizeComponent(page)
            pageValidation = validateComponent(page)
            valid = pageValidation.valid
            errors += pageValidation.errors
        } catch (Exception e) {
            println "Parsing page model exception: " + e
            errors << "Page Model parsing error: " + e.message
            valid = false
        }

        return [valid:valid, pageComponent:page, errors:errors]
    }

    // validate a component and all its children
    def static validateComponent(pageComponent, nameSet = []) {
        def valid = true
        def errors = []
        // validate this component first
        // check if name already exists on the page
        if (nameSet.contains(pageComponent.name)) {
            valid = false
            errors << "Name conflict at ${getComponentNamePath(pageComponent)}: $pageComponent.name (of type $pageComponent.type) already exists."
        } else
            nameSet << pageComponent.name

        // validate all child component
        // JsonSlurper does not automatically cast components from a list of Map to a list of PageComponent

        def componentList = []
        pageComponent.components.each {
            // 'it' is a map, convert to PageComponent
            def child=new PageComponent(it)
            componentList.push(child)
            def ret = validateComponent(child, nameSet)
            valid = valid && ret.valid
            errors = errors + ret.errors
            nameSet = ret.nameSet
        }
        // attached the converted components list
        pageComponent.components = componentList

        return [valid:valid, errors:errors, nameSet:nameSet]
    }

    /* return a string representing the concanated names of the given component starting from the top level page
         names are separated by "."
     */
    def static getComponentNamePath(pageComponent) {
        def nameList = []
        while (pageComponent) {
            nameList << pageComponent.name
            pageComponent = pageComponent.parent
        }
        nameList = nameList.reverse()

        return nameList.join(".")
    }
    def static getValidChildrenType(pageComponent) {
        def validPageChildren = [PageComponent.COMP_TYPE_RESOURCE, PageComponent.COMP_TYPE_BLOCK]
    }

    /* find all component that matches the criteria (map), starting from pageComponent

     */
    def static findComponent(pageComponent, criteria) {
        //
        def ret = []
        def found = true
        criteria.each { key, value ->
            found = found && (pageComponent."$key" == value)
        }
        if (found)
            ret << pageComponent

        pageComponent.components?.each {
            ret += findComponent(it, criteria)
        }

        return ret
    }

    def static removeQuotes(txt) {
        if (txt.startsWith('\"'))
			txt = txt.substring(1, txt.length()-1)
		if (txt.endsWith('\"'))
			txt = txt.substring(0, txt.length()-2)

        return txt
    }
}
