
/**
 * Created with IntelliJ IDEA.
 * User: hvthor
 * Date: 14-2-13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
package net.hedtech.banner.sspb

class PageComponent {
    final static COMP_TYPE_PAGE = "page"
    final static COMP_TYPE_FORM = "form"
    final static COMP_TYPE_GRID = "grid"
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

    // Types that can represent a single field - should add RADIO
    final static COMP_ITEM_TYPES = [COMP_TYPE_LITERAL,COMP_TYPE_DISPLAY,COMP_TYPE_TEXT,COMP_TYPE_TEXTAREA,COMP_TYPE_NUMBER,
                                    COMP_TYPE_DATETIME,COMP_TYPE_EMAIL,COMP_TYPE_TEL,COMP_TYPE_LINK,COMP_TYPE_BOOLEAN]

    // Single field non-input types
    final static COMP_DISPLAY_TYPES =  [COMP_TYPE_LITERAL,COMP_TYPE_DISPLAY,COMP_TYPE_LINK,COMP_TYPE_HIDDEN]

    // Types that have a DataSet associated  - not completely orthogonal yet. COMP_ITEM_TYPES can have it too
    final static COMP_DATASET_TYPES = [COMP_TYPE_GRID,COMP_TYPE_LIST,COMP_TYPE_SELECT,COMP_TYPE_DETAIL,COMP_TYPE_DATA, COMP_TYPE_RADIO]

    final static BINDING_REST = "rest"
    final static BINDING_SQL = "sql"
    final static BINDING_PAGE = "page"
    final static BINDING_API = "api"

    final static GRID_ITEM="item"
    final static LIST_ITEM="item"
    final static SELECT_ITEM="item"
    final static CURRENT_ITEM = "currentSelection"
    final static CONTROLLER_PLACEHOLDER="###CONTROLLER###"
    final static VAR_PRE = '$'  //page model variable prefix
    final static VAR_RES = '$$' // page model reserved variable prefix

    final static NEXT_BUTTON_DEFAULT_LABEL = "Next"

    final static translatableAttributes = ["title","label","submitLabel","nextButtonLabel","placeholder","value","description"]

    //escape flags for translatable strings
    final static ESC_0 = 0 //no escape
    final static ESC_H = 1 //escape HTML
    final static ESC_JS = 2 //escape

    String type        // -> Generic property for component type
    String name        // -> Generic property. Maybe use componentId?
    String title       // -> Page
    String scriptingLanguage = "JavaScript" // -> page property that specifies the scripting language to use in page model
    String importCSS   // specify what custom stylesheets to import, comma separated list of custom stylesheet names (without the .css extension )
    String label       // -> Items (inputs, display)
    String placeholder // -> Input Items (is text shown when no input is provided)
    String model       // -> top level data model should have first letter in upper case, data declared in controller will be lower case
                        // in grid the model will point to the property name of the parent model
                        // in grid and detail component model is used for both retrieve and update to a single resource
                        // TODO add constant support in the form of an array of maps (for grid) or map (for List, Select)
    String modelRoot    // internal - represent the root class of a model - e.g Todo.description root class is Todo
    String modelComponent // internal - represent the component of a model after the root, excluding the leading ".". e.g Todo.description model component is description
    def parameters     // -> parameters for data query, for REST this is a Map of key:value, where key is the property name of the resource attribute, value can be
    // another data item name . This is attached to the component that uses the data, not the data definition itself

    String sourceModel       // for component with both input(pre-populate) and output like "select" use this specify the input model
                            // TODO add constant support in the form of an array (for List) or map (for Select)
    def sourceValue      // for select using a constant map of value:
    def sourceParameters    // used for query with sourceModel

    // flow control
    String sequence
    Boolean activated = false
    def nextButtonLabel=NEXT_BUTTON_DEFAULT_LABEL //"Next"    //translated in tranGlobalInit
    //def lastButtonLabel=USE_DEFAULT //"Finish"  //seems not to be in use
    def submitLabel = ""
    //String condition

    // data
    String onLoad=""
    Boolean loadInitially = true   // specify if  data (query) from resource should be loaded after page is loaded

    def errorHandling = [:] // a map of error code-> code block that specifies what actions to take if an error is encountered during resource operation
    String value       // -> Item value, read only  for display type - also used to initialize an input type
    Boolean required=false
    Boolean readonly=false
    String onClick       // a code block to execute when a button is clicked
   // String controller  // -> Now associated with page (body), possibly use a controller per form - TODO depreciate - we always use one controller "CustomPageController"

    // Show properties
    Boolean showInitially = true
    Boolean visible = showInitially

    // grid properties
    String submit      // -> Controller function to be called when submitting a form
    Boolean allowNew = false  // grid property - indicate a new entity entry and a submit button should be added to the table
    Boolean allowModify = false // grid property - indicate table can be modified and a submit button should be added
    Boolean allowDelete = false // grid property - indicate table entry can be deleted and a selection checkbox and a submit button should be added
    Boolean allowReload = false  // grid property - add a reload button automatically to re-execute the query
    int pageSize = 5        // grid property - number of items to display per page

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

    String style        // styling support
    String labelStyle
    String valueStyle


    // data properties
    String resource    // -> Form Data Component. uri: <path>/resourceName e.g. rest/todo
    String binding = BINDING_REST    // method of data binding (sql, api, rest, page)

    def validation       // specify any validation definition as a map
    String onUpdate     // an code blovk to execute if the current component value is changed

                        // TODO deduce this from model automatically
    String ID           // generated ID, do we need this?
    String documentation    // for page development documentation purpose, not used by code generator

    List<PageComponent> components    // child components

    // internal use properties
    PageComponent parent    // record parent component for special code generation in grid, etc.
    PageComponent root      // the root (page) component

    def flowDefs = []       // global flow definitions, a list of map {flowName:"", sequence:["form1","form2"], condition, ""}
    def activeFlow = ""     // the initially activated flow
    def formSet = []        // the set of all form names on this page

    def rootProperties = [:]    // the key/value properties (set on page component)
    def globalProperties = [:]  // properties shared in pages

    def styleStr
    def labelStyleStr
    def valueStyleStr

    def idTxtParam = ""

    // map the validation key to angular attributes ? use HTML 5 validation instead with form
    // def validationKeyMap = ["minlength":"ngMinlength", "maxlength":"ngMaxlength", "pattern":"ngPattern"]

    static boolean isDataSetEditControl ( PageComponent pc ){
        if (pc)
            [COMP_TYPE_DETAIL,COMP_TYPE_GRID, COMP_TYPE_LIST].contains(pc?.type)
        else
            false;
    }

    def getPropertiesBaseKey() {
        def nameList = []
        def pageComponent = this
        while (pageComponent) {
            nameList << pageComponent.name
            pageComponent = pageComponent.parent
        }
        nameList = nameList.reverse()

        return nameList.join(".")
    }

    def tranSourceValue() {
        def result = sourceValue
        result.each {
            def key ="${getPropertiesBaseKey()}.sourceValue.${it."$valueKey"}"
            def label=it."$labelKey"
            label = tran(key,label,[] as List,ESC_JS)
            it."$labelKey"=label
        }
    }

    def tranMsg(key, List args=[], esc = ESC_H) {
        def encodingFlag=""
        def argsString=""
        if (!args.empty)
            argsString=",args: $args"
        switch(esc) {
          //case ESC_H  : encodingFlag = ", encodeAs: 'HTML'"; break    // this is default so no need to specify
            case ESC_JS : encodingFlag = ", encodeAs: 'JavaScript'"; break
        }
        def result =  "message(code: '$key' $argsString $encodingFlag)"
        return "\${${result}}"
    }

    def tran(String prop, esc = ESC_H ) {
        def defTranslation = this[prop]
        if (defTranslation && translatableAttributes.contains(prop))  {
            def key ="${getPropertiesBaseKey()}.$prop"
            root.rootProperties[key] = defTranslation
            return tranMsg(key,[] as List, esc)
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
        else
            text = root.globalProperties[key]
        if (text) {
            return tranMsg(key,args,esc)
        }
        return ""
    }

    String defaultValue() {
        def result = ""

        if (value && model ) {
            def  expr = CompileService.parseVariable(value)
            CompileService.dataSetIDsIncluded.each { //replace with dataSetIDs
                expr=expr.replace(".${it}_",".${it}DS.")
            }
            if (value == expr) { //assume a literal value if not changed - not sure if we should do this
                expr="'$expr'"
            }
            def parentRef = isDataSetEditControl(parent)?"$GRID_ITEM":"this"
            //result = "ng-init=\"${parentSelect}${model}=snvl(${parentSelect}${model},$expr)\""
            result = "ng-init=\"setDefault($parentRef,'$model',$expr)\""
        }
        result
    }

    /*
   utility function to generate a unique ID for generated HTML tag
   the id is of the format: pbid-<component_name>-<component-type>-<additional tag>
   Additional tag is used to differenciate generated controls inside an component (such as new button for grid)
   id is used for styling purpose
    */
    def getId(tag = "") {
        def s = """id='pbid-$name"""
        if (tag)
            s += """-$tag""";
        s +="'"
        return s
    }

    def recordControlPanel()  {
        def dataSet    =  "${name}DS"
        def result =
        """
        <!-- pagination -->
        <span ${getId('pagination-container')} ng-show='${dataSet}.totalCount > ${dataSet}.pagingOptions.pageSize'>
        <button ${getId('pagination-prev-button')} $styleStr ng-disabled="${dataSet}.pagingOptions.currentPage == 1" ng-click="${dataSet}.pagingOptions.currentPage=${dataSet}.pagingOptions.currentPage - 1">
                ${tranGlobal("page.previous.label","Previous")}
        </button>
        <span ${getId('pagination-page-count')}>
            {{${dataSet}.pagingOptions.currentPage}}/{{${dataSet}.numberOfPages()}}
        </span>
        <button ${getId('pagination-next-button')}  $styleStr ng-disabled="${dataSet}.pagingOptions.currentPage >= ${dataSet}.totalCount/${dataSet}.pagingOptions.pageSize " ng-click="${dataSet}.pagingOptions.currentPage=${dataSet}.pagingOptions.currentPage + 1">
                ${tranGlobal("page.next.label","Next")}
        </button>
        <br>
        </span>

        """

        if (allowNew) {
            result += """ <button ${getId('new-button')} $styleStr ng-click="${dataSet}.add()"> ${tranGlobal("newRecord.label","Add New")}  </button>"""
        }
        if (allowModify || allowDelete) {
            result += """ <button ${getId('save-button')} $styleStr ng-click="${dataSet}.save()" ng-disabled="!${dataSet}.dirty()"> ${tranGlobal("save.label","Save")} </button>"""
        }
        if (allowReload) {
            result += """ <button ${getId('reload-button')} $styleStr ng-click="${dataSet}.load({all:false,paging:true,clearCache:true})"> ${tranGlobal("refresh.label","Refresh")} </button> """
        }
        return result
    }

    def gridControlPanel()  {
        def dataSet    =  "${name}DS"
        def result = ""
        styleStr=styleStr?styleStr:""
        if (allowNew) {
            result += """ <button $styleStr ng-click="${dataSet}.add()"> ${tranGlobal("newRecord.label","Add New",[], ESC_JS)}  </button>"""
        }
        if (allowDelete) {
            result += """ <button $styleStr ng-click="${dataSet}.delete(${dataSet}.selectedRecords)" ng-disabled="${dataSet}.selectedRecords.length==0"> ${tranGlobal("deleteRecord.label","Delete selected",[], ESC_JS)}  </button>"""
        }
        if (allowModify || allowDelete) {
            result += """ <button $styleStr ng-click="${dataSet}.save()" ng-disabled="!${dataSet}.dirty()"> ${tranGlobal("save.label","Save",[], ESC_JS)} </button>"""
        }
        if (allowReload) {
            result += """ <button $styleStr ng-click="${dataSet}.load({all:false,paging:true,clearCache:true})"> ${tranGlobal("refresh.label","Refresh",[], ESC_JS)} </button> """
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
            def ro= child.readonly || COMP_DISPLAY_TYPES.contains(child.type)
            if (items.length()>0)
                items+=",\n"
            def optional =  (child.type==COMP_TYPE_HIDDEN)? ",visible: false":""
            //optional+= ro?"":",enableCellEdit: ${!ro}"   // breaks drop-down in firefox and is not really needed
            //TODO: for sorting need a way to distinguish JSON data from formula's or synthetic columns (i.e. boolean without model)
            //model gets automatically populated with Name - maybe don't do this and reserve model for JSON columns
            if (child.type==COMP_TYPE_LITERAL)// needs to be a column in the api to be sortable on server
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
            footerTemplate: \$templateCache.get('gridFooter.html').replace('#gridControlPanel#',${name}GridControlPanel),
            footerRowHeight: 55,
            jqueryUIDraggable:true,
            multiSelect: false,
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
            if (newVal !== oldVal ) {
                \$scope.${dataSet}.load({all:false,paging:true});
            }
        }, true);
        \$scope.\$watch('${dataSet}.sortInfo', function(newVal, oldVal) {
            if ( (newVal.fields.join(',') !== oldVal.fields.join(','))||(newVal.directions.join(',') !== oldVal.directions.join(',')) ) {
                \$scope.${dataSet}.load({all:false,paging:true});
            }
        }, true);
        """

        if (onClick)  { //TODO: this is not really on click but onSelectionChanged
            code+=
        """\$scope.\$watch('${dataSet}.selectedRecords[0]', function(newVal, oldVal) {
            if (newVal !== oldVal ) {
                \$scope.${name}_onClick(newVal);
            }
        });
        """
        }
        code
    }

    private def javaScriptString(s) {
        def result = s.replaceAll("\'","\\\\'").replaceAll("\n","\\n")
        println result
        result
    }
    //this returns a html template string as a javascript string - escape strings
    String gridChildHTML( int depth=0) {
        def ro= readonly || COMP_DISPLAY_TYPES.contains(type)
        def tagStart="<input"
        def tagEnd="/>"
        def typeAt="type=\"$type\""
        def styleAt="style=\"background-color:transparent; border:0; width: 100%; height:{{rowHeight}}px\""
        def specialAt=""
        def readonlyAt = (parent.allowModify && !ro)?"":"readonly"
        def requiredAt = required?"required":""
        def validateAt = ""
        def defaultAt = "" //ng-init seems not to work within grid.javaScriptString(defaultValue())
        def placeholderAt=""
        def ngModel="ng-model=\"COL_FIELD\""    // shorthand for  row.entity[col.field]
        def ngChange="ng-change=\""+(onUpdate?"\$parent.${parent.ID}_${name}_onUpdate(row.entity);":"")+"\$parent.${parent.name}DS.setModified(row.entity)\""
         if (type == COMP_TYPE_NUMBER ) {
            typeAt="type=\"text\" pb-number " //angular-ui doesn't use localized validators
        }
        else if (type == COMP_TYPE_DATETIME ) {
            typeAt="ui-date=\"{ changeMonth: true, changeYear: true}\" "
            //Assume format comes from jquery.ui.datepicker-<locale>.js
            //Cannot choose format with time, but lots of options. See http://jqueryui.com/datepicker/
        }

        switch (type) {
            case COMP_TYPE_SELECT:
                // SELECT must have a model
                def arrayName = "${name}DS.data"
                readonlyAt = (parent.allowModify && !ro)?"":"disabled" //select doesn't have readonly
                ngChange="ng-change=\""+(onUpdate?"\${name}DS.onUpdate(row.entity);":"")+"\$parent.${parent.name}DS.setModified(row.entity);${name}DS.setCurrentRecord(row.entity.$model);\""
                placeholderAt = placeholder?"""<option value="">${tran("placeholder")}</option>""":""
                return """<select ${styleAt} $ngModel $readonlyAt $ngChange $defaultAt ng-options="$SELECT_ITEM.$valueKey as $SELECT_ITEM.$labelKey for $SELECT_ITEM in $arrayName"> $placeholderAt </select>"""
            case [COMP_TYPE_TEXT, COMP_TYPE_TEXTAREA,COMP_TYPE_NUMBER, COMP_TYPE_DATETIME, COMP_TYPE_EMAIL, COMP_TYPE_TEL] :
                validateAt = validation?validation.collect { k,v -> "$k=\"$v\"" }.join(' '):""
                placeholderAt=placeholder?"placeholder=\"${tran("placeholder")}\"":""
                //specialAt="onClick=\"console.log(${javaScriptString("'Row  :{{row.rowIndex}}'")});\""
                break
            case COMP_TYPE_BOOLEAN:
                typeAt = "type=\"checkbox\""
                specialAt ="""${booleanTrueValue?"ng-true-value=\"$booleanTrueValue\"":""}  ${booleanFalseValue?"ng-false-value=\"$booleanFalseValue\"":""}  """
                break
            case COMP_TYPE_DISPLAY:
                typeAt=""
                break
            case COMP_TYPE_LITERAL:
                return "<span $styleAt>" + tran(getPropertiesBaseKey()+".value",CompileService.parseLiteral(value).replaceAll("item.","row.entity.") ) + "</span>"
                break
            default :
                println "***No ng-grid html edit template for $type ${name?name:model}"
        }
        def result = "$tagStart $typeAt $styleAt $specialAt $readonlyAt $requiredAt $validateAt $defaultAt $placeholderAt" +
                     " $ngModel $ngChange $tagEnd"
        return result
    }

    /*
    Special compilation for generating table specific controls
     */
    def gridCompile(int depth=0) {

        if (name.endsWith("NGrid")) { //TODO
            def borderpx=2
            //headerRowHeight doesn't work in {{ expression }} - assume same as rowHeight hence pageSize+1
            return """<div class="gridStyle" ng-grid="${name}Grid" style="width:99.5%; height:{{${borderpx*2}+${pageSize+1}*rowHeight+footerRowHeight}}px; border: ${borderpx}px solid rgb(212,212,212);$style"></div>"""
        }
        // Old code with HTML Table grid -- leave for now as some parts are not implemented yet in new Grid
        def dataSet    =  "${name}DS"
        def repeat = "$GRID_ITEM in ${dataSet}.data"
        //implement as table for now
        // generate table column headers
        def thead=""
        def items=""

        // add a delete checkbox column if allowDelete is true
        if (allowDelete) {
            thead = "<th ${getId('delete-column-header')} $styleStr >${tranGlobal("delete.label","Delete")}</th>"
            items = """
            <td ${getId('delete-column-data-'+idTxtParam)} $styleStr >
             <input ${getId('delete-column-checkbox-'+idTxtParam)} $styleStr ng-click="${dataSet}.delete($GRID_ITEM)" type="checkbox" style="width:20%"/>
            </td>
            """
        }
        // generate all table columns from the data model
        components.each { child ->
            child.parent = this
            if (child.type == COMP_TYPE_HIDDEN) {
                //no table data or headers
                items+= child.compileComponent("", depth)

            }   else {
                //get the labels from child components
                thead+="<th ${getId('data-header-'+child.name)} $styleStr >${child.tran("label")}</th>"
                //get the child components
                child.label=""
                items+="<td ${getId('data-cell-' + child.name + '-' + idTxtParam )} $styleStr >${child.compileComponent("", depth)}</td>\n"
            }
        }
        def click_txt=""
        if (onClick)
            click_txt = "ng-click=${name}_onClick($GRID_ITEM)"

        def result =
            """<table ${getId()} $styleStr >
            <thead ${getId('table-header')} ><tr ${getId('table-header-row')} >$thead</tr></thead>
            <tbody ${getId('table-body')} >
            <!-- Do this for every object in objects -->
            <tr ${getId('table-data-row-'+idTxtParam)}  ng-repeat="$repeat" $click_txt>
              $items
            </tr>
            </tbody>
            </table>
            """
        result +=  recordControlPanel()
        return result
    }




    def detailCompile(int depth=0) {
        def dataSet   =  "${name}DS"
        def repeat = "$GRID_ITEM in ${dataSet}.data"    //GRID_ITEM is confusing

        def result = """<span $styleStr ${getId()}>"""
        if (label)
            result += "<label ${getId('label')}>${tran("label")}</label>\n"
        result +="""<div ${getId("container-" + idTxtParam)} $styleStr ng-repeat="$repeat" >\n"""

        if (allowDelete) {
            result += """

                <div style="text-align:right" ${getId("delete-container" + idTxtParam)}>
                    <input ${getId("delete-checkbox-" + idTxtParam)} ng-click="${dataSet}.delete($GRID_ITEM)" type="checkbox" />
                </div>
                <label style="text-align:left" ${getId("delete-label-" + idTxtParam)}> <strong>${tranGlobal("delete.label","Delete")}</strong></label>

            """
        }
        // generate all table columns from the data model
        components.each { child ->
            //get the child components
            result+="${child.compileComponent("", depth)}\n"
        }
        result+= "</div>\n"
        result+= recordControlPanel()
        result += "</span>"
        return result
    }

    /*
    Special compilation for generating table specific controls
     */
    def listCompile(int depth=0) {

        def dataSet   =  "${name}DS"
        def txt = "<span ${getId()}>"
        def repeat = "$LIST_ITEM in ${dataSet}.data"

        if (label)
            txt += """<label ${getId('label')} $styleStr >${tran("label")}</label>"""
        // handle click event
        def click_txt=""
        if (onClick)
            click_txt = "ng-click=${name}_onClick($LIST_ITEM)"
        txt +=
            """<ul ${getId('ul-')} $styleStr >
            <li ${getId("li-" + idTxtParam)} $click_txt ng-repeat="$repeat">
             ${onClick?"<a ${getId('a-'+ idTxtParam)} href=\"\">":""} {{$LIST_ITEM.$value}}  ${onClick?"</a>":""}
            </li>
            </ul>
            """
        txt += recordControlPanel() +  "</span>"
        return txt
    }

    def radioCompile(int depth=0) {
        def arrayName = "${name}DS.data"
        def result
        def ngModel = name
        // TODO handle parent GRID/DETAIL ID generation
        def labelTxt = label? """<label ${getId('label')} $styleStr for="${name?name:model}">${tran("label")}</label>""":""
        def updateTxt = ""
        //def placeholderStr = placeholder?"""<option value="">$placeholder</option>""":""
        def initTxt = value?"""ng-init="\$parent.$ngModel='$value'" """:""
        def nameTxt = name

        if(parent.type == COMP_TYPE_DETAIL) {
            ngModel =  "\$parent.$GRID_ITEM.${model}"
            updateTxt +="\$parent.\$parent.${parent.name}DS.setModified(\$parent.$GRID_ITEM);"
            nameTxt += "{{'${name}_' + \$parent.\$index}}"
        } else if (parent.type == COMP_TYPE_GRID) {
            ngModel =  "\$parent.$GRID_ITEM.${model}"
            updateTxt +="\$parent.\$parent.${parent.name}DS.setModified(\$parent.$GRID_ITEM);"
            nameTxt = "{{'${name}_' + \$parent.\$index}}"
        } else {
            ngModel =  "\$parent.$ngModel"
        }
        updateTxt += onUpdate?"${name}DS.onUpdate();":""
        updateTxt = updateTxt?"ng-change=\"$updateTxt\"":""

        def radio = """<div ${getId("container")} $styleStr ng-repeat="$SELECT_ITEM in $arrayName" $initTxt>
            <input ${getId("radio-"+"{{\$index}}")} $styleStr type="radio"   ng-model=$ngModel name="$nameTxt" $updateTxt
                value="{{$SELECT_ITEM.$valueKey}}"/> {{$SELECT_ITEM.$labelKey}}
        </div>"""
        if(parent.type == COMP_TYPE_DETAIL) {
            result = """<div $styleStr ><label ${getId("label")}><strong>${tran("label")}</strong></label><span ${getId("radio")}>
                             $radio
                            </span> </div>"""
        } else {
            // TODO model for select is used for data input, not output - resolve model ambiguity
            result = """$labelTxt $radio """
        }
        return result

    }
    // ?
    def flowCompile() {

    }



    String componentStart(String t, int depth=0) {
        // determine heading level
        def heading = ""
        def MAX_HEADING = 6
        if (label) {
            def headingLevel = (depth < MAX_HEADING-1)? depth+1: MAX_HEADING
            heading = """<h$headingLevel ${getId("label")}>${tran("label")}</h$headingLevel>"""
        }

        // handle ID generation for items in a dataset
        // append -$index to each rendered items

        if(isDataSetEditControl(parent) || isDataSetEditControl(this))
            idTxtParam = "{{\$index}}"

        styleStr = style?""" class="$style" """:""
        labelStyleStr =  labelStyle?""" class="$labelStyle" """:""
        valueStyleStr =  valueStyle?""" class="$valueStyle" """:""


        switch (t) {

            case COMP_TYPE_PAGE:
                return pageHeader()
            case COMP_TYPE_GRID:
                return gridCompile(depth+1)
            case COMP_TYPE_SELECT:
                // SELECT must have a model
                def arrayName = "${name}DS.data"
                def result
                def ngModel = name
                def labelTxt = label? """<label ${getId("label")} $styleStr for="${name?name:model}">${tran("label")}</label>""":""
                def updateTxt = ""
                def placeholderStr = placeholder?"""<option value="">${tran("placeholder")}</option>""":""

                if(isDataSetEditControl(parent)) {
                    ngModel =  "$GRID_ITEM.${model}"
                    updateTxt +="\$parent.${parent.name}DS.setModified($GRID_ITEM); ${name}DS.setCurrentRecord($ngModel);"
                }
                updateTxt += onUpdate?"${name}DS.onUpdate();":""
                updateTxt = updateTxt?"ng-change=\"$updateTxt\"":""
                def select = """<select  ${getId(idTxtParam)} $styleStr ng-model="$ngModel" $updateTxt  ${defaultValue()}
                                           ng-options="$SELECT_ITEM.$valueKey as $SELECT_ITEM.$labelKey for $SELECT_ITEM in $arrayName">
                                   $placeholderStr
                                </select>"""
                if(parent.type == COMP_TYPE_DETAIL) {
                    result = """<div ${getId("container")} $styleStr><label ${getId("label-" + idTxtParam)}>${tran("label")}</label>
                             $select
                             </div>"""
                } else {
                    // TODO model for select is used for data input, not output - resolve model ambiguity
                    result = """$labelTxt $select"""
                }
                return result
            case COMP_TYPE_DETAIL:
                return detailCompile(depth+1)

            case COMP_TYPE_LIST:
                return listCompile((depth+1))

            case COMP_TYPE_RADIO:
                return radioCompile((depth+1))

            case COMP_TYPE_BLOCK:
                    return """<div ${getId(idTxtParam)} $styleStr id="$name" ng-show="${name}_visible"> $heading
                """
            case COMP_TYPE_FORM:
                def txt = ""

                // handle flow controlled
                def submitStr=""
                if (submit)
                    submitStr+=submit
                if (root.flowDefs)
                    submitStr+= "; _activateNextForm('$name');"

                txt += """<form ${getId()} $styleStr name="${name?name:model}" ng-show="${name}_visible"  ${submitStr?"""ng-submit="$submitStr" """:""}>$heading
                """
                return txt
            case COMP_TYPE_LITERAL:
                //println tran(getPropertiesBaseKey()+".value",CompileService.parseLiteral(value) ) + "\n"
                return "<span ${getId(idTxtParam)} $styleStr>" + tran(getPropertiesBaseKey()+".value",CompileService.parseLiteral(value) ) + "</span>\n"
            case COMP_TYPE_DISPLAY:
                def ret = ""
                if (parent.type == COMP_TYPE_DETAIL) {
                    ret += "<div ${getId("container-" + idTxtParam)} $styleStr>" +
                            """<label ${getId("label-"+ idTxtParam)}> ${tran("label")}</label>"""
                    if (asHtml) {
                        ret += "<span ${getId(idTxtParam)} $styleStr  ng-bind-html-unsafe='$GRID_ITEM.$model'></span>"
                    }
                    else   {
                        // TODO handle value in details for display
                        def val = "{{$GRID_ITEM.$model}}"
                        ret += "<span ${getId(idTxtParam)} $styleStr> $val</span>"
                    }
                    // for display in detail control take the parent model
                    ret += "</div>"
                } else if (parent.type == COMP_TYPE_GRID) {
                    if (asHtml)
                        ret = " <span ${getId(idTxtParam)} $styleStr ng-bind-html-unsafe='$GRID_ITEM.$model'> </span>";
                    else
                        ret = " <span ${getId(idTxtParam)} $styleStr> {{ $GRID_ITEM.${model} }} </span>";
                } else {
                    // otherwise the value is used
                    // TODO consolidate value and sourceModel?
                    // TODO is parseVariable still working after using DataSet as generic data object?
                    ret  = label?"<label ${getId(idTxtParam)} $styleStr>${tran("label")}</label>":""


                    if (asHtml) {
                        ret += "<span ${getId(idTxtParam)} $styleStr  ng-bind-html-unsafe='${CompileService.parseVariable(value)}'></span>"
                }
                    else   {
                        ret += value?"{{${CompileService.parseLiteral(value)}}}":""
                        ret  = "<span ${getId(idTxtParam)} $styleStr> $ret</span>"
                    }
                }
                return ret
            case COMP_TYPE_LINK:
                def ret = "<div ${getId("container-" + idTxtParam)} $styleStr>"
                def desc = description?tran("description"):url
                def clickStr = onClick?"""ng-click="${name}_onClick()" """:""

                // handle open link in new window attr
                def targetStr =''
                if (!replaceView)
                    targetStr = 'target="_blank"'
                // set url to empty string if it is null, otherwise the page is re-directed to a non-existing page
                url = (url==null)?"":url

                // otherwise the value is used
                // TODO consolidate value and sourceModel?
                // TODO is parseVariable still working after using DataSet as generic data object?
                ret += label?"""<label ${getId("label-" + idTxtParam)}>${tran("label")}</label>""":""
                ret +=  """<a ${getId(idTxtParam)} ng-href="${CompileService.parseLiteral(url)}" $targetStr $clickStr>$desc</a></div>"""
                return ret

            case COMP_TYPE_TEXT:
            case COMP_TYPE_TEXTAREA:
            case COMP_TYPE_NUMBER:
            case COMP_TYPE_DATETIME:
            case COMP_TYPE_EMAIL:
            case COMP_TYPE_TEL:
            case COMP_TYPE_HIDDEN:
                def txt = ""
                def validateStr = ""
                if (validation) {
                    validateStr = validation.collect { k,v -> "$k=\"$v\"" }.join(' ')
                }
                def attributes = "$validateStr ${required?"required":""} ${placeholder?"placeholder=\"${tran("placeholder")}\"":""}".trim()
                def typeString= "type=\"$t\""
                if (type == COMP_TYPE_NUMBER) {  // angular-ui doesn't use localized validators
                    typeString="type=\"text\" pb-number "
                }

                def styleDatepicker= ""
                if (type == COMP_TYPE_DATETIME)  { //Assume format comes from jquery.ui.datepicker-<locale>.js
                    typeString=" ui-date=\"{ changeMonth: true, changeYear: true}\" "
                    // override the datepicker style which is defined in banner-ss-ui.css, which causes layout issue
                    styleDatepicker = 'style="width:auto; margin-bottom:2px;" '
                }
                //Cannot choose format with time, but lots of options. See http://jqueryui.com/datepicker/
                if (isDataSetEditControl(parent)) {
                    def addOnUpdate=""
                    if (onUpdate)  {  //ToDo: add this to other controls in Grid/Detail
                        addOnUpdate="\$parent.${parent.ID}_${name}_onUpdate($GRID_ITEM);"
                    }
                    txt = """
                          <input ${getId(idTxtParam)} $styleDatepicker $styleStr $typeString   name="${name?name:model}" ${parent.allowModify?"":"readonly"}
                          ng-model="$GRID_ITEM.${model}"  ${defaultValue()}
                          ng-change="$addOnUpdate\$parent.${parent.name}DS.setModified($GRID_ITEM)" $attributes />
                          """
                    if (parent.type==COMP_TYPE_DETAIL) {
                        txt = """<div ${getId("container-"+idTxtParam)} $styleStr><label ${getId("label-"+idTxtParam)}>${tran("label")}</label>

                                 <span ${getId(idTxtParam)} $styleStr> $txt </span></div> """
                    }
                } else {
                    // TODO do we need a value field if ng-model is defined?  //added defaultValue
                    attributes += " ${readonly?"readonly":""}"
                    txt =  """<label ${getId("label")} $styleStr for="${name?name:model}">${tran("label")}</label>
                              <input ${getId()}  $styleDatepicker $styleStr type="$t"   name="${name?name:model}" id="${name?name:model}" ${value?"value=\"{{${CompileService.parseVariable(value)}}}\"":"" }
                               ${defaultValue()} $attributes """
                    if (model && !readonly) {
                        if (binding != BINDING_PAGE)
                            txt+="ng-model=\"${ID}DS.currentRecord"    // use DataSet current record
                        else
                            // there may be a value instead of a model
                            txt+= """ng-model="$modelRoot"""
                        if (modelComponent)
                            txt+=".$modelComponent"
                        txt+='" '
                    }
                    // handle change event
                    if (onUpdate) {
                        txt += """ng-change="${name}_onUpdate()"  """
                        println "****WARNING**** check if  property ${name}_onUpdate() is generated - possibly an inconsistency in generator"
                    }
                    txt+="/>\n"
                }
                return txt
            case COMP_TYPE_BOOLEAN:
                def txt ="""<input ${getId(idTxtParam)} $styleStr type="checkbox" name="${name?name:model}"
                           ${booleanTrueValue?"ng-true-value=\"$booleanTrueValue\"":""}  ${booleanFalseValue?"ng-false-value=\"$booleanFalseValue\"":""}
                           ${value?"value=\"{{$value}}\"":"" } ${defaultValue()}"""  // is value needed ever? Doesn't do anything if ng-model is used.
                // add change event handler for items in a table so the item can be marked dirty for save
                if (isDataSetEditControl(parent)) {
                    def addOnUpdate=""
                    if (onUpdate)  {  //ToDo: add this to other controls in Grid/Detail
                        addOnUpdate="\$parent.${parent.ID}_${name}_onUpdate($GRID_ITEM);"
                    }
                    return txt + """ ng-change="$addOnUpdate\$parent.${parent.name}DS.setModified($GRID_ITEM)" ${(parent.allowModify && !readonly)?"":"readonly"} ng-model="$GRID_ITEM.${model}"
                                      /> """
                }
                else  {
                    // handle change event
                    if (onUpdate) {
                        txt += """ng-change="${name}_onUpdate()"  """
                        //println "****WARNING**** check if  property ${name}_onUpdate() is generated - possibly an inconsistency in generator"
                    }
                    // if not in a table, add label for checkbox
                    return txt +  """ ${getId()} ng-model="$model" ${readonly?"readonly":""} /> <label ${getId("label")}>${tran("label")}</label>
                    """
                }
            case COMP_TYPE_SUBMIT:
                return """<input ${getId()} $styleStr type="submit" value="${tran("label")}"/>
                """
            case COMP_TYPE_BUTTON:
                // TODO for SQL generate the action ID for each method, assign ID to each click action
                if (onClick)
                    return """<button ${getId(idTxtParam)} $styleStr ng-click="${name}_onClick()">${tran("label")}</button>\n"""

            case COMP_TYPE_RESOURCE:      //fall through
            case COMP_TYPE_DATA:
            case COMP_TYPE_FLOW:
                // nothing to generate in HTML
                return ""
            default :
                // TODO log and ignore not implemented component
                println "***Not supported component: $type ${name?name:model}"
                return ""
        }
    }

    // handle non-terminal nodes
    String componentEnd(String t, int depth=0) {
        switch (t) {
            case COMP_TYPE_PAGE:
                return  "</div>\n</body>\n"
            case COMP_TYPE_FORM:
                def nextTxt = ""
                if (root.flowDefs || submit) {
                    def labelStr = ""
                    if (root.flowDefs) {
                        if (submitLabel) {
                            def key ="${getPropertiesBaseKey()}.submitAndNextLabel"
                            labelStr = "$submitLabel and $nextButtonLabel"
                            tran(key,labelStr)
                        } else {
                            if ( nextButtonLabel == NEXT_BUTTON_DEFAULT_LABEL)
                                labelStr = tranGlobal("flow.next.label",NEXT_BUTTON_DEFAULT_LABEL)
                            else
                                labelStr = tran("nextButtonLabel")
                        }
                        //labelStr += labelStr?" and $nextButtonLabel":nextButtonLabel
                    } else {
                        labelStr = tran('submitLabel')
                    }
                    nextTxt += """<div ${getId("submit-container")} $styleStr>
                    <input ${getId("submit")} type="submit" value="$labelStr"/>
                    </div>
                    """
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
        def importPath = "../../cssRender"
        def cssImp = ""
        //split the string into a list
        def is = importCSS?.tokenize(',')
        for (css in is) {
            cssImp += """<link type="text/css" rel="stylesheet" href="$importPath?name=${css.trim()}" />\n"""
        }

"""
<head>
<!-- sitemesh -->
<meta name="layout" content="bannerSelfServicePBPage"/>
<!--
<meta name="menuEndPoint" content="\${request.contextPath}/ssb/menu"/>
<meta name="menuBaseURL" content="\${request.contextPath}/ssb"/>
-->
<!--meta name="layout" content="simple"/-->
<title>${tran("title")}</title>
<script>
var pageID = "$name"
 // inject services and controller modules to be registered with the global ng-app
 var myCustomServices = ['ngResource','ngGrid','ui', 'pbrun.directives'];
</script>
<!-- inject global functions -->
 <script type="text/javascript">
    // Inject controller code here
    $CONTROLLER_PLACEHOLDER
</script>

<!-- import custom stylesheets -->
$cssImp
</head>
<body>
   <div id="content" ng-controller="CustomPageController"  class="customPage">
   ${label?"<h1 ${getId('label')}>${tran("label")}</h1>":""}
 """
    }

    def compileComponent(String inS, int depth=0){
        String result=inS
        result += componentStart(type, depth)
        if (this.type != COMP_TYPE_GRID && this.type != COMP_TYPE_DETAIL) {   //grid, detail will take care of its child objects
            components.each {
                //def child=inherit(new PageComponent(it))
                // JSON parser does not automatically convert child components to pageComponent(s) type
                //def child=new PageComponent(it)
                result=it.compileComponent(result, depth+1)
            }
        }
        def endString = componentEnd(type, depth+1)
        if (endString)
            result += endString
        return result
    }

    //recursively find components with types in componentTypes
    def findComponents(componentTypes) {
        def result=[]
        if (componentTypes.contains(this.type))
            result << this
        components.each {
            result += it.findComponents(componentTypes)
        }
        return result
    }

}
