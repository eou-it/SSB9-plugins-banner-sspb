/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.css

import grails.converters.JSON
import groovy.util.logging.Log4j
import net.hedtech.banner.security.CssSecurity
import net.hedtech.banner.security.CssSecurityId
import net.hedtech.banner.sspb.PBUser
import net.hedtech.banner.tools.PBUtilServiceBase
@Log4j
class CssUtilService extends PBUtilServiceBase {

    def static final actionImportInitally = 1
    def currentAction = null
    def cssService
    def developerSecurityService

    static Date getTimestamp(String oName, String path=PBUtilServiceBase.pbConfig.locations.css ) {
        def file = new File( "$path/${oName}.json")
        Date result
        if (file.exists())
            result =  new Date(file.lastModified() )
        result
    }

    //Export one or more virtual domains to the configured directory
    void exportToFile(String constantName, String pageLike=null, String path=PBUtilServiceBase.pbConfig.locations.css, Boolean skipDuplicates=false ) {
        def usedByPageLike
        if (pageLike) {
            def es = new CssExportService()
            usedByPageLike = es.cssForPages(pageLike)
        }
        Css.fetchAllByConstantNameLike(constantName).each { css ->
            if (usedByPageLike==null || usedByPageLike.contains(css.constantName)) {
                if (skipDuplicates && css.constantName.endsWith(".bak"))
                    log.info message(code:"sspb.css.export.skipDuplicate.message", args:[css.constantName])
                else {
                    def file = new File("$path/${css.constantName}.json")
                    JSON.use("deep") {
                        def cssStripped = new Css()
                        //nullify data that is derivable or not applicable in other environment
                        //cssStripped.properties['constantName', 'css', 'description'] = css.properties
                        cssStripped.constantName = css.constantName
                        cssStripped.css = css.css
                        cssStripped.description = css.description
                        cssStripped.fileTimestamp = new Date()
                        def json = new JSON(cssStripped)
                        def jsonString = json.toString(true)
                        log.info message(code: "sspb.css.export.done.message", args: [css.constantName])
                        file.text = jsonString
                    }
                }
            }
        }
    }

    void importInitially(mode = PBUtilServiceBase.loadSkipExisting) {
        currentAction = actionImportInitally
        def fileNames = CssUtilService.class.classLoader.getResourceAsStream("data/install/csss.txt").text
        def count=0
        bootMsg "Checking/loading system required css files."
        fileNames.eachLine { fileName ->
            def constantName = fileName.substring(0, fileName.lastIndexOf(".json"))
            def stream = CssUtilService.class.classLoader.getResourceAsStream("data/install/$fileName")
            count+=loadStream(constantName, stream, mode, true, true)
        }
        bootMsg "Finished checking/loading system required css files. Css files loaded: $count"
    }

    //Import/Install Utility
    int importAllFromDir(String path=PBUtilServiceBase.pbConfig.locations.css, mode=PBUtilServiceBase.loadIfNew, ArrayList names = null, boolean copyOwner = true, boolean copyDevSec = true) {
        bootMsg "Importing updated or new css files from $path."
        def count=0
        try {
            new File(path).eachFileMatch(jsonExt) { file ->
                if (!names || names.contains(file.name.take(file.name.lastIndexOf('.')))) {
                    count += loadFile(file, mode, copyOwner, copyDevSec)
                }
            }
        }
        catch (IOException e) {
            log.error "Unable to access import directory $path"
        }
        bootMsg "Finished importing updated or new css files from $path. Css files loaded: $count"
        count
    }

    int loadStream(name, stream, mode, copyOwner, copyDevSec) {
        load(name, stream, null, mode, copyOwner, copyDevSec)
    }
    int loadFile(file, mode, copyOwner, copyDevSec) {
        load(null, null, file, mode, copyOwner, copyDevSec)
    }

    //Load a css and save it
    int load( name, stream, file, mode, copyOwner, copyDevSec ) {
        def cssName = name?name:file.name.substring(0,file.name.lastIndexOf(".json"))
        def css = Css.fetchByConstantName(cssName)
        def result=0
        def jsonString
        if (file)
            jsonString = loadFileMode (file, mode, css)
        else if (stream && name )
            jsonString = loadStreamMode(stream, mode, css)
        else {
            log.error "Error, either file or stream and name is required, both cannot be null"
            return 0
        }
        if (jsonString) {
            def json
            if (!css) { css = new Css(constantName: cssName) }
            JSON.use("deep") {
                json = JSON.parse(jsonString)
            }
            if(json.serviceName && !developerSecurityService.isAllowImport(json.serviceName, developerSecurityService.PAGE_IND) && !currentAction) {
                log.error "Insufficient privileges to import"
                return result
            }
            def doLoad = true
            // when loading from resources (stream), check the file time stamp in the Json
            if ( stream && mode==loadIfNew ) {
                def existingMaxTime = safeMaxTime(css?.fileTimestamp?.getTime(), css?.lastUpdated?.getTime())
                def newTime = json.fileTimestamp ? json2date(json.fileTimestamp).getTime() : (new Date()).getTime()
                if ( newTime && existingMaxTime && (existingMaxTime >= newTime) ) {
                    doLoad = false
                }
            }
            if (doLoad) {
                css.css = json.css
                css.description = json.description
                //Copy owner and Dev Security
                if(copyOwner) {
                    css.owner = json.owner ?: null
                } else {
                    css.owner = PBUser.userCache.oracleUserName
                }
                if(copyDevSec) {
                    json.developerSecurity = json.developerSecurity ?: null
                } else {
                    json.developerSecurity = null
                }
                css.fileTimestamp = json2date(json.fileTimestamp)
                if (file)
                    css.fileTimestamp = new Date(file.lastModified())
                css = cssService.create(css)
                associateDeveloperSecurity(css, json.developerSecurity)
                if (file && css && !css.hasErrors()) {
                    file.renameTo(file.getCanonicalPath() + '.' + nowAsIsoInFileName() + ".imp")
                }
                log.info "Created/Updated Css $cssName"
                result++
            }
        }
        result
    }

    //Associate Developer security
    private def associateDeveloperSecurity(css, developerSecurity) {
        def cssDevEntries = CssSecurity.fetchAllByCssId(css.id)
        if(cssDevEntries) {
            cssDevEntries.each {CssSecurity cssObj ->
                cssObj.delete(flush:true)
            }
        }
        developerSecurity.each { securityEntry ->
            if ( securityEntry.name ) {
                try {
                    CssSecurity cssSecurityInstance = new CssSecurity()
                    CssSecurityId cssSecurityIdInstance = new CssSecurityId()
                    cssSecurityIdInstance.cssId = css.id
                    cssSecurityIdInstance.developerUserId = securityEntry.name
                    cssSecurityInstance.id = cssSecurityIdInstance
                    cssSecurityInstance.type = securityEntry.type
                    cssSecurityInstance.allowModifyInd = securityEntry.allowModify
                    cssSecurityInstance.userId = securityEntry.name
                    cssSecurityInstance.activityDate = new Date()
                    cssSecurityInstance.save(flush: true)
                } catch(e) {
                    log.error "Exception associating Developer security: ${e.message}"
                }
            }
        }

    }
}
