package net.hedtech.banner.sspb

import grails.converters.JSON
import net.hedtech.banner.tools.i18n.PageMessageSource
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

import org.springframework.context.ApplicationContext
import net.hedtech.banner.tools.i18n.SortedProperties


class PageUtilService extends net.hedtech.banner.tools.PBUtilServiceBase {
    def pageService // Injected by Spring
    def static bundleLocation = getBundleLocation()

    def static getBundleLocation() {
        if (bundleLocation) //only need to determine location once
            return bundleLocation

        def result = pbConfig.locations.bundle
        if (!result) result = System.getProperties().get(propertyDataDir)
        if (!result) result = System.getenv("temp")
        if (!result) result = System.getenv("tmp")
        result
    }

    //Used in integration test
    void exportAllToFile(String path) {
        exportToFile( "%", path, true)
    }

    //Export one or more pages to the configured directory
    void exportToFile(String pageName, String path=pbConfig.locations.page, Boolean skipDuplicates=false ) {

        Page.findAllByConstantNameLike(pageName).each { page ->
            if (skipDuplicates && page.constantName.endsWith(".bak"))
                println message(code:"sspb.pageutil.export.skipDuplicate.message", args:[page.constantName])
            else {
                def file = new File("$path/${page.constantName}.json")
                JSON.use("deep")
                def pageStripped = new Page()
                //nullify data that is derivable or not applicable in other environment
                pageStripped.properties[ 'constantName', 'modelView'] = page.properties
                page.pageRoles.each { role ->
                    pageStripped.addToPageRoles(new PageRole( roleName:role.roleName))
                }
                def json =  new JSON(pageStripped)
                def jsonString = json.toString(true)
                println message(code:"sspb.pageutil.export.page.done.message", args:[page.constantName])
                file.text = jsonString
            }
        }
    }

    static Date getTimestamp(String pageName, String path=pbConfig.locations.page ) {
        def file = new File( "$path/${pageName}.json")
        Date result
        if (file.exists())
            result =  new Date(file.lastModified() )
        result
     }


    //Load pages required for Page Builder administration
    //For now use text file with just the pageModel
    //TODO: refactor to use the same methods as for importing applications built with PB
    void importInitially(mode=loadSkipExisting) {
        def fileNames = PageUtilService.class.classLoader.getResourceAsStream( "data/install/pages.txt" ).text
        def count=0
        bootMsg "Checking/loading system required page builder pages."
        fileNames.eachLine {  fileName ->
            def pageName = fileName.substring(0,fileName.lastIndexOf(".json"))
            def modelView=PageUtilService.class.classLoader.getResourceAsStream( "data/install/$fileName" ).text
            count+=savePage(pageName, modelView, mode)
        }
        bootMsg "Finished checking/loading system required page builder pages. Pages loaded: $count"
        importAllNewFromDir()
    }
    //Original method to save a page
    int savePage (pageName, modelView, mode=loadRenameExisting) {
        def existingPage = Page.findByConstantName(pageName)
        def msg
        if (existingPage && mode==loadSkipExisting)
            return 0
        else if (existingPage && mode==loadRenameExisting) {
            existingPage.constantName += "."+nowAsIsoInFileName()+".bak"
            existingPage.save(flush: true)
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
        return 1
    }

    // Used in integration test - TODO: refactor to use importAllNewFromDir
    void importAllFromDir(String path, mode=loadRenameExisting) {
        new File(path).eachFileMatch(~/.*.json/) {   file ->
            def pageName = file.name.substring(0,file.name.lastIndexOf(".json"))
            def modelView = file.getText()
            savePage(pageName, modelView, mode)
        }
    }

    //Import/Install Utility
    void importAllNewFromDir(String path=pbConfig.locations.page, mode=loadIfNew) {
        bootMsg "Importing updated or new pages from $path."
        def count=0
        new File(path).eachFileMatch(~/.*.json/) {   file ->
            count+=savePageNew(file, mode)
        }
        bootMsg "Finished importing updated or new pages from $path. Pages loaded: $count"
    }

    int savePageNew(File file, mode) {
        def pageName = file.name.substring(0,file.name.lastIndexOf(".json"))
        def doLoad = true
        def page = Page.findByConstantName(pageName)
        def result=0
        switch ( mode )  {
            case loadIfNew:
                def ft = page?.fileTimestamp?.getTime()? page.fileTimestamp.getTime():0
                doLoad = (page == null) ||  (file.lastModified() > Math.max(ft,page.lastUpdated.getTime() )  )
                break
            case loadOverwriteExisting:
                break
            case loadRenameExisting:
                if (page) {
                    page.constantName += "."+nowAsIsoInFileName()+".bak"
                    page.save(flush: true)
                    page = null // create a new page
                }
                break
            case loadSkipExisting:
                doLoad = (page == null)
                break
        }
        if (doLoad) {
            if ( !page )  {
                page = new Page(constantName:pageName)
            }
            def jsonString = file.getText()
            JSON.use("deep")
            def json = JSON.parse(jsonString)
            page.modelView = json.modelView
            if (!json.pageRoles.equals(null)) {  //have to use equals for JSONObject as it is not really null
                json.pageRoles.each { newRole ->
                    if ( newRole.roleName && !page.pageRoles.find{ it.roleName == newRole.roleName } ) {
                        page.addToPageRoles(new PageRole(newRole))
                    }
                }
            }
            page.fileTimestamp=new Date(file.lastModified())
            if (!page.save(flush:true))
                page.errors.each {
                    println it
                }

            def compilationResult =  pageService.compilePage( page.constantName, page.modelView)
            if (compilationResult.statusCode>0) {
                println compilationResult
                def errorFile = new File(file.getCanonicalPath()+".err")
                errorFile.text = compilationResult
            }
            else {
                file.renameTo(file.getCanonicalPath()+'.'+nowAsIsoInFileName()+".imp"  )
            }
            result++
        }
        result
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

    // Handler for Page properties
    void updateProperties( Map properties, String baseName){
        def bundleLocation = "$bundleLocation/${baseName}.properties"
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
