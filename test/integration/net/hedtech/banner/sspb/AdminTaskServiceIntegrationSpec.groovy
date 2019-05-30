package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec

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
        if(!pbConfig.locations.css){
            pbConfig.locations.css = 'target'
        }
        if(!pbConfig.locations.page){
            pbConfig.locations.page = 'target'
        }
        if(!pbConfig.locations.virtualDomain){
            pbConfig.locations.virtualDomain = 'target'
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
