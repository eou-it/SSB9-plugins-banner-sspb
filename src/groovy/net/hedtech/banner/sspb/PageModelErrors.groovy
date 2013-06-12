package net.hedtech.banner.sspb

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 4/24/13
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
class PageModelErrors {

    def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }

    def static MODEL_PARSING_ERR                = [code:  -1, message: "sspb.modelValidation.modelParsingErr.message"]
    def static MODEL_MISSING_DEFINITION_ERR     = [code:   0, message: "sspb.modelValidation.modelMissingDefinitionErr.message"]
    def static MODEL_TYPE_MISSING_ERR           = [code:   1, message: "sspb.modelValidation.modelMissingTypeErr.message"]
    def static MODEL_TYPE_INVALID_ERR           = [code:   2, message: "sspb.modelValidation.modelInvalidTypeErr.message"]
    def static MODEL_REQUIRED_ATTR_MISSING_ERR  = [code:   3, message: "sspb.modelValidation.modelMissingAttributeErr.message"]
    def static MODEL_ATTR_INVALID_ERR           = [code:   4, message: "sspb.modelValidation.modelInvalidAttributeErr.message"]
    def static MODEL_REQUIRED_CHILD_MISSING_ERR = [code:   5, message: "sspb.modelValidation.modelMissingChildErr.message"]
    def static MODEL_CHILD_INVALID_ERR          = [code:   6, message: "sspb.modelValidation.modelInvalidChildErr.message"]
    def static MODEL_NAME_CONFLICT_ERR          = [code:   7, message: "sspb.modelValidation.modelInvalidNameErr.message"]
    def static MODEL_UNKNOWN_ERR                = [code: 100, message: "sspb.modelValidation.modelUnknownErr.message"]

    def static getError( errorDetails )  {
        def err = [ code:  errorDetails.error.code,
                    message: localizer(code: errorDetails.error.message, args: errorDetails.args),
                    path: errorDetails.path
                   ]
    }

    def static getMessage(code, args = []) {
        def grailsApplication = new DefaultGrailsApplication()
        def appCtx = grailsApplication.parentContext
        return appCtx.getMessage(code, args)
    }

}
