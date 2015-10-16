package net.hedtech.banner.sspb

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

class Page {

    static hasMany = [pageRoles: PageRole, extensions: Page] //Optional child page(s) (sub classes)
    Page extendsPage     //Optional parent page (super class), see constraints

    String constantName
    String modelView
    String mergedModelView
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

    static transients = ['diffModelView', 'mergedModelView']

    boolean isEmptyInstance() {
        return (
            modelView.equals("{}") &&
            compiledView == null &&
            compiledController == null
        )
    }

    void setModelView(String m, Boolean diff = false) {
        if (extendsPage && diff) {
            def json = diffModelView(m) as JSON
            modelView = json.toString()
        } else {
            modelView=m
        }
    }

    String getModelView() {
        modelView
    }

    //Routines to determine diffs between extended page and base page

    /**
     * Member method to determine the difference  between the full page model and this.extendsPage
     *
     * @param mergedModelView: the page model as seen by the user and compiler
     * @return : the diffModelView to be persisted as modelView and used as an extension
     */
    Map diffModelView(mergedModelView){
        def start = new Date()
        def baseProps = propertyMap(this.extendsPage.modelView)
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
                diff[k] = hasBase? [base: baseProps[k]]: [ext:extProps[k]]
            }
        }
        println "Determined diff in ${new Date().getTime()-start.getTime()} ms. diff= $diff"
        convertDiff(diff)
    }

    static private Map convertDiff(propsDiff) {
        def result = [:]
        def ancestors = [:] //map with number of ancestors and associated components
        def maxAncestors = 0
        //convert the diff properties to a map, add the component name in the matching ancestors element
        propsDiff.each{ k,v ->
            def nameProp =  k.split(':')
            def name=nameProp[0]
            def prop=nameProp[1]
            if (!result[name]) {
                result[name] = [:]
            }
            result[name][prop] = v.ext
            if (prop == "ancestors" ) {
                if ( !ancestors[v.ext] ) {
                    ancestors[v.ext]=[]
                }
                ancestors[v.ext]<<name
                maxAncestors = Math.max(maxAncestors,v.ext)
            }
        }
        println "Converted diff after step 1:\n${result as JSON}"
        //add the names of components without ancestors in ancestors[0]
        result.each {
            if (!it.value.ancestors) { // Add the nodes with unknown ancestors in the 0 element
                if ( !ancestors[0]) {
                    ancestors[0]=[]
                }
                ancestors[0] << it.key
            }
        }
        //now move the items having a parent to the compents array of the parent
        (maxAncestors..0).each{ n ->
            ancestors[n].each{ name ->
                def comp = result[name]
                if ( comp['parent'] && result[comp['parent']]) {
                    def parent = result[comp.parent]
                    if (!parent.components) {
                        parent.components = []
                    }
                    parent.components << comp
                    result.remove(name)
                }
            }
        }
        println "Converted diff after step 2:\n${result as JSON}"
        //Now put the result into the final extensions format
        def finalResult = [extensions:[]]
        result.each { k, v ->
            if (!v.name) {
                v.name=k
            }
            finalResult.extensions << v
        }
        println "Converted diff after step 3:\n${finalResult as JSON}"
        finalResult
    }

     static private Map propertyMap(String mergedModelString) {
        JSONObject mergedModelJSON = JSON.parse(mergedModelString)
        propertyMap(mergedModelJSON)
    }

    static def componentName(component, parent, siblingIndex) {
        return component? component.name ? component.name : "${parent?.name}_child_${siblingIndex}":null
    }

    static private Map propertyMap(JSONObject comp, int siblingIndex=0, JSONObject parent=null, JSONObject next = null, int ancestors=0) {
        def props = [:]
        comp.name=componentName(comp, parent, siblingIndex)
        println "Processing $comp.name"
        comp.each{ key, val ->
            if (key!='components') {
                props[comp.name + ':' + key] = val
            }
        }
        props[ comp.name + ':ancestors'] = ancestors  //use to add children to parent components - the more ancestors the earlier
        props[ comp.name + ':parent'] = parent?.name
        props[ comp.name + ':nextSibling'] = componentName(next,parent,siblingIndex+1)
        def nSiblings = comp.components?comp.components.size()-1:0
        if (nSiblings>=0) {
            comp.components?.eachWithIndex { entry, i ->
                def nextSibling = i < nSiblings ? comp.components[i + 1] : null
                props << propertyMap(entry, i, comp, nextSibling, ancestors+1 )
            }
        }
        props
    }

}