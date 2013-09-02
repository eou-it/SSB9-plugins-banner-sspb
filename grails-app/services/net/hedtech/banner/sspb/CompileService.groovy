package net.hedtech.banner.sspb

//import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class CompileService {

    /* use message
    def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }
    */

    // TODO configure Hibernate
    def transactional = false

    static def dataSetIDsIncluded =[]
    static def uiControlIDsIncluded = []

//major step 1.
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
        //reset global arrays
        dataSetIDsIncluded =[]
        uiControlIDsIncluded = []

        try {
            // first validate the raw JSON page model
            def pageModelValidator = new PageModelValidator()
            // TODO parsing of model definition should be done once
            def pageDefText = CompileService.class.classLoader.getResourceAsStream( 'PageModelDefinition.json' ).text
            slurper = new groovy.json.JsonSlurper()
            def pageBuilderModel = slurper.parseText(pageDefText)
            pageModelValidator.setPageBuilderModel(pageBuilderModel)
            // validate the raw Page JSON data
            def validateResult =  pageModelValidator.validatePage(json)

            // validate the unmarshalled page model
            if (!validateResult.valid)
                return  [valid:false, pageComponent:page, error:validateResult.error]

            def jsonResult = slurper.parseText(json)
            page = new PageComponent(jsonResult)
            page = normalizeComponent(page)
            // run second validation
            pageValidation = validateComponent(page)
            valid = pageValidation.valid
            errors += pageValidation.errors
        } catch (Exception e) {
            println "Parsing page model exception: " + e
            errors << PageModelErrors.getError(error: PageModelErrors.MODEL_UNKNOWN_ERR, args: [e.message])
            valid = false
        }


        //populate components to be used in name resolution
        //dataSets=page.findComponents(PageComponent.COMP_DATASET_TYPES)
        //uiControls=page.findComponents(PageComponent.COMP_UICTRL_TYPES)
        //return results
        return [valid:valid, pageComponent:page, error:errors]
    }

//major step 2.
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
        for top level SQL resource:   //TODO remove, we don't have direct SQL support
            - functions to get, list, update, create and delete
            - per grid - list of retrieved objects from list, modified, added and deleted object list
            - initial load
            - keys for query

         */
        modelComponents.each {
            preBuildCode(it)  // populates   dataSetIDsIncluded and  uiControlIDsIncluded
        }
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
        // inject common code into controller
        // TODO: refactor so it can be in a separate js.
        //def common =  new File("js/sspbCommon.js").getText()
        def common = CompileService.class.classLoader.getResourceAsStream( 'data/sspbCommon.js' ).text

        result = """
    function CustomPageController( \$scope, \$http, \$resource, \$parse, \$locale) {
        // copy global var to scope - HvT: do we really need this?
        \$scope._isUserAuthenticated = user.authenticated;
        \$scope._userFullName = user.fullName;
        \$scope._userFirstName = "";
        \$scope._userLastName = "";
        \$scope._pidm = "";
        \$scope._userRoles = user.roles;
        // page specific code
        $result
        //common code TODO - move out of this controller
        $common
    }
        """
        return result
    }

//major step 3. (done in pageComponent)
    // compile the page
    // accept a normalized page level pageComponent
    // output
    def static compile2page(pageComponent) {
        def pageTxt=pageComponent.compileComponent("")
        def pageUtilService = new PageUtilService()
        pageUtilService.updateProperties(pageComponent.rootProperties,"pages")
        pageUtilService.updateProperties(pageComponent.globalProperties,"pageGlobal")
        pageUtilService.reloadBundles()
        return pageTxt
    }


// public function used in rendering.
    /*
    Inject the JavaScript controller to the HTML page
    Return the combined page
     */
    def static assembleFinalPage(page, code) {
        def ind = page.indexOf(PageComponent.CONTROLLER_PLACEHOLDER)
        if (ind != -1)
            return page.substring(0, ind-1) + code + page.substring(ind + PageComponent.CONTROLLER_PLACEHOLDER.length())
    }


/////////////////////////////////////////////////////////
//    methods and closures for major steps 1. to 4.    //
/////////////////////////////////////////////////////////

    private def static getQueryParameters (component,dataComponent) {
        def buildParameters = {parameters -> // concatenate all map entries to a string
            def res = ""
            parameters?.each { key, value->
                //def v=value.replace("\$scope.","")    //fix for evaluate expression - not needed if we use js eval
                def v=value
                res +=  "$key : $v,"
            }
            if (res?.endsWith(","))   // remove trailing comma
                res = res.substring(0, res.length() - 1)
            res = "'{$res}'"
        }

        def queryParameters = "'{}'"
        if (dataComponent.binding == PageComponent.BINDING_REST) {
            if (component.parameters)
                queryParameters = buildParameters(component.parameters)
            if (component.sourceParameters)
                queryParameters = buildParameters(component.sourceParameters)
        }
        println "Query parameters: $queryParameters"
        return queryParameters
    }

    private def static getUIControlCode (component,dataComponent) {
        def result = ""
        def dataSource
        def queryParameters = "null"
        def apiPath = CH.config.sspb.apiPath;

        def staticData =""
        //should only COMP_TYPE_DATA have loadInitially?
        def autoPopulate = "true"
        if ( (component.type == PageComponent.COMP_TYPE_DATA ||
              PageComponent.COMP_ITEM_TYPES.contains(component.type) )
            && !component.loadInitially) {
            autoPopulate = "false"
        }
        // first handle data binding
        if (dataComponent.binding == PageComponent.BINDING_REST) {
            // can specify resource relative to current application like $rootWebApp/rest/emp
            //dataSource = "'${dataComponent.resource}'".replace("'\$rootWebApp/", "rootWebApp+'")
            dataSource = "rootWebApp+'$apiPath/${dataComponent.resource}'"

            if (dataSource.startsWith("'/\$rootWebApp")) {
                throw new Exception(message(code:"sspb.compiler.resourceInvalidRootReference.message"))
            }
            // transform parameters to angular $scope variable
            queryParameters = getQueryParameters(component, dataComponent)
            dataSource =  "resourceURL: $dataSource"
        } else {
            dataSource =  "data: $dataComponent.data"
            autoPopulate = "false"
        }

        def dataSetName = "${component.ID}DS"
        def uiControlName = "${component.ID}UICtrl"
        def postQuery = component.onLoad ? parseExpression(component.onLoad) : ""

        def optionalParams=""
        if  (PageComponent.COMP_ITEM_TYPES.contains(component.type)) //items don't support arrays, use the get
            optionalParams+=",useGet: true"
        result =
            """
            //\$scope.$component.ID=[];
            \$scope.$dataSetName = new CreateDataSet (
                {
                    componentId: "$component.ID",
                    $dataSource,
                    queryParams: $queryParameters,
                    autoPopulate: $autoPopulate,
                    postQuery: function() {$postQuery},
                    selectValueKey: ${component.valueKey ? "\"$component.valueKey\"" : null},
                    selectInitialValue: ""
                   $optionalParams
                });

            \$scope.$uiControlName = new CreateUICtrl (
                {
                    name: "$uiControlName",
                    dataSet: \$scope.$dataSetName,
                    pageSize: $component.pageSize,
                    onUpdate: function() {${parseOnEventFunction(component.onUpdate, component)} }
                });
            """

        return result
    }

    private def static parseOnEventFunction(expr, pageComponent) {
        // $SelectCollege.$populateSource(); $SelectCampus.$populateSource(); $SelectLevel.$populateSource();
        //->
        //  $scope.SelectCollegeDS.load(); ...

        def patterns = [[from:".\$populateSource", to:"DS.load"],
                        [from: ".\$load"          ,to:"DS.load"],
                        [from: ".\$get"           ,to:"DS.get"]   ]
        def result=expr
        if (result) {

            patterns.each {pattern ->
                dataSetIDsIncluded.each { pcId ->
                    //result=result.replace("\$${pcId}$pattern.from","\$scope.${pcId}$pattern.to" )
                    //remove $scope, now done by parseExpression
                    result=result.replace("\$${pcId}$pattern.from","\$${pcId}$pattern.to" )
                }
            }

            // fix 2013-07-02
            // add logic onClick - the show/hide block feature doesn't work in on change
            result=parseExpression(result)
            //end fix
            if (result == expr) {
                println "Warning: onEvent expression for $pageComponent.ID not changed."
            } else {
                println "onEvent expression for $pageComponent.ID $expr -> $result"
            }
        }
        return result
    }

    // prepare build Code
    // determine which DataSets we are going to build so we can replace references appropriately
    // use the same logic as in buildCode
    private def static preBuildCode(dataComponent) {
        def referenceComponents = findUsageComponents(dataComponent.parent, dataComponent)
        referenceComponents.each {component->
            if (PageComponent.COMP_DATASET_TYPES.contains(component.type)) {
                dataSetIDsIncluded<<component.ID   //remember  - used to replace variable names
                uiControlIDsIncluded<<component.ID
            } else if (dataComponent.binding != "page"){
                if ( PageComponent.COMP_ITEM_TYPES.contains(component.type)){
                    dataSetIDsIncluded<<component.ID   //remember  - used to replace variable names
                    uiControlIDsIncluded<<component.ID
                }
            }
        }
    }

    /* build JS function and models based on data definition
        input: pageComponent - data definirion
        return a string of the variables and JS function
     */
    private def static buildCode(dataComponent) {
        def functions = []
        // generate all scope variables / arrays for each component that uses the model
        // TODO recursive to find all children
        def referenceComponents = findUsageComponents(dataComponent.parent, dataComponent)
        // generate variable declarations for each usage of this model
        referenceComponents.each {component->
            // following same logic as in preBuildCode
            if (PageComponent.COMP_DATASET_TYPES.contains(component.type)) {
                def uiControlCode =  getUIControlCode (component, dataComponent)
                functions << uiControlCode
            } else if (dataComponent.binding != "page") {
                if ( PageComponent.COMP_ITEM_TYPES.contains(component.type)){
                    // try to make consistent with DS  - possibly should add something to enforce get instead of query
                    // when loading the data  possibly
                    def uiControlCode =  getUIControlCode (component, dataComponent)
                    functions << uiControlCode
                } else {
                    //TODO HvT- find out for what use case this is needed still
                    def dataVarName = "_"+dataComponent.name.toLowerCase()
                    println "****WARNING**** generating control $component.name $component.type - check generator"
                    // implicitly define a $scope variable  ${dataVarName}_${component.ID}
                    def queryParameters = getQueryParameters(component,dataComponent)
                    def functionDef = """
                        // initialize value
                        \$scope.${component.ID}_load = function() {
                        \$scope.${dataVarName}_${component.ID} = ${dataComponent.name}.get($queryParameters);
                        };\n"""
                    functions << functionDef
                }
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
        def code = ""
         if ( [PageComponent.COMP_TYPE_BUTTON,PageComponent.COMP_TYPE_LIST, PageComponent.COMP_TYPE_GRID, PageComponent.COMP_TYPE_LINK].contains( pageComponent.type ) ) {
            // generate a control function for each button/list/link/grid 'click' property
            if (pageComponent.onClick) {
                // handle variable and constant in expression
                def expr = parseExpression(pageComponent.onClick)
                dataSetIDsIncluded.each {   //TODO HvT - is this OK always?
                    expr=expr.replace(".${it}_",".${it}DS.")
                }
                // if we are dealing with clicking on an item from a list or table then pass the current selection to the control function
                def arg = "";
                if (pageComponent.type==PageComponent.COMP_TYPE_LIST || pageComponent.type==PageComponent.COMP_TYPE_GRID)
                    arg = PageComponent.CURRENT_ITEM

                code += """\$scope.${pageComponent.name}_onClick = function($arg) { $expr}; \n"""
                println "onClick expression for $pageComponent.name $pageComponent.onClick -> $expr"
            }
         } else if ((pageComponent.type == PageComponent.COMP_TYPE_SELECT || pageComponent.type == PageComponent.COMP_TYPE_RADIO) && pageComponent.sourceValue) {
             // static select: handle sourceValue for select sourceValue is already in javascript format
             pageComponent.sourceModel = pageComponent.name
             def dataComponent = [static: true, data: groovy.json.JsonOutput.toJson(pageComponent.tranSourceValue())]
             def uiControlCode =  getUIControlCode (pageComponent, dataComponent)
             dataSetIDsIncluded<<pageComponent.ID   //remember  - used to replace variable names
             uiControlIDsIncluded<<pageComponent.ID
             //TODO - could be added too late to the *Included  collections?
             code += uiControlCode
         } else if (pageComponent.submit) {
             // parse submit action
             // do not need $scope. prefix or {{ }}
             pageComponent.submit = parseVariable(pageComponent.submit)

         } else if (pageComponent.onUpdate && !uiControlIDsIncluded.contains(pageComponent.ID)) {
             // handle input field update
             // generate a ng-change function
             def  expr = parseExpression(pageComponent.onUpdate)
             dataSetIDsIncluded.each { //replace with dataSetIDs
                 expr=expr.replace(".${it}_",".${it}DS.")
             }
             def duplicateExpr =""
             if ((pageComponent.type == PageComponent.COMP_TYPE_SELECT || pageComponent.type == PageComponent.COMP_TYPE_RADIO)&&
                     (pageComponent.parent.type == PageComponent.COMP_TYPE_DETAIL
                             || pageComponent.parent.type == PageComponent.COMP_TYPE_GRID) ) {
                 // if a select is used in a grid or detail control its model is bound to parent model, we need to copy
                 // the model to $<selectName> in order for it to be referenced by other controls
                 println "****WARNING**** using duplicate - is this still needed?"
                 duplicateExpr =
                     "//duplicate\n"+
                             "\$scope.$pageComponent.name = \$scope._${pageComponent.parent.model.toLowerCase()}s_${pageComponent.parent.name}[0].$pageComponent.model;"

             }

             // add onUpdate for grid/detail items
             if ( [pageComponent.COMP_TYPE_GRID,pageComponent.COMP_TYPE_DETAIL].contains( pageComponent.parent.type ) )  {
                 def parentID = pageComponent.parent.ID
                 code += """
             \$scope.${parentID}_${pageComponent.name}_onUpdate = function(current_item) {
                  $expr
              };
             """
             } else {
                //next code is used for non-DataSet items with an onUpdate, i.e. an input field to set a filter
                code += """
             \$scope.${pageComponent.name}_onUpdate = function() {
                  $duplicateExpr
                  // handle undefined value
                  \$scope.${pageComponent.name} = nvl(\$scope.${pageComponent.name}, "");
                  $expr
             };
             """
             }
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
        def ret = rawExp?.replaceAll('\\$\\$(\\w*)', '#scope._$1')
        // replace $myVar with $scope.myVar, $myVar.prop with $scope.myVar_prop
        ret = ret?.replaceAll('\\$(\\w*)\\.\\$(\\w*)', '#scope.$1_$2')
        ret = ret?.replace('$', '$scope.')
        // finalize
        ret = ret?.replace('#', '$')

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
         def ret = rawExp?.replaceAll('\\$\\$([\\w\\.]*)', '_$1')
        ret = ret?.replaceAll('\\$(\\w*)\\.\\$(\\w*)', '$1_$2')
        ret = ret?.replace('$', '')
        // finalize
        ret = ret.replace('#', '$')

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
        def ret = rawExp?.replaceAll('\\$\\$([\\w\\.]*)', '{{ _$1 }}')
        ret = ret?.replaceAll('\\$([\\w\\.]*)', '{{ $1 }}')
        // finalize
        //ret = ret.replace('#', '$')

        return ret
    }

    // find all component that uses this data component, search child components
    def static findUsageComponents(component, dataComponent) {
        def compList = []
        if (component.ID != dataComponent.ID) {
           if (component.model==dataComponent.name || component.model?.startsWith("${dataComponent.name}.")
           || component.sourceModel==dataComponent.name || component.sourceModel?.startsWith("${dataComponent.name}.")) {
               compList.push(component)
               component.binding = dataComponent.binding
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


/* seems obsolete
    // accept a normalized pageComponent
    def static compile2js(page) {
        def codeList = []
        codeList = page.addJsItems(codeList)   //first do items so they are available already when generating controller
        codeList = page.compileComponentJS(codeList)
        String pageJS=finalizeJS(codeList)
        return pageJS
    }
*/

    /* TODO normalize model name etc for code generation
    * model needs to be at least two letters long and camel cased
    * set parent for each component
    * set model to parent model if a child does not have its own model
    * generate a normalized ID using name, replacing space with "_"
    * */
    def static normalizeComponent(pageComponent) {
        // type of input (except button or submit)
        def inputTypes = [PageComponent.COMP_TYPE_BOOLEAN, PageComponent.COMP_TYPE_TEXT, PageComponent.COMP_TYPE_TEXTAREA,
                          PageComponent.COMP_TYPE_EMAIL, PageComponent.COMP_TYPE_NUMBER, PageComponent.COMP_TYPE_DATETIME,
                          PageComponent.COMP_TYPE_TEL, PageComponent.COMP_TYPE_SELECT, PageComponent.COMP_TYPE_RADIO]

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
                // Fix: generates DataSet for Literal - also should use model for display types
                if (inputTypes.contains(it.type) || PageComponent.COMP_DISPLAY_TYPES.contains(it.type) ) {
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


    // validate a component and all its children
    def static validateComponent(pageComponent, nameSet = []) {
        def valid = true
        def errors = []
        // validate this component first
        // check if name already exists on the page
        if (nameSet.contains(pageComponent.name)) {
            valid = false
            def error = PageModelErrors.getError(error:PageModelErrors.MODEL_NAME_CONFLICT_ERR, path: getComponentNamePath(pageComponent), args: [getComponentNamePath(pageComponent),pageComponent.name,pageComponent.type] )
            errors << error
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

    /* return a string representing the concatenated names of the given component starting from the top level page
         names are separated by "."
     */
    def static getComponentNamePath(pageComponent) {
        def nameList = []
        while (pageComponent) {
            nameList << pageComponent.name + "(type=$pageComponent.type)"
            pageComponent = pageComponent.parent
        }
        nameList = nameList.reverse()
        nameList[0] = "/${nameList[0]}"

        return nameList.join("/")
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
