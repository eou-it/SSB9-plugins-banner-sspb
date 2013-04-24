package net.hedtech.banner.sspb

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 4/23/13
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
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
    def validatePage(pageSource) {
        if (!typeDef) {
            def ex = [errorCode:PageModelErrors.MODEL_MISSING_DEFINITION_ERR.code, errorMessage:"Page model component type definition not set"] as PageModelValidationException
            throw ex
        }
        def page
        // parse JSON
        try {
            def slurper = new groovy.json.JsonSlurper()
            page = slurper.parseText(pageSource)
        } catch (groovy.json.JsonException ex) {
            return [valid:false, error:[[code:PageModelErrors.MODEL_PARSING_ERR.code, message:"page model parsing error: ${ex.getMessage()}"]]]
        }

        return validateComponent(page)

    }

    /*
    recursively validate a page model
    all errors will be concatenated
     */
    def validateComponent(component, path = "") {
        // find if the component type is valid
        path += "/$component.name(type=$component.type)"
        def res = [error:[], valid:true ]

        // check if type if missing
        if (!component.type) {
            res.valid = false
            res.error << [code: PageModelErrors.MODEL_TYPE_MISSING_ERR.code, path : path, message: "type is missing"]
        }  else if (component.type == "all" || !compDef[component.type]) {
                res.valid = false
                res.error << [code:PageModelErrors.MODEL_TYPE_INVALID_ERR.code, path : path, message:"type '$component.type' is invalid"]
        }  else {
            // check required attributes for all components
            if (compDef.all.requiredAttributes) {
                for (attr in compDef.all.requiredAttributes) {
                    if (!component[attr]) {
                        res.valid = false
                        res.error << [code: PageModelErrors.MODEL_REQUIRED_ATTR_MISSING_ERR.code, path : path, message:"component is missing required attribute '${component[attr]}'"]
                    }
                }
            }

            // check required attributes for the specific component type
            if (compDef[component.type].requiredAttributes) {
                for (attr in compDef[component.type].requiredAttributes) {
                    if (!component[attr]) {
                        res.valid = false
                        res.error << [code:PageModelErrors.MODEL_REQUIRED_ATTR_MISSING_ERR.code, path : path, message:"component is missing required attribute '$attr'"]
                    }
                }
            }

            // check invalid attributes
            component.each { prop, val ->
                if ( prop!= "components" && (!compDef.all.optionalAttributes.contains(prop) &&
                        !compDef.all.requiredAttributes.contains(prop) &&
                !compDef[component.type].requiredAttributes.contains(prop) &&
                !compDef[component.type].optionalAttributes.contains(prop))) {
                    res.valid = false
                    res.error << [code: PageModelErrors.MODEL_ATTR_INVALID_ERR.code, path : path, message:"attribute '$prop' is not allowed for component type '$component.type'"]
                }
            }
            // get the list of all children of the component
            def children = []
            component?.components.each {children << it.type}

            // check required children
            compDef[component.type]?.requiredChildren.each {
                if (!children.contains(it)) {
                    res.valid = false
                    res.error << [code: PageModelErrors.MODEL_REQUIRED_CHILD_MISSING_ERR.code, path: path, message: "required child '$it' is missing"]
                }
            }

            // check optional children
            children.each {
                if (!compDef[component.type]?.optionalChildren.contains(it) ) {
                    res.valid = false
                    res.error << [code: PageModelErrors.MODEL_CHILD_INVALID_ERR.code, path: path, message: "child component of type '${it}' is not allowed"]
                }
            }
        }
        // recurse into children
        component?.components.each {
            def childRes = validateComponent(it, path)
            res.valid = res.valid & childRes.valid
            if (!childRes.valid)
                res.error += childRes.error
        }

        return res
    }


}
