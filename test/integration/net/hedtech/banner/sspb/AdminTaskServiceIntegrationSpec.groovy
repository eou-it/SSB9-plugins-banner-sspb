package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec
import net.hedtech.banner.security.DeveloperSecurityService

class AdminTaskServiceIntegrationSpec extends IntegrationSpec {

    def adminTaskService
    def pbConfig = grails.util.Holders.getConfig().pageBuilder


    def artifactFiles = [
              "test/testData/model/PageModel1.json"
             ,"test/testData/model/PageModel2.json"
             ,"test/testData/virtualDomain/stvnation.json"
             ,"test/testData/css/testCss.json"
            ]


    def artifacts = []

    def setup() {
        adminTaskService.developerSecurityService =  Stub(DeveloperSecurityService) {
            getImportConfigValue() >> false
            isAllowImport(_,_) >> true
        }
        adminTaskService.pageUtilService.developerSecurityService = adminTaskService.developerSecurityService
        adminTaskService.cssUtilService.developerSecurityService = adminTaskService.developerSecurityService
        adminTaskService.virtualDomainUtilService.developerSecurityService = adminTaskService.developerSecurityService
        if(!pbConfig.locations.css){
            pbConfig.locations.css = 'target/testData/css'
        }
        if(!pbConfig.locations.page){
            pbConfig.locations.page = 'target/testData/model'
        }
        if(!pbConfig.locations.virtualDomain){
            pbConfig.locations.virtualDomain = 'target/testData/virtualDomain'
        }
        Map content
        artifactFiles.eachWithIndex { fname, idx ->
            content = [
                task: 'import',
                artifact: [
                    count:artifactFiles.size(),
                    index: idx,
                    domain: (new File (fname) ).text
                ]
            ]
            artifacts << content
        }
    }

    def cleanup() {
    }

    void "test Import artifacts from client"() {
        given:
        def results = []
        artifacts.each {
            results << adminTaskService.create(it, null)
        }
        expect:
        results.size() == artifacts.size()
        results[results.size()-1].digested == artifacts.size()
    }
}
