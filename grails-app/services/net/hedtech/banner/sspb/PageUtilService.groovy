/*******************************************************************************
 Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.sspb

import grails.converters.JSON
import grails.util.Holders
import groovy.util.logging.Log4j
import net.hedtech.banner.security.PageSecurity
import net.hedtech.banner.security.PageSecurityId
import grails.web.context.ServletContextHolder
import org.grails.web.util.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import net.hedtech.banner.tools.i18n.SortedProperties

@Log4j
class PageUtilService extends net.hedtech.banner.tools.PBUtilServiceBase {
    def pageService
    def pageSecurityService
    def developerSecurityService

    def static final statusOk = 0
    def static final statusError = 1
    def static final statusDeferLoad = 2
    def static final actionImportInitally = 1
    def static final bundleLocation = getBundleLocation()
    def static final bundleName = "pageBuilder"

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
    void exportToFile(String pageName, String path=pbConfig.locations.page, Boolean skipDuplicates=false, Boolean isAllowExportDSPermission=false ) {
        Page.findAllByConstantNameLike(pageName).each { page ->
            if (skipDuplicates && page.constantName.endsWith(".bak")) {
                log.info message(code: "sspb.pageutil.export.skipDuplicate.message", args: [page.constantName])
            }
            else {
                def file = new File("$path/${page.constantName}.json")
                JSON.use("deep") {
                    def pageExport = new PageExport(page)
                    if (isAllowExportDSPermission) {
                        pageExport.owner = page.owner
                        PageSecurity.fetchAllByPageId(page.id)?.each { ps ->
                            pageExport.developerSecurity << [type: ps.type, name: ps.id.developerUserId, allowModify: ps.allowModifyInd]
                        }
                    }
                    def json = new JSON(pageExport)
                    def jsonString = json.toString(true)
                    log.info message(code:"sspb.pageutil.export.page.done.message", args:[page.constantName])
                    file.text = jsonString
                }
            }
        }
    }

    void exportToFile(Map content) {
        boolean isAllowExportDSPermission = content.isAllowExportDSPermission && "Y".equalsIgnoreCase(content.isAllowExportDSPermission)
        exportToFile(content.constantName,pbConfig.locations.page,false,isAllowExportDSPermission)
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
            def loadResult = load(pageName, stream, mode, false, true, true )
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
    int importAllFromDir(String path=pbConfig.locations.page, mode=loadIfNew, ArrayList names = null, boolean updateSecurity = false, boolean copyOwner = true, boolean copyDevSec = true) {
        importAllFromDir(path, mode, false, names, updateSecurity, copyOwner, copyDevSec)
    }

    int importAllFromDir(String path, mode, boolean deferred, ArrayList names, boolean updateSecurity, boolean copyOwner, boolean copyDevSec) {
        def count=0
        def needDeferred = false
        if (!deferred) {
            bootMsg "Importing updated or new pages from $path."
        }
        try {
            new File(path).eachFileMatch(jsonExt) { file ->
                if (!names || names.contains(file.name.take(file.name.lastIndexOf('.')))) {
                    def loadResult = load(file, mode, updateSecurity, copyOwner, copyDevSec)
                    needDeferred |= (loadResult.statusCode == statusDeferLoad)
                    finalizeFileImport(file, loadResult)
                    count += loadResult.loaded
                }
            }
        }
        catch (IOException e) {
            log.error "Unable to access import directory $path"
        }
        if (!deferred){
            if ( count > 0 && needDeferred) {
                // attempt import files that could not be loaded because of missing parent
                // which might have been imported if count > 0
                def i = importAllFromDir(path, mode, true, names, updateSecurity)
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
    private def load(String name, InputStream stream, int mode, boolean updateSecurity, boolean copyOwner, boolean copyDevSec) {
        load(name,stream, null, mode, updateSecurity, copyOwner, copyDevSec)
    }
    private def load(File file, int mode, boolean updateSecurity, copyOwner, copyDevSec) {
        load(null, null, file, mode, updateSecurity, copyOwner, copyDevSec)
    }
    private def load( String name, InputStream stream, File file, int mode, boolean updateSecurity, boolean copyOwner, boolean copyDevSec ) {
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
            if(!currentAction && json.constantName) {
                if (!developerSecurityService.isAllowImport(json.constantName, developerSecurityService.PAGE_IND)) {
                    result.statusCode = statusError
                    result.statusMessage = message(code: "sspb.renderer.page.deny.access", args: [json.constantName])
                    log.error "Insufficient privileges to import page - ${json.constantName}"
                    return result
                }
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

                //Copy owner and Dev Security
                if(copyOwner) {
                    page.owner = json.owner ?: null
                } else {
                    page.owner = PBUser.getTrimmed().oracleUserName
                }
                if(copyDevSec) {
                    json.developerSecurity = json.developerSecurity ?: null
                } else {
                    json.developerSecurity = null
                }

                //Check to see if parent page exists
                if (json.has('extendsPage') && json.extendsPage && json.extendsPage.constantName) {
                    page.extendsPage = pageService.get(json.extendsPage.constantName)
                    if ( page.extendsPage == null ) { //extendsPage does not (yet) exist
                        result.statusMessage = "Error, referenced page does not exist: " + json.extendsPage.constantName
                        result.statusCode = statusDeferLoad //Try in a deferred load
                        page.fileTimestamp = null //Set to null to allow deferred loading with loadIfNew

                    }
                }
                page=page.merge()
                if (result.statusCode == statusOk) {
                    result = pageService.compileAndSavePage(page.constantName, page.mergedModelText, page.extendsPage, page.owner)
                    result.loaded = result.page?1:0
                    if (page) {
                        if (result.loaded) {
                            associateRoles(page, json.pageRoles)
                            associateDeveloperSecurity(page, json.developerSecurity)
                            // Create the requestmap record to allow access]
                            if (updateSecurity) {
                                pageSecurityService.mergePage(result.page)
                            }
                        } else {
                            page.delete() //clean up if page did not compile
                        }
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
                def role = new PageRole(roleName: "ADMIN-GPBADMN")
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

    //Associate Developer security
    private def associateDeveloperSecurity(page, developerSecurity) {
        def pageDevEntries = PageSecurity.fetchAllByPageId(page.id)
        if(pageDevEntries) {
            pageDevEntries.each {PageSecurity psObj ->
                psObj.delete(flush:true)
            }
        }
        developerSecurity.each { securityEntry ->
            if ( securityEntry.name ) {
                try {
                    PageSecurity pageSecurityInstance = new PageSecurity()
                    PageSecurityId pageSecurityIdInstance = new PageSecurityId()
                    pageSecurityIdInstance.pageId = page.id
                    pageSecurityIdInstance.developerUserId = securityEntry.name
                    pageSecurityInstance.id = pageSecurityIdInstance
                    pageSecurityInstance.type = securityEntry.type
                    pageSecurityInstance.allowModifyInd = securityEntry.allowModify
                    pageSecurityInstance.userId = securityEntry.name
                    pageSecurityInstance.acitivityDate = new Date()
                    pageSecurityInstance.save(flush: true)
                } catch(e) {
                    log.error "Exception associating Developer security: ${e.message}"
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
            def result = pageService.compileAndSavePage(page.constantName, model, page.extendsPage, page.owner)
            if (result.statusCode>0)
                errors << result
            log.info result
        }
        errors
    }

    def compileMissingProperties() {
        /*def messageSource = ServletContextHolder.getServletContext()
                .getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT).getBean("messageSource")*/
        ApplicationContext context = (ApplicationContext) Holders.grailsApplication.getMainContext()
        context.getBean("messageSource")
        def externalMessageSource = context.messageSource?.externalMessageSource
        // Check if properties files exist, if not we will compile pages if non-baseline pages exist
        if (externalMessageSource.basenamesExposed.size()==0){
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
        if ( baseName.startsWith('pbadm') || baseName.startsWith(PageComponent.globalPropertiesName) || currentAction == actionImportInitally ) {
            def props = messageSource.getPropertiesByNormalizedName("plugins/banner-sspb/$baseName", new Locale("root"))
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
                messageSource?.externalMessageSource?.addBasename(baseName)
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
        messageSource.externalMessageSource?.clearCache()
    }

}