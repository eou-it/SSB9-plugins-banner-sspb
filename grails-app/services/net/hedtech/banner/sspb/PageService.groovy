package net.hedtech.banner.sspb

import grails.converters.JSON

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
        def result
        result = Page.find{constantName==params.id}
        //result = Page.get(params.id)

        //supplementPage( result )
        log.trace "PageService.show returning ${result}"
        def showResult = [constantName : result.constantName, id: result.id, extendsPage: result.extendsPage, version: result.version, modelView: result.modelView]

        showResult
    }



    // TODO for now update(post) handles both update and creation to simplify client side logic
    def create(Map content, params) {
        log.trace "PageService.create invoked"
        //checkForExceptionRequest()
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

    def validateExtensions(Page page ) {
        if (page.extendsPage) {

            ///
            def mergedModelMap = new groovy.json.JsonSlurper().parseText(page.mergedModelView)
            def diff = page.diffModelView(mergedModelMap) as JSON
            def extendedPage = extendPageModel(page.extendsPage.modelView, diff.toString())

            def decomposedBasePage = page.decomposeExtendsPage()
            def equal = mergedModelMap.equals(extendedPage)

            println "pageExtended + delta == Submitted? $equal "
        }
    }

    def compileAndSavePage( pageName, pageSource, extendsPage) {
        log.trace "in compileAndSavePage: pageName=$pageName"
        def overwrite=false
        def pageInstance  = Page.findByConstantName(pageName)
        def ret
        // check name duplicate
        if (pageInstance) {
            overwrite = true;
        }
        if (pageSource)  {
            if (!pageInstance) {
                pageInstance = new Page([constantName:pageName, extendsPage:extendsPage.size?extendsPage:null])
            }
            else {
                pageInstance.extendsPage = extendsPage ? Page.findByConstantName(extendsPage.constantName) : null
            }
            pageInstance.modelView=pageSource
            ret = compilePage(pageInstance)
            pageInstance.mergedModelView = pageSource
            validateExtensions(pageInstance)
            if (ret.statusCode == 0) {
                if (!ret.page.save()) {
                    ret.page.errors.allErrors.each { ret.statusMessage += it +"\n" }
                    ret.statusCode = 3
                }
            }
        } else
            ret = [statusCode: 1, statusMessage:"Page source is empty. Page is not compiled."]  //TODO: I18N

        ret << [overwrite:overwrite]
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
                page.modelView=pageSource
                page.compiledView = compiledView
                page.compiledController=compiledJSCode
                compileService.updateProperties(validateResult.pageComponent)
                result = [statusCode:0, statusMessage:"Page has been compiled and ${overwrite?'updated':'saved'} successfully."]
                //TODO: I18N - should not use logic to construct message using updated  or saved
            } catch (e)   {
                result = [statusCode: 2, statusMessage:e.getMessage()+"\n"]
            }
            result << [page: page] // pass the page in the result
        } else {
            result = [statusCode: 2, statusMessage:"Page validation error. Page is not saved."] //TODO: I18N
        }
        result << [pageValidationResult:[errors: validateResult.error.join('\n'),
                                          warn: validateResult.warn?"\nWarnings:\n"+validateResult.warn.join('\n'):""] ]
        return result
    }

    // note the content-type header still needs to be set in the request even we don't send in any content in the body
    void delete(Map content, params) {
        pageSecurityService.delete([:],[constantName:params.id])
        Page.withTransaction {
            def page = Page.find{constantName==params.id}
            if (page.extensions?.size() > 0) {
                throw new RuntimeException( "Deletion of page not allowed where dependent pages exist.")
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
        pageComponents[pageComponent.name] = pageComponent
        pageComponent?.components.each {
            it.parentComponent = pageComponent
            buildComponentMap(it,pageComponents)
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

        // find next sibling
        def nextSiblingIndex = parentComponents.indexOf(parentComponents.find {it.name == component.nextSibling})

        // Remove redundant attributes from newComponent - parent and next sibling
        component.remove("parent")
        component.remove("nextSibling")

        if (nextSiblingIndex == -1) {
            // not in list ie null so add to end of list
            parentComponents.add(component)
        } else {
            parentComponents.add(nextSiblingIndex, component)
        }

        // update component map.
        pageComponents[component.name] = component
    }

    /**
     * Move a component to a new parent (if specified) and position according to nextSibling
     *
     * @param component : component to be reordered
     * @param pageComponents : map of all page components. key = component name
     * @param extension : ordering information
     */
    void reorderComponent(component,pageComponents,extension) {

        Map oldParent = component.parentComponent
        Map newParent = pageComponents[extension.parent] ?: oldParent

        // exclude component from old parent components
        oldParent.components -= component

        // add component to new parent components
        component.parent = newParent.name
        component.nextSibling = extension.nextSibling
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
    void addAndReorderComponents(pageComponents, pageExtensions) {
        pageExtensions?.reverseEach {
            if (!it.exclude) {
                Map component = pageComponents[it.name]
                if (!component) {
                    addComponent(pageComponents,it)
                } else if (it.containsKey("nextSibling")) {
                    reorderComponent(component,pageComponents,it)
                }
            }
        }
    }

    /**
     * Removes temporary parentComponent attribute
     * Ensures empty component lists are removed
     *
     * @param pageComponents : map of all page components. key = component name
     */
    void cleanup(pageComponents) {
        pageComponents.each {
            it.value.remove("parentComponent")
            if (it.value.components?.size() == 0) {
                it.value.remove("components")
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

        log.trace "Extending page $extendedPageModel.name"

        // apply all extensions
        excludeComponents(pageComponents, pageExtensions)
        extendComponentAttributes(pageComponents, pageExtensions)
        addAndReorderComponents(pageComponents, pageExtensions)

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
