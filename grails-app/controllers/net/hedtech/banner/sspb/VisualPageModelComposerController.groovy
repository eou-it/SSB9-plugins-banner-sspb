/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

class VisualPageModelComposerController {
    static defaultAction = "loadComposerPage"

    def loadComposerPage = {
        render (view:"visualComposer")
    }

    // TODO replace with REST API
    def pageModelDef = {
        //println "in pageModel, params = $params"
        def pageDefText = CompileService.class.classLoader.getResourceAsStream( 'PageModelDefinition.json' ).text
        render pageDefText
    }
}
