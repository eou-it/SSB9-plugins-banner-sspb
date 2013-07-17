package net.hedtech.banner.sspb


import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import net.hedtech.banner.tools.i18n.SortedProperties
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib

class PageUtilService {

    def final static propertyDataDir = 'SSPB_DATA_DIR'
    def static externalDataLocation = getExternalDataLocation()

    def static getExternalDataLocation() {
        def result = System.getProperties().get(propertyDataDir)
        if (!result)
            result=System.getenv("TEMP")
        if (!result)
            result=System.getenv("TMP")
        result
    }

    //Internationalize println in this service - this service is to be used by a batch export/import utility
    def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }

    void exportAllToFile(String path) {
        Page.findAll().each { page ->
            if (page.constantName.endsWith(".imp.dup"))
                println localizer(code:"sspb.pageutil.export.skipDuplicate.message", args:[page.constantName])
            else {
                def file = new File("$path/${page.constantName}.json")
                def jsonString =  page.modelView
                println localizer(code:"sspb.pageutil.export.page.done.message", args:[page.constantName])
                file.text = jsonString
            }
        }
    }

    void importAllFromFile(String path) {
        new File(path).eachFileMatch(~/.*.json/) {   file ->
            def modelView = file.getText()
            def pageName = file.name.substring(0,file.name.lastIndexOf(".json"))
            def page = new Page(constantName: pageName, modelView: modelView)
            if (Page.findByConstantName(pageName)) {
                page.constantName+=".imp.dup"
                def page1=Page.findByConstantName(page.constantName)
                if (page1) //if we have already saved a duplicate, get rid of it.
                    page1.delete(flush: true)
                println localizer(code:"sspb.pageutil.import.duplicate.page.done.message", args:[page.constantName])
            } else {
                println localizer(code:"sspb.pageutil.import.page.done.message", args:[page.constantName])
            }
            if (page.constantName.startsWith("pbadm.")){
                //add a WTAILORADMIN role so the pages can be used
                //TODO export PageRoles
                def role=new PageRole(roleName: "WTAILORADMIN")
                page.addToPageRoles(role)
            }
            page = page.save(flush: true)
        }
    }

    def compileAll(String pattern) {
        def pat = pattern?pattern:"%"
        def pages = Page.findAllByConstantNameLike(pat)
        def errors =[]
        pages.each { page ->
            def validateResult =  CompileService.preparePage(page.modelView)
            def statusMessage

            if (validateResult.valid) {
                page.compiledController=CompileService.compileController(validateResult.pageComponent)
                page.compiledView = CompileService.compile2page(validateResult.pageComponent)
                statusMessage = localizer(code:"sspb.pageutil.compile.page.done.message", args:[page.constantName])
                page=page.save(flush: true)
            }  else {
                def error = [pageName: page.constantName, errorMessage:validateResult.error.join('\n')]
                statusMessage = localizer(code:"sspb.pageutil.compile.page.done.message", args:[page.constantName,error.errorMessage])
                errors <<error
            }
            println statusMessage
        }
        errors
    }

    void updateProperties( Map properties, String baseName){
        def bundleLocation = "$externalDataLocation/${baseName}.properties"
        def bundle = new File(bundleLocation)
        def temp = new SortedProperties()
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

        def extensibleMessageSource = applicationContext.getBean("extensibleMessageSource")
        extensibleMessageSource.clearCache()
    }

}
