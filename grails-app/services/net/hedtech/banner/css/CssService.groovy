/*******************************************************************************
 Copyright 2018-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.css

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.security.DeveloperSecurityService
import net.hedtech.banner.sspb.CommonService
import org.codehaus.groovy.grails.web.util.WebUtils
import net.hedtech.banner.service.ServiceBase
import org.springframework.context.i18n.LocaleContextHolder

class CssService extends ServiceBase {

    static transactional = true
    def dateConverterService
    def developerSecurityService

    def list(Map params) {

        log.trace "CssService.list invoked with params $params"
        def result

        // TODO: Do validation testing in create or update -- this is temporary
        /*
        if (params.forceValidationError == 'y') {
            // This will throw a validation exception...
            new Css(code:'FAIL', description: 'Code exceeds 2 chars').save(failOnError:true)
        } */
        params = extractReqPrams(params)

        def max = Math.min( params.max ? params.max.toInteger() : 100,  100)
        def offset = params.offset ?: 0

        def qp= [offset: offset, max: max, sort: params.sort ?: 'constantName' , order:params.order ?: 'asc']

        if  (params.constantName) {
            result = super.getDomainClass().findAllByConstantNameIlike(params.constantName, qp)
        } else {
            result = super.list( qp )
        }

        def listResult = []
        result.each {
            //supplementCss( it )
            // trim the object since we only need to return the constantName properties for listing
            if (params.containsKey('getGridData')) {
                listResult << [constantName: it.constantName, id: it.id, version: it.version, owner: it.owner ,
                               dateCreated: dateConverterService.parseGregorianToDefaultCalendar(it.dateCreated), lastUpdated: dateConverterService.parseGregorianToDefaultCalendar(it.lastUpdated)]
            } else {
                listResult << [css: [constantName: it.constantName, id: it.id, version: it.version, owner: it.owner ]]
            }
        }
        log.trace "CssService.list is returning a ${result.getClass().simpleName} containing ${result.size()} style sheets"
        listResult
    }


    def count(Map params) {
        log.trace "CssService.count invoked"
        params = extractReqPrams(params)
        if (params.constantName) {
            super.getDomainClass().countByConstantNameIlike(params.constantName)
        } else {
            super.count()
        }

    }


    def show(Map params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        if(params && params.containsKey('getGridData')) {
            def returnMap = [
                    result: list(params),
                    length: count(params)
            ]

            return returnMap

        }

        log.trace "CssService.show invoked"
        def result
        def showResult
        result = Css.fetchByConstantName(params.id)
        if (result) {
            //supplementCss( result )
            log.trace "CssService.show returning ${result}"
            showResult = [constantName : result.constantName, id: result.id, version: result.version, css: result.css, owner: result.owner ,
                          description: result.description,  allowModify: developerSecurityService.isAllowModify(result.constantName,DeveloperSecurityService.CSS_IND),
                          allowUpdateOwner: developerSecurityService.isAllowUpdateOwner(result.constantName, DeveloperSecurityService.CSS_IND)]
        }
        showResult
    }



    // TODO for now update(post) handles both update and creation to simplify client side logic
    def create(Map content, ignore) {
        log.trace "cssService.create invoked"


        if (WebUtils.retrieveGrailsWebRequest().getParameterMap().forceGenericError == 'y') {
            throw new ApplicationException( CssService, "generic failure" )
        }

        def result = compileCss(content.cssName, content.source, content.description, content.owner)
        result <<[allowModify:developerSecurityService.isAllowModify(content.cssName,developerSecurityService.CSS_IND) ,
                  allowUpdateOwner: developerSecurityService.isAllowUpdateOwner(content.cssName, developerSecurityService.CSS_IND)]
        //supplementCss( result )
        log.trace "CssService.create returning $result"
        result
    }

    // update is not used since the client may not know if a CSS exists or not when submitting (concurrent editing)
    def update(/*def id,*/ Map content) {
        log.trace "CssService.update invoked"

        //checkForExceptionRequest()

        def result = compileCss(content.cssName, content.source, content.description, content.owner)

        //supplementCss( result )
        result
    }

    def compileCss( cssName, cssSource, description, owner) {
        log.trace "in compileCss: \ncssSource=$cssSource"

        description = description?:""
        def cssInstance  = Css.fetchByConstantName(cssName)
        def ret

        if (!validateInput([constantName: cssName, description: description])) {
            ret = [statusCode: 1, statusMessage: message(code: "sspb.css.cssManager.cssSource.invalid.name.message")]
        }
        else if (cssSource)  {
            // TODO CSS validation
            def validateResult =  [valid: true]
            if (validateResult.valid) {
                if (cssInstance) {
                    cssInstance.css = cssSource
                    cssInstance.description = description
                    cssInstance.owner = owner
                    super.update(cssInstance)
                } else {
                    cssInstance = new Css([constantName: cssName, description: description, css: cssSource, owner:owner])
                    super.create(cssInstance)
                }
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
            def css = Css.fetchByConstantName(params.id)
            super.delete(css)
    }

    private def validateInput(params) {
        def name = params?.constantName
        def valid = (name?.size() <= 60)
        valid &= name ==~ /[a-zA-Z]+[a-zA-Z0-9_\-\.]*/
        def description = params?.description
        valid &=  (description.size() <= 255)
        valid
    }

    def extractReqPrams(Map reqParams) {
        Map params = [:]
        if(reqParams && reqParams.searchString){
            params.constantName = "%$reqParams.searchString%"
        }

        if(reqParams && reqParams.sortColumnName){
            params.sort = reqParams.sortColumnName
        }

        if(reqParams && 'true'.equalsIgnoreCase(reqParams.ascending)){
            params.order = 'asc'
        }

        if(reqParams && 'false'.equalsIgnoreCase(reqParams.ascending)){
            params.order = 'desc'
        }

        params << reqParams
    }
}
