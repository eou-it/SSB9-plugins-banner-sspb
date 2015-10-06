package net.hedtech.banner.sspb

import org.codehaus.groovy.grails.web.util.WebUtils
import grails.validation.ValidationException
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import org.hibernate.StaleObjectStateException
import org.springframework.dao.OptimisticLockingFailureException
import java.security.MessageDigest

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
            if (ret.statusCode == 0) {
                Page.withTransaction {
                    if (!ret.page.save()) {
                        ret.page.errors.allErrors.each { ret.statusMessage += it +"\n" }
                        ret.statusCode = 3
                    }
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

    // Is below code really needed?

//    public def checkOptimisticLock( domainObject, content ) {
//
//        if (domainObject.hasProperty( 'version' )) {
//            if (!content?.version) {
//                domainObject.errors.reject( 'version', "net.hedtech.restfulapi.Page.missingVersion")
//                throw new ValidationException( "Missing version field", domainObject.errors )
//            }
//            int ver = content.version instanceof String ? content.version.toInteger() : content.version
//            if (ver != domainObject.version) {
//                throw exceptionForOptimisticLock( domainObject, content )
//            }
//        }
//    }
//
//
//    private def exceptionForOptimisticLock( domainObject, content ) {
//        new HibernateOptimisticLockingFailureException( new StaleObjectStateException( domainObject.class.getName(), domainObject.id ) )
//    }
//
//
//    /**
//     * Checks the request for a flag asking for a specific exception to be thrown
//     * so error handling can be tested.
//     * This is a method to support testing of the plugin, and should not be taken
//     * as an example of good service construction.
//     **/
//    private void checkForExceptionRequest() {
//        def params = WebUtils.retrieveGrailsWebRequest().getParameterMap()
//        if (params.forceGenericError == 'y') {
//            throw new Exception( "generic failure" )
//        }
//        if (params.throwOptimisticLock == 'y') {
//            throw new OptimisticLockingFailureException( "requested optimistic lock for testing" )
//        }
//        if (params.throwApplicationException == 'y') {
//           //throw new DummyApplicationException( params.appStatusCode, params.appMsgCode, params.appErrorType )
//        }
//    }
//
//
//    private void supplementPage( Page page ) {
//        MessageDigest digest = MessageDigest.getInstance("SHA1")
//        digest.update("constantName:${page.getConstantName()}".getBytes("UTF-8"))
//        //digest.update("description${page.getDescription()}".getBytes("UTF-8"))
//        def properties = [sha1:new BigInteger(1,digest.digest()).toString(16).padLeft(40,'0')]
//        page.metaClass.getSupplementalRestProperties << {-> properties }
//    }

}
