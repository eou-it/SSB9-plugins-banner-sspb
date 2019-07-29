/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

class PageModelValidator {

    def pageBuilderModel
    def typeDef
    def compDef = [:]

    /*
    Assign a page model definition to this validator
    also generate internal data structure to optimize performance
     */
    def setPageBuilderModel(pageModelDef) {
        pageBuilderModel = pageModelDef
        typeDef = pageBuilderModel?.definitions?.componentTypeDefinition
        // build a map of component type -> component definition to optimize search
        if (typeDef) {
            for(e in typeDef) {
                for (t in e.componentType)
                    compDef[t] = e
            }
        }
    }

    /*
    Validate a raw page model in JSON (not a PageComponent which may have taken attributes with default values during JSON unmarshalling)
     pageComponent - page model source in JSON
     return - map of validation result  [valid: isValid, error [list of [code: errorCode, message: errorMessage, path : path of component that erred ] ] ]
     */
    def parseAndValidatePage(pageSource) {
        if (!typeDef) {
            def error = PageModelErrors.getError(error: PageModelErrors.MODEL_MISSING_DEFINITION_ERR)
            def ex = [errorCode:error.code, errorMessage:error.message] as PageModelValidationException
            throw ex
        }
        def page
        // parse JSON
        try {
            def slurper = new groovy.json.JsonSlurper()
            page = slurper.parseText(pageSource)
        } catch (groovy.json.JsonException ex) {
            return [valid:false, warn:[], error:[PageModelErrors.getError(error:PageModelErrors.MODEL_PARSING_ERR, args: [ex.getLocalizedMessage()] )  ]]
        }
        def res = validateComponent(page)
        if (res.valid){
            res.page = page
        }
        return res
    }

    /*
    recursively validate a page model
    all errors will be concatenated
     */
    def validateComponent(component, path = "") {
        // find if the component type is valid
        path += "/$component.name(type=$component.type)"
        def res = [warn:[], error:[], valid:true ]

        // check if type if missing
        if (!component.type) {
            res.valid = false
            res.error << PageModelErrors.getError(error: PageModelErrors.MODEL_TYPE_MISSING_ERR, path: path)
        }  else if (component.type == "all" || !compDef[component.type]) {
                res.valid = false
                res.error << PageModelErrors.getError(error: PageModelErrors.MODEL_TYPE_INVALID_ERR, path: path, args: [component.type])
        }  else {
            // check required attributes for all components
            if (compDef.all.requiredAttributes) {
                for (attr in compDef.all.requiredAttributes) {
                    if (!component[attr]) {
                        res.valid = false
                        res.error << PageModelErrors.getError(error: PageModelErrors.MODEL_REQUIRED_ATTR_MISSING_ERR, path: path, args: [attr])
                    }
                }
            }

            // check required attributes for the specific component type
            if (compDef[component.type].requiredAttributes) {
                for (attr in compDef[component.type].requiredAttributes) {
                    if (!component[attr]) {
                        res.valid = false
                        res.error << PageModelErrors.getError(error: PageModelErrors.MODEL_REQUIRED_ATTR_MISSING_ERR, path: path, args: [attr])
                    }
                }
            }

            // check invalid attributes  - changed behaviour, will generate a warning instead of an error
            component.each { prop, val ->
                if ( prop!= "components" && (!compDef.all.optionalAttributes.contains(prop) &&
                        !compDef.all.requiredAttributes.contains(prop) &&
                        !compDef[component.type].requiredAttributes.contains(prop) &&
                        !compDef[component.type].optionalAttributes.contains(prop) &&
                        !compDef[component.type].ignoredAttributes?.contains(prop))) {
                    //res.valid = false
                    res.warn << PageModelErrors.getError(error: PageModelErrors.MODEL_ATTR_INVALID_ERR, path: path, args: [prop,component.type])
                }
            }
            // get the list of all children of the component
            def children = []
            component?.components.each {children << it.type}

            // check required children
            compDef[component.type]?.requiredChildren.each {
                if (!children.contains(it)) {
                    res.valid = false
                    res.error << PageModelErrors.getError(error: PageModelErrors.MODEL_REQUIRED_CHILD_MISSING_ERR, path: path, args: [it])
                }
            }

            // check optional children
            children.each {
                if (!compDef[component.type]?.optionalChildren.contains(it) ) {
                    res.valid = false
                    res.error << PageModelErrors.getError(error: PageModelErrors.MODEL_CHILD_INVALID_ERR, path: path, args: [it])
                }
            }
        }
        // recurse into children
        component?.components.each {
            def childRes = validateComponent(it, path)
            res.valid = res.valid && childRes.valid
            if (!childRes.valid)
                res.error += childRes.error
            res.warn += childRes.warn
        }

        return res
    }


}
