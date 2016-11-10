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
import net.hedtech.banner.tools.i18n.PageMessageSource

@Log4j
class PageUtilService extends net.hedtech.banner.tools.PBUtilServiceBase {
    def pageService

    def static final statusOk = 0
    def static final statusError = 1
    def static final statusDeferLoad = 2
    def static final actionImportInitally = 1
    def static final bundleLocation = bundleLocation?:getBundleLocation()

    def currentAction = null

    def static getBundleLocation() {
        if (bundleLocation) {//only need to determine location once
            return bundleLocation
        }
        if (!locationsValidated) {
            //Should not be needed  - is already in base class
            locationsValidated = locationsValidated?:configLocations()
        }
        pbConfig.locations.bundle?pbConfig.locations.bundle:System.getProperty("java.io.tmpdir")
    }



    //Export one or more pages to the configured directory
    void exportToFile(String pageName, String path=pbConfig.locations.page, Boolean skipDuplicates=false ) {
        Page.findAllByConstantNameLike(pageName).each { page ->
            if (skipDuplicates && page.constantName.endsWith(".bak")) {
                log.info message(code: "sspb.pageutil.export.skipDuplicate.message", args: [page.constantName])
            }
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

    //Load pages required for Page Builder administration
    int importInitially(mode=loadIfNew, deferred = false) {
        currentAction = actionImportInitally
        def fileNames = PageUtilService.class.classLoader.getResourceAsStream( "data/install/pages.txt" ).text
        def count=0
        def needDeferred = false
        if (!deferred) {
            bootMsg "Checking/loading system required page builder pages."
        }
        fileNames.eachLine {  fileName ->
            def pageName = fileName.substring(0,fileName.lastIndexOf(".json"))
            InputStream stream = PageUtilService.class.classLoader.getResourceAsStream( "data/install/$fileName" )
            def loadResult = load(pageName, stream, mode )
            needDeferred |= (loadResult.statusCode == statusDeferLoad)
            count += loadResult.loaded

        }
        if (!deferred){
            if ( count > 0 && needDeferred) {
                // attempt import files that could not be loaded because of missing parent
                // which might have been imported if count > 0
                count += importInitially( mode, true)
            }
            bootMsg "Finished checking/loading system required page builder pages. Pages loaded: $count"
        }
        currentAction = null
        count
    }

    //Import/Install Utility
    int importAllFromDir(String path=pbConfig.locations.page, mode=loadIfNew, deferred = false) {
        def count=0
        def needDeferred = false
        if (!deferred) {
            bootMsg "Importing updated or new pages from $path."
        }
        try {
            new File(path).eachFileMatch(~/.*.json/) { file ->
                def loadResult = load(file, mode)
                needDeferred |= (loadResult.statusCode == statusDeferLoad)
                finalizeFileImport(file, loadResult)
                count += loadResult.loaded
            }
        }
        catch (IOException e) {
            log.error "Unable to access import directory $path"
        }
        if (!deferred){
            if ( count > 0 && needDeferred) {
                // attempt import files that could not be loaded because of missing parent
                // which might have been imported if count > 0
                def i = importAllFromDir(path, mode, true)
                bootMsg "Pages loaded deferred: $i"
                count += i
            }
            bootMsg "Finished importing updated or new pages from $path. Pages loaded: $count"
        }
        count
    }

    //Helper method for Import/Install Utility
    private def finalizeFileImport(file, statusRecord){
        if (statusRecord.statusCode > statusOk ){
            log.error statusRecord
            def errorFile = new File(file.getCanonicalPath() + ".err")
            errorFile.text = statusRecord
        } else {
            file.renameTo(file.getCanonicalPath() + '.' + nowAsIsoInFileName() + ".imp")
            new File(file.getCanonicalPath() + ".err")?.delete()
        }
    }

    private def isDeferredLoad(page, File file) {
        def result = page && page.extendsPage == null && page.fileTimestamp == null && page.compiledView == null
        //If fileStamp is null this is probably a page needing deferred loading because extension did not exist
        if (file) {
            //To be sure check if .err file exists
            result = result && new File(file.getCanonicalPath() + ".err").exists()
        }
        result
    }

    //Load a page, save and compile it
    private def load(String name, InputStream stream, int mode) {
        load(name,stream, null, mode)
    }
    private def load(File file, int mode) {
        load(null, null, file, mode)
    }
    private def load( String name, InputStream stream, File file, int mode ) {
        // either name + stream is needed or file
        def pageName = name?name:file.name.substring(0,file.name.lastIndexOf(".json"))
        def page = pageService.get(pageName)
        def result = [page: null, statusCode: statusOk, statusMessage: "", loaded: 0]
        def jsonString
        def doLoad = true
        if (isDeferredLoad(page, file)) {
            mode = loadOverwriteExisting
        }
        if (file) {
            jsonString = loadFileMode(file, mode, page)
        }
        else if (stream && name ) {
            jsonString = loadStreamMode(stream, mode, page)
        }
        else {
            result.statusCode = statusError
            result.statusMessage = "Error, either file or stream and name is required, both cannot be null"
            log.error result.statusMessage
        }
        if (jsonString) {
            def json
            JSON.use("deep") {
                json = JSON.parse(jsonString)
            }
            page = page ?: pageService.getNew(pageName)
            // when loading from resources (stream), check the file time stamp in the Json
            if ( stream && mode==loadIfNew ) {
                def existingMaxTime = safeMaxTime(page?.fileTimestamp?.getTime(), page?.lastUpdated?.getTime())
                def newTime = json2date(json.fileTimestamp).getTime()
                if ( newTime && existingMaxTime && (existingMaxTime >= newTime) ) {
                    doLoad = false
                }
            }
            if (doLoad) {
                // if the json has a modelView the json is a marshaled page, otherwise it is just the modelView
                page.modelView = json.has('modelView') ? json.modelView: jsonString
                page.fileTimestamp = file?new Date(file.lastModified()):json2date(json.fileTimestamp)
                //Check to see if parent page exists
                if (json.has('extendsPage') && json.extendsPage && json.extendsPage.constantName) {
                    page.extendsPage = pageService.get(json.extendsPage.constantName)
                    if ( page.extendsPage == null ) { //extendsPage does not (yet) exist
                        result.statusMessage = "Error, referenced page does not exist: " + json.extendsPage.constantName
                        result.statusCode = statusDeferLoad //Try in a deferred load
                        page.fileTimestamp = null //Set to null to allow deferred loading with loadIfNew

                    }
                }
                associateRoles(page, json.pageRoles)
                page=page.merge()
                if (result.statusCode == statusOk) {
                    result = pageService.compileAndSavePage(page.constantName, page.mergedModelText, page.extendsPage)
                    result.loaded = result.page?1:0
                    if (page && !result.loaded) { //clean up if page did not compile
                        page.delete()
                    }
                }
            }
        }
        result
    }

    //Helper method for load
    private def associateRoles(page, roles) {
        if (roles.equals(null)){  //have to use equals for JSONObject as it is not really null
            if (page.constantName.startsWith("pbadm.")){
                //add a WTAILORADMIN role so the pages can be used
                def role = new PageRole(roleName: "WTAILORADMIN")
                role.page=page
                page.addToPageRoles(role)
            }
        } else {
            roles.each { newRole ->
                if ( newRole.roleName && !page.pageRoles.find{ it.roleName == newRole.roleName } ) {
                    try {
                        def role = new PageRole(newRole)
                        role.page=page
                        role.validate()
                        page.addToPageRoles(role)
                    } catch(e) {
                        log.error "Exception adding role: ${e.message}"
                    }
                }
            }
        }
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

    def compileMissingProperties() {
        def messageSource = ServletContextHolder.getServletContext()
                .getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT).getBean("messageSource")
        def pageMessageSource = messageSource.pageMessageSource
        // Check if properties files exist, if not we will compile pages if non-baseline pages exist
        if (pageMessageSource.pageResources.size()==0){ // 1 file exists: pageGlobal
            log.debug "No Page Resources found"
            // Check if custom pages exist
            def totalPages = Page.count()
            def pbPages = Page.countByConstantNameLike('pbadm%')
            if (totalPages>pbPages) {
                log.info "Total #pages: $totalPages > # pbadm pages: $pbPages - Recompiling pages to create missing resources."
                compileAll()
                log.info "Pages recompiled: $totalPages"
            }
        }
    }

    // Handler for Page properties
    void updateProperties( Map properties, String baseName){
        ApplicationContext applicationContext = (ApplicationContext) ServletContextHolder.getServletContext()
                .getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
        def messageSource = applicationContext.getBean("messageSource")
        def isBaseline = false
        if ( baseName.startsWith('pbadm') || baseName.startsWith(PageMessageSource.globalPropertiesName) || currentAction == actionImportInitally ) {
            def props = messageSource.getRootProperties(baseName)
            if (props) {
                isBaseline = true // assume ok
                //Check if all key/value pairs have a match in the properties
                properties.keySet().each {
                    isBaseline &= properties.get(it).equals(props.get(it))
                }
                if (!isBaseline){
                    log.info "Creating external properties file $baseName for modified PageBuilder admin page."
                }
            }
        }
        if (!isBaseline) {
            def bundleLocation = "$bundleLocation/${baseName}.properties"
            def bundle = new File(bundleLocation)
            def temp = new SortedProperties()
            if (bundle.exists()) {
                new org.springframework.util.DefaultPropertiesPersister().load(temp, new InputStreamReader(new FileInputStream(bundle), "UTF-8"))
            } else {
                // if a new file we need to add it to the base names
                messageSource?.pageMessageSource?.addPageResource(baseName)
            }
            temp.putAll(properties)
            new org.springframework.util.DefaultPropertiesPersister().store(temp, new OutputStreamWriter(new FileOutputStream(bundle), "UTF-8"), "")
        }
    }

    def reloadBundles = {
        ApplicationContext applicationContext = (ApplicationContext) ServletContextHolder.getServletContext()
                                                .getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
        def messageSource = applicationContext.getBean("messageSource")
        messageSource.clearCache()
        messageSource.pageMessageSource?.clearCache()
    }

}
