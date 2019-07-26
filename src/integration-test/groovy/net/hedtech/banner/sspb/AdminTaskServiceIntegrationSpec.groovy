package net.hedtech.banner.sspb

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import net.hedtech.banner.security.DeveloperSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
@Rollback
class AdminTaskServiceIntegrationSpec extends Specification {

    @Autowired
    AdminTaskService adminTaskService
    @Autowired
    WebApplicationContext ctx

    //def adminTaskService
    def pbConfig
    def grailsApplication


    def artifactFiles = [
              "/src/test/testData/model/PageModel1.json"
             ,"test/testData/model/PageModel2.json"
             ,"test/testData/virtualDomain/stvnation.json"
             ,"test/testData/css/testCss.json"
            ]


    def artifacts = []

    def setup() {
        pbConfig = grailsApplication.config.pageBuilder
        GrailsWebMockUtil.bindMockWebRequest(ctx)
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
        RequestContextHolder.resetRequestAttributes()
    }

    void "test Import artifacts from client"() {
        given:
        def results = []
        artifacts.each {
            results << adminTaskService.create(it, null)
        }
        expect:
        results.size() == artifacts.size()
       // results[results.size()-1].digested == artifacts.size()
    }
}
