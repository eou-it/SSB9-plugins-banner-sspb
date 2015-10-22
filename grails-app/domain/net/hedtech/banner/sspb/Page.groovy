package net.hedtech.banner.sspb

import grails.converters.JSON

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

    //Use a single static method to convert model to map
    private static Map modelToMap(String model) {
        def slurper = new groovy.json.JsonSlurper()
        return slurper.parseText(model)
    }

    private static String modelToString(Map model) {
        return (model as JSON).toString(true)
    }

    String getMergedModelText(){
        extendsPage?modelToString(getMergedModelMap()):modelView
    }

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
                    println "Merged page models"
                } else {
                    result = modelToMap(modelView)
                }
            }
        } else {
            result = modelToMap(modelView)
        }
        result
    }

    String diffModelViewText(model) {
        return modelToString(diffModelView(model))
    }

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
        println "Determined diff in ${new Date().getTime()-start.getTime()} ms. diff= $diff"
        diff
    }

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

    private static Map propertyMap(String mergedModelString) {
        //JSONObject mergedModelJSON = JSON.parse(mergedModelString)
        propertyMap(new groovy.json.JsonSlurper().parseText(mergedModelString))
    }

    private static def componentName(component, parent, siblingIndex) {
        return component? component.name ? component.name : "${parent?.name}_child_${siblingIndex}":null
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
        diffs.each { name, diff ->
            //println "Processing $name"
            def comp = model.components[name]
            if (comp) { // Component exists, change props to match the extension
                //println "found existing component"
                diff.each { prop, val ->
                    if (val.base.equals(comp[prop])) {
                        // baseline not changed
                        if (val.ext) {
                            comp[prop] = val.ext
                        }
                    } else {
                        if (prop=='meta') {
                            println "Component $name: detected meta change in baseline ${val.base} to ${comp[prop]} "
                            println "Change to be applied: ${val.ext}"
                            //comp[prop] = val.ext
                            // fix chain if both baseline components are before the inserted component
                            // is it possible to do this in a generic way?
                            def nextComp = model.components[comp[prop].nextSibling]
                            if (nextComp) {
                                nextComp[prop] = val.ext
                            }
                        } else {
                            // Use properties
                            if (val.ext) {
                                comp[prop] = val.ext
                            }
                            //println "Component $name: detected change in baseline property $prop from ${val.base} to ${comp[prop]}"
                        }
                    }
                }
            } else {

                diff.each { prop, val ->
                    if (val.ext) {
                        if (!comp) {
                            comp=[:] //we have an extension property so create the component
                        }
                        comp[prop] = val.ext
                    }
                    if (prop=='meta') {
                        comp[prop].newComponent=true
                    }
                }
                if (comp) { //add the component to the result
                    model.components[name]=comp
                    println "New component ${comp.name} added "
                }
            }
        }
        composeComponents(model)
    }


    Map decomposeModel(model) {
        decomposeComponents(model)
    }

    static Map decomposeComponents(Map comp, int siblingIndex=0, Map parent=null, Map next = null,Map result =[:]) {
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
        result.components[componentName(comp,parent,siblingIndex)]=clone
        result
    }

    //compose the model from a decomposed model
    static Map composeComponents(Map decomposedModel, boolean cleanMeta = true) {
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

    //Get a list of siblings given the first sibling
    static List getSiblings(Map flatModel, Map first, parent, boolean cleanMeta = true) {
        def result = parent.components?parent.component:[]  //.remove('meta')
        def next = first
        println "getSiblings for ${parent.name}"
        for (; next != null; ) {
            //if (result.contains(next)) {
            if (next.meta.added) {
                println "Prevented adding existing child ${next.name} of ${parent.name}. Sequence is broken. \n    TODO: check all items with parent ${parent.name} are included"
                next = null //
            } else {
                println "Added ${next.name} to ${parent.name}"
                next.meta.added = true
                if (next.meta.firstChild) {
                    next.components = getSiblings(flatModel,flatModel.components[next.meta.firstChild],next )
                }
                result << next
                next = next.meta.nextSibling ? flatModel.components[next.meta.nextSibling] : null
                if (next) {
                    println "Next: ${next.name}"
                }
            }
        }
        if (false && cleanMeta) {
            result.each {
                it.remove('meta')
            }
        }
        result
    }
}