package net.hedtech.banner.sspb

class PageModelComposerController {

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
        render (view:"composer", model: [pageModel: pageModel])
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
                try {
                    def compiledJSCode=compileService.compileController(validateResult.pageComponent)
                    statusMessage=g.message(code: message(code:"sspb.compiler.js.okMessage"), args: [new Date()])+"\n"
                    def compiledView = compileService.compile2page(validateResult.pageComponent)
                    statusMessage+=g.message(code: message(code:"sspb.compiler.html.okMessage"), args: [new Date()])+"\n"
                    //def combinedView = compileService.assembleFinalPage(compiledView, compiledJSCode)
                    println "Page is compiled\n"
                    pageInstance.modelView=params.modelView
                    pageInstance.compiledView = compiledView
                    pageInstance.compiledController=compiledJSCode
                    pageInstance=pageInstance.save()
                    pageModel=[status:statusMessage, pageInstance: pageInstance, modelView: pageInstance.modelView,
                               compiledView: pageInstance.compiledView, compiledController: pageInstance.compiledController]
                } catch (e) {
                    validateResult.valid=false
                    validateResult.error=[e.getLocalizedMessage()]
                }
            }

            if ( !validateResult.valid) {
                pageModel=[status:statusMessage, pageInstance: pageInstance, modelView: params.modelView,
                           compiledView: g.message(code:"sspb.modelValidation.error", args: [validateResult.error.join('\n')]),
                           compiledController:""]
            }
        }
        render (view:"composer", model: [pageModel: pageModel])
        println "finished compile on ${new Date()}"
    }

}
