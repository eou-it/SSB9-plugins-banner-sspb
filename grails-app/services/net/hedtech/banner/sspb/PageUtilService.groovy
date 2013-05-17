package net.hedtech.banner.sspb

import net.hedtech.banner.sspb.Page

class PageUtilService {

    def compileAjsService

    void exportAllToFile(String path) {
        Page.findAll().each { page ->
            if (page.constantName.endsWith(".imp.dup"))
                println "Skipped exporting duplicate ${page.constantName}"
            else {
                def file = new File("$path/${page.constantName}.json")
                def jsonString =  page.modelView
                println "Exported $page.constantName"
                file.text = jsonString
            }
        }
    }

    void importAllFromFile(String path) {
        new File(path).eachFileMatch(~/.*.json/) {   file ->
            def modelView = file.getText()
            def pageName = file.name.substring(0,file.name.lastIndexOf(".json"))
            def page = new Page(pageName: pageName, modelView: modelView)
            if (Page.findByconstantName(pageName)) {
                page.constantName+=".imp.dup"
                def page1=Page.findByconstantName(page.constantName)
                if (page1) //if we have already saved a duplicate, get rid of it.
                    page1.delete(flush: true)
                println "WARN: Page already exists. Imported as ${page.constantName}."
            } else {
                println "Imported ${page.constantName} "
            }
            page = page.save(flush: true)
        }
    }

    void compileAll(String pattern) {
        def pat = pattern?pattern:"%"
        Page.findByConstantNameIlike(pat).each { page ->
            def validateResult =  compileAjsService.preparePage(page.modelView)
            def statusMessage
            if (validateResult.valid) {
                page.compiledController=compileAjsService.compileController(validateResult.pageComponent)
                page.compiledView = compileAjsService.compile2page(validateResult.pageComponent)
                statusMessage = "Page is compiled\n"
                page=page.save(flush: true)
            }  else {
                statusMessage = "Page compiled with Errors:\n ${validateResult.error.join('\n')}"
            }
            println statusMessage
        }
    }
}
