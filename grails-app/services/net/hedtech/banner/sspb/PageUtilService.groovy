/*******************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.sspb

import grails.converters.JSON
import groovy.util.logging.Log4j
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import net.hedtech.banner.tools.i18n.SortedProperties

@Log4j
class PageUtilService extends net.hedtech.banner.tools.PBUtilServiceBase {
    def pageService
    def static final bundleLocation = getBundleLocation()

    def static getBundleLocation() {
        if (bundleLocation) //only need to determine location once
            return bundleLocation

        pbConfig.locations.bundle?pbConfig.locations.bundle:System.getProperty("java.io.tmpdir")
    }

    //Used in integration test
    void exportAllToFile(String path) {
        exportToFile( "%", path, true)
    }

    //Export one or more pages to the configured directory
    void exportToFile(String pageName, String path=pbConfig.locations.page, Boolean skipDuplicates=false ) {

        Page.findAllByConstantNameLike(pageName).each { page ->
            if (skipDuplicates && page.constantName.endsWith(".bak"))
                log.info message(code:"sspb.pageutil.export.skipDuplicate.message", args:[page.constantName])
            else {
                def file = new File("$path/${page.constantName}.json")
                JSON.use("deep") {
                    def pageExport = new PageExport(page)
                    def json = new JSON(pageExport)
                    def jsonString = json.toString(true)
                    log.info message(code:"sspb.pageutil.export.page.done.message", args:[page.constantName])
                    file.text = jsonString
                }
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

    private def associateRoles(page, roles) {
        if (roles.equals(null)){  //have to use equals for JSONObject as it is not really null
            if (page.constantName.startsWith("pbadm.")){
                //add a WTAILORADMIN role so the pages can be used
                def role=new PageRole(roleName: "WTAILORADMIN")
                page.addToPageRoles(role)
            }
        } else {
            roles.each { newRole ->
                if ( newRole.roleName && !page.pageRoles.find{ it.roleName == newRole.roleName } ) {
                    try {
                        def role = new PageRole(newRole)
                        role.validate()
                        page.addToPageRoles(role)
                    } catch(e) {
                        log.error "Exception adding role: ${e.message}"
                    }
                }
            }
        }
    }

    //Load a page, save and compile it
    int load( name, stream, file, mode ) {
        // either name + stream is needed or file
        def pageName = name?name:file.name.substring(0,file.name.lastIndexOf(".json"))
        def page = pageService.get(pageName)
        def result=0
        def jsonString

        if (file)
            jsonString = loadFileMode (file, mode, page)
        else if (stream && name )
            jsonString = loadStreamMode(stream, mode, page)
        else {
            log.error "Error, either file or stream and name is required, both cannot be null"
            return 0
        }
        if (jsonString) {
            if ( !page ) {
                page=pageService.getNew(pageName)
            }

            def json
            JSON.use("deep") {
                json = JSON.parse(jsonString)
            }
            def doLoad = true
            // when loading from resources (stream), check the file time stamp in the Json
            if ( stream && mode==loadIfNew ) {
                def existingMaxTime = safeMaxTime(page?.fileTimestamp?.getTime(), page?.lastUpdated?.getTime())
                def newTime = json2date(json.fileTimestamp).getTime()
                if ( newTime && existingMaxTime && (existingMaxTime >= newTime) ) {
                    doLoad = false
                }
            }
            if (doLoad) {
                if (json.has('modelView')) {// file contains a marshaled page
                    page.modelView = json.modelView // instanceof String ? json.modelView : Page.modelToString(json.modelView)
                    //page.properties['modelView' /*, 'fileTimestamp'*/] = json
                } else { // file is a representation of the page modelView
                    page.modelView = jsonString
                }
                def compilationResult = pageService.compilePage(page)
                page = compilationResult.page
                if(page) {
                    page.fileTimestamp = json2date(json.fileTimestamp)
                    if (file)
                        page.fileTimestamp = new Date(file.lastModified())
                    if (json.has('extendsPage') && json.extendsPage) {//pointer to parent page
                        Page extendsPage = pageService.get(json.extendsPage.constantName)
                        if (!extendsPage) {  // page does not yet exist but may be imported after. Create dummy page for now
                            extendsPage = pageService.getNew(json.extendsPage.constantName)
                            extendsPage.modelView = "{}"  // define it with empty contents
                            extendsPage = saveObject(extendsPage)
                        }
                        if (extendsPage) {
                            page.extendsPage = extendsPage
                        } else {
                            log.error "Error, referenced page does not exist and cannot be created: " + json.extendsPage.constantName
                            return 0
                        }
                    }
                    associateRoles(page, json.pageRoles)
                    page = saveObject(page)
                }

                if (file) {
                    if (compilationResult.statusCode > 0) {
                        log.info compilationResult
                        def errorFile = new File(file.getCanonicalPath() + ".err")
                        errorFile.text = compilationResult
                    } else {
                        file.renameTo(file.getCanonicalPath() + '.' + nowAsIsoInFileName() + ".imp")
                    }
                }
                result++
            }
        }
        result
    }



    def compileAll(String pattern) {
        def pat = pattern?pattern:"%"
        def pages = Page.findAllByConstantNameLike(pat)
        def errors =[]
        pages.each { page ->
            def model=page.extendsPage?page.mergedModelText:page.modelView
            def result = pageService.compileAndSavePage(page.constantName, model, page.extendsPage)
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
            applicationContext.getBean("messageSource")?.pageMessageSource?.addPageResource(baseName)
        }
        temp.putAll(properties)
        new org.springframework.util.DefaultPropertiesPersister().store( temp, new OutputStreamWriter( new FileOutputStream(bundle),"UTF-8" ), "")
    }

    def reloadBundles = {
        ApplicationContext applicationContext = (ApplicationContext) ServletContextHolder.getServletContext()
                                                .getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
        def messageSource = applicationContext.getBean("messageSource")
        messageSource.clearCache()
        messageSource.pageMessageSource?.clearCache()
    }

}
