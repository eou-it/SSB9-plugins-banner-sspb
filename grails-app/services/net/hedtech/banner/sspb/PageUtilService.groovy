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
                //nullify data that is not applicable in other environment
                pageStripped.properties[ 'constantName', 'modelView', 'fileTimestamp'] = page.properties
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
     void importInitially(mode=loadSkipExisting) {
        def fileNames = PageUtilService.class.classLoader.getResourceAsStream( "data/install/pages.txt" ).text
        def count=0
        bootMsg "Checking/loading system required page builder pages."
        fileNames.eachLine {  fileName ->
            def pageName = fileName.substring(0,fileName.lastIndexOf(".json"))
            def stream = PageUtilService.class.classLoader.getResourceAsStream( "data/install/$fileName" )
            count+=loadStream(pageName, stream, mode )
        }
        bootMsg "Finished checking/loading system required page builder pages. Pages loaded: $count"
    }

    //Import/Install Utility
    void importAllFromDir(String path=pbConfig.locations.page, mode=loadIfNew) {
        bootMsg "Importing updated or new pages from $path."
        def count=0
        new File(path).eachFileMatch(~/.*.json/) {   file ->
            count+=loadFile( file, mode)
        }
        bootMsg "Finished importing updated or new pages from $path. Pages loaded: $count"
    }

    int loadStream(name, stream, mode) {
        load(name, stream, null, mode)
    }
    int loadFile(file, mode) {
        load(null, null, file, mode)
    }

    //Load a page, save and compile it
    int load( name, stream, file, mode ) {
        // either name + stream is needed or file
        def pageName = name?name:file.name.substring(0,file.name.lastIndexOf(".json"))
        def page = Page.findByConstantName(pageName)
        def result=0
        def jsonString
        if (file)
            jsonString = loadFileMode (file, mode, page)
        else if (stream && name )
            jsonString = loadStreamMode(stream, mode, page)
        else {
            println "Error, either file or stream and name is required, both cannot be null"
            return 0
        }
        if (jsonString) {
            if ( !page ) { page = new Page(constantName:pageName) }
            JSON.use("deep")
            def json = JSON.parse(jsonString)
            if (json.has('modelView')) // file contains a marshaled page
                page.properties[ 'modelView' /*, 'fileTimestamp'*/] = json
            else // file is a representation of the page modelView
                page.modelView=jsonString
            if (!json.pageRoles.equals(null)){  //have to use equals for JSONObject as it is not really null
                json.pageRoles.each { newRole ->
                    if ( newRole.roleName && !page.pageRoles.find{ it.roleName == newRole.roleName } ) {
                        page.addToPageRoles(new PageRole(newRole))
                    }
                }
            } else {
                if (page.constantName.startsWith("pbadm.")){
                    //add a WTAILORADMIN role so the pages can be used
                    def role=new PageRole(roleName: "WTAILORADMIN")
                    page.addToPageRoles(role)
                }
            }
            page.fileTimestamp=json2date(json.fileTimestamp)
            if (file)
                page.fileTimestamp=new Date(file.lastModified())

            saveObject(page)
            def compilationResult =  pageService.compilePage( page.constantName, page.modelView)
            if (file) {
                if (compilationResult.statusCode>0) {
                    log.info compilationResult
                    def errorFile = new File(file.getCanonicalPath()+".err")
                    errorFile.text = compilationResult
                }
                else {
                    file.renameTo(file.getCanonicalPath()+'.'+nowAsIsoInFileName()+".imp"  )
                }
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
            log.info result
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
