package net.hedtech.banner.sspb

import org.codehaus.groovy.grails.web.util.WebUtils
import grails.validation.ValidationException
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import org.hibernate.StaleObjectStateException
import org.springframework.dao.OptimisticLockingFailureException
import java.security.MessageDigest

class PageService {
    def compileService

    def list(Map params) {

        log.trace "PageService.list invoked with params $params"
        def result

        // TODO: Do validation testing in create or update -- this is temporary
        if (params.forceValidationError == 'y') {
            // This will throw a validation exception...
            new Page(code:'FAIL', description: 'Code exceeds 2 chars').save(failOnError:true)
        }
        def max = Math.min( params.max ? params.max.toInteger() : 100,  100)
        def offset = params.offset ?: 0
        result = Page.list( offset: offset, max: max, sort: 'constantName' )

        def listResult = []

        result.each {
            //supplementPage( it )
            // trim the object since we only need to return the constantName properties for listing
            listResult << [page : [constantName : it.constantName, id: it.id, version: it.version]]
        }

        log.trace "PageService.list is returning a ${result.getClass().simpleName} containing ${result.size()} pages"
        listResult
    }


    def count(Map params) {
        log.trace "PageService.count invoked"
        Page.count()
    }


    def show(Map params) {
        log.trace "PageService.show invoked"
        def result
        result = Page.find{constantName==params.id}
        //result = Page.get(params.id)

        supplementPage( result )
        log.trace "PageService.show returning ${result}"
        def showResult = [constantName : result.constantName, id: result.id, version: result.version, modelView: result.modelView]

        showResult
    }



    // TODO for now update(post) handles both update and creation to simplify client side logic
    def create(Map content) {
        log.trace "PageService.create invoked"


        if (WebUtils.retrieveGrailsWebRequest().getParameterMap().forceGenericError == 'y') {
            throw new Exception( "generic failure" )
        }

        def result

        Page.withTransaction {
            // compile first
            result = compilePage(content.pageName, content.source)
            /*
            def instance = new Page()
            instance.properties['constantName','modelView'] = content

            instance.save(failOnError:true)
            result = instance
            //if (content['parts']) result.parts //force lazy loading
            //else result.parts = [] as Set
            */
        }
        //supplementPage( result )
        log.trace "PageService.create returning $result"
        result
    }

    // update is not used since the client may not know if a page exists or not when submitting (concurrent editing)
    def update(def id, Map content) {
        log.trace "PageService.update invoked"

        checkForExceptionRequest()

        def result
        Page.withTransaction {
            /* def page = Page.get(id)

            checkOptimisticLock( page, content )

            page.properties = content
            page.save(failOnError:true)
            result = page
            //result.parts //force lazy loading
            */
            // note source is in unmarshalled JSON text representation
            result = compilePage(content.pageName, content.source)
        }
        //supplementPage( result )
        result
    }

    def compilePage( pageName, pageSource) {
        log.trace "in compilePage: \npageSource=$pageSource"

        def overwrite=false
        def pageInstance  = Page.findByConstantName(pageName)
        def ret
        // check name duplicate
        if (pageInstance) {
            overwrite = true;
        }

        if (pageSource)  {
            def validateResult =  compileService.preparePage(pageSource)
            if (validateResult.valid) {
                def compiledJSCode=compileService.compileController(validateResult.pageComponent)
                log.trace "JavaScript is compiled\n"
                def compiledView = compileService.compile2page(validateResult.pageComponent)
                log.trace "Page is compiled\n"
                if (!pageInstance)
                    pageInstance = new Page([constantName:pageName])
                pageInstance.modelView=pageSource
                pageInstance.compiledView = compiledView
                pageInstance.compiledController=compiledJSCode
                pageInstance.save()
                ret = [statusCode:0, statusMessage:"Page has been compiled and ${overwrite?'updated':'saved'} successfully."]
            } else {
                ret = [statusCode: 2, statusMessage:"Page validation error. Page is not saved."]
                ret << [pageValidationResult:[errors: validateResult.error.join('\n')] ]
            }
        } else
            ret = [statusCode: 1, statusMessage:"Page source is empty. Page is not compiled."]

        ret << [overwrite:overwrite]

        return ret
    }

    // note the content-type header still needs to be set in the request even we don't send in any content in the body
    void delete(id,Map content) {
        Page.withTransaction {
            def page = Page.find{constantName==id}
            page.delete(failOnError:true)
        }
    }


    public def checkOptimisticLock( domainObject, content ) {

        if (domainObject.hasProperty( 'version' )) {
            if (!content?.version) {
                domainObject.errors.reject( 'version', "net.hedtech.restfulapi.Page.missingVersion")
                throw new ValidationException( "Missing version field", domainObject.errors )
            }
            int ver = content.version instanceof String ? content.version.toInteger() : content.version
            if (ver != domainObject.version) {
                throw exceptionForOptimisticLock( domainObject, content )
            }
        }
    }


    private def exceptionForOptimisticLock( domainObject, content ) {
        new HibernateOptimisticLockingFailureException( new StaleObjectStateException( domainObject.class.getName(), domainObject.id ) )
    }


    /**
     * Checks the request for a flag asking for a specific exception to be thrown
     * so error handling can be tested.
     * This is a method to support testing of the plugin, and should not be taken
     * as an example of good service construction.
     **/
    private void checkForExceptionRequest() {
        def params = WebUtils.retrieveGrailsWebRequest().getParameterMap()
        if (params.throwOptimisticLock == 'y') {
            throw new OptimisticLockingFailureException( "requested optimistic lock for testing" )
        }
        if (params.throwApplicationException == 'y') {
           //throw new DummyApplicationException( params.appStatusCode, params.appMsgCode, params.appErrorType )
        }
    }


    private void supplementPage( Page page ) {
        MessageDigest digest = MessageDigest.getInstance("SHA1")
        digest.update("constantName:${page.getConstantName()}".getBytes("UTF-8"))
        //digest.update("description${page.getDescription()}".getBytes("UTF-8"))
        def properties = [sha1:new BigInteger(1,digest.digest()).toString(16).padLeft(40,'0')]
        page.metaClass.getSupplementalRestProperties << {-> properties }
    }
}
