package net.hedtech.banner.sspb

class CustomPageController {

    static defaultAction = "page"
    def groovyPagesTemplateEngine
    def compileService

    def user = PBUser.get()

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


    // Check the user roles against the page roles
    private def userAccess = {user, page ->
        def result=[allow: false ]
        for (it in user.authorities) {
            //objectName is like SELFSERVICE-ALUMNI
            //role is BAN_DEFAULT_M
            //strip SELFSERVICE- this can be handled by spring security
            def r = it.objectName.substring(it.objectName.indexOf("-") + 1)
            page.pageRoles.findAll {p -> p.roleName == r}.each {
                result.allow |= it.allow
                if (result.allow)
                    return result //no need to evaluate more
            }
        }
        result
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
            if (page)
            	allow = userAccess(user, page).allow

            // maybe better to only store the assembled page?
            if (allow && page && page.compiledView && page.compiledController)
                html = compileService.assembleFinalPage(page.compiledView, page.compiledController)
        }
        if (html)
            render renderGsp(html, "Page$pageId")
        else if (!allow) {
            //TODO should redirect to log on page if anonymous user (or i18n at least)
            render(status: 401, text: "Deny access for $user.loginName")  
        }
        else
            invalidPage(message(code:"sspb.renderer.page.does.not.exist"))
    }
}