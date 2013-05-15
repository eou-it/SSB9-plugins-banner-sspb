
/**
 * Created with IntelliJ IDEA.
 * User: hvthor
 * Date: 14-2-13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
package net.hedtech.banner.sspb

class PageComponentAjs {
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
    //final static COMP_TYPE_FUNCTION = "function"

    // Types that can represent a single field
    final static COMP_ITEM_TYPES = [COMP_TYPE_LITERAL,COMP_TYPE_DISPLAY,COMP_TYPE_TEXT,COMP_TYPE_TEXTAREA,COMP_TYPE_NUMBER,
                                    COMP_TYPE_DATETIME,COMP_TYPE_EMAIL,COMP_TYPE_TEL,COMP_TYPE_LINK,COMP_TYPE_BOOLEAN ]

    // Types that have a DataSet associated  - not completely orthogonal yet. COMP_ITEM_TYPES can have it too
    final static COMP_DATASET_TYPES = [COMP_TYPE_GRID,COMP_TYPE_LIST,COMP_TYPE_SELECT,COMP_TYPE_DETAIL,COMP_TYPE_DATA, COMP_TYPE_RADIO]

    final static COMP_UICTRL_TYPES = COMP_DATASET_TYPES //not sure if they ever will be different

    final static BINDING_REST = "rest"
    final static BINDING_SQL = "sql"
    final static BINDING_PAGE = "page"
    final static BINDING_API = "api"

    final static GRID_ITEM="item"
    final static LIST_ITEM="item"
    final static SELECT_ITEM="item"
    final static CURRENT_ITEM = "current_item"
    final static CONTROLLER_PLACEHOLDER="###CONTROLLER###"
    final static VAR_PRE = '$'  //page model variable prefix
    final static VAR_RES = '$$' // page model reserved variable prefix



    String type        // -> Generic property for component type
    String name        // -> Generic property. Maybe use componentId?
    String title       // -> Page
    String scriptingLanguage = "JavaScript" // -> page property that specifies the scripting language to use in page model
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
    def nextButtonLabel="Next"
    def lastButtonLabel="Finish"
    def submitLabel = ""
    //String condition

    // data
    String onLoad=""
    Boolean loadInitially = false   // specify if the data should be loaded after page is loaded

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

    // link property
    String description
    String url
    String imageUrl

    String style        // TODO add styling support

    // data properties
    String resource    // -> Form Data Component. uri: <path>/resourceName e.g. rest/todo
    String binding = BINDING_PAGE    // method of data binding (sql, api, rest, page)

    def validation       // specify any validation definition as a map
    String onUpdate     // an code blovk to execute if the current component value is changed

                        // TODO deduce this from model automatically
    String ID           // generated ID, do we need this?
    String documentation    // for page development documentation purpose, not used by code generator

    List<PageComponentAjs> components    // child components

    // internal use properties
    PageComponentAjs parent    // record parent component for special code generation in grid, etc.
    PageComponentAjs root      // the root (page) component

    def flowDefs = []       // global flow definitions, a list of map {flowName:"", sequence:["form1","form2"], condition, ""}
    def activeFlow = ""     // the initially activated flow
    def formSet = []        // the set of all form names on this page

    // map the validation key to angular attributes ? use HTML 5 validation instead with form
    // def validationKeyMap = ["minlength":"ngMinlength", "maxlength":"ngMaxlength", "pattern":"ngPattern"]

    def recordControlPanel()  {
        def dataSet    =  "${name}DS"
        def arrayName  =  "${name}DS.data"
        def uiControl =  "${name}UICtrl"

        def result =
        """
        <!-- pagination -->
        <button ng-disabled="${uiControl}.currentPage == 0" ng-click="${uiControl}.currentPage=${uiControl}.currentPage - 1">
                Previous
        </button>
            {{${uiControl}.currentPage+1}}/{{${uiControl}.numberOfPages()}}
        <button ng-disabled="${uiControl}.currentPage >= ${arrayName}.length/${uiControl}.pageSize - 1" ng-click="${uiControl}.currentPage=${uiControl}.currentPage + 1">
                Next
        </button><br>
        """

        if (allowNew) {
            result += """ <button ng-click="${dataSet}.add()"> Add New  </button>"""
        }
        if (allowModify || allowDelete) {
            result += """ <button ng-click="${dataSet}.save()"> Save </button>"""
        }
        if (allowReload) {
            result += """ <button ng-click="${dataSet}.load()"> Refresh </button> """
        }
        return result
    }

    /*
    Special compilation for generating table specific controls
     */
    def gridCompile(int depth=0) {
        def dataSet    =  "${name}DS"
        def uiControl =  "${name}UICtrl"
        def repeat = "$GRID_ITEM in ${dataSet}.data"
        //implement as table for now
        // generate table column headers
        def thead=""
        def items=""

        // add a delete checkbox column if allowDelete is true
        if (allowDelete) {
            thead = "<th>Delete</th>"
            items = """
            <td>
             <input ng-click="${dataSet}.delete($GRID_ITEM)" type="checkbox" style="width:20%"/>
            </td>
            """
        }
        // generate all table columns from the data model
        components.each { child ->
            child.parent = this
            //get the labels from child components
            thead+="<th>$child.label</th>"
            //get the child components
            child.label=""
            items+="<td>${child.compileComponent("", depth)}</td>\n"
        }

        def result =
            """<table>
            <tr>$thead</tr>
            <!-- Do this for every object in objects -->
            <tr ng-repeat="$repeat | startFrom:${uiControl}.currentPage * ${uiControl}.pageSize | limitTo:${uiControl}.pageSize">
              $items
            </tr>
            </table>
            """
        result +=  recordControlPanel()
        return result
    }

    /* special compilation for details list
       refactoring to use the 'same' model as a grid
     */
    def detailCompile(int depth=0) {
        def dataSet    =  "${name}DS"
        def uiControl =  "${name}UICtrl"
        def repeat = "$GRID_ITEM in ${dataSet}.data"    //GRID_ITEM is confusing

        def result = ""
        if (label)
            result += "<label>$label</label>\n"
        result +="""<table ng-repeat="$repeat | startFrom:${uiControl}.currentPage * ${uiControl}.pageSize | limitTo:${uiControl}.pageSize" >\n"""

        // add a delete checkbox column if allowDelete is true
        // def PageComponentAjs delete = new PageComponentAjs()
        //*
        if (allowDelete) {
            result += """
            <tr>
                <td style="text-align:right">
                    <input ng-click="${dataSet}.delete($GRID_ITEM)" type="checkbox" />
                </td>
                <td style="text-align:left"> <strong>Delete</strong></td>
             </tr>
            """
        }
        //*/
        // generate all table columns from the data model
        components.each { child ->
            //def child=new PageComponent(it)
            //child.parent = this
            //get the labels from child components
            //get the child components
            result+="${child.compileComponent("", depth)}\n"
        }
        result+= "</table>\n"

        result +=  recordControlPanel()
        return result
    }

    /*
    Special compilation for generating table specific controls
     */
    def listCompile(int depth=0) {

        def dataSet    =  "${name}DS"
        def uiControl =  "${name}UICtrl"

        def txt = ""

        def repeat = "$LIST_ITEM in ${dataSet}.data"

        if (label)
            txt += "<label>$label</label>"
        // handle click event
        def click_txt=""
        if (onClick)
            click_txt = "ng-click=${name}_onClick($LIST_ITEM)"
        txt +=
            """<ul>
            <li $click_txt ng-repeat="$repeat | startFrom:${uiControl}.currentPage * ${uiControl}.pageSize | limitTo:${uiControl}.pageSize">
             ${onClick?"<a href=\"\">":""} {{$LIST_ITEM.$value}}  ${onClick?"</a>":""}
            </li>
            </ul>
            """
        txt += recordControlPanel()
        return txt
    }

    def radioCompile(int depth=0) {
        def arrayName = "${name}DS.data"
        def result
        def ngModel = name
        def labelTxt = label? """<label for="${name?name:model}">$label</label>""":""
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
        updateTxt += onUpdate?"${name}UICtrl.onUpdate();":""
        updateTxt = updateTxt?"ng-change=\"$updateTxt\"":""

        def radio = """<div ng-repeat="$SELECT_ITEM in $arrayName" $initTxt>
            <input type="radio"   ng-model=$ngModel name="$nameTxt" $updateTxt
                value="{{$SELECT_ITEM.$valueKey}}"/> {{$SELECT_ITEM.$labelKey}}
        </div>"""
        if(parent.type == COMP_TYPE_DETAIL) {
            result = """<tr><td style="text-align:right; width: 15%"><strong>${label?"$label:":""}</strong></td><td style="text-align:left;">
                             $radio
                             </td></tr>"""
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
            heading = "<h$headingLevel>$label</h$headingLevel><br>"
        }


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
                def labelTxt = label? """<label for="${name?name:model}">$label</label>""":""
                def updateTxt = ""
                def placeholderStr = placeholder?"""<option value="">$placeholder</option>""":""

                if(parent.type == COMP_TYPE_DETAIL) {
                    ngModel =  "$GRID_ITEM.${model}"
                    updateTxt +="\$parent.${parent.name}DS.setModified($GRID_ITEM); ${name}DS.setCurrentRecord($ngModel);"
                } else if (parent.type == COMP_TYPE_GRID) {
                    ngModel =  "$GRID_ITEM.${model}"
                    updateTxt +="\$parent.${parent.name}DS.setModified($GRID_ITEM); ${name}DS.setCurrentRecord($ngModel);"
                }
                updateTxt += onUpdate?"${name}UICtrl.onUpdate();":""
                updateTxt = updateTxt?"ng-change=\"$updateTxt\"":""
                def select = """<select   ng-model="$ngModel" $updateTxt
                                           ng-options="$SELECT_ITEM.$valueKey as $SELECT_ITEM.$labelKey for $SELECT_ITEM in $arrayName">
                                   $placeholderStr
                                </select>"""
                if(parent.type == COMP_TYPE_DETAIL) {
                    result = """<tr><td style="text-align:right; width: 15%"><strong>${label?"$label:":""}</strong></td><td style="text-align:left;">
                             $select
                             </td></tr>"""
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

                return """<div  id="$name" ng-show="${name}_visible"> $heading
                """
            case COMP_TYPE_FORM:
                def txt = ""

                // handle flow controlled
                def submitStr=""
                if (submit)
                    submitStr+=submit
                if (root.flowDefs)
                    submitStr+= "; _activateNextForm('$name');"

                txt += """<form name="${name?name:model}" ng-show="${name}_visible"  ${submitStr?"""ng-submit="$submitStr" """:""}>$heading
                """
                return txt
            case COMP_TYPE_LITERAL:
                return CompileService.parseLiteral(value) + "\n"
            case COMP_TYPE_DISPLAY:
                def ret = ""
                if (parent.type == COMP_TYPE_DETAIL) {
                    ret += "<tr>"
                    // for display in detail control take the parent model, and make a table row
                    ret += """<td  style="text-align:right; width: 15%"><strong> ${label?"$label:":""}</strong></td><td style="text-align:left;">
                              {{$GRID_ITEM.${model}}}</td>"""
                    ret += "</tr>"
                } else if (parent.type == COMP_TYPE_GRID) {
                    ret = "{{ $GRID_ITEM.${model} }}";
                } else
                    // otherwise the value is used
                    // TODO consolidate value and sourceModel?
                    // TODO is parseVariable still working after using DataSet as generic data object?
                    ret += label?"<label>$label:</label>":"" + value?"value=\"{{${CompileService.parseVariable(value)}}}\"":""
                return ret
            case COMP_TYPE_LINK:
                def ret = "<div>"
                def desc = description?description:url
                // otherwise the value is used
                // TODO consolidate value and sourceModel?
                // TODO is parseVariable still working after using DataSet as generic data object?
                ret += label?"<label>$label</label>":""
                ret +=  """<a href="$url">$desc</a></div>"""
                return ret

            case COMP_TYPE_TEXT:
            case COMP_TYPE_TEXTAREA:
            case COMP_TYPE_NUMBER:
            case COMP_TYPE_DATETIME:
            case COMP_TYPE_EMAIL:
            case COMP_TYPE_TEL:
                def txt = ""
                def validateStr = ""
                if (validation) {
                    validateStr = validation.collect { k,v -> "$k=\"$v\"" }.join(' ')
                }
                def attributes = "$validateStr ${required?"required":""} ${placeholder?"placeholder=\"$placeholder\"":""}".trim()
                def typeString= "type=\"$t\""
                if (type == COMP_TYPE_DATETIME)  //TODO localize format
                    typeString=" ui-date=\"{dateFormat:'dd-M-yy', changeMonth: true, changeYear: true}\" "
                //Cannot choose format with time, but lots of options. See http://jqueryui.com/datepicker/
                if (attributes) println "Attributes: $attributes"
                if (parent.type==COMP_TYPE_GRID)
                    txt = """
                          <input $typeString   name="${name?name:model}" id="${name?name:model}" ${parent.allowModify?"":"readonly"}
                          ng-model="$GRID_ITEM.${model}"
                          ng-change="\$parent.${parent.name}DS.setModified($GRID_ITEM)" $attributes />
                          """
                else if (parent.type==COMP_TYPE_DETAIL) {
                    txt = """<tr><td style="text-align:right; width: 15%"><strong>${label?"$label:":""}</strong></td>"""
                    txt+= """<td style="text-align:left;">
                          <input $typeString   name="${name?name:model}" id="${name?name:model}" ${parent.allowModify?"":"readonly"}
                          ng-model="$GRID_ITEM.${model}"
                          ng-change="\$parent.${parent.name}DS.setModified($GRID_ITEM)" $attributes />
                          </td></tr>
                          """
                } else {
                    // TODO do we need a value field if ng-model is defined?
                    attributes += " ${readonly?"readonly":""}"
                    txt =  """<label for="${name?name:model}">$label</label>
                              <input type="$t"   name="${name?name:model}" id="${name?name:model}" ${value?"value=\"{{${CompileService.parseVariable(value)}}}\"":"" }
                              $attributes """
                    if (model && !readonly) {
                        if (binding != BINDING_PAGE)
                            //txt+= "ng-model=\"_${modelRoot.toLowerCase()}_${ID}"
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
                def txt ="""<input type="checkbox" name="${name?name:model}" id="${name?name:model}"
                           ${booleanTrueValue?"ng-true-value=\"$booleanTrueValue\"":""}  ${booleanFalseValue?"ng-false-value=\"$booleanFalseValue\"":""}
                           ${value?"value=\"{{$value}}\"":"" } """
                // add change event handler for items in a table so the item can be marked dirty for save
                if (parent.type==COMP_TYPE_GRID || parent.type==COMP_TYPE_DETAIL )
                    return txt + """ ng-change="\$parent.${parent.name}DS.setModified($GRID_ITEM)" ${(parent.allowModify && !readonly)?"":"readonly"} ng-model="$GRID_ITEM.${model}"/> """
                else  {
                    // handle change event
                    if (onUpdate) {
                        txt += """ng-change="${name}_onUpdate()"  """
                        println "****WARNING**** check if  property ${name}_onUpdate() is generated - possibly an inconsistent"
                    }
                    // if not in a table, add label for checkbox
                    return txt +  """ng-model="$model" ${readonly?"readonly":""} /> <label for="${name?name:model}">$label</label>
                    """
                }
            case COMP_TYPE_SUBMIT:
                return """<input type="submit" value="$label"/>
                """
            case COMP_TYPE_BUTTON:
                // TODO for SQL generate the action ID for each method, assign ID to each click action
                if (onClick)
                    return """<button ng-click="${name}_onClick()">$label</button>\n"""

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
                    if (submitLabel)
                        labelStr+=submitLabel
                    if (root.flowDefs)
                        labelStr += labelStr?" and $nextButtonLabel":nextButtonLabel
                    nextTxt += """<div>
                    <input type="submit" value="$labelStr"/>
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
"""\
<head>
<!-- sitemesh -->
<meta name="layout" content="BannerXECustomPage"/>

<!--meta name="layout" content="simple"/-->

<title>$title</title>

<script>
//Test New Compile
// inject unique page ID
var pageID = "$name"

 // inject services and controller modules to be registered with the global ng-app
 var myCustomServices = ['ngResource','ngGrid','ui'];


</script>

<!-- inject global functions -->

 <script type="text/javascript">

    // Inject controller code here
    $CONTROLLER_PLACEHOLDER
</script>


<style type="text/css">
    .scrollable {  overflow-y:scroll; overflow-x:auto;  }
</style>
</head>
<body>
<style>
    *.margin
    {
        margin-top: 10px;
        margin-left:10px;
        margin-right:10px;
    }
</style>

   <div ng-controller="CustomPageController"  class="margin">
   ${label?"<h1>$label</h1>":""}
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
