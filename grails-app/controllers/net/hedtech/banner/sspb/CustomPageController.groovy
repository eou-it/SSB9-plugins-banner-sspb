/*******************************************************************************
 Copyright 2013-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.sspb

class CustomPageController {

    static defaultAction = "page"
    def groovyPagesTemplateEngine
    def compileService
    def grailsApplication
    def pageUtilService

    def page() {
        if (params.id=="menu") {    //Work around aurora issue calling this 'page'. Todo: analyse and provide better fix
            render ""
            return
        }
        if (params.id=="pbadm.ssoauth") {
            if (params.url) {
                redirect(uri: params.url)
            }
        }
        if (grailsApplication.config.pageBuilder?.enabled) {
            // render view page.gsp which will be including getHTML
            render(view: "page", model: [id: params.id])
        } else {
            redirect(uri: '/themeEditor')
        }
    }

    private def invalidPage(e) {
        render(status: 404, text: e)
    }

    private def renderGsp(String templateString, String pageName) {
        def t = groovyPagesTemplateEngine.createTemplate(templateString, "${pageName}.gsp")
        def writer = new StringWriter()
        t.make().writeTo(writer)
        return writer.toString()
    }


    def getHTML() {
        try {
            def pageId = params.id
            def html
            def page = Page.findByConstantName(pageId)

            // maybe better to only store the assembled page?
            if (page && page.compiledView && page.compiledController) {
                html = compileService.assembleFinalPage(page.compiledView, page.compiledController)
            }
            if (html) {
                def pageName = pageId+'_'+page.version
                synchronized (CustomPageController.class) {
                    boolean isNewProperties = false
                    if (PageUtilService.pageVersionMap.get(page.constantName)) {
                        Integer pageCurrVersion = page.version
                        Integer pageMapVersion = PageUtilService.pageVersionMap.get(page.constantName)
                        if (pageCurrVersion > pageMapVersion) {
                            isNewProperties = true
                        }
                    } else {
                        isNewProperties = true
                    }
                    if(isNewProperties){
                        pageUtilService.compileAll(page.constantName)
                        PageUtilService.pageVersionMap.put(page.constantName, page.version)
                    }
                }
                render renderGsp(html, pageName)
            } else {
                invalidPage(message(code: "sspb.renderer.page.does.not.exist"))
            }
        } catch ( RuntimeException ex ) {
            ex.printStackTrace()
            invalidPage( message( code: "sspb.renderer.page.does.not.exist"))
        }
    }

}