package net.hedtech.banner.sspb

class VisualPageModelComposerController {

    static defaultAction = "loadPageModel"
    def compileService

    def loadPageModel = {
        def pageInstance
        if (params.constantName) {
            pageInstance = Page.findByConstantName(params.constantName)
        } else {
            pageInstance = new Page()
        }
        def pageModel=[status: "", pageInstance: pageInstance, modelView: pageInstance.modelView, compiledView: pageInstance.compiledView, compiledController: pageInstance.compiledController  ]
        render (view:"visualComposer", model: [pageModel: pageModel])
    }

    // TODO replace with REST API
    def page = {
        println "in page, params = $params"
        def pageInstance
        if (params.pageName) {
            pageInstance = Page.findByConstantName(params.pageName)
            render pageInstance.modelView
        } else
            render ""
    }

    // TODO replace with REST API
    def pageModelDef = {
        println "in pageModel, params = $params"
        def pageDefText = CompileService.class.classLoader.getResourceAsStream( 'PageModelDefinition.json' ).text
        render pageDefText
    }

    def compile = {
        def pageInstance
        def statusMessage=""
        pageInstance = Page.findByConstantName(params.constantName)
        if (!pageInstance) {
            pageInstance = new Page(params)
        }
        def pageModel
        //println "Page ModelView = " + pageInstance.modelView
        if (params.modelView)  {
            def validateResult =  compileService.preparePage(params.modelView)
            if (validateResult.valid) {
                def compiledJSCode=compileService.compileController(validateResult.pageComponent)
                statusMessage="JavaScript is compiled\n"
                def compiledView = compileService.compile2page(validateResult.pageComponent)
                statusMessage+="HTML is compiled\n"
                def combinedView = compileService.assembleFinalPage(compiledView, compiledJSCode)
                //validateHtml(combinedView)
                println "Page is compiled\n"
                pageInstance.modelView=params.modelView
                pageInstance.compiledView = compiledView
                pageInstance.compiledController=compiledJSCode
                pageInstance=pageInstance.save()
                pageModel=[status:statusMessage, pageInstance: pageInstance, modelView: pageInstance.modelView, compiledView: pageInstance.compiledView, compiledController: pageInstance.compiledController]
            } else {
                pageModel=[status:statusMessage, pageInstance: pageInstance, modelView: params.modelView, compiledView: "Page model validation error (model not saved):\n" + validateResult.error.join('\n'),
                        compiledController:""]
            }
        }
        render (view:"visualComposer", model: [pageModel: pageModel])
        println "finished compile on ${new Date()}"
    }

    private def validateHtml(pageTxt)  {
        def slurper = new XmlSlurper()
        return
        // Not sure what the slurper chokes on, error messages are not helping
        def test =
            """ <html>
                    $pageTxt
               </html>"""
        try {
            def htmlParser = slurper.parseText(test)
            println htmlParser.text()
        }  catch (e) {
            println e
            println "Source: \n$test"
        }
    }
}
