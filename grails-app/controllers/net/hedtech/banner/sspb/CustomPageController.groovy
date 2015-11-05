/*******************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.sspb

class CustomPageController {

    static defaultAction = "page"
    def groovyPagesTemplateEngine
    def compileService
    def user = PBUser.get()

    def page = {
        if (params.id=="menu") {    //Work around aurora issue calling this 'page'. Todo: analyse and provide better fix
            render ""
            return
        }
        def page
        def allow = false
        def pageId = params.id
        //support numeric ID or constantName
        try {
            Long id = pageId
            page = Page.get(id)
        }
        catch (e) { //pageId is not a Long, find by name
            page = Page.findByConstantName(pageId)
        }
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
        def allow=true


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
        else if (!allow) {
            render(status: 401, message(code: "sspb.renderer.page.deny.access",  args:user.loginName))
        }
        else
            invalidPage(message(code:"sspb.renderer.page.does.not.exist"))
    }
}