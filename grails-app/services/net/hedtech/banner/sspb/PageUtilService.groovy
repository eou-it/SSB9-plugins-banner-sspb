package net.hedtech.banner.sspb

import net.hedtech.banner.tools.i18n.PageMessageSource
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import net.hedtech.banner.tools.i18n.SortedProperties


class PageUtilService {

    def final static propertyDataDir = 'SSPB_DATA_DIR'
    def final static loadOverwriteExisting=0
    def final static loadSkipExisting=1
    def final static loadRenameIfExisting=2
    def final static loadIfNew=3

    def pageService

    def static externalDataLocation = getExternalDataLocation()

    def static getExternalDataLocation() {
        def result = System.getProperties().get(propertyDataDir)
        if (!result)
            result=System.getenv("TEMP")
        if (!result)
            result=System.getenv("TMP")
        result
    }

    void exportAllToFile(String path) {
        Page.findAll().each { page ->
            if (page.constantName.endsWith(".imp.dup"))
                println message(code:"sspb.pageutil.export.skipDuplicate.message", args:[page.constantName])
            else {
                def file = new File("$path/${page.constantName}.json")
                def jsonString =  page.modelView
                println message(code:"sspb.pageutil.export.page.done.message", args:[page.constantName])
                file.text = jsonString
            }
        }
    }


    void importInitially(mode=loadSkipExisting) {
        def fileNames = PageUtilService.class.classLoader.getResourceAsStream( "data/install/pages.txt" ).text
        fileNames.eachLine {  fileName ->
            def pageName = fileName.substring(0,fileName.lastIndexOf(".json"))
            def modelView=PageUtilService.class.classLoader.getResourceAsStream( "data/install/$fileName" ).text
            savePage(pageName, modelView, mode)
        }
    }

    void importAllFromFile(String path, mode=loadRenameIfExisting) {
        new File(path).eachFileMatch(~/.*.json/) {   file ->
            def pageName = file.name.substring(0,file.name.lastIndexOf(".json"))
            def modelView = file.getText()
            savePage(pageName, modelView, mode)
        }
    }

    void savePage (pageName, modelView, mode=loadRenameIfExisting) {
        def existingPage = Page.findByConstantName(pageName)
        def msg
        if (existingPage && mode==loadSkipExisting)
            return
        else if (existingPage && mode==loadRenameIfExisting) {
            pageName += ".imp.dup"
            def oldDup=Page.findByConstantName(pageName)
            if (oldDup) //if we have already saved a duplicate, get rid of it.
                oldDup.delete(flush: true)
            msg=message(code:"sspb.pageutil.import.duplicate.page.done.message", args:[pageName])
        }
        else if (existingPage && mode==loadOverwriteExisting) {
            existingPage.delete(flush: true)
            msg=message(code:"sspb.pageutil.import.page.done.message", args:[pageName])
        }

        def page = new Page(constantName: pageName, modelView: modelView)
        println msg
        if (page.constantName.startsWith("pbadm.")){
            //add a WTAILORADMIN role so the pages can be used
            def role=new PageRole(roleName: "WTAILORADMIN")
            page.addToPageRoles(role)
        }
        page = page.save(flush: true)
        compileAll(pageName)
    }

    def compileAll(String pattern) {
        def pat = pattern?pattern:"%"
        def pages = Page.findAllByConstantNameLike(pat)
        def errors =[]
        pages.each { page ->
            def result=pageService.compilePage( page.constantName, page.modelView)
            if (result.statusCode>0)
                errors << result
            println result
        }
        errors
    }

    void updateProperties( Map properties, String baseName){
        def bundleLocation = "$externalDataLocation/${baseName}.properties"
        def bundle = new File(bundleLocation)
        def temp = new SortedProperties()
        if (bundle.exists()) {
            new org.springframework.util.DefaultPropertiesPersister().load(temp, new InputStreamReader( new FileInputStream(bundle), "UTF-8"))
        }  else {
            // if a new file we need to add it to the base names
            ApplicationContext applicationContext = (ApplicationContext) ServletContextHolder.getServletContext()
                    .getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
            PageMessageSource pageMessageSource = applicationContext.getBean("pageMessageSource")
            pageMessageSource.addPageResource(baseName)
        }
        temp.putAll(properties)
        new org.springframework.util.DefaultPropertiesPersister().store( temp, new OutputStreamWriter( new FileOutputStream(bundle),"UTF-8" ), "")
    }

    def reloadBundles = {
        ApplicationContext applicationContext = (ApplicationContext) ServletContextHolder.getServletContext()
                                                .getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
        def messageSource = applicationContext.getBean("messageSource")
        messageSource.clearCache()

        def pageMessageSource = applicationContext.getBean("pageMessageSource")
        pageMessageSource.clearCache()
    }

}
