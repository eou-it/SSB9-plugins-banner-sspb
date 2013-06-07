package net.hedtech.banner.sspb


import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

class PageUtilService {

    def extData = System.getProperties().get('SSPB_DATA_DIR')

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
            println "Importing $pageName"
            def page = new Page(constantName: pageName, modelView: modelView)
            if (Page.findByConstantName(pageName)) {
                page.constantName+=".imp.dup"
                def page1=Page.findByConstantName(page.constantName)
                if (page1) //if we have already saved a duplicate, get rid of it.
                    page1.delete(flush: true)
                println "WARN: Page already exists. Imported as ${page.constantName}."
            } else {
                println "Imported ${page.constantName} "
            }
            page = page.save(flush: true)
        }
    }

    def compileAll(String pattern) {
        def pat = pattern?pattern:"%"
        def pages = Page.findAllByConstantNameLike(pat)
        def errors =[]
        pages.each { page ->
            println "Compiling $page.constantName"
            def validateResult =  CompileService.preparePage(page.modelView)
            def statusMessage

            if (validateResult.valid) {
                page.compiledController=CompileService.compileController(validateResult.pageComponent)
                page.compiledView = CompileService.compile2page(validateResult.pageComponent)
                statusMessage = "Page is compiled\n"
                page=page.save(flush: true)
            }  else {
                def error = [pageName: page.constantName, errorMessage:validateResult.error.join('\n')]
                statusMessage = "Page compiled with Errors:\n ${error.errorMessage}"
                errors <<error
            }
            println statusMessage
        }
        errors
    }

    void updateProperties( Map properties, String baseName){
        def bundleLocation = "$extData/${baseName}.properties"
        def bundle = new File(bundleLocation)
        def temp = new Properties()
        if (bundle.exists())
            new org.springframework.util.DefaultPropertiesPersister().load(temp, new InputStreamReader( new FileInputStream(bundle), "UTF-8"))
        temp.putAll(properties)
        new org.springframework.util.DefaultPropertiesPersister().store( temp, new OutputStreamWriter( new FileOutputStream(bundle),"UTF-8" ), "")
    }

    def reloadBundles = {
        ApplicationContext applicationContext = (ApplicationContext) ServletContextHolder.getServletContext()
                                                .getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
        def messageSource = applicationContext.getBean("messageSource")
        messageSource.clearCache()
    }

}
