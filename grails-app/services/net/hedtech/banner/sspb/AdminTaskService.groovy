/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.converters.JSON
import net.hedtech.banner.tools.PBUtilServiceBase

class AdminTaskService {
    //static transactional = false

    def pageUtilService
    def virtualDomainUtilService
    def cssUtilService

    def nameLists = [:]
    def listCount = 0
    def listsStartedMilis = 0    // beginning of time
    def listTimeout = 300 * 1000 // milis

    def create(Map content, ignore) {
        def result = [:]
        if (content.task == 'import') {
            if (content.pages) {
                def count = pageUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.page, PBUtilServiceBase.loadOverwriteExisting)
                result << [importedPagesCount: count]
            }
            if (content.virtualDomains) {
                def count = virtualDomainUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.virtualDomain, PBUtilServiceBase.loadOverwriteExisting)
                result << [importedVirtualDomainsCount: count]
            }
            if (content.css) {
                def count = cssUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.css, PBUtilServiceBase.loadOverwriteExisting)
                result << [importedCssCount: count]
            }
            if (content.artifact) {
                if (content.artifact.domain instanceof String) {
                    def domain = JSON.parse(content.artifact.domain)
                    def at = determineArtifact(domain)
                    if ( at.valid ) {
                        def fileName = "${PBUtilServiceBase.pbConfig.locations[at.type]}/${at.name}.json"
                        def file = new File(fileName)
                        file.text = content.artifact.domain
                        result = [imported: 1, type: at.type, name: at.name, location: fileName]
                        log.info "Uploaded artifact. File name: ${content.artifact.fileName} Size: ${content.artifact.size} Index: ${content.artifact.index} Count: ${content.artifact.count}"
                        result.digested = pushArtifactForImport(at.type, at.name, content)
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
    private def pushArtifactForImport(type, name, content) {
        def importCount = 0
        if ( (new Date()).getTime() - listsStartedMilis > listTimeout) {
            nameLists = [:] // Reset the namesAssume data are from a failed previous upload
            listCount = 0
        }
        if (listCount == 0 ) {
            listsStartedMilis = (new Date()).getTime()
        }
        if (nameLists[type]) {
            nameLists[type]<<name
        } else {
            nameLists[type]= [name]
        }
        listCount++
        if (listCount >= content.artifact.count ) {
            def mode = PBUtilServiceBase.loadOverwriteExisting
            // copied all artifacts
            if (nameLists.css?.size() > 0){
                importCount += cssUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.css, mode, nameLists.css)
            }
            if (nameLists.page?.size() > 0) {
                importCount += pageUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.page, mode, nameLists.page)
            }
            if (nameLists.virtualDomain?.size() > 0) {
                importCount += virtualDomainUtilService.importAllFromDir(PBUtilServiceBase.pbConfig.locations.virtualDomain, mode, nameLists.virtualDomain)
            }
            listCount = 0
            listsStartedMilis = 0
        }
        importCount
    }
}