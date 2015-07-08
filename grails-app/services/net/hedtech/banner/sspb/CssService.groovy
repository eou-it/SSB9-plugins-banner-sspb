package net.hedtech.banner.sspb

import grails.validation.ValidationException
import org.codehaus.groovy.grails.web.util.WebUtils
import org.hibernate.StaleObjectStateException
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.security.MessageDigest
import net.hedtech.banner.css.Css

class CssService {

    def list(Map params) {

        log.trace "CssService.list invoked with params $params"
        def result

        // TODO: Do validation testing in create or update -- this is temporary
        /*
        if (params.forceValidationError == 'y') {
            // This will throw a validation exception...
            new Css(code:'FAIL', description: 'Code exceeds 2 chars').save(failOnError:true)
        } */
        def max = Math.min( params.max ? params.max.toInteger() : 100,  100)
        def offset = params.offset ?: 0
        result = Css.list( offset: offset, max: max, sort: 'constantName' )

        def listResult = []

        result.each {
            //supplementCss( it )
            // trim the object since we only need to return the constantName properties for listing
            listResult << [css : [constantName : it.constantName, id: it.id, version: it.version]]
        }

        log.trace "CssService.list is returning a ${result.getClass().simpleName} containing ${result.size()} style sheets"
        listResult
    }


    def count(Map params) {
        log.trace "CssService.count invoked"
        Css.count()
    }


    def show(Map params) {
        log.trace "CssService.show invoked"
        def result
        result = Css.find{constantName==params.id}

        supplementCss( result )
        log.trace "CssService.show returning ${result}"
        def showResult = [constantName : result.constantName, id: result.id, version: result.version, css: result.css, description: result.description]

        showResult
    }



    // TODO for now update(post) handles both update and creation to simplify client side logic
    def create(Map content, params) {
        log.trace "CssService.create invoked"


        if (WebUtils.retrieveGrailsWebRequest().getParameterMap().forceGenericError == 'y') {
            throw new Exception( "generic failure" )
        }

        def result

        Css.withTransaction {
            // compile/validate first
            result = compileCss(content.cssName, content.source, content.description)
        }
        //supplementCss( result )
        log.trace "CssService.create returning $result"
        result
    }

    // update is not used since the client may not know if a CSS exists or not when submitting (concurrent editing)
    def update(/*def id,*/ Map content) {
        log.trace "CssService.update invoked"

        checkForExceptionRequest()

        def result
        Css.withTransaction {
            result = compileCss(content.cssName, content.source, content.description)
        }
        //supplementCss( result )
        result
    }

    def compileCss( cssName, cssSource, description) {
        log.trace "in compileCss: \ncssSource=$cssSource"

        if (description==null)
            description = '';

        def overwrite=false
        def cssInstance  = Css.findByConstantName(cssName)
        def ret
        // check name duplicate
        if (cssInstance) {
            overwrite = true;
        }

        if (cssSource)  {
            // TODO CSS validation
            def validateResult =  [valid: true]
            if (validateResult.valid) {
                if (!cssInstance)
                    cssInstance = new Css([constantName:cssName])
                cssInstance.css=cssSource
                cssInstance.description = description

                cssInstance.save()
                ret = [statusCode:0, statusMessage:"Css has been validated and ${overwrite?'updated':'saved'} successfully."]
            } else {
                ret = [statusCode: 2, statusMessage:"CSS validation error. Stylesheet is not saved."]
                ret << [cssValidationResult:[errors: validateResult.error.join('\n')] ]
            }
        } else
            ret = [statusCode: 1, statusMessage:"CSS source is empty. Stylesheet is not compiled."]

        ret << [overwrite:overwrite]

        return ret
    }

    // note the content-type header still needs to be set in the request even we don't send in any content in the body
    void delete(id,Map content, params) {
        Css.withTransaction {
            def css = Css.find{constantName==id}
            css.delete(failOnError:true)
        }
    }



    public def checkOptimisticLock( domainObject, content ) {

        if (domainObject.hasProperty( 'version' )) {
            if (!content?.version) {
                domainObject.errors.reject( 'version', "net.hedtech.restfulapi.Css.missingVersion")
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


    private void supplementCss( Css css ) {
        MessageDigest digest = MessageDigest.getInstance("SHA1")
        digest.update("constantName:${css.getConstantName()}".getBytes("UTF-8"))

        def properties = [sha1:new BigInteger(1,digest.digest()).toString(16).padLeft(40,'0')]
        css.metaClass.getSupplementalRestProperties << {-> properties }
    }
}
