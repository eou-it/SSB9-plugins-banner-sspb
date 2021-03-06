/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

@SuppressWarnings('UnusedVariable')
class PageModelErrors {


    def static MODEL_PARSING_ERR                = [code:  -1, message: "sspb.modelValidation.modelParsingErr.message"]
    def static MODEL_MISSING_DEFINITION_ERR     = [code:   0, message: "sspb.modelValidation.modelMissingDefinitionErr.message"]
    def static MODEL_TYPE_MISSING_ERR           = [code:   1, message: "sspb.modelValidation.modelMissingTypeErr.message"]
    def static MODEL_TYPE_INVALID_ERR           = [code:   2, message: "sspb.modelValidation.modelInvalidTypeErr.message"]
    def static MODEL_REQUIRED_ATTR_MISSING_ERR  = [code:   3, message: "sspb.modelValidation.modelMissingAttributeErr.message"]
    def static MODEL_ATTR_INVALID_ERR           = [code:   4, message: "sspb.modelValidation.modelInvalidAttributeErr.message"]
    def static MODEL_REQUIRED_CHILD_MISSING_ERR = [code:   5, message: "sspb.modelValidation.modelMissingChildErr.message"]
    def static MODEL_CHILD_INVALID_ERR          = [code:   6, message: "sspb.modelValidation.modelInvalidChildErr.message"]
    def static MODEL_NAME_CONFLICT_ERR          = [code:   7, message: "sspb.modelValidation.modelInvalidNameErr.message"]
    def static MODEL_INVALID_DELTA_ERR          = [code:   8, message: "sspb.modelValidation.modelInvalidDeltaErr.message"]
    def static MODEL_UNKNOWN_ERR                = [code: 100, message: "sspb.modelValidation.modelUnknownErr.message"]

    def static getError( errorDetails )  {

        // Work around for unit testing
        if (grails.util.Holders.servletContext == null) {
            return [ code:  errorDetails.error.code,
                     message: "code: ${errorDetails.error.message}, args: ${errorDetails.args}",
                     path: errorDetails.path
            ]
        }

        def err = [ code:  errorDetails.error.code,
                    message: message(code: errorDetails.error.message, args: errorDetails.args),
                    path: errorDetails.path
                   ]

    }

}
