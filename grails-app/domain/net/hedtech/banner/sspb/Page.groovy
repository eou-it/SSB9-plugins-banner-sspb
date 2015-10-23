package net.hedtech.banner.sspb

import grails.converters.JSON
import groovy.util.logging.Log4j
@Log4j
class Page {

    static hasMany = [pageRoles: PageRole, extensions: Page] //Optional child page(s) (sub classes)
    Page extendsPage     //Optional parent page (super class), see constraints

    String constantName
    String modelView
    //String mergedModel
    //String diffModelView
    String compiledView
    String compiledController


    Date dateCreated
    Date lastUpdated
    Date fileTimestamp

    static constraints = {
        constantName       nullable: false , unique: true, maxSize: 60
        modelView          nullable: false , maxSize: 1000000, widget: 'textarea'
        compiledView       nullable: true  , maxSize: 1000000, widget: 'textarea'
        compiledController nullable: true  , maxSize: 1000000, widget: 'textarea'
        extendsPage        nullable: true
        //dateCreated     nullable:true
        lastUpdated     nullable:true
        fileTimestamp   nullable:true
    }

    static mapping = {
        //autoTimestamp true
        datasource 'sspb'
        //uncomment first time if db object is created
        //modelView type: "clob"
        //compiledView type: "clob"
        //compiledController type: "clob"
    }

    static transients = ['mergedModelText', 'getMergedModelMap']

    boolean isEmptyInstance() {
        return (
            modelView.equals("{}") &&
            compiledView == null &&
            compiledController == null
        )
    }


    //Methods for merging models and getting deltas

    //Use a common static method to convert json model to map
    private static Map modelToMap(String model) {
        //JSON.parse(model)
        def slurper = new groovy.json.JsonSlurper()
        return slurper.parseText(model)
    }

    //Use a common static method to convert map to json string
    private static String modelToString(Map model) {
        return (model as JSON).toString(true)
    }

    //Member method to get the merged modelView as a JSON text
    String getMergedModelText(){
        extendsPage?modelToString(getMergedModelMap()):modelView
    }

    //Member method to get the merged modelView as a Map
    Map getMergedModelMap() {
        def result
        if (extendsPage) {
            //Should never return null
            if (!modelView || modelView.equals("null") || modelView.equals("{}")) {
                result = extendsPage.getMergedModelMap()
            } else {
                def base = decomposeModel(extendsPage.getMergedModelMap())
                def diff = modelToMap(modelView)
                if (diff.containsKey('deltas')) {
                    result = applyDiffs(base, diff.deltas)
                    log.info "Merged page models"
                } else {
                    result = modelToMap(modelView)
                }
            }
        } else {
            result = modelToMap(modelView)
        }
        result
    }

    //Member method to retrieve the delta for a given full page model text in JSON text format
    String diffModelViewText(model) {
        return modelToString(diffModelView(model))
    }

    //Member method to retrieve the delta for a given full page model text in JSON text format
    private Map diffModelView(model) {
        def base = extendsPage.getMergedModelMap()
        diffModelView(model, base)
    }

    // Static method to determine the difference  between the full page model and this.extendsPage
    private static Map diffModelView(mergedModelView, base){
        def start = new Date()
        def baseProps = propertyMap(base)
        def extProps = propertyMap(mergedModelView)
        def diff = [:]
        // iterate over all keys in base and ext props
        [baseProps,extProps]*.keySet().flatten().unique().each { k ->
            def hasBase = baseProps.containsKey(k)
            def hasExt = extProps.containsKey(k)
            if (hasBase && hasExt) {
                //Both base and ext have the property, record if different
                if (baseProps[k] != extProps[k] ) {
                    diff[k] = [base: baseProps[k], ext:extProps[k]]
                }
            } else {
                // Record the property that exists
                diff[k] = hasBase? [base: baseProps[k]]: [ext: extProps[k]]
            }
        }
        diff = convertDiff(diff)
        log.info "Determined diff in ${new Date().getTime()-start.getTime()} ms. diff= $diff"
        diff
    }

    //Helper method to convert the properties into the common delta format
    private static Map convertDiff(propsDiff) {
        def result = [:]
        //convert the diff properties to a map
        propsDiff.each{ k,v ->
            def nameProp =  k.split(':')
            def name=nameProp[0]
            def prop=nameProp[1]
            if (!result[name]) {
                result[name] = [:]
            }
            result[name][prop] = v //v.ext
        }
        //println "Difference :\n${result as JSON}"
        [deltas: result]
    }

    private static def componentName(component, parent, siblingIndex) {
        return component? component.name ? component.name : "${parent?.name}_child_${siblingIndex}":null
    }

    private static Map propertyMap(String mergedModelString) {
        propertyMap(modelToMap(mergedModelString))
    }

    private static Map propertyMap(Map comp, int siblingIndex=0, Map parent=null, Map next = null) {
        def props = [:]
        def decomposed = decomposeComponents(comp).components
        decomposed.each {
            it.value.each { key, val ->
                if (key!='components') {
                    props[it.key + ':' + key] = val
                }
            }
        }
        props
    }

    private static Map applyDiffs(Map decomposedBasePage, Map diffs ) {
        def model = decomposedBasePage
        model.added=[:]
        model.conflicts=[]
        diffs.each { name, diff ->
            //println "Processing $name"
            def comp = model.components[name]
            if (comp) { // Component exists, change props to match the extension
                diff.each { prop, val ->
                    if (val.base.equals(comp[prop]) || prop!='meta') {
                        // accept change as is. Todo: handle parameters and validation in a better way
                        if (val.ext) {
                            comp[prop] = val.ext
                        }
                    } else {
                        if (prop=='meta') {
                            def type=diff.meta.base.nextSibling.equals(comp.meta.nextSibling)?"newParent":"reOrder"
                            model.conflicts<<[type:type, diff:diff, comp:comp]
                            log.warn "Component $name: detected meta change in baseline ${val.base} to ${comp[prop]}. Change to be applied: ${val.ext}"
                        }
                    }
                }
            } else {
                //if we are here, a new component is added by the extension or a baseline component is removed
                //a new component must at least have a type attribute
                if (diff.type) {
                    diff.each { prop, val ->
                        if (val.ext) {
                            if (!comp) {
                                comp = [:] //we have an extension property so create the component
                            }
                            comp[prop] = val.ext
                        }
                        if (prop == 'meta') {
                            comp[prop].newComponent = true
                        }
                    }
                    if (comp) { //add the component to the result
                        model.components[name] = comp
                        model.added[name] = comp
                        log.info "New component ${comp.name} added "
                    }
                } else {
                    model.conflicts<<[type:"removedBaseline", diff:diff, comp:[name:name]]
                }
            }
        }
        composeComponents(model)
    }


    private Map decomposeModel(model) {
        decomposeComponents(model)
    }

    private static Map decomposeComponents(Map comp, int siblingIndex=0, Map parent=null, Map next = null,Map result =[:]) {
        if (comp.type == 'page') {
            result = [root: comp.name, components:[:]]
        }
        Map clone = comp.clone()
        clone.meta=[:]
        if (next) {
            clone.meta.nextSibling=componentName(next,parent,siblingIndex+1)
        }
        if (parent) {
            clone.meta.parent=parent.name
        }
        if (comp.components) {
            clone.remove('components')
            def nSiblings = comp.components ? comp.components.size() - 1 : 0
            if (nSiblings >= 0) {
                clone.meta.firstChild = componentName(comp.components[0],parent,0)
                comp.components?.eachWithIndex { entry, i ->
                    def nextSibling = i < nSiblings ? comp.components[i + 1] : null
                    result = decomposeComponents(entry, i, comp, nextSibling,result)
                }
            }
        }
        result.components[componentName(comp, parent, siblingIndex)] = clone
        result
    }

    //compose the model from a decomposed model
    private static Map composeComponents(Map decomposedModel, boolean cleanMeta = true) {
        decomposedModel = resolveConflicts(decomposedModel)
        Map root = decomposedModel.components[decomposedModel.root]
        if (root.meta.firstChild) {
            def child = decomposedModel.components[root.meta.firstChild]
            root.components = getSiblings(decomposedModel,child, root)
        }
        if (cleanMeta) {
            root.remove('meta')
        }
        root
    }

    private static def resolveConflicts(model){
        model.conflicts.each{ conflict ->
            if (conflict.type=="reOrder") {
                def nextName = conflict.comp.meta.nextSibling
                def extNextName = conflict.diff.meta.ext?.nextSibling
                def extNextComp = model.added[extNextName]
                if (extNextComp) {
                    conflict.comp.meta.nextSibling = extNextName
                    extNextComp.meta.nextSibling = nextName
                    log.warn "Resolved baseline reorder conflict by inserting new component $extNextName after component ${conflict.comp.name}"
                } else {
                    log.warn "Unresolved conflict. No new component found matching the nextSibling of component ${conflict.comp.name}"
                }
            } else if (conflict.type=="removedBaseline") {
                log.warn "Resolve removed baseline component conflict"
                def resolvedStatusMessage = "Unresolved"
                def removedNextName = conflict.diff.meta.ext.nextSibling
                def addedNoReference = model.added[removedNextName]

                if (addedNoReference) {
                    def addedNext = model.components[addedNoReference.meta.nextSibling]
                    if (addedNext) {
                        def addedNextReference = model.components.find {
                            it.value.meta?.nextSibling == addedNext.name
                        }
                        if (addedNextReference){
                            addedNextReference.value.meta.nextSibling = addedNoReference.name
                            resolvedStatusMessage = "Resolved baseline remove component conflict by inserting new component ${removedNextName} before component ${addedNext.name}"
                        } else {
                            resolvedStatusMessage = "Unable to find component referencing $addedNext.name - need to add somewhere else"
                        }
                    }
                }
                log.warn resolvedStatusMessage
            }
        }
        model
    }

    //Get a list of siblings given the first sibling
    private static List getSiblings(Map flatModel, Map first, parent, boolean cleanMeta = true) {
        def result = parent.components?parent.component:[]  //.remove('meta')
        def next = first
        log.info "getSiblings for ${parent.name}"
        for (; next != null; ) {
            //if (result.contains(next)) {
            if (next.meta.added) {
                log.warn "Prevented adding existing child ${next.name} of ${parent.name}. Sequence is broken. \n    TODO: check all items with parent ${parent.name} are included"
                next = null //
            } else {
                log.trace "Added ${next.name} to ${parent.name}"
                next.meta.added = true
                if (next.meta.firstChild) {
                    next.components = getSiblings(flatModel,flatModel.components[next.meta.firstChild],next )
                }
                result << next
                next = next.meta.nextSibling ? flatModel.components[next.meta.nextSibling] : null
                if (next) {
                    log.trace "Next: ${next.name}"
                }
            }
        }
        if ( cleanMeta ) {
            result.each {
                it.remove('meta')
            }
        }
        result
    }
}