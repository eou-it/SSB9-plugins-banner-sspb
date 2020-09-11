/*******************************************************************************
 * Copyright 2018-2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb

import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import net.hedtech.banner.css.Css
import net.hedtech.banner.exceptions.ApplicationException
import org.springframework.context.i18n.LocaleContextHolder

import java.util.regex.Pattern

@Slf4j
class PageComponent {

    final static def globalPropertiesName = "pageGlobal" //Properties file name with internal props in pages
    // Context for parsing expressions
    static enum ExpressionTarget{
        CtrlFunction,  // Controller function (need to use $scope)
        DOMExpression, // Expression in a place that will be evaluated by Angular (ng-change etc.)
        DOMDisplay     // Expression with AngularJS expression between curly braces
    }

    final static exprBra ="{{"    // start expression
    final static exprKet ="}}"    // end expression

    // Page Component Types
    final static COMP_TYPE_PAGE = "page"
    final static COMP_TYPE_FORM = "form"
    final static COMP_TYPE_GRID = "grid"
    final static COMP_TYPE_DATATABLE = "dataTable"
    final static COMP_TYPE_HTABLE = "htable"
    final static COMP_TYPE_SELECT = "select"
    final static COMP_TYPE_LIST = "list"
    final static COMP_TYPE_DETAIL = "detail"
    final static COMP_TYPE_DATA = "data"
    final static COMP_TYPE_RESOURCE = "resource"
    final static COMP_TYPE_LITERAL = "literal"  // pre-formatted text for display
    final static COMP_TYPE_DISPLAY = "display"  // read only data
    final static COMP_TYPE_TEXT = "text"
    final static COMP_TYPE_TEXTAREA = "textArea"   // TODO merge with text using a style hint
    final static COMP_TYPE_NUMBER = "number"
    final static COMP_TYPE_DATETIME = "datetime"
    final static COMP_TYPE_EMAIL = "email"
    final static COMP_TYPE_TEL = "tel"
    final static COMP_TYPE_LINK = "link"
    final static COMP_TYPE_BOOLEAN = "boolean"
    final static COMP_TYPE_SUBMIT = "submit"       // HTML5 uses this when enter key is pressed
    final static COMP_TYPE_BUTTON = "button"
    final static COMP_TYPE_BLOCK = "block"
    final static COMP_TYPE_FLOW = "flow"
    final static COMP_TYPE_RADIO = "radio"
    final static COMP_TYPE_HIDDEN = "hidden"
    //final static COMP_TYPE_FUNCTION = "function"
    //Added UXD components
    //final static COMP_TYPE_XE_TEXT_BOX = "xeTextBox"


    // Types that can represent a single field - should add RADIO
    final static COMP_ITEM_TYPES = [COMP_TYPE_LITERAL,COMP_TYPE_DISPLAY,COMP_TYPE_TEXT,COMP_TYPE_TEXTAREA,COMP_TYPE_NUMBER,
                                    COMP_TYPE_DATETIME,COMP_TYPE_EMAIL,COMP_TYPE_TEL,COMP_TYPE_LINK,COMP_TYPE_BOOLEAN]

    // Single field non-input types
    final static COMP_DISPLAY_TYPES =  [COMP_TYPE_LITERAL,COMP_TYPE_DISPLAY,COMP_TYPE_LINK,COMP_TYPE_HIDDEN]

    // Types that have a DataSet associated  - not completely orthogonal yet. COMP_ITEM_TYPES can have it too
    final static COMP_DATASET_TYPES = [COMP_TYPE_GRID,COMP_TYPE_HTABLE,COMP_TYPE_DATATABLE,COMP_TYPE_LIST,COMP_TYPE_SELECT,COMP_TYPE_DETAIL,COMP_TYPE_DATA,COMP_TYPE_RADIO]

    // Data Set types only for display
    final static COMP_DATASET_DISPLAY_TYPES = [COMP_TYPE_SELECT,COMP_TYPE_RADIO]

    // component type that is renderable
    final static COMP_VISUAL_TYPES = [COMP_TYPE_PAGE,COMP_TYPE_FORM, COMP_TYPE_BLOCK, COMP_TYPE_LITERAL,
            COMP_TYPE_DISPLAY,COMP_TYPE_TEXT,COMP_TYPE_TEXTAREA,COMP_TYPE_NUMBER,COMP_TYPE_BUTTON,
            COMP_TYPE_DATETIME,COMP_TYPE_EMAIL,COMP_TYPE_TEL,COMP_TYPE_LINK,COMP_TYPE_BOOLEAN,COMP_TYPE_SUBMIT,
            COMP_TYPE_GRID,COMP_TYPE_HTABLE,COMP_TYPE_LIST,COMP_TYPE_SELECT,COMP_TYPE_DETAIL,COMP_TYPE_RADIO]

    final static BINDING_REST = "rest"
    //final static BINDING_SQL = "sql"
    final static BINDING_PAGE = "page"
    //final static BINDING_API = "api"

    final static GRID_ITEM="item"
    final static LIST_ITEM="item"
    final static SELECT_ITEM="item"
    final static CURRENT_ITEM = "currentSelection"
    final static CONTROLLER_PLACEHOLDER="###CONTROLLER###"
    final static VAR_PRE = '$'  //page model variable prefix
    final static VAR_RES = '$$' // page model reserved variable prefix
    final static STYLE_ATTR = 'style' // component attribute name for use in JavaScript code

    final static NEXT_BUTTON_DEFAULT_LABEL = "Next"

    final static translatableAttributes = ["title","label","submitLabel","nextButtonLabel","placeholder","value","description",
                                           "newRecordLabel","deleteRecordLabel","saveDataLabel","refreshDataLabel"]

    //escape flags for translatable strings
    final static ESC_0 = 0 //no escape
    final static ESC_H = 1 //escape HTML
    final static ESC_JS = 2 //escape

    //default style sheet
    final static DEFAULT_STYLESHEET = "pbDefault"

    String type        // -> Generic property for component type
    String subType     // -> Use if component has multiple variants
    String name        // -> Generic property. Maybe use componentId?
    String title       // -> Page
    String scriptingLanguage = "JavaScript" // -> page property that specifies the scripting language to use in page model
    String importCSS   // specify what custom stylesheets to import, comma separated list of custom stylesheet names (without the .css extension )
    String label       // -> Items (inputs, display)
    String objectName

    String newRecordLabel        //Grid/HTable/Detail  label
    String deleteRecordLabel     //Grid/HTable/Detail  label
    String saveDataLabel         //Grid/HTable/Detail  label
    String refreshDataLabel      //Grid/HTable/Detail  label


    String placeholder // -> Input Items (is text shown when no input is provided)
    String model       // -> top level data model should have first letter in upper case, data declared in controller will be lower case
                        // in grid the model will point to the property name of the parent model
                        // in grid and detail component model is used for both retrieve and update to a single resource

    String modelRoot    // internal - represent the root class of a model - e.g Todo.description root class is Todo
    String modelComponent // internal - represent the component of a model after the root, excluding the leading ".". e.g Todo.description model component is description
    def parameters     // -> parameters for data query, for REST this is a Map of key:value, where key is the property name of the resource attribute, value can be
    // another data item name . This is attached to the component that uses the data, not the data definition itself

    String sourceModel       // for component with both input(pre-populate) and output like "select" use this specify the input model
    def sourceParameters    // used for query with sourceModel

    // flow control
    String sequence
    Boolean activated = false
    def nextButtonLabel=NEXT_BUTTON_DEFAULT_LABEL //"Next"    //translated in tranGlobalInit
    def submitLabel = ""
    //String condition

    // data
    String onLoad = ""
    String onError = ""
    String onSave = ""
    String onSaveSuccess = ""
    Boolean loadInitially = true   // specify if  data (query) from resource should be loaded after page is loaded

    def errorHandling = [:]       // a map of error code-> code block that specifies what actions to take if an error is encountered during resource operation
    String value                  // -> Item value, read only  for display type - also used to initialize an input type
    Boolean required=false
    Boolean readonly=false
    String onClick               // a code block to execute when a button is clicked
    String onFocus = ""          // executed when an input gets focus - see Angular ngFocus
    String onBlur = ""           // executed when an input looses focus - see Angular ngBlur

    // Show properties
    Boolean showInitially = true
    Boolean visible = showInitially

    // grid properties
    String submit      // -> Controller function to be called when submitting a form
    Boolean allowNew    = false // grid property - indicate a new entity entry and a submit button should be added to the table
    Boolean allowModify = false // grid property - indicate table can be modified and a submit button should be added
    Boolean allowDelete = false // grid property - indicate table entry can be deleted and a selection checkbox and a submit button should be added
    Boolean allowReload = false // grid property - add a reload button automatically to re-execute the query
    int pageSize = 5            // grid property - number of items to display per page

    // select properties
    String labelKey       // property name of the model to use for displaying in the drop down list
    String valueKey       // property name of the model to use for setting the selected value

    // boolean type properties
    String booleanTrueValue     // by default if these values are omitted JS will assume true and false (not quotes around) as they can be transferred in JSON as true/false
    String booleanFalseValue

    // display option
    def asHtml = false         // specify if the content should be render as innerHTML or text node

    // link property
    String description
    String url
    String imageUrl
    Boolean replaceView=true    // if set to false the rendering engine will attempt to open the link content in a new window/tab

    String style = ""       // styling support
    String labelStyle = ""
    String valueStyle = ""


    // data properties
    String resource    // -> Form Data Component. uri: <path>/resourceName e.g. rest/todo
    def staticData     // -> JSON Array [{"sex": "M", "descr": "Male"}, {"sex": "F", "descr": "FEMALE"}]   e.g. for static Select or Radio
    String binding = BINDING_REST    // internal method of data binding (rest, page). Implicitly derived from resource/staticData.

    def validation      // specify any validation definition as a map
    def fractionDigits  // specify how numbers are displayed
    String onUpdate     // code block to execute if the current component value is changed

    String ID           // normalized ID from name (space to _), do we need this? Better to forbid space in name.
    String documentation    // for page development documentation purpose, not used by code generator

    List<PageComponent> components    // child components

    // internal use properties
    PageComponent parent    // record parent component for special code generation in grid, etc.
    PageComponent root      // the root (page) component

    Map mergeInfo          //readOnly property to provide merge information to user
    List spareComponents   // used in merging - will contain unreferenced components

    // for compiler
    def resourceIDsIncluded = []   // record which resourceIDs are used in a page (only populate on root)
    def dataSetIDsIncluded  = []   // record which dataSets are used in a page (only populate on root)
    def resourceUsage = []        // resources and its usage (only populate on root)

    // New for compiler
    def meta = [
            pageResources: [:],        // Resources defined in this page, only set on root
            referencedBy: [],          // Components referencing this component (for type resource as of now)
            modelResource: null,       // Resource Component for model
            sourceModelResource: null, // Resource Component for sourceModel
            dataSetIDsIncluded: []     // record which dataSets are used in a page (only populate on root)
    ]


    def flowDefs = []       // global flow definitions, a list of map {flowName:"", sequence:["form1","form2"], condition, ""}
    def activeFlow = ""     // the initially activated flow
    def formSet = []        // the set of all form names on this page

    def rootProperties = [:]    // the key/value properties (set on page component)
    def globalProperties = [:]  // properties shared in pages

    def styleStr = ""
    //def labelStyleStr = ""
    //def valueStyleStr = ""

    def idTxtParam = ""
    def modelOrigin  //Save model here if  changed in normalization so we know item is not bound to a resource

    def content ="" //Added for template compile
    def templateName = "" //Added for template compile
    def pageURL


    // map the validation key to angular attributes ? use HTML 5 validation instead with form
    // def validationKeyMap = ["minlength":"ngMinlength", "maxlength":"ngMaxlength", "pattern":"ngPattern"]

    static boolean isDataSetEditControl ( PageComponent pc ){
        if (pc)
            [COMP_TYPE_DETAIL,COMP_TYPE_GRID,COMP_TYPE_HTABLE,COMP_TYPE_LIST].contains(pc?.type)
        else {
            false;
        }
    }

    boolean isDataSetEditControl() {
        isDataSetEditControl(this)
    }

    def propertiesBaseKey() {
        def nameList = []
        def pageComponent = this
        while (pageComponent) {
            nameList << pageComponent.name
            pageComponent = pageComponent.parent
        }
        nameList = nameList.reverse()

        return nameList.join(".")
    }

    def tranSourceValue(data) {
        def result = []
        def baseKey = propertiesBaseKey()
        data.each {
            def row = new HashMap(it) //Make sure to create a deep copy and not modify data in input
            def key ="${baseKey}.staticData.${it."$valueKey"}"
            def labelRoot=it."$labelKey"
            row."$labelKey"=tran(key,labelRoot,[] as List,ESC_JS)
            result << row
        }
        result
    }

    def tranMsg(key, List args=[], esc = ESC_H, q = "'") {
        def encodingFlag=""
        def argsString=""
        if (!args.empty)
            argsString=",args:$args"
        switch(esc) {
          //case ESC_H  : encodingFlag = ", encodeAs: 'HTML'"; break    // this is default so no need to specify
            case ESC_JS : encodingFlag = ",encodeAs:${q}JavaScript$q"; break
        }
        def result =  "message(code:$q$key$q$argsString$encodingFlag)"
        return "\${${result}}"
    }

    def tran(String prop, esc = ESC_H, q = "'" ) {
        def defTranslation = this[prop]
        if (defTranslation && translatableAttributes.contains(prop))  {
            defTranslation=compileDOMDisplay(defTranslation)
            def key ="${propertiesBaseKey()}.$prop"
            root.rootProperties[key] = defTranslation
            return tranMsg(key,[] as List, esc, q)
        }
        return ""
    }

    def tran(String key, String message, List args=[], esc = ESC_H) {
        root.rootProperties[key] = message
       tranMsg(key,args,esc)
    }


    def tranGlobal(String key, String text = null, List args = [], esc = ESC_H ) {
        key = "global.$key"
        if (text)
            root.globalProperties[key] = text
        else {
            text = root.globalProperties[key]
        }
        if (text) {
            return tranMsg(key,args,esc)
        }
        return ""
    }

    //Duck type determination if a string should be interpreted as a String and needs quoting in Angular
    static Boolean isStringType(text) {
        !text.isNumber() && !['true','false'].contains(text)
    }

    //If the value looks like a string quote it
    static String htmlValue(value, quote="'"){
        //If value is quoted already, remove quotes in order to make sure the right quotes are used
        if (  (value.startsWith("'") && value.endsWith("'")  ) || (value.startsWith("\"") && value.endsWith("\"")  ) ) {
            value = quote+value.substring(1,value.length()-1)+quote

        } else if (isStringType(value) && !value.startsWith(quote) && !value.endsWith(quote) ) {
            value = quote+value+quote
        }
        value
    }

    String defaultValue(acceptLiteral=true) {
        def result = ""
        if (value && model ) {
            def  expr = compileDOMExpression(value)
            if (acceptLiteral && value ==  expr) { //assume a literal value if not changed - not sure if we should do this
                expr=htmlValue(expr,"'")
            }
            def parentRef = isDataSetEditControl(parent)?"$GRID_ITEM":"this"
            result = "ng-init=\"setDefault($parentRef,'$model',$expr)\""
        }
        result
    }

    /*
   utility function to generate a unique ID for generated HTML tag
   the id is of the format: pbid-<component_name>-<component-type>-<additional tag>
   Additional tag is used to differentiate generated controls inside an component (such as new button for grid)
   id is used for styling purpose
    */

    def idAttribute(tag = "") {
        """id='pbid-$name${tag?tag:""}'"""
    }
    def idForAttribute(tag = "") {
        """for="pbid-$name${tag?tag:""}" """
    }

    def recordControlPanel()  {
        def button = "button"
        def dataSet    =  "${name}DS"
        def local = LocaleContextHolder.getLocale()
        def result
        if(local.getLanguage().equals("ar")){
             result = """|
                   |<!-- pagination -->
                   |<div class="pagination-container">
                   |    <div ${idAttribute('-pagination-container')} class="pagination-controls" ng-show='${dataSet}.totalCount > ${dataSet}.pagingOptions.pageSize'>
                   |        <$button ${idAttribute('-pagination-next-button')} class="secondary next" ng-disabled="${dataSet}.pagingOptions.currentPage >= ${dataSet}.totalCount/${dataSet}.pagingOptions.pageSize "
                   |            ng-click="${dataSet}.pagingOptions.currentPage=${dataSet}.pagingOptions.currentPage + 1" type="button">
                   |        </button>
                   |        <span ${idAttribute('-pagination-page-count')}>
                   |        {{${dataSet}.numberOfPages()}}/{{${dataSet}.pagingOptions.currentPage}}
                   |        </span>
                   |        
                   |      <$button ${idAttribute('-pagination-prev-button')} class="secondary previous" ng-disabled="${dataSet}.pagingOptions.currentPage == 1"
                   |            ng-click="${dataSet}.pagingOptions.currentPage=${dataSet}.pagingOptions.currentPage - 1" type="button">
                   |        </button>
                   |    </div>
                   |</div>
                   |""".stripMargin()
        }else{
             result = """|
                   |<!-- pagination -->
                   |<div class="pagination-container">
                   |    <div ${idAttribute('-pagination-container')} class="pagination-controls" ng-show='${dataSet}.totalCount > ${dataSet}.pagingOptions.pageSize'>
                   |        <$button ${idAttribute('-pagination-prev-button')} class="secondary previous" ng-disabled="${dataSet}.pagingOptions.currentPage == 1"
                   |            ng-click="${dataSet}.pagingOptions.currentPage=${dataSet}.pagingOptions.currentPage - 1" type="button" aria-label="Previous Page">
                   |        </button>
                   |        <span ${idAttribute('-pagination-page-count')}>
                   |        {{${dataSet}.pagingOptions.currentPage}}/{{${dataSet}.numberOfPages()}}
                   |        </span>
                   |        <$button ${idAttribute('-pagination-next-button')} class="secondary next" ng-disabled="${dataSet}.pagingOptions.currentPage >= ${dataSet}.totalCount/${dataSet}.pagingOptions.pageSize "
                   |            ng-click="${dataSet}.pagingOptions.currentPage=${dataSet}.pagingOptions.currentPage + 1" type="button" aria-label="Next Page">
                   |        </button>
                   |    </div>
                   |</div>
                   |""".stripMargin()
        }
        def changeData = ""
        def btnLabel
        if (allowNew) {
            btnLabel=newRecordLabel?tran("newRecordLabel"):tranGlobal("newRecord.label","Add New")
            changeData += """ <$button ${idAttribute('-new-button')} class="primary" ng-click="${dataSet}.add(${newRecordName()}())"  type="button"> $btnLabel </button>"""
        }
        if (allowModify || allowDelete) {
            btnLabel=saveDataLabel?tran("saveDataLabel"):tranGlobal("save.label","Save")
            changeData += """ <$button ${idAttribute('-save-button')} class="primary" ng-click="${dataSet}.save()" ng-disabled="!${dataSet}.dirty()"  type="button"> $btnLabel </button>"""
        }
        if (allowReload) {
            btnLabel=refreshDataLabel?tran("refreshDataLabel"):tranGlobal("refresh.label","Refresh")
            changeData += """ <$button ${idAttribute('-reload-button')} class="secondary" ng-click="${dataSet}.load({all:false,paging:true,clearCache:true})" type="button"> $btnLabel </button> """
        }
        if (changeData) {
            changeData = "<span ${idAttribute('-change-data-container')} class=\"pb-change-data-control\" > $changeData </span>"
        }
        result += changeData
        result = " <div ${idAttribute('-record-control-container')} class=\"pb-record-control\"> $result </div>"
        return result
    }

    String newRecordName() {
        return "pbNew_$name"
    }

    String initNewRecordJS() {
        def initialValues=""
        components.each { child ->
            if (child.value  && child.type != COMP_TYPE_LITERAL /*&& child.modelOrigin*/) {
                def  expr = compileCtrlFunction(child.value)
                // this is a bit horrible with HTML - can't really distinguish number from string literals
                if ([child.booleanFalseValue, child.booleanTrueValue].contains(child.value)&& isStringType(child.value) ) {
                    // use quotes
                    expr = htmlValue(expr, "\"")
                }
                initialValues+="${initialValues?",":""}${child.model}: $expr"
            }
        }
        initialValues="""\$scope.${newRecordName()}=function(){return {$initialValues};}"""
    }

    def gridControlPanel()  {
        def dataSet    =  "${name}DS"
        def result = ""
        def btnLabel
        if (allowNew) {
            btnLabel=newRecordLabel?tran("newRecordLabel",ESC_JS):tranGlobal("newRecord.label","Add New",[], ESC_JS)
            result += """ <button class="primary" $styleStr ng-click="${dataSet}.add(${newRecordName()}())" type="button"> $btnLabel  </button>"""
        }
        if (allowDelete) {
            btnLabel=deleteRecordLabel?tran("deleteRecordLabel",ESC_JS):tranGlobal("deleteRecord.label","Delete selected",[], ESC_JS)
            result += """ <button class="secondary" $styleStr ng-click="${dataSet}.deleteRecords(${dataSet}.selectedRecords)" ng-disabled="${dataSet}.selectedRecords.length==0" type="button"> $btnLabel  </button>"""
        }
        if (allowModify || allowDelete) {
            btnLabel=saveDataLabel?tran("saveDataLabel",ESC_JS):tranGlobal("save.label","Save",[], ESC_JS)
            result += """ <button class="primary" $styleStr ng-click="${dataSet}.save()" ng-disabled="!${dataSet}.dirty()" type="button"> $btnLabel </button>"""
        }
        if (allowReload) {
            btnLabel=refreshDataLabel?tran("refreshDataLabel",ESC_JS):tranGlobal("refresh.label","Refresh",[], ESC_JS)
            result += """ <button class="secondary" $styleStr ng-click="${dataSet}.load({all:false,paging:true,clearCache:true})" type="button"> $btnLabel </button> """
        }
        // alas, but cannot dynamically toggle multiSelect property of grid
        //result += "<input type=\"checkbox\" ng-model=\"${name}Grid.multiSelect\">Select multiple</input>"
        return result
    }



    String gridJS() {
        def dataSet    =  "${name}DS"
        def items =""

        // generate all table columns from the data model
        components.each { child ->
            child.parent = this
            if (items.length()>0)
                items+=",\n"
            def optional =  (child.type==COMP_TYPE_HIDDEN)? ",visible: false":""
            //Only allow sorting if model is originally set
            if (!child.modelOrigin)// needs to be a column in the api to be sortable on server
                optional+=",sortable: false"
            items+="""
                   { field: '${child.model}', displayName: '${child.tran("label",ESC_JS)}',
                     cellTemplate: '${child.gridChildHTML()}'
                     $optional}"""
        }

        def code = """
        var ${name}GridControlPanel = '${gridControlPanel()}';
        \$scope.${name}Grid = {
            columnDefs: [
            $items
            ],
            data: '${dataSet}.data',
            enableCellSelection: true,
            enableColumnResize: true,
            enablePaging: true,
            footerTemplate: \$templateCache.get('gridFooter.html').replace('#gridControlPanel#',${name}GridControlPanel).replace('#gridName#','${name}'),
            footerRowHeight: 55,
            jqueryUIDraggable:true,
            multiSelect:false,
            showSelectionCheckbox:false,
            selectWithCheckboxOnly:false,
            pagingOptions: \$scope.${dataSet}.pagingOptions,
            selectedItems: \$scope.${dataSet}.selectedRecords,
            showColumnMenu: true,
            showFilter:true,
            showFooter: true,
            sortInfo: \$scope.${dataSet}.sortInfo,
            totalServerItems: '${dataSet}.totalCount',
            useExternalSorting: true,
            i18n: gridLocale
        };
        ${dataSetWatches()}
        """
        return code
    }

    String dataSetWatches() {
        def dataSet = "${name}DS"
        def code = """
        \$scope.\$watch('${dataSet}.pagingOptions', function(newVal, oldVal) {
            if (newVal !== oldVal) {
                if( newVal.currentPage && oldVal.currentPage ) {
                    \$scope.${dataSet}.load({all:false,paging:true});
                }
            }
        }, true);
        \$scope.\$watch('${dataSet}.sortInfo', function(newVal, oldVal) {
            if ( (newVal.fields.join(',') !== oldVal.fields.join(','))||(newVal.directions.join(',') !== oldVal.directions.join(',')) ) {
                \$scope.${dataSet}.load({all:false,paging:true});
            }
        }, true);
        """

        //currentRecord is also set with a click handler. Cannot remove below setting because click does not capture keyboard
        //navigation
        // As a work around until we change the model, we fire the onClick event handler if it contains "selectionChanged"
        def onClickCode=onClick && onClick.contains("\"selectionChanged\"")?"\$scope.${name}_onClick(newVal, \"selectionChanged\");":""
        //Todo: implement onSelectionChanged event handler

         code+=
            """\$scope.\$watch('${dataSet}.selectedRecords[0]', function(newVal, oldVal) {
                if (newVal !== oldVal ) {
                    \$scope.${dataSet}.setCurrentRecord(newVal);
                     $onClickCode
                }
            });
            """
        code
    }

    //this returns a html template string as a javascript string - escape strings
    String gridChildHTML( int depth=0) {
        def templateResult = compileComponentTemplate(depth, ESC_JS)
        if ( templateResult.compiled) {
            return templateResult.code  //OK, supported by a template, return the result
        }
        // No supported by a template, go with the old method
        def ro= readonly || COMP_DISPLAY_TYPES.contains(type)
        def tagStart="<input"
        def tagEnd="/>"
        def nameAt="name=\"$name\""
        def typeAt="type=\"$type\""
        def styleAt="class=\"pb-${parent.type} pb-$type ${valueStyle?valueStyle:""}\" ng-class=\"${name}_$STYLE_ATTR\" style=\"background-color:transparent; border:0; width: 100%; height:{{rowHeight}}px\""
        def specialAt=""
        def readonlyAt = (parent.allowModify && !ro)?"":"readonly"
        def requiredAt = required?"required":""
        def validateAt = ""
        def placeholderAt=""
        def ngModel="ng-model=\"COL_FIELD\""    // shorthand for  row.entity[col.field]
        def ngChange=ro?"":"ng-change=\""+(onUpdate?"\$parent.${parent.ID}_${name}_onUpdate(row.entity);":"")+"\$parent.${parent.name}DS.setModified(row.entity)\""
        def onClickCode=parent.onClick?"\$parent.${parent.name}_onClick(row.entity, col);":""
        //Do not remove setCurrentRecord without checking all is good (may be done 2x but need to make sure it is before onClickCode)
        def ngClick="""ng-click="\$parent.${parent.name}DS.setCurrentRecord(row.entity);$onClickCode" """
        def ariaLabel = "aria-label=\"COL_FIELD\""
        def role = ""
        def typeInternal = type
        if (type == COMP_TYPE_NUMBER ) {
            //angular-ui doesn't use localized validators - use own (but rather limited still)
            typeAt="""type="text" pb-number ${fractionDigits?"fraction-digits=\"$fractionDigits\"":""} """
        }
        else if (type == COMP_TYPE_DATETIME ) {
            if (readonly)
                typeInternal=COMP_TYPE_DISPLAY
            typeAt="ui-date=\"{ changeMonth: true, changeYear: true}\" "
            //Assume format comes from jquery.ui.datepicker-<locale>.js
            //Cannot choose format with time, but lots of options. See http://jqueryui.com/datepicker/
        }
        switch (typeInternal) {
            case COMP_TYPE_SELECT:
                // SELECT must have a model
                def arrayName = "${name}DS.data"
                readonlyAt = (parent.allowModify && !ro)?"":"disabled" //select doesn't have readonly
                ngChange="ng-change=\""+(onUpdate?"${name}DS.onUpdate(row.entity);":"")+"\$parent.${parent.name}DS.setModified(row.entity);${name}DS.setCurrentRecord(row.entity.$model);\""
                placeholderAt = placeholder?"""<option value="" role="menuitem">${tran("placeholder")}</option>""":""
                return """<select ${idForAttribute(idTxtParam+"-label")} role="menu" $ariaLabel ${styleAt} $ngModel $readonlyAt $ngChange $ngClick ng-options="$SELECT_ITEM.$valueKey as $SELECT_ITEM.$labelKey for $SELECT_ITEM in $arrayName"> $placeholderAt </select>"""
            case [COMP_TYPE_TEXT, COMP_TYPE_TEXTAREA,COMP_TYPE_NUMBER, COMP_TYPE_DATETIME, COMP_TYPE_EMAIL, COMP_TYPE_TEL] :
                validateAt = validationAttributes()
                placeholderAt=placeholder?"placeholder=\"${tran("placeholder")}\"":""
                break
            case COMP_TYPE_BOOLEAN:
                typeAt = "type=\"checkbox\" tabindex=\"0\""
                styleAt="style=\"background-color:transparent; border:0; width: 30%; height:{{rowHeight}}px\""
                specialAt ="""${booleanTrueValue?"ng-true-value=\"${htmlValue(booleanTrueValue,"\\\'")}\"":""} ${booleanFalseValue?"ng-false-value=\"${htmlValue(booleanFalseValue,"\\\'")}\"":""}  """
                role= "role=checkbox"
                ariaLabel = "aria-label=\"${tran("label",ESC_JS)}\""
                break
            case COMP_TYPE_DISPLAY:
                typeAt=""
                if (asHtml) {
                    tagStart="<span"
                    tagEnd="></span>"
                    ngModel="ng-bind-html=\"COL_FIELD | to_trusted\""
                }
                if (type==COMP_TYPE_DATETIME) {
                    ngModel="value=\"{{COL_FIELD|date:\\'medium\\'}}\""
                }
                break
            case COMP_TYPE_LITERAL:
                return "<span $styleAt $ngClick>" + tran(propertiesBaseKey()+".value",compileDOMDisplay(value).replaceAll("item.","row.entity.") ) + "</span>"
                break
            default :
                log.info "***No ng-grid html edit template for $type ${name?name:model}"
        }
        def result = "$tagStart $nameAt $typeAt $styleAt $specialAt $readonlyAt $requiredAt $validateAt $placeholderAt $role $ariaLabel" +
                     " $ngModel $ngChange $ngClick $tagEnd"
        return result
    }


    /*
    Special compilation for generating table specific controls
     */
    def htableCompile(int depth=0) {

        // Old code with HTML Table grid -- leave for now as some parts are not implemented yet in new Grid
        def dataSet    =  "${name}DS"
        def repeat = "$GRID_ITEM in ${dataSet}.data"
        //implement as table for now
        // generate table column headers
        def thead=""
        def items=""
        // Set label as header
        def heading = ""
        def MAX_HEADING = 6
        if (label) {
            def headingLevel = (depth < MAX_HEADING-1)? depth+1: MAX_HEADING
            heading = """<h$headingLevel class="pb-htable-label" ${idAttribute("-label")}>${tran("label")}</h$headingLevel>"""
        }
        idTxtParam="-{{\$index}}"
        // add a delete checkbox column if allowDelete is true
        if (allowDelete) {
            def deleteLabel=deleteRecordLabel?tran("deleteRecordLabel"):tranGlobal("delete.label","Delete")
            thead = "<th ${idAttribute('delete-column-header')}>$deleteLabel</th>"
            items = """
                  |<td ${idAttribute('delete-column-data'+idTxtParam)} role="gridcell">
                  |<input ${idAttribute('delete-column-checkbox'+idTxtParam)} ng-click="${dataSet}.deleteRecords($GRID_ITEM)" type="checkbox" aria-label="Delete Record"/>
                  |</td>
                  |""".stripMargin()
        }
        // generate all table columns from the data model
        components.each { child ->
            child.parent = this
            if (child.type == COMP_TYPE_HIDDEN) {
                //column not displayed - have to use th/td to keep IE8 happy
                thead+="<th style=\"display:none;\"></th>"
                //get the child components
                child.label=""
                items+="<td style=\"display:none;\"> ${child.compileComponent("", depth)} </td>\n"
            }   else {
                def labelStyleStr=child.labelStyle?"class=\"$child.labelStyle\"":""
                //get the labels from child components
                thead+="<th ${idAttribute('data-header-'+child.name)} $labelStyleStr role=\"columnheader\">${child.tran("label")}</th>"
                //get the child components
                child.label=""
                items+="<td ${idAttribute('-td-' + child.name + idTxtParam )} role=\"gridcell\">${child.compileComponent("", depth)}</td>\n"
            }
        }
        def click_txt=""
        if (onClick)
            click_txt = "ng-click=${name}_onClick($GRID_ITEM)"

        def result =  """
                   |  <table ${idAttribute()} $styleStr role="grid" aria-labelledby="pbid-$name-label">
                   |    <caption>$heading</caption>
                   |    <thead ${idAttribute('-th')} role="rowgroup"><tr ${idAttribute('-thr')} role="row">$thead</tr></thead>
                   |    <tbody ${idAttribute('-tb')} role="rowgroup">
                   |      <!-- Do this for every object in objects -->
                   |      <tr ${idAttribute('-tr'+idTxtParam)}  ng-repeat="$repeat" $click_txt role="row">
                   |        $items
                   |      </tr>
                   |    </tbody>
                   |  </table>
                   |""".stripMargin()
        result +=  recordControlPanel()

        return result
    }




    def detailCompile(int depth=0) {
        def dataSet = "${name}DS"
        def repeat = "$GRID_ITEM in ${dataSet}.data"    //GRID_ITEM is confusing
        def result = """<div $styleStr ${idAttribute()} class="pb-$type-container">"""
        idTxtParam="-{{\$index}}"

        if (label)
            result += "<label class=\"pb-$type pb-label\" ${idAttribute('label')} tabindex=\"0\">${tran("label")}</label>\n"
        result +="""<div ${idAttribute("container" + idTxtParam)} class="pb-$type-record" ng-repeat="$repeat">\n"""


        // generate all table columns from the data model
        components.each { child ->
            //get the child components
            result+="${child.compileComponent("", depth)}\n"
        }
        if (allowDelete) {
            def idTag="delete-checkbox" + idTxtParam
            result += """
                    |<div style="text-align:right" ${idAttribute("delete-container" + idTxtParam)}>
                    |    <input ${idAttribute(idTag)} ng-click="${dataSet}.deleteRecords($GRID_ITEM)" type="checkbox" aria-label="Delete"/>
                    |<label style="text-align:left" ${idAttribute("delete-label" + idTxtParam)} ${idForAttribute(idTag)}> <strong>${tranGlobal("delete.label","Delete")}</strong></label>
                    |</div>
                    |""".stripMargin()
        }
        result+= "</div>\n"
        result+= recordControlPanel()
        result += "</div>"
        return result
    }

    def dataTableJS() {
        def dataSet = "${name}DS"
        def columns = ""
        def draggableColumns = ""
        components.eachWithIndex { child, idx ->
            //get the child components
            def lbl = child.tran("label", ESC_JS)
            columns += """,{position: {desktop: $idx, mobile: $idx}, name: "$child.model", title: "$lbl", options: {visible: true, sortable:true}}\n"""
            draggableColumns += ",'$child.model'"
        }
        draggableColumns = "[${draggableColumns.substring(1)}]"
        columns = columns.substring(1)
        def code = """|    \$scope.${name}Config = {
                      |        columns: [$columns],
                      |        onDoubleClick: function(data,index) {
                      |                    console.log("data-->" , data,index);
                      |                },
                      |        onBtnClick: function(data, index) {
                      |                    console.log(data, index);
                      |                },
                      |        refreshContent: function() {
                      |                    console.log('Refreshing grid data...');
                      |                },
                      |        fetch: function(query) {
                      |                 var deferred = \$q.defer();
                      |                 var paging=false;
                      |                 if (\$scope.${name}DS.pagingOptions.pageSize!=query.pageSize) {
                      |                     \$scope.${name}DS.pagingOptions.pageSize = query.pageSize;
                      |                     paging=true;
                      |                 }
                      |                 if (\$scope.${name}DS.pagingOptions.currentPage!=query.onPage) {
                      |                     \$scope.${name}DS.pagingOptions.currentPage = query.onPage;
                      |                     paging=true;
                      |                 }
                      |                 if (paging) {
                      |                   \$scope.${name}DS.load({paging:paging});
                      |                 }
                      |                 setTimeout(function() {
                      |                   deferred.resolve({result:\$scope.${name}DS.data,
                      |                                     length:\$scope.${name}DS.totalCount});
                      |                   //deferred.reject(data);
                      |                 }, 100);
                      |                 console.log(query);
                      |                 return deferred.promise;
                      |                },
                      |        postFetch: function(response, oldResult) {
                      |                    console.log('Post fetch handler', response);
                      |                },
                      |        draggableColumnNames: $draggableColumns,
                      |        //mobile: { term: 2,  crn: 2},
                      |        search: {
                      |                    id: '${name}Search',
                      |                    title: 'Search (Alt+Y)',
                      |                    ariaLabel: 'Search text field. Short cut is Alt+Y, Search for any text',
                      |                    delay: 300,
                      |                    //searchString : 201410,
                      |                    maxlength: 200,
                      |                    minimumCharacters : 2
                      |                },
                      |        pagination: {
                      |                    pageLengths : [ 5, 10, 25, 50, 100],
                      |                    offset : \$scope.${name}DS.pagingOptions.pageSize,
                      |                    recordsFoundLabel : "Results found",
                      |                    pageTitle: "Go To Page (End)",
                      |                    pageLabel: "Page",
                      |                    pageAriaLabel: "Go To Page. Short cut is End",
                      |                    ofLabel: "of",
                      |                    perPageLabel: "Per Page"
                      |                }
                      |    };
                      |""".stripMargin('|')

        code;
    }

    def dataTableCompile(int depth=0){
        def dataSet = "${name}DS"
        def children = ""
        // generate all table columns from the data model
        components.each { child ->
            //get the child components
            //children += """<xe-cell-markup column-name="${child.name}"> ${child.compileComponent("", depth)} </xe-cell-markup>\n"""
        }
        def cfg = "${name}Config"

        def html = """
            |<div $styleStr ${idAttribute()} class="pb-$type-container">
            |<xe-table-grid
            |  table-id="dataTable-${name}"
            |  caption="${tran("label")}"
            |  header="${cfg}.columns"
            |  content="${dataSet}.data"
            |  toolbar="true" paginate="true"
            |  fetch="${cfg}.fetch(query)"
            |  continuous-scrolling="false"
            |  pagination-config="${cfg}.pagination"
            |  on-row-double-click="${cfg}.onDoubleClick(data,index)"
            |  draggable-column-names="${cfg}.draggableColumnNames"
            |  no-data-msg="" empty-table-msg=""
            |  search-config="${cfg}.search"
            |  height="16em"
            |  refresh-content="${cfg}.refreshContent">
            |$children
            |</xe-table-grid>
            |</div>
        """.stripMargin('|')
        html
        //|  pagination-config="${cfg}.pagination"
        //|  fetch="${cfg}.fetch(query)"
        //|  post-fetch="${cfg}.postFetch(response, oldResult)"
        //|  mobile-layout="${cfg}.mobile"

//                |  <xe-cell-markup heading-name="tick">
//                |      <xe-checkbox xe-id="selectionAll" xe-label="Select All Rows" xe-label-hidden="true" xe-value="all" xe-model="model.allRowsSelected" xe-on-click="selectAll()">
//                |  </xe-checkbox></xe-cell-markup>
//                |  <xe-cell-markup column-name="tick">
//                |      <xe-checkbox xe-id="selection- + {{row.term}}" xe-label="Term Code {{row.term}}" xe-label-hidden="true" xe-value="{{row.term}}" xe-model="row.isChecked" xe-on-click="checkBoxChecked(row)"></xe-checkbox>
//                |  </xe-cell-markup>
//                |  <xe-cell-markup column-name="subject">
//                |      <xe-simple-text-box xe-value="row.subject" value="row.subject" on-change="updateInput(inputField)" disabled="editableInput" on-focus="focus()" on-blur="focus()"></xe-simple-text-box>
//                |  </xe-cell-markup>
//                |  <xe-cell-markup column-name="status">
//                |      <xe-status-label colun-name="status" xe-label="{{row.status.label}}" xe-type="{{row.status.type}}"></xe-status-label>
//                |      <!--<xe-button xe-type= "secondary" xe-label="Delete" xe-btn-click="onBtnClick(row,index)" xe-disabled="false"></xe-button><br></br>-->
//                |  </xe-cell-markup>

    }

    /*
    Special compilation for generating table specific controls
     */
    def listCompile(int ignoreDepth=0) {
        def dataSet = "${name}DS"
        def txt = "<div ${idAttribute()} $styleStr>"
        def repeat = "$LIST_ITEM in ${dataSet}.data"
        idTxtParam = "-{{\$index}}"
        if (label)
            txt += """<label class="pb-${type}-label  $labelStyle" ${idAttribute('label')} >${tran("label")}</label>"""
        // handle click event
        def click_txt=""
        if (onClick)
            click_txt = "ng-click=${name}_onClick($LIST_ITEM)"
        txt +=
            """<ul ${idAttribute('-ul')} class = "pb-ul">
            <li ${idAttribute("-li" + idTxtParam)} class="pb-list-item" $click_txt ng-repeat="$repeat">
             ${onClick?"<a ${idAttribute('-a'+ idTxtParam)} href=\"\" aria-label=\"${LIST_ITEM.value}\">":""} {{$LIST_ITEM.$value}}  ${onClick?"</a>":""}
            </li>
            </ul>
            """
        txt += recordControlPanel() +  "</div>"
        return txt
    }

    String compileItem(String t, int ignoreDepth=0){
        def autoStyleStr //generate class attributes from model
        // handle ID generation for items in a dataset
        // append -$index to each rendered items
        if(isDataSetEditControl(parent) || isDataSetEditControl(this))
            idTxtParam = "-{{\$index}}"
        if (t != COMP_TYPE_HIDDEN) {
            autoStyleStr = """class="pb-${parent.type} pb-$type pb-item $valueStyle"  """
        }
        def tranLabel = ( !(label) || parent.type == COMP_TYPE_HTABLE)?"&#x2007;&#x2007;":tran("label")
        def tindex
        if([COMP_TYPE_BOOLEAN].contains(t)){
            tindex = "tabindex=\"0\" onkeypress=\"clickEvent(this)\" role=\"checkbox\"";
        }
        def labelTxt = "<label class=\"pb-${parent.type} pb-$type pb-item pb-label $labelStyle \" ${idAttribute("-label"+idTxtParam)} $tindex ${idForAttribute(idTxtParam)} /*aria-labelledby=\"pbid-$name\"*/>$tranLabel</label>"

        //Use empty labels for radio and boolean to support xeStyle
        labelTxt = ( !label && ![COMP_TYPE_RADIO,COMP_TYPE_BOOLEAN].contains(t))?"":labelTxt

        def result=""
        def ngClick=""
        def ngChange=""
        // if item can be updated (TODO: check readonly)
        if ( !COMP_DISPLAY_TYPES.contains(type) && t!=COMP_TYPE_SELECT ) {
            if (isDataSetEditControl(parent)) {
                if (onUpdate)  {
                    ngChange="\$parent.${parent.ID}_${name}_onUpdate($GRID_ITEM);"  //
                }
                ngChange="""ng-change="$ngChange\$parent.${parent.name}DS.setModified($GRID_ITEM)"  """
                ngClick="""ng-click="\$parent.${parent.name}DS.setCurrentRecord($GRID_ITEM)" """
            } else {
                if (onUpdate)  {
                    ngChange="""ng-change="${name}_onUpdate()"  """
                }
            }
        }
        def typeInternal=t
        if (t == COMP_TYPE_DATETIME && readonly) // need to render as display item as date picker isn't disabled.
                typeInternal=COMP_TYPE_DISPLAY
    
        switch (typeInternal) {
            case COMP_TYPE_SELECT:
                // SELECT must have a model
                def arrayName = "${name}DS.data"
                def ngModel = name
                def placeholderStr = placeholder?"""<option value="">${tran("placeholder")}</option>""":""" <option style="display:none" value=""></option> """
                ngChange = "" //override default
                if(isDataSetEditControl(parent)) {
                    ngModel =  "$GRID_ITEM.${model}"
                    ngChange +="\$parent.${parent.name}DS.setModified($GRID_ITEM); ${name}DS.setCurrentRecord($ngModel);"
                } else {
                    ngChange += "${name}DS.setCurrentRecord($name);"
                }
                ngChange += onUpdate?"${name}DS.onUpdate(item);":""
                ngChange = ngChange?"ng-change=\"$ngChange\"":""
                result = """
                           |<select ${required?"required":""}  ${idAttribute(idTxtParam)} $autoStyleStr ng-model="$ngModel" $ngChange  ${defaultValue()} 
                           |  ng-options="$SELECT_ITEM.$valueKey as $SELECT_ITEM.$labelKey for $SELECT_ITEM in $arrayName"> 
                           |  $placeholderStr
                           |</select>""".stripMargin()

                break;
            case COMP_TYPE_RADIO:
                def arrayName = "${name}DS.data"
                def ngModel = name
                // TODO handle parent GRID/DETAIL ID generation
                def initTxt = value?"""ng-init="\$parent.$ngModel='$value'" """:""
                def nameTxt = name

                ngChange = "" //override default
                def idxStr
                if(parent.type == COMP_TYPE_DETAIL || parent.type == COMP_TYPE_HTABLE) {
                    ngModel =  "\$parent.$GRID_ITEM.${model}"
                    ngChange +="\$parent.\$parent.${parent.name}DS.setModified(\$parent.$GRID_ITEM);"
                    nameTxt = "{{'${parent.name}_${name}' + \$parent.\$index}}"
                    idxStr= "{{\$parent.\$index}}-{{\$index}}" //include parent index to make id's unique
                } else {
                    ngModel =  "\$parent.$ngModel"
                    idxStr= "{{\$index}}"
                }
                ngChange += onUpdate?"${name}DS.onUpdate(item);":""
                ngChange = ngChange?"ng-change=\"$ngChange\"":""
                def radioLabelStyleStr= """class="pb-${parent.type} pb-item pb-radiolabel $labelStyle" """

                result = """
                  |<div class="$valueStyle" ng-repeat="$SELECT_ITEM in $arrayName" $initTxt role="radiogroup">
                  |    <label ${idAttribute("-label-$idxStr")} ${idForAttribute("-radio-$idxStr")} $radioLabelStyleStr>
                  |    <input ${required?"required":""} ${idAttribute("-radio-$idxStr")} type="radio" number-to-string ng-model=$ngModel name="$nameTxt" $ngChange value="{{$SELECT_ITEM.$valueKey}}" aria-checked="false"/>
                  |    <span tabindex="0" onkeypress="clickEvent(this)">{{$SELECT_ITEM.$labelKey}}</span></label>
                  |</div>""".stripMargin()
                result = """<div ${idAttribute(idTxtParam)} $autoStyleStr> $result </div>"""
                break;
            case COMP_TYPE_LITERAL:
                //Todo: should we do something for safe/unsafe binding as in next item type?
                result = "<span ${idAttribute(idTxtParam)}  $ngClick $autoStyleStr>" + tran(propertiesBaseKey()+".value",compileDOMDisplay(value) ) + "</span>\n"
                break;
            case COMP_TYPE_DISPLAY: //migrated to use template engine
                if (type != COMP_TYPE_DATETIME) {
                    return "<span>*** ERROR This code should now be handled in template. Item=$name ***</span>"
                }
                def modelTxt_unsafe = ""
                def modelTxt_safe = ""

                if ( [COMP_TYPE_HTABLE, COMP_TYPE_DETAIL].contains(parent.type)) {
                    if (type == COMP_TYPE_DATETIME) {
                        modelTxt_safe = "{{ $GRID_ITEM.${model}|date:'medium' }}"
                    } else if (asHtml) {
                        modelTxt_unsafe = "ng-bind-html='$GRID_ITEM.$model | to_trusted' "
                    } else {
                        modelTxt_safe = "{{ $GRID_ITEM.${ model } }}"
                    }
                } else {
                    if (model) {
                        modelTxt_safe = "{{ ${model}|date:'medium' }}"
                        //modelTxt_unsafe = "ng-bind-html=\"${compileDOMExpression(value)} | date:'medium'\" "
                    } else {
                        return "<span>*** ERROR model is expected to be populated Item=$name ***</span>"
                    }
                }
                result = """<span ${idAttribute(idTxtParam)} $ngClick ${defaultValue(false)} $autoStyleStr $modelTxt_unsafe> $modelTxt_safe </span>""";
                break;
        // TODO handle value in details for display
        // TODO consolidate value and sourceModel?
        // TODO is parseVariable still working after using DataSet as generic data object?
            case COMP_TYPE_LINK:
                def desc = description?tran("description"):url
                def clickStr = onClick?"""ng-click="${name}_onClick()" """:""
                // handle open link in new window attr
                def targetStr =''
                if (!replaceView)
                    targetStr = 'target="_blank"'
                // set url to empty string if it is null, otherwise the page is re-directed to a non-existing page
                url = (url==null)?"":url
                result =  """
                          |<a ${idAttribute(idTxtParam)} ng-href="${compileDOMDisplay(url)}" $targetStr $clickStr tabindex="0">
                          |<span $autoStyleStr> $desc </span></a>
                          |""".stripMargin()
                break;
            case COMP_TYPE_TEXT:
            case COMP_TYPE_TEXTAREA:
            case COMP_TYPE_NUMBER:
            case COMP_TYPE_DATETIME:
            case COMP_TYPE_EMAIL:
            case COMP_TYPE_TEL:
            case COMP_TYPE_HIDDEN:
                def tag = "input"
                def endTag = "/>"
                def attributes = "${validationAttributes()} ${required?"required":""} ${placeholder?"placeholder=\"${tran("placeholder")}\"":""} ${labelTxt?"":"aria-label=\"${tran("placeholder")}\""}".trim()
                def typeString= "type=\"$t\""
                if (type == COMP_TYPE_NUMBER) {  // angular-ui doesn't use localized validators
                    typeString="""type="text" pb-number ${fractionDigits?"fraction-digits=\"$fractionDigits\"":""} """
                }

                if (type == COMP_TYPE_DATETIME)  { //Assume format comes from jquery.ui.datepicker-<locale>.js
                    typeString=" ui-date=\"{ changeMonth: true, changeYear: true}\" "
                }
                if (type == COMP_TYPE_TEXTAREA) {
                    tag = COMP_TYPE_TEXTAREA
                    endTag = "${required?"required":""} "+"></$COMP_TYPE_TEXTAREA>"
                }

                // for datetime input do NOT assign an ID otherwise it won't work!
                def inputIdStr = (type==COMP_TYPE_DATETIME)?"":idAttribute(idTxtParam)
                //Cannot choose format with time, but lots of options. See http://jqueryui.com/datepicker/
                if (isDataSetEditControl(parent)) {
                    //ngChange moved to common part
                    //defaulValue() removed, now should be handled by initNewRecordJS() call in compileService.
                    result = """|<$tag $inputIdStr $typeString $autoStyleStr  name="${name?name:model}" ${parent.allowModify && !readonly?"":"readonly"}
                                | ng-model="$GRID_ITEM.${model}"
                                | $ngChange $attributes $ngClick $endTag
                                |""".stripMargin()
                } else {
                    // TODO do we need a value field if ng-model is defined?  //added defaultValue
                    attributes += " ${readonly?"readonly":""}"
                    result = """|<$tag $inputIdStr $typeString $autoStyleStr  name="${name?name:model}"
                                |${defaultValue()} $ngChange $attributes
                                |""".stripMargin()
                    if (model && !readonly) {
                        if (binding != BINDING_PAGE) // use DataSet current record
                            result+="ng-model=\"${ID}DS.currentRecord"
                        else {// there may be a value instead of a model
                            result += """ng-model="$modelRoot"""
                        }
                        if (modelComponent)
                            result+=".$modelComponent"
                        result+='" '
                    }
                    result+="$endTag\n"
                }
                break;
            case COMP_TYPE_BOOLEAN:
                result ="""<input ${idAttribute(idTxtParam)} $autoStyleStr  type="checkbox" tabindex="0" name="${name?name:model}"
                           ${booleanTrueValue?"ng-true-value=\"${htmlValue(booleanTrueValue,"'")}\"":""} ${booleanFalseValue?"ng-false-value=\"${htmlValue(booleanFalseValue,"'")}\"":""}
                           $ngChange $ngClick
                           """
                // add change event handler for items in DataSet so the item can be marked dirty for save
                if (isDataSetEditControl(parent)) {
                    result+= """ ${(parent.allowModify && !readonly)?"":"readonly"} ng-model="$GRID_ITEM.${model}" /> $labelTxt """
                }
                else  {
                    // is value needed ever? Doesn't do anything if ng-model is used.
                    result += """  ng-model="$model" ${readonly?"readonly":""}  ${defaultValue()}/> $labelTxt """
                }
                break;
            case COMP_TYPE_SUBMIT: //Is this ever used?
                result = """<input ${idAttribute(idTxtParam)} $autoStyleStr type="submit" value="${tran("label")}"/> """
                break;
            case COMP_TYPE_BUTTON:
                // TODO for SQL generate the action ID for each method, assign ID to each click action
                if (onClick)
                    result = """<button ${idAttribute(idTxtParam)} $autoStyleStr ng-click="${name}_onClick()" type="button">${tran("label")}</button>\n"""
                break;
            default :
                // TODO log and ignore not implemented component
                log.warn "*** WARNING: Compile Item. No HTML generated for component: $type ${name?name:model} ***"
                result = ""
        }
        if ([COMP_TYPE_BOOLEAN,COMP_TYPE_BUTTON, COMP_TYPE_SUBMIT].contains(type)) {
            labelTxt = "" //No labels in front
        }
        if (parent.type==COMP_TYPE_DETAIL) {
            result = """|<div ${idAttribute("-container"+idTxtParam)} $styleStr class="pb-${parent.type}-item-container pb-$type">
                        |$labelTxt $result
                        |</div>""".stripMargin()
        } else {
            result = """|<div ${idAttribute("-container"+idTxtParam)} $styleStr class="pb-${parent.type}-item-container pb-$type">
                        | $labelTxt $result
                        |</div> """.stripMargin()
        }
        return result
    }

    String componentStart(String t, int depth=0) {
        // determine heading level
        def heading = ""
        def MAX_HEADING = 6
        if (label && t in [COMP_TYPE_BLOCK,COMP_TYPE_FORM,COMP_TYPE_GRID]) {
            def headingLevel = (depth < MAX_HEADING-1)? depth+1: MAX_HEADING
            heading = """<h$headingLevel class="pb-${t}-label" ${idAttribute("-label")}>${tran("label")}</h$headingLevel>"""
        }
        styleStr = """ ng-class='${name}_$STYLE_ATTR' """
        def result = ""
        switch (t) { //Handle Non Items
            case COMP_TYPE_PAGE:
                return pageHeader()
            case COMP_TYPE_HTABLE:
                return htableCompile(depth+1)
            case COMP_TYPE_GRID:
                def borderpx=2
                //headerRowHeight doesn't work in {{ expression }} - assume same as rowHeight hence pageSize+1
                //style="...{{expression }}..."  does not evaluate properly in IE8 - fixed using ng-style
                return """\n$heading\n<div ${idAttribute(idTxtParam)} class="gridStyle" role="grid" ng-grid="${name}Grid" $styleStr ng-style="{height: (${borderpx*2}+${pageSize+1}*rowHeight+footerRowHeight) + 'px'}" aria-labelledby="pbid-$name${idTxtParam?idTxtParam:""}-label"></div>\n"""
            case COMP_TYPE_DATATABLE:
                return dataTableCompile(depth+1)
            case COMP_TYPE_DETAIL:
                return detailCompile(depth+1)
            case COMP_TYPE_LIST:
                return listCompile((depth+1))
            case COMP_TYPE_BLOCK:
                return """\n<div ${idAttribute(idTxtParam)} $styleStr class="pb-$t" ng-show="${name}_visible"> $heading \n"""
            case COMP_TYPE_FORM:
                // handle flow controlled
                def submitStr=""
                if (submit)
                    submitStr+="${name}_onSubmit(\$event)"
                if (root.flowDefs)
                    submitStr+= "; _activateNextForm('$name');"
                //don not use ng-form, this breaks page flows!
                result += """<form ${idAttribute()} $styleStr class="pb-$t" name="${name}" ng-show="${name}_visible"  ${submitStr?"""ng-submit="$submitStr" """:""}>$heading \n"""
                return result
            case COMP_TYPE_RESOURCE: //fall through
            case COMP_TYPE_DATA:
            case COMP_TYPE_FLOW:
                return "" // nothing to generate in HTML
            default :
                // Handle Items
                result = compileItem(t,depth)
                if (!result)
                    log.debug "*** WARNING: ComponentStart. No HTML generated for component: $type ${name} ***"
        }
        return result
    }

    // handle non-terminal nodes
    String componentEnd(String t, int ignoreDepth=0) {
        switch (t) {
            case COMP_TYPE_PAGE:

                Page page =Page.findByConstantName(name)
                def purlContent = ""
                if(page || pageURL) {

                    def purl=pageURL
                    if(!purl){
                        def model = new groovy.json.JsonSlurper().parseText(page.modelView)
                        purl = model.get("pageURL")
                    }
                    purlContent = "<div ng-controller='homePageUrlCtr'>"
                    if (purl) {
                        purlContent +=
                                "<div ng-view></div>\n" +
                                        "   <input name='pageURL' value='${purl}' id= 'homeURL' hidden='true'/>"
                    }
                    purlContent += "</div>"
                }
                return  "</div>\n${purlContent}\n</body>\n"
            case COMP_TYPE_FORM:
                def nextTxt = ""
                if (root.flowDefs || submit) {
                    def labelStr = ""
                    if (root.flowDefs) {
                        // TODO: probably only need one Button label - then we don't need the i18n incorrect concatenation using 'and'
                        // Proposed change: Only have Submit Label.
                        // Use NEXT_BUTTON_DEFAULT_LABEL if form is in a flowdef or has a submit handler
                        if (submitLabel) {
                            def key ="${propertiesBaseKey()}.submitAndNextLabel"
                            labelStr = "$submitLabel and $nextButtonLabel"  //TODO: I18N  or make logic different
                            tran(key,labelStr)
                        } else {
                            if ( nextButtonLabel == NEXT_BUTTON_DEFAULT_LABEL)
                                labelStr = tranGlobal("flow.next.label",NEXT_BUTTON_DEFAULT_LABEL)
                            else {
                                labelStr = tran("nextButtonLabel")
                            }
                        }
                    } else {
                        labelStr = tran('submitLabel')
                    }
                    if (labelStr) {
                        nextTxt += """|<div ${ idAttribute("submit-container") } class="pb-form-submit" $styleStr>
                                      |  <input ${ idAttribute("submit") } class="pb-form-submit-button" ng-class='${name}_$STYLE_ATTR' type="submit" value="$labelStr"/>
                                      |</div>
                                   """.stripMargin()
                    }
                }
                // <button ng-click="_activateNextForm('$name')">Next Step</button>
                return "$nextTxt</form>\n"
            case COMP_TYPE_BLOCK: return "</div>\n"
            default: return ""
        }
    }

    /*
    Generate HTML head and page wide JS code
     */
    def pageHeader() {
        // generate page header for global functions, CSS and Angular directives
        // TODO : generate and register all services for REST resources
        // TODO: move static functions, CSS to decorator
        // TODO: generate unique page ID
        def importPath = "\${ request.contextPath}/cssRender"
        def cssImp = ""
        // start css list with our default style sheet
        def tempList = DEFAULT_STYLESHEET
        if (importCSS)
            tempList+=","+importCSS
        //split the string into a list
        def is = tempList?.tokenize(',')
        for (css in is) {
            if (  Css.fetchByConstantName(css) )
                cssImp += """<link type="text/css" rel="stylesheet" href="$importPath?name=${css.trim()}" />\n"""
            else {
                log.warn "Warning: Style sheet $css will not be imported as it does not exist."
            }
        }
        def controllerName = "CustomPageController_${name}"

        """
        |<head>
        |<!-- sitemesh -->
        |<meta name="layout" content="bannerSelfServicePBPage"/>
        |<!-- import custom stylesheets -->
        |$cssImp
        |<!--meta name="layout" content="simple"/-->
        |<title>${tran("title")}</title>
        |<script type="text/javascript">
        |  var pageId = "$name";
        |  var controllerId = "$controllerName"
        |  pageControllers[controllerId] = $CONTROLLER_PLACEHOLDER;
        |</script>
        |</head>
        |<body>
        |   <div id="content" role="main" ng-controller="$controllerName"  class="customPage container-fluid">
        |   ${label?"<h1 ${idAttribute('label')}>${tran("label")}</h1>":""}
         """.stripMargin()
    }


    def compileComponentTemplate(  int depth = 0, esc = ESC_H) {
        // First see if a template exists for parent type and type
        templateName = ComponentTemplateEngine.supports("${parent?.type}.$type")?"${parent?.type}.$type":""
        // Next see if a template exists for type only
        if (!templateName && ComponentTemplateEngine.supports(type) ) {
            templateName = type
        }
        def result = [compiled:false]
        if (templateName) {
            components.each {
                content = it.compileComponent(content, depth +1)
            }
            def bindings = this.properties
            def q = esc==ESC_H?"'":"#q#" // Custom escape to bypass apostrophe escape in message
            translatableAttributes.each{
                if (bindings[it]) {
                    bindings[it]=tran(it, esc, q)
                }
            }
            bindings.thiz = this
            result = [compiled: true, code: ComponentTemplateEngine.render(bindings).toString()]
            if (esc==ESC_JS) {
                result.code = StringEscapeUtils.escapeJavaScript(result.code).replace(q,"'")
            }
        }
        return result
    }

    def handlesChildren() {
        [COMP_TYPE_GRID, COMP_TYPE_HTABLE, COMP_TYPE_DETAIL, COMP_TYPE_DATATABLE].contains(this.type)
    }



    def compileComponent(String inS, int depth=0){
        String result=inS
        def templateResult = compileComponentTemplate(depth)
        if ( templateResult.compiled) {
            result += templateResult.code
        } else {
            result += componentStart(type, depth)
            if (!handlesChildren()) {
                //grid, detail and others will take care of its child objects
                components.each {
                    result = it.compileComponent(result, depth + 1)
                }
            }
            def endString = componentEnd(type, depth + 1)
            if (endString) {
                result += endString
            }
        }
        return result
    }


    //recursively find components with types in componentTypes
    def findComponents(componentTypes ) {
        def result=[]
        if (componentTypes.contains(this.type))
            result << this
        components.each {
            result += it.findComponents(componentTypes)
        }
        return result
    }

    //Lists structure of page (very simple view)
    String showHierarchy (int depth = 0) {
        def result = "".padRight(depth*2," ")+this.name
        components.each {
            result += "\n"+it.showHierarchy(depth+1)
        }
        result
    }

    def validationAttributes() {
        def trKey ={ k ->
            //map some attributes to angular attributes and others to standard html
            //angular attributes do not really prevent inputting; e.g. maxlength really restricts the length input
            def trMap = [minlength:"ng-minlength",maxlength:"maxlength", pattern:"ng-pattern"]
            def result = trMap[k]?trMap[k]:k
            result
        }
        def trVal = { k,v ->
            def result = v
            if (k=="pattern") {
                switch (v[0]) {
                    case "/" : break //assume value is a regular expression as expected by angular
                    case "\$":       //assume a variable
                        result = compileDOMExpression(v)
                        break
                    default :
                        result = "/$v/" // angularjs ng-pattern wants regular expression like this
                }
            }
            result
        }
        validation?validation.collect { k,v -> "${trKey(k)}=\"${trVal(k,v)}\"" }.join(' '):""
    }

    //light weight conversion without any validation
    static PageComponent parseJSON(jsonComp)  {
        def result = new PageComponent( jsonComp )
        def componentList = []
        jsonComp.components.each {
            def child = parseJSON(it)
            componentList.push(child)
        }
        result.components = componentList
        result
    }

    // New method for resource usage
    def updateResourceBindings() {
        if (ID != name) {
            throw new ApplicationException(PageComponent, "***WARN*** Found a component with ID != name") //Todo: remove once we have consolidated name and ID
        }
        if (type == COMP_TYPE_PAGE) { //Make sure to have the resources first
            components.each { pc ->
                if (pc.type == COMP_TYPE_RESOURCE) {
                    root.meta.pageResources[pc.name] = pc
                }
            }
        } else {
            if (model || sourceModel) {
                //println "model: $model sourceModel: $sourceModel"
                def ref = model?root.meta.pageResources[model.tokenize(".")[0]] : null
                if (ref) {
                    ref.meta.referencedBy << this
                    meta.modelResource = ref
                    if (COMP_DATASET_TYPES.contains(type) || (COMP_ITEM_TYPES.contains(type) && ref.binding != BINDING_PAGE)){
                        root.meta.dataSetIDsIncluded += name
                    }
                }
                ref = sourceModel ? root.meta.pageResources[sourceModel.tokenize(".")[0]] : null
                if (ref) {
                    ref.meta.referencedBy << this
                    meta.sourceModelResource = ref
                    if (COMP_DATASET_TYPES.contains(type) || (COMP_ITEM_TYPES.contains(type) && ref.binding != BINDING_PAGE)){
                        root.meta.dataSetIDsIncluded += name
                    }
                }
            }
        }
        //update the children
        components.each{
            it.updateResourceBindings()
        }
    }

    // member method to calculate the resource parameters
    def resourceParameters() {
        def dataComponent = meta.sourceModelResource?:meta.modelResource
        def buildParameters = {parameters -> // concatenate all map entries to a string
            def res = ""
            parameters?.each { key, value->
                res +=  "$key : ${compileCtrlFunction(value)},"
            }
            if (res?.endsWith(","))   // remove trailing comma
                res = res.substring(0, res.length() - 1)
            res = "'{$res}'"
        }

        def queryParameters = "'{}'"
        if (dataComponent.binding == BINDING_REST) {
            if (parameters)
                queryParameters = buildParameters(parameters)
            if (sourceParameters)
                queryParameters = buildParameters(sourceParameters)
        }
        return queryParameters
    }

    def dataSetControlCode() {
        def dataComponent = meta.sourceModelResource?:meta.modelResource
        def result = ""
        def dataSource
        def resourceParams = "null"
        //should only COMP_TYPE_DATA have loadInitially?
        def autoPopulate = "true"
        if ( (type == COMP_TYPE_DATA || COMP_DATASET_TYPES.contains(type) )
             && !loadInitially) {
            autoPopulate = "false"
        }
        // first handle data binding
        if (dataComponent.binding == BINDING_REST && dataComponent.resource) {
            dataSource = "resource: \$scope.${dataComponent.name}"
            // transform parameters to angular $scope variable
            resourceParams = resourceParameters()
            autoPopulate = loadInitially?"true":"false"

        } else if (dataComponent.staticData){
            def data
            if ( COMP_DATASET_DISPLAY_TYPES.contains(type)  ){
                sourceModel = name //Not clear why this is needed.
                data = groovy.json.JsonOutput.toJson(tranSourceValue(dataComponent.staticData))  // translate labels
            }
            else {
                data = groovy.json.JsonOutput.toJson(dataComponent.staticData)
            }
            dataSource =  "data: $data"
            autoPopulate = "false"
        } else {
            // Changed. Allow empty data set
            //throw new Exception("Error Compiling UI. Either a Rest Resource or Static Data is required for Resource ${dataComponent.name}")
            dataSource =  "data: []"
            autoPopulate = "false"
        }

        def dataSetName = "${name}DS"
        def optionalParams=""
        if ( false == COMP_DATASET_DISPLAY_TYPES.contains(type)) {
            optionalParams += ",pageSize: $pageSize"
        }
        if (onUpdate)
            optionalParams+="\n,onUpdate: function(item){\n${compileCtrlFunction(onUpdate)}\n}"
        if (onLoad)
            optionalParams+="\n,postQuery: function(data,response){\n${compileCtrlFunction(onLoad)}\n}"
        if (onError)
            optionalParams+="\n,onError: function(response){\n${compileCtrlFunction(onError)}\n}"
        if (onSave) {
            optionalParams += "\n,onSave: function(){\n${compileCtrlFunction(onSave)}\n}"
        }
        if (onSaveSuccess) {
            optionalParams += "\n,onSaveSuccess: function(response,action){\n${compileCtrlFunction(onSaveSuccess)}\n}"
        }

        result = """
              |    \$scope.$dataSetName = pbDataSet ( \$scope,
              |    {
              |        componentId: "$name",
              |        $dataSource,
              |        queryParams: $resourceParams,
              |        autoPopulate: $autoPopulate,
              |        selectValueKey: ${valueKey ? "\"$valueKey\"" : null},
              |        selectInitialValue: ${value?"\"$value\"":"null"}
              |        $optionalParams
              |    });
              |""".stripMargin()

        def initNew = allowNew? initNewRecordJS(): ""
        if (type == COMP_TYPE_GRID) {
            result += gridJS() + initNew
        }
        if (type == COMP_TYPE_DATATABLE) {
            result += dataTableJS() + initNew
        }
        if ( [COMP_TYPE_HTABLE,COMP_TYPE_DETAIL,COMP_TYPE_LIST].contains( type)) {
            result += dataSetWatches() + initNew
        }
        return result
    }

    ////////////////////////////////////////////////////////////////////////////////////

    // Shorthand Expression compilation methods
    def compileCtrlFunction(expression) {
        compileExpression(expression,ExpressionTarget.CtrlFunction)
    }

    def compileDOMExpression(expression) {
        compileExpression(expression,ExpressionTarget.DOMExpression)
    }
    def compileDOMDisplay(expression) {
        compileExpression(expression,ExpressionTarget.DOMDisplay)
    }

    // split an expression " L1 <bra>E1<ket>L2<bra>E1<ket>L3"
    // into an array of [ preBra: Li, expression, postKet: Lj ]
    // where bra = {{ AngularJS expression start
    //   and ket = }} AngularJS expression end
    def static  splitAngularBrackets( String expr ) {
        def prep = expr.split(Pattern.quote(exprBra))
        def parts = []
        prep.eachWithIndex { str, i ->
            def subParts = str.split(Pattern.quote(exprKet))
            if (subParts.size()==1) {   // did not find exprKet or there is no part after it
                if (i==0 && !expr.startsWith(exprBra) ) {
                    parts += [preBra: subParts[0]]
                } else {
                    parts += [expression: subParts[0]]
                }
            }
            else {
                parts += [expression: subParts[0], postKet: subParts[1]]
            }
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
    // cannot be a static member as root it needed.
    def compileExpression(expression,
                          ExpressionTarget target=ExpressionTarget.CtrlFunction,
                          ArrayList dataSets=root.meta.dataSetIDsIncluded,
                          ArrayList resources=root.meta.pageResources.keySet() ) {

        final def dataSetReplacements = [
                [from:  ".\$populateSource"  , to:"DS.load"],
                [from:  ".\$load"            , to:"DS.load"],
                [from:  ".\$save"            , to:"DS.save"],
                [from:  ".\$get"             , to:"DS.get" ],
                [from:  ".\$selected"        , to:"DS.currentRecord" ],
                [from:  ".\$selection"       , to:"DS.selectedRecords" ],
                [from:  ".\$data"            , to:"DS.data" ],
                [from:  ".\$dirty"           , to:"DS.dirty()" ],
                [from:  ".\$setModified"     , to:"DS.setModified" ],
                [from:  ".\$setResource"     , to:"DS.setResource" ]

        ]
        final def resourceReplacements = [
                [from:  ".\$post"            , to:".post"],
                [from:  ".\$put"             , to:".put"]
        ]
        final def pbProperties = ["visible", "style"]
        final def pbFunctions = ["eval"] //to be called with $$function

        // Handle escaped dollar signs \$ to allow using a literal dollar in expressions
        def escDollar = '#escDollar#'
        def escapeDollar = { expr ->
            if (expr.contains(escDollar)) {
                escDollar = '@escDollar@' // if the expression contains escDollar use a different escape text
                if (expr.contains(escDollar)) {
                    throw new RuntimeException("Expression contains unhandled text combination #escDollar# and @escDollar@")
                }
            }
            expr = expr.replaceAll('\\\\\\$', escDollar)
            expr
        }

        def unescapeDollar = { expr ->
            expr = expr.replaceAll(escDollar,'\\$')
        }

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
                result = result.replaceAll(/([^\.]|^)\$([\w]+)/, '$1#scope.$2')
                //replace the underscore with $ for pbFunctions
                pbFunctions.each {
                    result=result.replace("#scope._$it","#scope.\$$it")
                }
                if (target == ExpressionTarget.CtrlFunction) {
                    result = result.replace('#scope.', '$scope.')
                } else {
                    result = result.replace('#scope.', '')
                }
            }
            result
        }
        def literalReplace = { result ->
            result = componentReplace(result)
            result = result?.replaceAll('\\$([\\$]*[\\w]+[\\w\\.\\$]*)', '{{ $1 }}')       // grab the variable expressions and make it an angular expression
            //result = result?.replaceAll('\\$([\\w\\.\\$]*)', '{{ $1 }}')
            result = result?.replaceAll('\\{\\{ \\$([\\w\\.\\$]*)', '{{ _$1')  // if the variable starts with a $ still it was $$ and it needs to start with _
            pbProperties.each {
                result = result?.replace(".\$$it","_$it")
            }
            result
        }
        if (expression == null) {
            log.debug "Compile Expression: skip null"
            return expression
        } else if(target == ExpressionTarget.CtrlFunction && (expression instanceof  Boolean || expression instanceof Integer)) {
            log.debug "Compile Expression: skip boolean or Integer of source parameters"
            return expression
        }

        def result = expression
        log.debug "Compile Expression: $expression"
        result = escapeDollar(result)
        if (target in [ExpressionTarget.CtrlFunction, ExpressionTarget.DOMExpression] ) {
            result = expressionReplace(result)
        } else {
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
        result = unescapeDollar(result)
        log.debug "-> $result"
        result
    }
}
