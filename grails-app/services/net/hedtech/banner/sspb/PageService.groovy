/*******************************************************************************
 Copyright 2018-2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb

import grails.converters.JSON
import org.springframework.context.i18n.LocaleContextHolder


class PageService {
    def compileService
    def groovyPagesTemplateEngine
    def pageSecurityService
    def springSecurityService

    def get(String constantName) {
        Page.findByConstantName(constantName)
    }

    def getNew(String constantName) {
        new Page(constantName:constantName)
    }

    def list(Map params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        params = extractReqPrams(params)
        log.trace "PageService.list invoked with params $params"
        def result

        // TODO: Do validation testing in create or update -- this is temporary
        if (params.forceValidationError == 'y') {
            // This will throw a validation exception...                      Log
            new Page(code:'FAIL', description: 'Code exceeds 2 chars').save(failOnError:true)
        }
        def max = Math.min( params.max ? params.max.toInteger() : 10000,  10000)
        def offset = params.offset ?: 0
        def qp= [offset: offset, max: max, sort: params.sort ?: 'constantName' , order:params.order ?: 'asc']
        if (params.constantName && params.excludePage?.trim()) {
            result = Page.findAllByConstantNameIlikeAndConstantNameNotEqual(params.constantName, params.excludePage ?: ' ', qp)
        } else if (params.constantName) {
            result = Page.findAllByConstantNameIlike(params.constantName, qp)
        } else if (params.excludePage?.trim()) {
            result = Page.findAllByConstantNameNotEqual(params.excludePage, qp)
        } else {
            result = Page.list(qp)
        }

        def listResult = []
       Locale locale = LocaleContextHolder.getLocale()
        String date_format = "dd/MM/yyyy"
        if(locale && ['ar','fr_CA'].contains(locale.toString())){
            date_format = "yyyy/MM/dd"
        }
        if(locale.toString().contains("en_US")){
            date_format = "MM/dd/YYYY"
        }
        result.each {
            //supplementPage( it )
            // trim the object since we only need to return the constantName properties for listing
            //listResult << [page : [constantName : it.constantName, id: it.id, version: it.version]]
            listResult << [constantName : it.constantName,extendsPage:  it.extendsPage?.constantName, id: it.id, version: it.version, dateCreated:it.dateCreated?.format(date_format), lastUpdated:it.lastUpdated?.format(date_format)]
        }

        log.trace "PageService.list is returning a ${result.getClass().simpleName} containing ${result.size()} pages"
        listResult
    }


    def count(Map params) {
        log.trace "PageService.count invoked"
        params = extractReqPrams(params)

        if (params.constantName && params.excludePage?.trim()) {
            Page.countByConstantNameIlikeAndConstantNameNotEqual(params.constantName,params.excludePage ?: ' ')
        } else if (params.constantName) {
             Page.countByConstantNameIlike(params.constantName)
        } else if (params.excludePage?.trim()) {
            Page.countByConstantNameNotEqual(params.excludePage)
        } else {
            Page.count()
        }

    }


    def show(Map params) {
        if(params && params.containsKey('getGridData')) {
            def returnMap = [
                    result: list(params),
                    length: count(params)
            ]

            return returnMap

        }

        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        log.trace "PageService.show invoked"
        def page = Page.find { constantName == params.id }
        log.trace "PageService.show returning ${page}"
        def result = null
        if (page) {
            String model = page.getMergedModelText(true) //Get the merged model with merge Info
            result = [constantName: page.constantName, id: page.id, extendsPage: page.extendsPage, version: page.version, modelView: model]
        }
        result
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
        boolean isUpdated = updateConfigAttr(pageName, pageSource,pageInstance)

        def ret
        if ( !validateInput([constantName:pageName])) {
            ret = [statusCode: 1, statusMessage: message(code: "sspb.page.visualcomposer.invalid.name.message")]
        } else if (pageSource) {
            def pageJSON = JSON.parse(pageSource)
            def duplicateObjects
            if(pageJSON.objectName) {
                duplicateObjects = Page.findAllByModelViewLikeAndConstantNameNotEqual("%\"objectName\": \"" + pageJSON.objectName.trim().toUpperCase() + "\"%", pageJSON.name)
            }
            if (!duplicateObjects) {
                if (!(extendsPage instanceof Page)) {
                    // Maps and Json Objects don't compare directly with nulls
                    extendsPage = extendsPage.equals(null)||extendsPage?.size()==0?null:extendsPage
                }

                if (pageInstance) {
                    pageInstance.extendsPage = extendsPage ? Page.findByConstantName(extendsPage.constantName) : null
                } else {
                    pageInstance = new Page([constantName :pageName, extendsPage :extendsPage])
                }
                pageInstance.modelView = pageSource
                //remove merge Info if page is not extended
                if (!pageInstance.extendsPage ){
                    pageInstance.modelView = Page.modelToString(Page.cleanModelMap(pageInstance.getMergedModelMap()))
                }
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
                ret = [statusCode: 4, statusMessage: ""]
                ret.pageValidationResult=[:]
                ret.pageValidationResult.errors = message(code: "sspb.page.visualComposer.object.validation.error", args:[pageJSON.objectName])
            }
        } else {
            ret = [statusCode: 1, statusMessage: message(code: "sspb.page.visualcomposer.no.source.message")]
        }

        groovyPagesTemplateEngine.clearPageCache() //Make sure that new page gets used
        if(isUpdated && 0==ret.get("statusCode")){
            def updMsg= message(code:"sspb.page.visualComposer.role.update")
            def msg = ret.get("statusMessage")+"\n"+updMsg
            ret << [statusMessage:msg ]
            springSecurityService.clearCachedRequestmaps()
        }
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
                log.error("Unexpected Exception in compile page\n"+e.printStackTrace())
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
                page.delete(failOnError:true, flush: true)
                springSecurityService.clearCachedRequestmaps()
            }
        }
    }

    private def validateInput(params) {
        def name = params?.constantName
        def valid = (name?.size() <= 60)
        valid &= name ==~ /[a-zA-Z]+[a-zA-Z0-9_\-\.]*/
        valid
    }


    def boolean updateConfigAttr(pageName, pageSource,page){
        def model = page?.modelView?new groovy.json.JsonSlurper().parseText(page.modelView):null
        def pageData = pageSource?new groovy.json.JsonSlurper().parseText(pageSource):null
        String objName = model?.get("objectName")?model?.get("objectName"):""
        String objectName = pageData?.get("objectName")?pageData?.get("objectName"):""
        if(objName && !objectName.equals(objName)){
            def url = "/customPage/page/${pageName}/**"
            def rm=Requestmap.findByUrl(url)
            def configAttributes = rm?.getConfigAttribute()
            if(configAttributes){
                String[] roles = configAttributes?.split(",")
                String cfg=""
                roles.eachWithIndex { str, i ->
                    if(!str.contains("ROLE_${objName}")){
                        cfg+=str
                        if(i<roles.length-1){
                            cfg+=","
                        }
                    }
                }
                if(cfg){
                    rm.setConfigAttribute(cfg)
                    rm.save(flush: true, failOnError: true)
                }else{
                    rm.delete(flush: true, failOnError: true)
                }
                String roleN = "ADMIN-${objName}"
                page.pageRoles.each { pageRole ->
                    if(roleN.equals(pageRole.roleName)) {
                        page.removeFromPageRoles(pageRole)
                        pageRole.delete()
                    }
                }
                return true
            }
        }
        return false
    }


    def extractReqPrams(Map reqParams) {
        Map params = [:]
        if (reqParams && reqParams.excludePage) {
            params.excludePage = reqParams.excludePage
        }

        if (reqParams && reqParams.searchString) {
            params.constantName = "%$reqParams.searchString%"
        }

        if (reqParams && reqParams.sortColumnName) {
            params.sort = reqParams.sortColumnName
        }

        if (reqParams && 'true'.equalsIgnoreCase(reqParams.ascending)) {
            params.order = 'asc'
        }

        if (reqParams && 'false'.equalsIgnoreCase(reqParams.ascending)) {
            params.order = 'desc'
        }

        params << reqParams
    }
 }
