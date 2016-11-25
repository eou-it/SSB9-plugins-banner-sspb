/*******************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.sspb

import net.hedtech.banner.css.Css
import net.hedtech.banner.exceptions.ApplicationException
import org.codehaus.groovy.grails.web.util.WebUtils

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


    def count(Map ignore) {
        log.trace "CssService.count invoked"
        Css.count()
    }


    def show(Map params) {
        log.trace "CssService.show invoked"
        def result
        def showResult
        //result = Css.find{constantName==params.id}
        result = Css.findByConstantName(params.id)
        if (result) {
            //supplementCss( result )
            log.trace "CssService.show returning ${result}"
            showResult = [constantName : result.constantName, id: result.id, version: result.version, css: result.css, description: result.description]
        }
        showResult
    }



    // TODO for now update(post) handles both update and creation to simplify client side logic
    def create(Map content, ignore) {
        log.trace "CssService.create invoked"


        if (WebUtils.retrieveGrailsWebRequest().getParameterMap().forceGenericError == 'y') {
            throw new ApplicationException( CssService, "generic failure" )
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

        //checkForExceptionRequest()

        def result
        Css.withTransaction {
            result = compileCss(content.cssName, content.source, content.description)
        }
        //supplementCss( result )
        result
    }

    def compileCss( cssName, cssSource, description) {
        log.trace "in compileCss: \ncssSource=$cssSource"

        description = description?:""
        def cssInstance  = Css.findByConstantName(cssName)
        def ret

        if (!validateInput([constantName: cssName, description: description])) {
            ret = [statusCode: 1, statusMessage: message(code: "sspb.css.cssManager.cssSource.invalid.name.message")]
        }
        else if (cssSource)  {
            // TODO CSS validation
            def validateResult =  [valid: true]
            if (validateResult.valid) {
                if (!cssInstance)
                    cssInstance = new Css([constantName:cssName])
                cssInstance.css=cssSource
                cssInstance.description = description

                cssInstance.save()
                ret = [statusCode:0, statusMessage:"${message(code:'sspb.css.cssManager.saved.message')}"]
            } else {
                ret = [statusCode: 2, statusMessage:message(code:"sspb.css.cssManager.stylesheet.validation.error.message")]
                ret << [cssValidationResult:[errors: validateResult.error.join('\n')] ]
            }
        } else {
            ret = [statusCode: 1, statusMessage: message(code:"sspb.css.cssManager.cssSource.empty.message")]
        }

        return ret
    }

    // note the content-type header still needs to be set in the request even we don't send in any content in the body
    void delete(Map ignore, params) {
        Css.withTransaction {
            def css = Css.find{constantName==params.id}
            css.delete(failOnError:true)
        }
    }

    private def validateInput(params) {
        def name = params?.constantName
        def valid = (name?.size() <= 60)
        valid &= name ==~ /[a-zA-Z]+[a-zA-Z0-9_\-\.]*/
        def description = params?.description
        valid &=  (description.size() <= 255)
        valid
    }

}
