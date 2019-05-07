/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.converters.JSON
import net.hedtech.banner.tools.PBUtilServiceBase

class AdminTaskService {

    def pageUtilService
    def virtualDomainUtilService
    def cssUtilService
    def developerSecurityService

    def nameLists = [:]
    def listCount = 0
    def listsStartedMilis = 0    // beginning of time
    def listTimeout = 300 * 1000 // milis

    def create(Map content, ignore) {
        def result = [:]
        if (content.task == 'import') {
            def copyOwner = content.copyOwner ?: false
            def copyDevSec =  content.copyDevSec ?: false
            if (content.virtualDomains) {
                def count = virtualDomainUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.virtualDomain,
                        PBUtilServiceBase.loadOverwriteExisting, null, copyOwner, copyDevSec)
                result << [importedVirtualDomainsCount: count]
            }
            if (content.css) {
                def count = cssUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.css,
                        PBUtilServiceBase.loadOverwriteExisting, null, copyOwner, copyDevSec)
                result << [importedCssCount: count]
            }
            if (content.pages) {
                def count = pageUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.page, PBUtilServiceBase.loadOverwriteExisting,
                        false, null, true, copyOwner, copyDevSec)
                result << [importedPagesCount: count]
            }
            if (content.artifact) {
                if (content.artifact.domain instanceof String) {
                    def domain = JSON.parse(content.artifact.domain)
                    def at = determineArtifact(domain)
                    if ( at.valid ) {
                        if(!developerSecurityService.isAllowImport(domain.constantName, developerSecurityService.PAGE_IND)) {
                            result << [accessError: message(code: "sspb.renderer.page.deny.access", args: [domain.constantName])]
                            return result
                        }
                        def fileName = "${PBUtilServiceBase.pbConfig.locations[at.type]}/${at.name}.json"
                        def file = new File(fileName)
                        file.text = content.artifact.domain
                        result = [imported: 1, type: at.type, name: at.name, location: fileName]
                        log.info "Uploaded artifact. File name: ${content.artifact.fileName} Size: ${content.artifact.size} Index: ${content.artifact.index} Count: ${content.artifact.count}"
                        result.digested = pushArtifactForImport(at.type, at.name, content, copyOwner, copyDevSec)
                    } else {
                        throw new RuntimeException(message(code:"sspb.admintask.invalid.artifact.type", args:[content.artifact.fileName]))
                    }
                }
            }
        }
        result
    }

    private def determineArtifact(domain) {
        def result = [valid:true]
        // Ducktype the submitted artifact
        if (domain.css) {
            result.type = 'css'
            result.name = domain.constantName
        } else if (domain.modelView) {
            result.type = 'page'
            result.name = domain.constantName
        } else if (domain.codeGet) {
            result.type = 'virtualDomain'
            result.name = domain.serviceName
        } else {
            result.valid = false
        }
        result
    }

    // This method checks if all artifacts have been submitted. If so, the import is started.
    private def pushArtifactForImport(type, name, content, copyOwner, copyDevSec) {
        def importCount = 0
        if ( listCount ==0 || ( (new Date()).getTime() - listsStartedMilis > listTimeout) ) {
            nameLists = [:] // Reset the namesAssume data are from a failed previous upload
            listCount = 0
            listsStartedMilis = (new Date()).getTime()
        }
        if (nameLists[type]) {
            nameLists[type]<<name
        } else {
            nameLists[type]= [name]
        }
        listCount++
        log.info "* $listCount of ${content.artifact.count}"
        if (listCount >= content.artifact.count ) {
            log.info "* All artifacts are uploaded, start importing"
            listCount = 0 //Clear to start a new set of artifacts
            def mode = PBUtilServiceBase.loadOverwriteExisting
            // copied all artifacts
            if (nameLists.css?.size() > 0){
                importCount += cssUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.css, mode,
                        nameLists.css, copyOwner, copyDevSec)
            }
            if (nameLists.page?.size() > 0) {
                importCount += pageUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.page, mode,
                        nameLists.page, true, copyOwner, copyDevSec)
            }
            if (nameLists.virtualDomain?.size() > 0) {
                importCount += virtualDomainUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.virtualDomain,
                        mode, nameLists.virtualDomain, copyOwner, copyDevSec)
            }
        }
        importCount
    }
}