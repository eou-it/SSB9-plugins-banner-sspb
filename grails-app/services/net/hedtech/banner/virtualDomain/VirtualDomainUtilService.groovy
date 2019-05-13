/*******************************************************************************
 Copyright 2017-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.converters.JSON
import groovy.util.logging.Log4j
import net.hedtech.banner.security.VirtualDomainSecurity
import net.hedtech.banner.security.VirtualDomainSecurityId
import net.hedtech.banner.sspb.PBUser

@Log4j
class VirtualDomainUtilService extends net.hedtech.banner.tools.PBUtilServiceBase {

    def static final actionImportInitally = 1
    def currentAction = null
    def developerSecurityService

    //Used in integration test
    void exportAllToFile(String path) {
        exportToFile( "%",null, path, true)
    }

    //Export one or more virtual domains to the configured directory
    void exportToFile(String serviceName, String pageLike=null, String path=pbConfig.locations.virtualDomain, Boolean skipDuplicates=false ) {
        def usedByPageLike
        if (pageLike) {
            def es = new VirtualDomainExportService()
            usedByPageLike = es.vdForPages(pageLike)
        }
        VirtualDomain.findAllByServiceNameLike(serviceName).each { vd ->
            if (usedByPageLike==null || usedByPageLike.contains(vd.serviceName)) {
                if (skipDuplicates && vd.serviceName.endsWith(".bak"))
                    log.info message(code:"sspb.virtualdomain.export.skipDuplicate.message", args:[vd.serviceName])
                else {
                    def file = new File("$path/${vd.serviceName}.json")
                    JSON.use("deep") {
                        def vdStripped = new VirtualDomain()
                        //nullify data that is derivable or not applicable in other environment
                        vdStripped.properties['serviceName', 'typeOfCode', 'codeGet', 'codePost', 'codePut', 'codeDelete'] = vd.properties
                        vdStripped.fileTimestamp = new Date()
                        vd.virtualDomainRoles.each { role ->
                            def r = new VirtualDomainRole()
                            r.properties['roleName', 'allowGet', 'allowPost', 'allowPut', 'allowDelete'] = role.properties
                            vdStripped.addToVirtualDomainRoles(r)
                        }
                        def json = new JSON(vdStripped)
                        def jsonString = json.toString(true)
                        log.info message(code:"sspb.virtualdomain.export.done.message", args:[vd.serviceName])
                        file.text = jsonString
                    }
                }
            }
        }
    }

    static Date getTimestamp(String vdName, String path=pbConfig.locations.virtualDomain ) {
        def file = new File( "$path/${vdName}.json")
        Date result
        if (file.exists())
            result =  new Date(file.lastModified() )
        result
    }

    //Load virtual domains required for Page Builder administration
    void importInitially(mode = loadSkipExisting) {
        currentAction = actionImportInitally
        def fileNames = VirtualDomainUtilService.class.classLoader.getResourceAsStream("data/install/virtualDomains.txt").text
        def count=0
        bootMsg "Checking/loading system required virtual domains."
        fileNames.eachLine { fileName ->
            def serviceName = fileName.substring(0, fileName.lastIndexOf(".json"))
            def stream = VirtualDomainUtilService.class.classLoader.getResourceAsStream("data/install/$fileName")
            count+=loadStream(serviceName, stream, mode, true, true)
        }
        bootMsg "Finished checking/loading system required virtual domains. Virtual domains loaded: $count"
    }


    //Import/Install Utility
    int importAllFromDir(String path=pbConfig.locations.virtualDomain, mode=loadIfNew, ArrayList names = null, copyOwner = true, copyDevSec = true) {
        bootMsg "Importing updated or new virtual domains from $path."
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
        bootMsg "Finished importing updated or new virtual domains from $path. Virtual domains loaded: $count"
        count
    }

    int loadStream(name, stream, mode, copyOwner, copyDevSec) {
        load(name, stream, null, mode, copyOwner, copyDevSec)
    }
    int loadFile(file, mode, copyOwner, copyDevSec) {
        load(null, null, file, mode, copyOwner, copyDevSec)
    }

    //Load a virtual domain and save it
    int load( name, stream, file, mode, copyOwner, copyDevSec) {
        // either name + stream is needed or file
        def vdName = name?name:file.name.substring(0,file.name.lastIndexOf(".json"))
        def vd = VirtualDomain.findByServiceName(vdName)
        def result=0
        def jsonString
        if (file)
            jsonString = loadFileMode (file, mode, vd)
        else if (stream && name )
            jsonString = loadStreamMode(stream, mode, vd)
        else {
            log.error "Error, either file or stream and name is required, both cannot be null"
            return 0
        }
        if (jsonString) {
            if (!vd) { vd = new VirtualDomain(serviceName: vdName) }
            def json
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
                def existingMaxTime = safeMaxTime(vd?.fileTimestamp?.getTime(), vd?.lastUpdated?.getTime())
                def newTime = json.fileTimestamp ? json2date(json.fileTimestamp).getTime() : (new Date()).getTime()
                if ( newTime && existingMaxTime && (existingMaxTime >= newTime) ) {
                    doLoad = false
                }
            }
            if (doLoad) {
                vd.properties['typeOfCode', 'codeGet', 'codePost', 'codePut', 'codeDelete'] = json
                if (!json.virtualDomainRoles.equals(null)) {
                    //have to use equals for JSONObject as it is not really null
                    json.virtualDomainRoles.each { newRole ->
                        if (newRole.roleName && !vd.virtualDomainRoles.find { it.roleName == newRole.roleName }) {
                            vd.addToVirtualDomainRoles(new VirtualDomainRole(newRole))
                        }
                    }
                }
                vd.fileTimestamp = json.fileTimestamp? json2date(json.fileTimestamp) : new Date()

                //Copy owner and Dev Security
                if(copyOwner) {
                    vd.owner = json.owner ?: null
                } else {
                    vd.owner = PBUser.userCache.oracleUserName
                }
                if(copyDevSec) {
                    json.developerSecurity = json.developerSecurity ?: null
                } else {
                    json.developerSecurity = null
                }

                if (file)
                    vd.fileTimestamp = new Date(file.lastModified())
                vd = saveObject(vd)
                associateDeveloperSecurity(vd, json.developerSecurity)
                if (file && !vd.hasErrors()) {
                    file.renameTo(file.getCanonicalPath() + '.' + nowAsIsoInFileName() + ".imp")
                }
                result++
            }
        }
        result
    }

    //Associate Developer security
    private def associateDeveloperSecurity(vd, developerSecurity) {
        def vdDevEntries = VirtualDomainSecurity.fetchAllByVirtualDomainId(vd.id)
        if(vdDevEntries) {
            vdDevEntries.each {VirtualDomainSecurity vdObj ->
                vdObj.delete(flush:true)
            }
        }
        developerSecurity.each { securityEntry ->
            if ( securityEntry.name ) {
                try {
                    VirtualDomainSecurity vdSecurityInstance = new VirtualDomainSecurity()
                    VirtualDomainSecurityId vdSecurityIdInstance = new VirtualDomainSecurityId()
                    vdSecurityIdInstance.virtualDomainId = vd.id
                    vdSecurityIdInstance.developerUserId = securityEntry.name
                    vdSecurityInstance.id = vdSecurityIdInstance
                    vdSecurityInstance.type = securityEntry.type
                    vdSecurityInstance.allowModifyInd = securityEntry.allowModify
                    vdSecurityInstance.userId = securityEntry.name
                    vdSecurityInstance.activityDate = new Date()
                    vdSecurityInstance.save(flush: true)
                } catch(e) {
                    log.error "Exception associating Developer security: ${e.message}"
                }
            }
        }

    }
}