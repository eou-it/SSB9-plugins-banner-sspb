/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.converters.JSON
import groovy.util.logging.Log4j
import difflib.*

@Log4j
class Page {

    static hasMany = [pageRoles: PageRole, extensions: Page] //Optional child page(s) (sub classes)

    Page extendsPage     //Optional parent page (super class), see constraints

    String constantName

    String modelView

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
        lastUpdated     nullable: true
        fileTimestamp   nullable: true
    }

    static mapping = {
        //autoTimestamp true
        datasource 'sspb'
        //uncomment first time if db object is created
        //modelView type: "clob"
        //compiledView type: "clob"
        //compiledController type: "clob"
    }

    static transients = ['mergedModelText', 'mergedModelMap']


    boolean isEmptyInstance() {
        return (
                modelView.equals("{}") &&
                        compiledView == null &&
                        compiledController == null
        )
    }

    //Compare the models
    boolean equals(Map modelMap){
        def thisMap = getMergedModelMap(false)        // get the model of this as a map without mergeInfo
        thisMap.equals(cleanModelMap(modelMap)) // clean the input map to compare to and check for equality
    }

    boolean equals(String model) {
        def modelMap = modelToMap(model)
        equals(modelMap)
    }

    //Remove any mergeInfo recursively
    static Map cleanModelMap(Map modelMap) {
        def result = cleanComponent(modelMap,false)
        result.components.each{ component ->
            component = cleanModelMap(component)
        }
        result
    }

    //Keys map to avoid hardcoding the key strings multiple times
    final static Map KEYS = [page: null, deltas: null, meta: null, components: null, mergeInfo: null, noReference: null,
                             idx: null, ext: null].collectEntries{ k, v -> [k,k] }
    //Methods for merging models and getting deltas

    //Use a common static method to convert json model to map
    static Map modelToMap(String model) {
        //JSON.parse(model)
        def final slurper = new groovy.json.JsonSlurper()
        return slurper.parseText(model)
    }

    //Use a common static method to convert map to json string
    static String modelToString(Map model) {
        return (model as JSON).toString(true)
    }

    //Member method to get the merged modelView as a JSON text
    String getMergedModelText(withInfo = true) {
        extendsPage ? modelToString(getMergedModelMap(withInfo)) : modelView
    }

    //Member method to get the merged modelView as a Map
    Map getMergedModelMap(withInfo = true) {
        def result
        if (extendsPage) {
            //Should never return null
            if (!modelView || modelView.equals("null") || modelView.equals("{}")) {
                result = extendsPage.getMergedModelMap()
            } else {
                def diff = modelToMap(modelView)
                if (diff.containsKey(KEYS.deltas)) {
                    result = applyDiffs(diff.deltas, withInfo)
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
    private static Map diffModelView(mergedModelView, base) {
        def start = new Date()
        def baseProps = propertyMap(base)
        def extProps = propertyMap(mergedModelView)
        def diff = [:]
        // iterate over all keys in base and ext props
        [baseProps, extProps]*.keySet().flatten().unique().each { k ->
            def hasBase = baseProps.containsKey(k)
            def hasExt = extProps.containsKey(k)
            if (hasBase && hasExt) {
                //Both base and ext have the property, record if different
                if (!baseProps[k].equals(extProps[k])) {
                    diff[k] = diffAttribute(k, [base: baseProps[k], ext: extProps[k]])
                }
            } else {
                // Record the property that exists
                diff[k] = hasBase ? [base: baseProps[k]] : [ext: extProps[k]]
            }
        }
        diff = convertDiff(diff)
        log.info "Determined diff in ${ new Date().getTime() - start.getTime() } ms. diff= $diff"
        diff
    }

    //Static method for determining the difference for an attribute
    private static diffAttribute(name, diff) {
        if (diff.base instanceof Map) {
            diff = diffMapAttribute(name, diff)
        } else if (diff.base instanceof String) {
            diff = diffStringAttribute(name, diff)
        }
        diff
    }
    //Static method for Map type attributes such as parameters and validation
    private static diffMapAttribute(name, Map diff) {
        def nameProp = name.split(':')
        if (nameProp[1] != KEYS.meta) {
            [diff.base, diff.ext]*.keySet().flatten().unique().each { k ->
                def hasBase = diff.base.containsKey(k)
                def hasExt = diff.ext.containsKey(k)
                if (hasBase && hasExt && diff.base[k].equals(diff.ext[k])) {
                    //Remove equals
                    diff.base.remove(k)
                    diff.ext.remove(k)
                }
            }
        }
        diff
    }

    //Static method for String type attribute
    private static diffStringAttribute(name, diff) {
        List<String> base = diff.base.split('\n')
        List<String> ext = diff.ext.split('\n')
        if (base.size() > 1) {
            def contextSize = base.size() == 2 ? 1 : 2
            def patch = DiffUtils.diff(base, ext)
            def unifiedDiff = DiffUtils.generateUnifiedDiff("original", "revised", base, patch, contextSize)
            diff.patch = unifiedDiff.join('\n')
        }
        diff
    }

    //Merge attributes from diff into base
    private static mergeAttribute(name, base, diff) {
        def result = base
        if (name != KEYS.meta && base instanceof Map) {
            [diff.base, diff.ext]*.keySet().flatten().unique().each { k ->
                def hasBase = diff.base.containsKey(k)
                def hasExt = diff.ext.containsKey(k)
                if (hasExt) {
                    // Changed or Added attribute
                    result[k] = diff.ext[k]
                } else if (hasBase && !hasExt) {
                    // Removed attribute
                    result.remove(k)
                }
            }
        } else if (diff.patch) {
            List<String> original = base.split('\n')
            List<String> patchLines = diff.patch.split('\n')
            Patch patch = DiffUtils.parseUnifiedDiff(patchLines)
            def patched = DiffUtils.patch(original, patch)
            result = patched.join('\n')
        } else {
            result = diff.ext
        }
        result
    }

    //Static helper method to convert the properties into the common delta format
    private static Map convertDiff(propsDiff) {
        def result = [:]
        //convert the diff properties to a map
        propsDiff.each { k, v ->
            def nameProp = k.split(':')
            def name = nameProp[0]
            def prop = nameProp[1]
            if (!result[name]) {
                result[name] = [:]
            }
            result[name][prop] = v
        }
        [deltas: result]
    }


    //Static helper method to convert a page model JSON string to a propertyMap (intermediate format used in diff determination)
    private static Map propertyMap(String mergedModelString) {
        propertyMap(modelToMap(mergedModelString))
    }

    //Static helper method to convert a page model Map to a propertyMap (intermediate format used in diff determination)
    private static Map propertyMap(Map comp, int siblingIndex = 0, Map parent = null, Map next = null) {
        def props = [:]
        def decomposed = decomposeComponents(comp).components
        decomposed.each {
            it.value.each { key, val ->
                if (key != KEYS.components && key != KEYS.mergeInfo ) {
                    props[it.key + ':' + key] = val
                }
            }
        }
        props
    }

    //Member method to apply the differences to a decomposed page
    private Map applyDiffs(Map diffs, withInfo) {
        def model = decomposeComponents(extendsPage.getMergedModelMap())
        model.added = [:]
        model.conflicts = []
        diffs.each { name, diff ->
            //println "Processing $name"
            def comp = model.components[name]
            if (comp) { // Component exists, change props to match the extension
                diff.each { prop, val ->
                    if (val.base.equals(comp[prop]) || prop != KEYS.meta) {
                        // accept change as is.
                        if (val.containsKey(KEYS.ext)) {
                            try {
                                comp[prop] = mergeAttribute(prop, comp[prop], val)
                                if (prop == KEYS.meta) {
                                    def parent=model.components[comp.meta.parent]
                                    parent?.mergeInfo << [modifiedBy: constantName]
                                } else {
                                    comp.mergeInfo  << [modifiedBy: constantName]
                                }
                            } catch (e) {
                                model.conflicts << [type: "mergeAttribute", message: e.message, comp: comp, property: prop]
                            }
                        } else {
                            comp.remove(prop)
                        }
                    } else {
                        if (prop == KEYS.meta) {
                            def type = diff.meta.base?.nextSibling?.equals(comp.meta.nextSibling) ? "newParent" : "reOrder"
                            comp.mergeInfo  << [modifiedBy: constantName]
                            model.conflicts << [type: type, diff: diff, comp: comp]
                            log.warn "Component $name: detected meta change in baseline ${ val.base } to ${ comp[prop] }. Change to be applied: ${ val.ext }"
                        }
                    }
                }
            } else {
                //if we are here, a new component is added by the extension or a baseline component is removed
                //a new component must at least have a type attribute
                if (diff.type) {
                    diff.each { prop, val ->
                        if (val.containsKey(KEYS.ext)) {
                        //if (val.ext) {
                            if (!comp) {
                                comp = [:] //we have an extension property so create the component
                            }
                            comp[prop] = val.ext
                        }
                    }
                    if (comp) { //add the component to the result
                        comp.mergeInfo = [addedBy: constantName, noReference: true]
                        model.components[name] = comp
                        model.added[name] = comp
                        log.info "New component ${ comp.name } added "
                    }
                } else {
                    model.conflicts << [type: "removedBaseline", diff: diff, comp: [name: name]]
                }
            }
        }
        composeComponents(model, withInfo)
    }

    //Static helper method to decompose a page model to a flat map, adding sibling and parent meta information
    private static Map decomposeComponents(Map comp, int siblingIndex = 0, Map parent = null, Map next = null, Map result = [:]) {
        if (comp.type == KEYS.page) {
            result = [root: comp.name, components: [:]]
        }
        Map clone = comp.clone()
        clone.meta = [:]
        if (next) {
            clone.meta.nextSibling = next.name
        }
        if (parent) {
            clone.meta.parent = parent.name
            clone.mergeInfo=[noReference: true, idx: siblingIndex] // assume components are unreferenced until used in compose
        }
        if (comp.components) {
            clone.remove(KEYS.components)
            def nSiblings = comp.components ? comp.components.size() - 1 : 0
            if (nSiblings >= 0) {
                clone.meta.firstChild = comp.components[0].name
                comp.components?.eachWithIndex { entry, i ->
                    def nextSibling = i < nSiblings ? comp.components[i + 1] : null
                    result = decomposeComponents(entry, i, comp, nextSibling, result)
                }
            }
        }
        result.components[comp.name] = clone
        result
    }

    //Remove merge and meta information depending on withInfo
    private static def cleanComponent(component, withInfo) {
       if (withInfo && component.mergeInfo)  {
            if (!(component.mergeInfo?.noReference)) {
                component.mergeInfo.remove(KEYS.noReference)
            }
            component.mergeInfo.remove(KEYS.idx)
            if (component.mergeInfo.size() == 0) {
                component.remove(KEYS.mergeInfo)
            }
        } else {
            component.remove(KEYS.mergeInfo)
        }
        component.remove(KEYS.meta)
        component
    }

    //Static helper method to compose the page model from a decomposed model
    private static Map composeComponents(Map decomposedModel, withInfo) {
        decomposedModel = resolveConflicts(decomposedModel)
        Map root = decomposedModel.components[decomposedModel.root]
        if (root.meta.firstChild) {
            def child = decomposedModel.components[root.meta.firstChild]
            root.components = getComponents(decomposedModel, root)
        }
        //Iterate over components to see if we have any components with noReference.
        def unReferenced =[]
        decomposedModel.components.each { name, component ->
            if (component.mergeInfo?.noReference) {
                unReferenced  << component
            } else {
                component=cleanComponent(component, withInfo)
            }
        }
        def finalUnreferenced = unReferenced
        //Iterate over the unReferenced and add into parent components in whatever order we find them.
        //The order may not be optimal so we leave the mergeInfo in place; to be used to flag the field for checking by user
        //The base sibling index is present in mergeInfo.idx, so could asure that order is being followed, but it seem to preserve this order already as is.
        //Also, if a parent is found for adding the component, the component is removed from finalUnreferenced
        unReferenced.each { component ->
            def parent = decomposedModel.components[component?.meta?.parent]
            if (parent && parent.components) {
                component.mergeInfo.validate=true
                component = cleanComponent(component, withInfo)
                parent.components << component
                finalUnreferenced=finalUnreferenced.minus(component)
            }
        }
        //If any components remain in the finalUnreferenced List, they are added as 'spare component'
        //They can be made visible in the VPC
        if (finalUnreferenced.size()>0) {
            root.spareComponents = finalUnreferenced
        }
        root
    }

    //Static helper method to resolve conflicts after baseline upgrades
    //The method for resolving baseline reorder conflicts is not optimal.
    //Any siblings that are missed here will be added in a final step to add un-referenced components
    private static def resolveConflicts(model) {
        model.conflicts.each { conflict ->
            if (conflict.type == "reOrder") {
                def nextName = conflict.comp.meta.nextSibling         //the current next name for the component
                def extNextName = conflict.diff.meta.ext?.nextSibling //the next name according to the extension
                def extNextComp = model.added[extNextName]            //see if next is an added component
                if (nextName == extNextName ) {
                    log.warn "Skip changing next component for ${ conflict.comp.name } - names already match"
                } else if (extNextComp ) {
                    //Replace B -> N with B -> E -> N; so we move the extension between B and N
                    //This only handles some cases; can improve this.
                    conflict.comp.meta.nextSibling = extNextName //Accept the new nextSibling from the extension  for this conflict
                    extNextComp.meta.nextSibling = nextName      //Set the nextSibling of the nextSibling to the existing next sibling of the conflict
                    log.warn "Resolved baseline reorder conflict by inserting new component $extNextName after component ${ conflict.comp.name }"
                } else {
                    //The next component is not a new component, assume an existing component (extension is a baseline reorder)
                    extNextComp = model.components[extNextName]
                    if (extNextComp) {
                        conflict.comp.meta.nextSibling = extNextName
                        //change next sibling of the base.
                        def base = model.components[conflict.diff.meta?.base?.nextSibling]
                        if (base) {
                            base.meta.nextSibling = extNextComp.meta?.nextSibling
                        }
                        //change the extended next comp
                        extNextComp.meta.nextSibling = nextName
                        log.warn "Resolved baseline reorder conflict by placing existing component $extNextName after component ${ conflict.comp.name }"
                    } else {
                        log.warn "Unresolved conflict. No component found matching the nextSibling of component ${ conflict.comp.name }"
                    }
                }
            } else if (conflict.type == "removedBaseline") {
                log.warn "Resolve removed baseline component conflict"
                def resolvedStatusMessage = "Unresolved"
                def removedNextName = conflict.diff.meta?.ext?.nextSibling
                def addedNoReference = removedNextName ? model.added[removedNextName] : null
                if (addedNoReference) {
                    def addedNext = model.components[addedNoReference.meta?.nextSibling]
                    if (addedNext) {
                        def addedNextReference = model.components.find {
                            it.value.meta?.nextSibling = addedNext.name
                        }
                        if (addedNextReference) {
                            addedNextReference.value.meta.nextSibling = addedNoReference.name
                            resolvedStatusMessage = "Resolved baseline remove component conflict by inserting new component ${ removedNextName } before component ${ addedNext.name }"
                        } else {
                            resolvedStatusMessage = "Unable to find component referencing $addedNext.name - need to add somewhere else"
                        }
                    }
                } else {
                    resolvedStatusMessage = "Extension cannot be applied as component ${ conflict.comp.name } appears to be removed from parent page"
                }
                log.warn resolvedStatusMessage
                conflict.statusMessage = resolvedStatusMessage
            }
        }
        model
    }

    //Construct a list of components for a parent component
    private static List getComponents(Map decomposedModel, parent) {
        def components = []
        def nextComponent = { component ->
            component.meta?.nextSibling ? decomposedModel.components[component.meta?.nextSibling] : null
        }
        def next = decomposedModel.components[parent.meta.firstChild]
        log.info "Executing getComponents for parent ${ parent.name }, first = ${ next.name }"
        while (next != null) {
            if (next.mergeInfo.noReference == false) {
                log.warn "Prevented adding existing child ${ next.name } of ${ parent.name }. Sequence is broken. \n    TODO: check all items with parent ${ parent.name } are included"
                next = null
            } else {
                log.trace "Added ${ next.name } to ${ parent.name }"
                if (next.meta?.firstChild) {
                    next.components = getComponents(decomposedModel, next)
                }
                next?.mergeInfo?.noReference = false
                components << next
                next = nextComponent(next)
            }
        }
        components.each { it.remove(KEYS.meta) }
        components
    }
}
