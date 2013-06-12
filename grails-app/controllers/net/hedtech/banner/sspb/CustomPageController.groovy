package net.hedtech.banner.sspb





class CustomPageController {

    static defaultAction = "page"
    def groovyPagesTemplateEngine
    def compileService

    def page = {
        // render view page.gsp which will be including getHTML
        render  (view:"page", model:[id: params.id])
    }

    private def invalidPage = { e ->
        render(status: 404, text: e)
    }

    private def renderGsp(String templateString, String pageName) {
        def t = groovyPagesTemplateEngine.createTemplate(templateString, "${pageName}.gsp")
        def writer = new StringWriter()
        t.make().writeTo(writer)
        return writer.toString()
    }

    def getHTML = {
        def pageId = params.id
        def html
        if (params.file == "true") { // render a file
            html = new File("target/compiledPage/page${pageId}.html").getText()
        } else {
            def page
            //support numeric ID or constantName
            try {
                Long id = pageId
                page = Page.get(id)
            }
            catch (e) { //pageId is not a Long, find by name
                page = Page.findByConstantName(pageId)
            }
            // maybe better to only store the assembled page?
            if (page && page.compiledView && page.compiledController)
                html = compileService.assembleFinalPage(page.compiledView, page.compiledController)
        }
        if (html)
            render renderGsp(html, "Page$pageId")
        else
            invalidPage(message(code:"sspb.renderer.page.does.not.exist"))
    }
}