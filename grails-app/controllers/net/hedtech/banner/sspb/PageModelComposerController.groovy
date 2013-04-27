package net.hedtech.banner.sspb

class PageModelComposerController {

    static defaultAction = "loadPageModel"
    def compileService
    def compileAjsService //hack: use this service to compile if page name ends with Ajs.

    def loadPageModel = {
        def pageInstance
        if (params.constantName) {
            pageInstance = Page.findByConstantName(params.constantName)
        } else {
            pageInstance = new Page()
        }
        def pageModel=[status: "", pageInstance: pageInstance]
        render (view:"composer", model: [pageModel: pageModel])
    }

    def compile = {
        def pageInstance
        def statusMessage=""
        pageInstance = Page.findByConstantName(params.constantName)
        if (pageInstance) {
            pageInstance.modelView=params.modelView
        } else {
            pageInstance = new Page(params)
        }
        def pageModel
        //println "Page ModelView = " + pageInstance.modelView
        if (pageInstance.modelView)  {
            if (pageInstance.constantName.endsWith("Ajs"))  {
                // test new compiler for "Ajs" pages
                //TODO : if happy with new compiler get rid if special compiler for pages ending wit Ajs
                def validateResult =  compileAjsService.preparePage(pageInstance.modelView)
                if (validateResult.valid) {
                    def compiledJSCode=compileAjsService.compileController(validateResult.pageComponent)
                    statusMessage="JavaScript is compiled\n"
                    def compiledView = compileAjsService.compile2page(validateResult.pageComponent)
                    statusMessage+="HTML is compiled\n"
                    def combinedView = compileAjsService.assembleFinalPage(compiledView, compiledJSCode)
                    //validateHtml(combinedView)
                    println "Page is compiled\n"
                    pageInstance.compiledView = compiledView
                    pageInstance.compiledController=compiledJSCode
                } else {
                    pageInstance.compiledView="Page model validation error:\n" + validateResult.errors.join('\n')
                }
            } else {  //Original compiler
                def validateResult =  compileService.preparePage(pageInstance.modelView)
                if (validateResult.valid) {
                    def compiledJSCode=compileService.compileController(validateResult.pageComponent)
                    statusMessage="JavaScript is compiled\n"
                    def compiledView = compileService.compile2page(validateResult.pageComponent)
                    statusMessage+="HTML is compiled\n"
                    def combinedView = compileService.assembleFinalPage(compiledView, compiledJSCode)
                    //validateHtml(combinedView)
                    println "Page is compiled\n"
                    pageInstance.compiledView = compiledView
                    pageInstance.compiledController=compiledJSCode
                } else {
                    pageInstance.compiledView="Page model validation error:\n" + validateResult.errors.join('\n')
                }
            }
            pageInstance=pageInstance.save()
            pageModel=[status:statusMessage, pageInstance: pageInstance]
        }
        render (view:"composer", model: [pageModel: pageModel])
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
