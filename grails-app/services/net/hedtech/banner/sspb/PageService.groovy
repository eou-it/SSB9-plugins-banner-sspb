/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb

class PageService {
    def compileService
    def groovyPagesTemplateEngine
    def pageSecurityService

    def get(String constantName) {
        Page.findByConstantName(constantName)
    }

    def getNew(String constantName) {
        new Page(constantName:constantName)
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
        if (params.constantName) {
            Page.countByConstantNameLike(params.constantName)
        } else {
            Page.count()
        }
    }


    def show(Map params) {
        log.trace "PageService.show invoked"
        def page= Page.find{constantName==params.id}
        log.trace "PageService.show returning ${page}"
        String model = page.getMergedModelText(true) //Get the merged model with merge Info
        def showResult = [constantName : page.constantName, id: page.id, extendsPage: page.extendsPage, version: page.version, modelView: model]
        showResult
    }



    // TODO for now update(post) handles both update and creation to simplify client side logic
    def create(Map content, ignore) {
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

            if (pageInstance) {
                pageInstance.extendsPage = extendsPage ? Page.findByConstantName(extendsPage.constantName) : null
            } else {
                pageInstance = new Page([constantName:pageName, extendsPage:extendsPage])
            }
            pageInstance.modelView=pageSource
            ret = compilePage(pageInstance)

            if (ret.statusCode == 0) {

                if (pageInstance.extendsPage) {
                    pageInstance.modelView = pageInstance.diffModelViewText(pageSource)// save the diff if an extension

                    //Validate the extended model matches the submitted model
                    if ( !pageInstance.equals(pageSource) ) {
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
        } else {
            ret = [statusCode: 1, statusMessage: message(code: "sspb.page.visualcomposer.no.source.message")]
        }

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
                                         warn:  validateResult.warn ? message(code:"sspb.page.visualComposer.warnings", args: [validateResult.warn.join('\n')]): "" ]]
        return result
    }

    // note the content-type header still needs to be set in the request even we don't send in any content in the body
    void delete(Map ignore, params) {
        pageSecurityService.delete([:],[constantName:params.id])
        Page.withTransaction {
            def page = Page.find{constantName==params.id}
            if (page.extensions?.size() > 0) {
                throw new RuntimeException( message(code:"sspb.page.visualComposer.deletion.failed.message",args: [page.extensions.constantName.join(", ")]))
            }
            else {
                page.delete(failOnError:true)
            }
        }
    }

 }
