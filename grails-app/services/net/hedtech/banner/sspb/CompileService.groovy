package net.hedtech.banner.sspb

import net.hedtech.banner.tools.i18n.PageMessageSource

//import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class CompileService {

    /* use message function (added by plugin to each service class)
    def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }
    */

    // TODO configure Hibernate
    def transactional = false

    static def dataSetIDsIncluded =[]
    static def resourceIDsIncluded = []

    // Context for parsing expressions
    static enum ExpressionTarget{
        CtrlFunction,  // Controller function (need to use $scope)
        DOMExpression, // Expression in a place that will be evaluated by Angular (ng-change etc.)
        DOMDisplay     // Expression with AngularJS expression between curly braces
    }

    static def exprBra ="{{"    // start expression
    static def exprKet ="}}"    // end expression


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
        def warn=[]
        def valid = true
        def pageValidation = [:]
        //reset global arrays
        dataSetIDsIncluded =[]
        resourceIDsIncluded = []

        try {
            // first validate the raw JSON page model
            def pageModelValidator = new PageModelValidator()
            // TODO parsing of model definition should be done once
            def pageDefText = CompileService.class.classLoader.getResourceAsStream( 'PageModelDefinition.json' ).text
            slurper = new groovy.json.JsonSlurper()
            def pageBuilderModel = slurper.parseText(pageDefText)
            pageModelValidator.setPageBuilderModel(pageBuilderModel)
            // validate the raw Page JSON data
            def validateResult =  pageModelValidator.validatePage(json) //

            // validate the unmarshalled page model
            if (!validateResult.valid)
                return  [valid:false, pageComponent:page, error:validateResult.error, warn:validateResult.warn]
            warn=validateResult.warn

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
        //return results
        return [valid:valid, pageComponent:page, error:errors, warn:warn]
    }

//major step 2.
    /* compile the controller for the given page
   page: top level page component
   output: a string containing all the Java script code goes into the HTML header
    */
    def static compileController(page) {
        // find all data definition in page
        def modelComponents = page.findComponents([PageComponent.COMP_TYPE_RESOURCE])
        def resourceUsage = []  // map with resource and usage
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

         */
        modelComponents.each {
            def ru =  [resource:it, usedBy: findUsageComponents(page, it)]
            addDataSets(ru)// populates   dataSetIDsIncluded
            resourceUsage += ru
            resourceIDsIncluded += it.ID
        }

        resourceUsage.each {
            codeList << buildCode(it)
        }

        // Add control specific scope variables, functions
        // add visibility control
        // do this for block and form for now
        codeList << buildControlVar(page)

        // add flow control functions
        codeList << buildFlowControl(page)

        // add style initialization code
        codeList << initializeStyle(page)

        // TODO Add each function to result
        def result = codeList.join("\n")
        // inject common code into controller
        // TODO: refactor so it can be in a separate js.
        def common = CompileService.class.classLoader.getResourceAsStream( 'data/sspbCommon.js' ).text

        //  Removed Dependencies: $http,$resource,$cacheFactory,$parse (now in pbDataSet )
        result = """
               |function CustomPageController(\$scope ,\$locale,\$templateCache,pbDataSet,pbResource,pbAddCommon) {
               |    // copy global var to scope
               |    \$scope._user = user;
               |    \$scope._params = params;
               |    // page specific code
               |    $result
               |    $common
               |}//End Controller
               |""".stripMargin()
        return result
    }

    // iterate through all components and add a style attributes (name.style_attr)
    def static initializeStyle(pageComponent) {
        def ret = ""
        if (PageComponent.COMP_VISUAL_TYPES.contains(pageComponent.type))
        {
            // set the initial value if specified in the page definition
            def style = pageComponent.style?"'${pageComponent.style}'":"''"
            // add a scope variable to for dynamic CSS manipulation
            ret += """  \$scope.${pageComponent.name}_${PageComponent.STYLE_ATTR}=$style;\n"""
        }
        pageComponent.components.each { child ->
            ret+= initializeStyle(child)
        }
        return ret

    }
//major step 3. (done in pageComponent)
    // compile the page
    // accept a normalized page level pageComponent
    // output
    def static compile2page(pageComponent) {
        def pageTxt=pageComponent.compileComponent("")
        def pageUtilService = new PageUtilService()
        pageUtilService.updateProperties(pageComponent.rootProperties,propertiesFileName(pageComponent.name))
        pageUtilService.updateProperties(pageComponent.globalProperties,PageMessageSource.globalPropertiesName)
        pageUtilService.reloadBundles()
        return pageTxt
    }

    def static propertiesFileName(String pageName) {
        pageName.tr('_ ','-.')
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
        //should only COMP_TYPE_DATA have loadInitially?
        def autoPopulate = "true"
        if ( (component.type == PageComponent.COMP_TYPE_DATA || PageComponent.COMP_DATASET_TYPES.contains(component.type) )
             && !component.loadInitially) {
            autoPopulate = "false"
        }
        // first handle data binding
        if (dataComponent.binding == PageComponent.BINDING_REST && dataComponent.resource) {
            dataSource = "resource: \$scope.${dataComponent.name}"
            // transform parameters to angular $scope variable
            queryParameters = getQueryParameters(component, dataComponent)
        } else if (dataComponent.staticData){
            def data
            if ( PageComponent.COMP_DATASET_DISPLAY_TYPES.contains(component.type)  ){
                component.sourceModel = component.name //Not clear why this is needed.
                component.staticData = dataComponent.staticData // clone data to component
                data = groovy.json.JsonOutput.toJson(component.tranSourceValue())  // translate labels
            }
            else {
                data = groovy.json.JsonOutput.toJson(dataComponent.staticData)
            }
            dataSource =  "data: $data"
            autoPopulate = "false"
        } else {
            throw new Exception("Error Compiling UI. Either a Rest Resource or Static Data is required for Resource ${dataComponent.name}")
        }

        def dataSetName = "${component.ID}DS"
        def optionalParams=""
        if  (PageComponent.COMP_ITEM_TYPES.contains(component.type)) //items don't support arrays, use the get
            optionalParams+=",useGet: true"
        if (component.type != PageComponent.COMP_TYPE_SELECT)
             optionalParams+=",pageSize: $component.pageSize"
        if (component.onUpdate)
            optionalParams+="\n,onUpdate: function(item){\n${compileCtrlFunction(component.onUpdate)}}"
        if (component.onLoad)
            optionalParams+="\n,postQuery: function(data,response){\n${compileCtrlFunction(component.onLoad)}}"
        if (component.onError)
            optionalParams+="\n,onError: function(response){\n${compileCtrlFunction(component.onError)}}"


        result = """
              |    //\$scope.$component.ID=[];
              |    \$scope.$dataSetName = pbDataSet ( \$scope,
              |    {
              |        componentId: "$component.ID",
              |        $dataSource,
              |        queryParams: $queryParameters,
              |        autoPopulate: $autoPopulate,
              |        selectValueKey: ${component.valueKey ? "\"$component.valueKey\"" : null},
              |        selectInitialValue: ${component.value?"\"$component.value\"":"null"}
              |        $optionalParams
              |    });
              |""".stripMargin()

        def initNew = component.allowNew? component.initNewRecordJS(): ""
        if (component.type == PageComponent.COMP_TYPE_GRID) {
            result +=component.gridJS() + initNew
        }
        if ( [PageComponent.COMP_TYPE_HTABLE,PageComponent.COMP_TYPE_DETAIL,PageComponent.COMP_TYPE_LIST].contains( component.type)) {
            result +=component.dataSetWatches() + initNew
        }
        return result
    }



    // determine which DataSets we are going to build so we can replace references appropriately
    // logic to match buildCode
    private def static addDataSets(resourceUsage) {
        resourceUsage.usedBy.each {component->
            if (PageComponent.COMP_DATASET_TYPES.contains(component.type)) {
                dataSetIDsIncluded<<component.ID   //remember  - used to replace variable names
            } else if (resourceUsage.resource.binding != PageComponent.BINDING_PAGE){
                if ( PageComponent.COMP_ITEM_TYPES.contains(component.type)){
                    dataSetIDsIncluded<<component.ID   //remember  - used to replace variable names
                }
            }
        }
    }

    /* build JS function and models based on data definition
        input: pageComponent - data definirion
        return a string of the variables and JS function
     */
    private def static buildCode(resourceUsage) {
        def functions = []
        def dataComponent = resourceUsage.resource
        if (dataComponent.binding == PageComponent.BINDING_REST && dataComponent.resource) {
            def apiPath = CH.config.sspb.apiPath;
            def url = "rootWebApp+'$apiPath/${dataComponent.resource}'"
            functions << """\$scope.${dataComponent.name} = pbResource($url);"""
        }

        // generate all scope variables / arrays for each component that uses the model
        // generate variable declarations for each usage of this model
        resourceUsage.usedBy.each {component->
            if (PageComponent.COMP_DATASET_TYPES.contains(component.type)
                || (PageComponent.COMP_ITEM_TYPES.contains(component.type) && dataComponent.binding != "page") ) {

                def uiControlCode =  getUIControlCode (component, dataComponent)
                functions << uiControlCode
            } else if (dataComponent.binding != "page") {
                throw new Exception("*** Unhandled case in code generator for ${component.type} $component.name}")
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
           code += """\n    \$scope.${pageComponent.name}_visible = $pageComponent.showInitially;"""
        }

        if (pageComponent.type == PageComponent.COMP_TYPE_BLOCK) {
            // add an variable of _name_visible to the scope and set the initial value
            // determine if a form needs to be shown initially if there is an active flow defined
            // build a form array

           code += """\n    \$scope.${pageComponent.name}_visible = $pageComponent.showInitially;"""
        }

        // loop through all forms and flow controls first before adding code
         pageComponent.components.each { child ->
            code+= buildFlowControl(child, depth+1)
        }

        // now all page flows and forms have been accounted for, generate the data and functions
        if (pageComponent.type == PageComponent.COMP_TYPE_PAGE && pageComponent.flowDefs) {
            // initialize global structures and data
            code += """
                |    $flowArray = ${groovy.json.JsonOutput.toJson(pageComponent.flowDefs)};
                |    $activeFlowVar = ${pageComponent.activeFlow?"\"$pageComponent.activeFlow\"":"null"};
                |    $formSetVar = ${groovy.json.JsonOutput.toJson(pageComponent.formSet)};
                |
                |    // find the flow definition by flow name
                |    \$scope._findFlow = function(flowName) {
                |        var ind;
                |        for (ind = 0; ind < ${flowArray}.length; ind++) {
                |            if ($flowArray[ind].name == flowName)
                |                return $flowArray[ind];
                |        }
                |        return null;
                |    }
                |
                |    // return the next form of the active flow, or the same form if the form is the last one in the flow
                |    \$scope._nextForm = function(formName) {
                |        var activeflow = \$scope._findFlow($activeFlowVar);
                |        // activeSeq is an array of forms
                |        var activeSeq = activeflow["sequence"];
                |        // curIndex is the position of the current form in the active sequence
                |        var curIndex = activeSeq.indexOf(formName);
                |        if (curIndex < activeSeq.length - 1) {
                |            curIndex = curIndex+1;
                |            return activeSeq[curIndex];
                |        } else {
                |            return null;
                |        }
                |    }
                |
                |    // called by form "next" button
                |    \$scope._activateNextForm = function(formName, hideExistingForm) {
                |        // set hideExistingForm default to true
                |        hideExistingForm = (typeof hideExistingForm === 'undefined') ? true : hideExistingForm;
                |        var nextForm = \$scope._nextForm(formName);
                |
                |        if (hideExistingForm)
                |            \$scope[formName + "_visible"] = false;
                |
                |        if (nextForm != null)
                |            \$scope[nextForm + "_visible"] = true;
                |    }
                |
                |    // return the name of the first form of a flow
                |    \$scope._findFirstForm = function(flowName) {
                |        // find the flow definition by name
                |        var flow = \$scope._findFlow(flowName);
                |        // seq is an array of forms
                |        var seq = flow["sequence"];
                |        return seq[0];
                |    }
                |
                |    // called when a flow is activated
                |    \$scope._activateFlow = function(flowName) {
                |        // disable all existing forms
                |        angular.forEach($formSetVar, function(value, index) {
                |            // value is form name
                |            \$scope[value+"_visible"] = false;
                |        })
                |
                |        // set the active flow name
                |        $activeFlowVar = flowName;
                |
                |        // now enable the first form of the active flow
                |        var fname = \$scope._findFirstForm(flowName);
                |
                |        \$scope[fname + "_visible"] = true;
                |    }
                |    //activate the $activeFlowVar if it is set
                |    if ($activeFlowVar)
                |        \$scope._activateFlow($activeFlowVar);
                |""".stripMargin()
        }

        return code
    }


    def static buildControlVar(pageComponent, depth = 0) {
        def code = ""
         if ( [PageComponent.COMP_TYPE_BUTTON,PageComponent.COMP_TYPE_LIST, PageComponent.COMP_TYPE_GRID, PageComponent.COMP_TYPE_HTABLE, PageComponent.COMP_TYPE_LINK].contains( pageComponent.type ) ) {
            // generate a control function for each button/list/link/grid 'click' property
            if (pageComponent.onClick) {
                // handle variable and constant in expression
                def expr = compileCtrlFunction(pageComponent.onClick)
                // if we are dealing with clicking on an item from a list or table then pass the current selection to the control function
                def arg = "";
                if (pageComponent.type==PageComponent.COMP_TYPE_LIST || pageComponent.type==PageComponent.COMP_TYPE_GRID ||pageComponent.type==PageComponent.COMP_TYPE_HTABLE)
                    arg = PageComponent.CURRENT_ITEM

                code += """    \$scope.${pageComponent.name}_onClick = function($arg) { $expr}; \n"""
                println "onClick expression for $pageComponent.name $pageComponent.onClick -> $expr"
            }
         } else if (pageComponent.submit) {
             // parse submit action
             // do not need $scope. prefix or {{ }}
             pageComponent.submit = compileDOMExpression(pageComponent.submit)
             //TODO should there be a ! in next line?
             //I think yes(HvT) - because the onUpdate is put in the DataSet it doesn't need ng-change
         } else if (pageComponent.onUpdate && !dataSetIDsIncluded.contains(pageComponent.ID)) {
             // handle input field update
             // generate a ng-change function
             def  expr = compileCtrlFunction(pageComponent.onUpdate)
             def duplicateExpr =""
             if ((pageComponent.type == PageComponent.COMP_TYPE_SELECT || pageComponent.type == PageComponent.COMP_TYPE_RADIO)&&
                     (pageComponent.parent.type == PageComponent.COMP_TYPE_DETAIL
                      || pageComponent.parent.type == PageComponent.COMP_TYPE_GRID
                      || pageComponent.parent.type == PageComponent.COMP_TYPE_HTABLE) ) {
                 // if a select is used in a grid or detail control its model is bound to parent model, we need to copy
                 // the model to $<selectName> in order for it to be referenced by other controls
                 println "****WARNING**** using duplicate - is this still needed?"
                 duplicateExpr =
                     "//duplicate\n"+
                     "\$scope.$pageComponent.name = \$scope._${pageComponent.parent.model.toLowerCase()}s_${pageComponent.parent.name}[0].$pageComponent.model;"

             }

             // add onUpdate for grid/detail items
             if ( [pageComponent.COMP_TYPE_GRID,pageComponent.COMP_TYPE_HTABLE,pageComponent.COMP_TYPE_DETAIL].contains( pageComponent.parent.type ) )  {
                 def parentID = pageComponent.parent.ID
                 code += """
                       |    \$scope.${parentID}_${pageComponent.name}_onUpdate = function(current_item) {
                       |         $expr
                       |    };
                       |""".stripMargin()
             } else {
                 //next code is used for non-DataSet items with an onUpdate, i.e. an input field to set a filter
                 code += """
                       |    \$scope.${pageComponent.name}_onUpdate = function() {
                       |         $duplicateExpr
                       |         // handle undefined value
                       |         \$scope.${pageComponent.name} = nvl(\$scope.${pageComponent.name}, "");
                       |         $expr
                       |    };
                       |""".stripMargin()
             }
         }
        pageComponent.components.each { child ->
            code+= buildControlVar(child, depth+1)
        }
        return code
    }

    // Shorthand
    def static compileCtrlFunction(expression) {
        compileExpression(expression,ExpressionTarget.CtrlFunction)
    }

    def static compileDOMExpression(expression) {
        compileExpression(expression,ExpressionTarget.DOMExpression)
    }
    def static compileDOMDisplay(expression) {
        compileExpression(expression,ExpressionTarget.DOMDisplay)
    }

    // split an expression " L1 <bra>E1<ket>L2<bra>E1<ket>L3"
    // into an array of [ preBra: Li, expression, postKet: Lj ]
    // where bra = {{ AngularJS expression start
    //   and ket = }} AngularJS expression end
    def static splitAngularBrackets( String expr ) {
        def prep = expr.tokenize(exprBra)
        def parts = []
        prep.eachWithIndex { str, i ->
            def subParts = str.tokenize(exprKet)
            if (subParts.size()==1) {   // did not find exprKet or there is no part after it
                if (i==0 && !expr.startsWith(exprBra) )
                    parts += [preBra:subParts[0]]
                 else
                    parts += [expression:subParts[0]]
            }
            else
                parts += [expression:subParts[0], postKet:subParts[1]]
        }
        parts
    }


    // parse expression defined in page model for use in scope functions
    // $var --> $scope.var
    // $$var --> reserved page model variables
    // custom variable cannot start with '_'
    // var.property will be transformed to var_property
    // var --> constant -> not change

    // Context for parsing expressions
    // enum ExpressionTarget {CtrlFunction, DOMExpression, DOMDisplay}
    def static compileExpression(expression,
                                 ExpressionTarget target=ExpressionTarget.CtrlFunction,
                                 ArrayList dataSets=dataSetIDsIncluded,
                                 ArrayList resources=resourceIDsIncluded ) {

        final def dataSetReplacements = [
                [from:  ".\$populateSource"  , to:"DS.load"],
                [from:  ".\$load"            , to:"DS.load"],
                [from:  ".\$save"            , to:"DS.save"],
                [from:  ".\$get"             , to:"DS.get" ],
                [from:  ".\$currentRecord"   , to:"DS.currentRecord" ],
                [from:  ".\$selection"       , to:"DS.selectedRecords" ],
                [from:  ".\$data"            , to:"DS.data" ],
                [from:  ".\$dirty"           , to:"DS.dirty()" ]
        ]
        final def resourceReplacements = [
                [from:  ".\$post"            , to:".post"],
                [from:  ".\$put"             , to:".put"]
        ]
        final def pbProperties = ["visible", "style"]
        final def pbFunctions = ["eval"] //to be called with $$function

        def componentReplace = { result->
            dataSetReplacements.each {pattern ->
                dataSets.each { pcId ->
                    result=result.replace("\$${pcId}$pattern.from","\$${pcId}$pattern.to" )
                }
            }
            resourceReplacements.each { pattern ->
                resources.each { pcId ->
                    result=result.replace("\$${pcId}$pattern.from","\$${pcId}$pattern.to" )
                }
            }
            result
        }
        def expressionReplace = { result ->
            if (result) {
                result = componentReplace(result)
                //replace pb variables
                result = result.replaceAll('\\$\\$(\\w*)', '#scope._$1')
                //replace pb properties
                pbProperties.each {
                    def p = "\\\$([\\w\\.]*)\\.[\$]{1}$it"
                    result = result.replaceAll(p, "#scope.\$1_$it")
                }
                result = result.replaceAll(/([^\.]|^)\$([\w]*)/, '$1#scope.$2')
                //replace the underscore with $ for pbFunctions
                pbFunctions.each {
                    result=result.replace("#scope._$it","#scope.\$$it")
                }
                if (target == ExpressionTarget.CtrlFunction)
                    result = result.replace('#scope.', '$scope.')
                else {
                    result = result.replace('#scope.', '')
                }
            }
        }
        def literalReplace = { result ->
            result = componentReplace(result)
            result = result?.replaceAll('\\$([\\w\\.\\$]*)', '{{ $1 }}')       // grab the variable expressions and make it an angular expression
            result = result?.replaceAll('\\{\\{ \\$([\\w\\.\\$]*)', '{{ _$1')  // if the variable starts with a $ still it was $$ and it needs to start with _
            pbProperties.each {
                result = result?.replace(".\$$it","_$it")
            }
            result
        }

        def result = expression
        if (target in [ExpressionTarget.CtrlFunction, ExpressionTarget.DOMExpression] )
            result = expressionReplace(result)
        else {
            def parts = splitAngularBrackets(result)
            if (parts.size()==1 && !parts[0].expression) { // expression doesn't have {{... }}
                result = literalReplace(result) // do original literal processing
            } else {
                result="" // build the result from the parts
                parts.each { part ->
                    if (part.preBra)
                        result+=part.preBra
                    if (part.expression)
                        result+="{{${expressionReplace(part.expression)}}}"
                    if (part.postKet)
                        result+=part.postKet
                }
            }
        }
        result
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

        switch (pageComponent.type) {
            case PageComponent.COMP_TYPE_PAGE:
                pageComponent.parent = null
                pageComponent.root = pageComponent
                break
            case PageComponent.COMP_TYPE_RESOURCE:
                pageComponent.binding = pageComponent.resource? PageComponent.BINDING_REST : PageComponent.BINDING_PAGE
        }

        // parse parameters
        if (pageComponent.parameters) {
            def newParam = [:]
            pageComponent.parameters.each{key, value ->
                def v = compileCtrlFunction(value)
                newParam.put(key, v)
            }
            pageComponent.parameters = newParam
        }
        // parse source parameters
        if (pageComponent.sourceParameters) {
            def newParam = [:]
            pageComponent.sourceParameters.each{key, value ->
                def v = compileCtrlFunction(value)
                newParam.put(key, v)
            }
            pageComponent.sourceParameters = newParam
        }

        pageComponent.components?.eachWithIndex {it, index ->
            it.parent = pageComponent
            it.root = pageComponent.root
            it.modelOrigin=it.model
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
