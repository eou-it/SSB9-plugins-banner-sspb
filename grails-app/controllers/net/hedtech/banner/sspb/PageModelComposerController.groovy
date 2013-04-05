package net.hedtech.banner.sspb

class PageModelComposerController {
    def compileService
    def index() { println "in index()" }

    def compose = {
        println "in compose"
        def pageInstance = new Page(params)
        //pageInstance.modelView = "123"

        render (view:"composer", model: [pageInstance: pageInstance])

    }


    def compile = {
        def pageInstance = new Page(params)
        //println "Page ModelView = " + pageInstance.modelView
        if (pageInstance.modelView)  {
            def validateResult =  compileService.preparePage(pageInstance.modelView)
            if (validateResult.valid) {
                def compiledJSCode=compileService.compileController(validateResult.pageComponent)
                println "JavaScript is compiled\n"

                def compiledView = compileService.compile2page(validateResult.pageComponent)
                println "HTML is compiled\n"

                def combinedView = compileService.assembleFinalPage(compiledView, compiledJSCode)
                println "Page is compiled\n"

                pageInstance.compiledView = compiledView

                pageInstance.compiledController=compiledJSCode
            } else {
                pageInstance.compiledView="Page model validation error:\n" + validateResult.errors.join('\n')
            }


        }

        render (view:"composer", model: [pageInstance: pageInstance])
    }


}
