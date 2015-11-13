/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb

class PageService {
    def compileService
    def groovyPagesTemplateEngine
    def pageSecurityService

    def get(String constantName) {
        def result = Page.findByConstantName(constantName)
    }

    def getNew(String constantName) {
        def result = new Page(constantName:constantName)
    }

    def list(Map params) {

        log.trace "PageService.list invoked with params $params"
        def result

        // TODO: Do validation testing in create or update -- this is temporary
        if (params.forceValidationError == 'y') {
            // This will throw a validation exception...                      Log
            new Page(code:'FAIL', description: 'Code exceeds 2 chars').save(failOnError:true)
        }
        def max = Math.min( params.max ? params.max.toInteger() : 10000,  10000)
        def offset = params.offset ?: 0
        def qp= [offset: offset, max: max, sort: 'constantName']
        if  (params.constantName) {
            result = Page.findAllByConstantNameLike(params.constantName, qp)
        } else {
            result = Page.list( qp )
        }

        def listResult = []

        result.each {
            //supplementPage( it )
            // trim the object since we only need to return the constantName properties for listing
            //listResult << [page : [constantName : it.constantName, id: it.id, version: it.version]]
            listResult << [constantName : it.constantName, id: it.id, version: it.version]
        }

        log.trace "PageService.list is returning a ${result.getClass().simpleName} containing ${result.size()} pages"
        listResult
    }


    def count(Map params) {
        log.trace "PageService.count invoked"
        if (params.constantName)
            Page.countByConstantNameLike(params.constantName)
        else
            Page.count()
    }


    def show(Map params) {
        log.trace "PageService.show invoked"
        def page= Page.find{constantName==params.id}
        log.trace "PageService.show returning ${page}"
        String model = page.getMergedModelText()
        def showResult = [constantName : page.constantName, id: page.id, extendsPage: page.extendsPage, version: page.version, modelView: model]
        showResult
    }



    // TODO for now update(post) handles both update and creation to simplify client side logic
    def create(Map content, params) {
        log.trace "PageService.create invoked"
        def result
        Page.withTransaction {
            // compile first
            result = compileAndSavePage(content.pageName, content.source, content.extendsPage)
        }
        log.trace "PageService.create returning $result"
        result
    }

    // update is not used to update pages since the client may not know if a page exists or not when submitting (concurrent editing)
    def update( /*def id,*/ Map content, params) {
        log.trace "PageService.update invoked"
        create(content, params)
    }

    def compileAndSavePage( pageName, pageSource, extendsPage) {
        log.trace "in compileAndSavePage: pageName=$pageName"
        def pageInstance  = Page.findByConstantName(pageName)
        def ret
        if (pageSource)  {

            if (!(extendsPage instanceof Page)) {
                // Maps and Json Objects don't compare directly with nulls
                extendsPage = extendsPage.equals(null)||extendsPage?.size()==0?null:extendsPage
            }

            if (!pageInstance) {
                pageInstance = new Page([constantName:pageName, extendsPage:extendsPage])
            }
            else {
                pageInstance.extendsPage = extendsPage ? Page.findByConstantName(extendsPage.constantName) : null
            }
            pageInstance.modelView=pageSource
            ret = compilePage(pageInstance)

            if (ret.statusCode == 0) {

                if (pageInstance.extendsPage) {
                    pageInstance.modelView = pageInstance.diffModelViewText(pageSource)// save the diff if an extension

                    def slurper = new groovy.json.JsonSlurper()
                    def VPCModel = slurper.parseText(pageSource)
                    def MergedModelMap = pageInstance.getMergedModelMap(/*true*/) //get model without mergeInfo
                    if ( !VPCModel.equals(MergedModelMap) ) {
                        ret.pageValidationResult.errors = PageModelErrors.getError(error: PageModelErrors.MODEL_INVALID_DELTA_ERR).message
                        ret.statusCode = 8
                        ret.statusMessage = ""
                    }
                }
                if (ret.statusCode == 0) {
                    if (!ret.page.save()) {
                        ret.page.errors.allErrors.each { ret.statusMessage += it +"\n" }
                        ret.statusCode = 3
                    }
                }
            }
        } else
            ret = [statusCode: 1, statusMessage: message(code:"sspb.page.visualcomposer.no.source.message")]

        groovyPagesTemplateEngine.clearPageCache() //Make sure that new page gets used
        return ret
    }

    def compilePage(Page page) {
        log.trace "in compilePage: pageName=$page.constantName"
        def result
        def pageSource = page.modelView
        def validateResult =  compileService.preparePage(pageSource)
        if (validateResult.valid) {
            try {
                def compiledJSCode=compileService.compileController(validateResult.pageComponent)
                log.trace "JavaScript is compiled\n"
                def compiledView = compileService.compile2page(validateResult.pageComponent)
                log.trace "Page is compiled\n"
                //page.modelView=pageSource
                page.compiledView = compiledView
                page.compiledController=compiledJSCode
                compileService.updateProperties(validateResult.pageComponent)
                result = [statusCode:0, statusMessage:"${message(code:'sspb.page.visualcomposer.compiledsaved.ok.message')}"]
            } catch (e)   {
                result = [statusCode: 2, statusMessage: message(code:"sspb.page.visualcomposer.validation.error.message")]
            }
            result << [page: page] // pass the page in the result
        } else {
            result = [statusCode: 2, statusMessage: message(code:"sspb.page.visualcomposer.validation.error.message")]
        }
        result << [pageValidationResult:[errors: validateResult.error.join('\n'),
                                         warn:  validateResult.warn ? message(code:"sspb.page.visualComposer.warnings", args[validateResult.warn.join('\n')]): ""]]
        return result
    }

    // note the content-type header still needs to be set in the request even we don't send in any content in the body
    void delete(Map content, params) {
        pageSecurityService.delete([:],[constantName:params.id])
        Page.withTransaction {
            def page = Page.find{constantName==params.id}
            if (page.extensions?.size() > 0) {
                throw new RuntimeException( message(code:"sspb.page.visualComposer.deletion.failed.message"))
            }
            else {
                page.delete(failOnError:true)
            }
        }
    }

    /**
     * ********************************************************************************************
     * Functionality to generate a merged page model from a base page model and a set of extensions
     * ********************************************************************************************
     */

    /**
     * Loads test data from a file representing a base page and a file representing a
     * set of extensions
     *
     * @param testPageFile : test file containing page model in JSON format
     * @param testExtensionsFile : tes extension file in JSON format
     * @return map of file contents
     */
    private Map loadTestData(testPageFile,
                             testExtensionsFile) {
        Map testData = [:]
        testData.pageModelJSON = new File(testPageFile).text
        testData.pageExtensionsJSON = new File(testExtensionsFile).text

        return testData
    }

    /**
     * Creates a map of all components. This allows easy access to component by name as opposed
     * to constantly traversing the component hierarchy.
     * This map is constantly updated as extensions are being applied
     *
     * @param pageComponent : next component to be added to map - initially set to page component
     * @param pageComponents : map of all page components. key = component name
     */
    void buildComponentMap( pageComponent, pageComponents ) {
        log.trace "Building component map"
        pageComponents[pageComponent.name] = pageComponent

        pageComponent?.components.each { subComp ->
            subComp.parentComponent = pageComponent
            buildComponentMap(subComp,pageComponents)
        }
    }

    /**
     * Removes temporary attributes
     * Ensures empty component lists are removed
     *
     * @param pageComponents : map of all page components. key = component name
     */
    void cleanup(pageComponents) {
        log.trace "cleanup"
        pageComponents.each {
            it.value.remove("parentComponent")
            it.value.remove("parent")
            it.value.remove("nextSibling")
            it.value.remove("subCompReorderExtensions")
            if (it.value.components?.size() == 0) {
                it.value.remove("components")
            }
        }
    }

    /**
     * Excludes a component and all it's subcomponents from component map
     *
     * @param pageComponents : map of all page components. key = component name
     * @param component : component to be excluded
     */
    void excludeFromComponentMap(pageComponents, component) {
        component.components.each {
            excludeFromComponentMap(pageComponents,it)
        }
        pageComponents.remove(component.name)
    }

    /**
     * Excludes all components with an extension of exclude: true from page model
     *
     * @param pageComponents : map of all page components. key = component name
     * @param pageExtensions : all extensions
     */
    void excludeComponents(pageComponents, pageExtensions) {
        log.trace "Excluding components"
        pageExtensions?.each {
            if (it.exclude) {
                Map component = pageComponents[it.name]
                Map parent = component.parentComponent

                // exclude component from parent components and component map
                parent.components -= component
                excludeFromComponentMap(pageComponents, component)
    }
        }
    }

    /**
     * Removes, modifies and adds attributes based on extensions
     *
     * @param component : the component to be extended OR
     *                    parameter or validation map to be extended
     * @param extendedAttributes : attribute extensions
     */
    void extendAttributes(component, extendedAttributes) {

        // only log when invoked for component map - not parameter or validations maps
        if (component?.name) {
            log.trace "Extending attributes for $component.name"
        }

        extendedAttributes.each{
            if (it.exclude) {
                component?.remove(it.name)
                return;
            }
            switch(it.name) {
                case ["parameters", "validation"]:
                    extendAttributes(component[it.name], it.attributes)
                    break;
                default:
                    component[it.name] = it.value
                    break;
            }
        }
    }

    /**
     * Adds, excludes and modifies all component attributes
     *
     * @param pageComponents : map of all page components. key = component name
     * @param pageExtensions : all extensions
     */
    void extendComponentAttributes(pageComponents, pageExtensions) {
        pageExtensions?.each {
            if (!it.exclude) {
                Map component = pageComponents[it.name]
                if (component) {
                    extendAttributes(component,it.attributes)
                }
            }
        }
    }

    /**
     * Adds a new component to extended page model
     * Design notes:
     * - component added to end of list. Reordering takes place later
     * - The subcomponents of a newly added component are represented in the new component extension
     *   with the appropriate hierarchical structure. As such, they will be unmodified after this
     *   initial addition to the page and so don't need to be present in the pageComponents map
     *
     * - Component reordering is implemented by simply changing it's parent (if necessary) and
     *   changing position based on nextSibling. As such, subcomponents of this component are not
     *   removed and then readded to pageComponents map
     *
     * @param pageComponents : map of all page components. key = component name
     * @param component : new component to be added
     */
    void addComponent(pageComponents,component) {

        Map parentComponent = pageComponents[component.parent]
        component.parentComponent = parentComponent

        if (!parentComponent.components) {
            parentComponent.components = []
        }
        List parentComponents = parentComponent.components

        // add component and update component map.
            parentComponents.add(component)
        pageComponents[component.name] = component
    }

    /**
     * Moves a component from 1 parent to another
     * This takes place if component exists and extension specifies a parent
     *
     * @param component : component to be moved
     * @param pageComponents : map of all page components. key = component name
     * @param extension ; extension detailing the move
     */
    void switchParents(component,pageComponents,extension) {

        Map oldParent = component.parentComponent
        Map newParent = pageComponents[extension.parent]

        log.trace "Switching parents"

        // exclude component from old parent components
        oldParent.components -= component

        // add component to new parent components
        component.parent = newParent.name
        addComponent(pageComponents, component)
    }

    /**
     * Adds new components and reorders existing components
     * Design note:
     * - It is assumed that the extensions are generated in a depth first manner based
     *   on the visual model of the extended page. Processing these extensions in
     *   reverse order ensures the final component order is correct
     *
     * @param pageComponents : map of all page components. key = component name
     * @param pageExtensions : all extensions
     */
    void addComponents(pageComponents, pageExtensions) {
        log.trace "Adding components"

        pageExtensions?.reverseEach {
            if (!it.exclude) {
                Map component = pageComponents[it.name]
                if (!component) {
                    addComponent(pageComponents,it)
                }  else if (it.containsKey("parent")) {
                        switchParents(component,pageComponents,it)
                }
            }
        }
    }

    /**
     * Creates an ordered list of subcomponents
     * Find nextSibling information initially from extensions. If not found then
     * infer nextSibling from original subcomponent ordering ie unchanged
     *
     * @param parent : the parent of the subcomponents to be sorted
     * @param pageComponents : map of all page components. key = component name
     * @return an ordered list of subcomponents
     */
    List reorderSubcomponents(parent,pageComponents) {
        Short maxIterations=parent.components.size()
        Short iteration=0
        boolean sortComplete=false
        List orderedComponents=[]
        String nextSibling=null
        String compWithNextSibling=null
        Integer nextSiblingIndex

        while (!sortComplete) {
            compWithNextSibling = parent.subCompReorderExtensions.find { it.value == nextSibling }?.key
            if (compWithNextSibling) {
                // find component with next sibling in extensions
                orderedComponents.add(0,pageComponents[compWithNextSibling])
                nextSibling = compWithNextSibling
            } else {
                //find component with next sibling in original comps
                nextSiblingIndex = parent.components.indexOf(parent.components.find {it.name == nextSibling})
                orderedComponents.add(0,parent.components[nextSiblingIndex-1])
                nextSibling = parent.components[nextSiblingIndex-1].name
            }
            iteration++
            if (iteration == maxIterations) {
                sortComplete = true
            }
        }
        return orderedComponents
    }

    /**
     * Groups extensions together for subcomponents of component - subCompReorderExtensions
     * Initiates a sort of subcomponents for which there are reordering extensions
     *
     * @param pageComponents : map of all page components. key = component name
     * @param pageExtensions : all extensions
     */
    void reorderComponents(pageComponents,pageExtensions) {
        Map parentComponent

        log.trace "Reordering components"

        pageExtensions.each {
            if (it.containsKey("nextSibling")) {
                parentComponent = pageComponents[it.name].parentComponent
                if (!parentComponent.subCompReorderExtensions) {
                    parentComponent.subCompReorderExtensions = [:]
                }
                parentComponent.subCompReorderExtensions[it.name] = it.nextSibling
            }
        }

        pageComponents.each {
            def comp = it.value
            if (comp.subCompReorderExtensions) {
                comp.components = reorderSubcomponents(comp,pageComponents)
            }
        }
    }

    /**
     * Applies a set of extensions to a base page model and returns a new extended page model
     *
     * @param pageModelJSON : text of base page in JSON format
     * @param pageExtensionsJSON : text of extensions in JSON format
     * @return : new merged page model
     */
    Map extendPageModel(pageModelJSON, pageExtensionsJSON) {

        def slurper = new groovy.json.JsonSlurper()
        Map pageComponents = [:]

        // deserialize page model and extensions JSON text
        Map extendedPageModel = slurper.parseText(pageModelJSON)
        List pageExtensions = slurper.parseText(pageExtensionsJSON).extensions

        // create map of individual components to ensure efficient traversal of page
        buildComponentMap(extendedPageModel, pageComponents)

       // reorderComponents(pageComponents,pageExtensions)

        log.trace "Extending page $extendedPageModel.name"

        // apply all extensions
        excludeComponents(pageComponents, pageExtensions)
        extendComponentAttributes(pageComponents, pageExtensions)
        addComponents(pageComponents, pageExtensions)
        reorderComponents(pageComponents,pageExtensions)

        cleanup(pageComponents)

        return extendedPageModel
    }

    /**
     * Given the extensions for an extended page, this routine iterates up the extensions to find the base page
     * and then applies all the extensions in turn to produce the final extended page model
     *
     * @param pageName: extended page name
     * @return : extended page constructed from base page and all extensions
     */
    Map constructExtendedPage(pageName) {

        Page page
        boolean basePageFound = false
        List extensions = []
        Map extendedPageModel
        def jsonOutput = new groovy.json.JsonOutput()

        log.trace "Constructing page: $pageName"
        while (!basePageFound) {
            page = Page.findByConstantName(pageName)
            if (!page) {
                log.trace "Unable to retrieve page: $pageName"
                return null
            }
            if (page.extendsPage) {
                extensions << page.modelView
                pageName = page.extendsPage.constantName
            } else {
                basePageFound = true
            }
        }

        // apply all extensions in turn to base page
        if (basePageFound) {
            String pageModel = page.modelView
            extensions.reverseEach {
                extendedPageModel = extendPageModel(pageModel,it)
                pageModel = jsonOutput.toJson(extendedPageModel)
                }
            }
        return extendedPageModel
    }



}
