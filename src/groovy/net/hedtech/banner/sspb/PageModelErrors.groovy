package net.hedtech.banner.sspb

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 4/24/13
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
class PageModelErrors {
    def static MODEL_PARSING_ERR = [code : -1, message : "sspb.modelValidation.modelParsingErr.message"]
    def static MODEL_MISSING_DEFINITION_ERR = [code: 0, message: "sspb.modelValidation.modelMissingDefinitionErr.message"]
    def static MODEL_TYPE_MISSING_ERR = [code: 1, message: "sspb.modelValidation.modelMissingTypeErr.message"]
    def static MODEL_TYPE_INVALID_ERR = [code: 2, message: "type '{0}' is invalid"]
    def static MODEL_REQUIRED_ATTR_MISSING_ERR = [code: 3, message: "component is missing required attribute '{0}'"]
    def static MODEL_ATTR_INVALID_ERR = [code: 4, message: "attribute '{0}' is not allowed for component type '{1}'"]
    def static MODEL_REQUIRED_CHILD_MISSING_ERR = [code: 5, message: "child component '{0}' is missing"]
    def static MODEL_CHILD_INVALID_ERR = [code: 6, message: "child component of type '{0}' is not allowed"]



    def static getMessage(code, args = []) {
        def grailsApplication = new DefaultGrailsApplication()
        def appCtx = grailsApplication.parentContext
        return appCtx.getMessage(code, args)
    }

}
