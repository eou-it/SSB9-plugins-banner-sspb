/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.converters.JSON
import net.hedtech.banner.tools.PBUtilServiceBase
import java.io.File

class AdminTaskService {
    //static transactional = false

    def pageUtilService
    def virtualDomainUtilService
    def cssUtilService

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
                    def name
                    def type
                    // Ducktype the submitted artifact
                    if (domain.css) {
                        type = 'css'
                        name = domain.constantName
                    } else if (domain.modelView) {
                        type = 'page'
                        name = domain.constantName
                    } else if (domain.codeGet) {
                        type = 'virtualDomain'
                        name = domain.serviceName
                    } else {
                        result = [error: message(code:"sspb.admintask.invalid.artifact.type", args:[content.artifact.fileName])]
                        throw new RuntimeException(message(code:"sspb.admintask.invalid.artifact.type", args:[content.artifact.fileName]))
                    }
                    if (type && name) {
                        def fileName = "${PBUtilServiceBase.pbConfig.locations[type]}/${name}.json"
                        def file = new File(fileName)
                        file.text = content.artifact.domain
                        result = [imported: 1, type: type, name: name, location: fileName]
                        println "Size: ${content.artifact.size} Index: ${content.artifact.index} Count: ${content.artifact.count}"
                    }
                }
            }
        }
        result
    }
}