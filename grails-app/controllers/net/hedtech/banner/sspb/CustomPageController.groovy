/*******************************************************************************
 Copyright 2013-2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.sspb

import grails.core.GrailsApplication
import net.hedtech.banner.exceptions.MepCodeNotFoundException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.core.context.SecurityContextHolder

class CustomPageController {

    static defaultAction = "page"
    def groovyPagesTemplateEngine
    def compileService
    GrailsApplication grailsApplication
    def pageService

    def userSessionValidationCheck() {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        render(status: 200, text: userIn?.class?.name?.endsWith('BannerUser'))
    }

    def page() {
        if (params.id=="menu") {    //Work around aurora issue calling this 'page'. Todo: analyse and provide better fix
            render ""
            return
        }
        def pageId = params.id
        if(params?.format){
            pageId = params.id+"."+params.format
        }
        if (pageId =="pbadm.ssoauth") {
            if (params.url) {
                redirect(uri: params.url)
            }
        }
        if (grailsApplication.config.pageBuilder?.enabled) {
            // render view page.gsp which will be including getHTML
            render(view: "page", model: [id: pageId])
        } else {
            redirect(uri: '/themeEditor')
        }
    }

    private def invalidPage(e) {
        render(status: 404, text: e)
    }

    private def renderGsp(String templateString, String pageName) {
        def t = groovyPagesTemplateEngine.createTemplate(templateString, "${pageName}")
        def writer = new StringWriter()
        t.make().writeTo(writer)
        return writer.toString()
    }


    def getHTML() {
        def local = LocaleContextHolder.getLocale()
        try {
            def pageId = params.id
            def html
            def page = Page.findByConstantName(pageId)

            // maybe better to only store the assembled page?
            if (page && page.compiledView && page.compiledController) {
                html = compileService.assembleFinalPage(page.compiledView, page.compiledController)
            }
            if (html) {
                def pageName = pageId+'_'+local.getLanguage()+page.version+".gsp"
                 if(!groovyPagesTemplateEngine.pageCache.get(pageName)){
                     pageService.compilePage(page)
                }
                render renderGsp(html, pageName)
            } else {
                invalidPage(message(code: "sspb.renderer.page.does.not.exist"))
            }
        } catch(MepCodeNotFoundException mex){
            throw mex
        } 
        catch ( RuntimeException ex ) {
            ex.printStackTrace()
            invalidPage( message( code: "sspb.renderer.page.does.not.exist"))
        }
    }

}
