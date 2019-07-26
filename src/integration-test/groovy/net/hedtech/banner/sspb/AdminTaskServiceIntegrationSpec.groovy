/*******************************************************************************
 * Copyright 2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
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
              "/testData/model/PageModel1.json"
             ,"/testData/model/PageModel2.json"
             ,"/testData/virtualDomain/stvnation.json"
         //    ,"/testData/css/testCss.json"
            ]


    def artifacts = []

    def setup() {
        pbConfig = grailsApplication.config.pageBuilder
        GrailsWebMockUtil.bindMockWebRequest(ctx)
        adminTaskService.metaClass.pageBuilderLocation = pbConfig
        adminTaskService.developerSecurityService.metaClass.isAllowModify = { String a, String b -> return true }
        adminTaskService.developerSecurityService.metaClass.getImportConfigValue = { return false }
        adminTaskService.pageUtilService.developerSecurityService.metaClass.isAllowModify = { String a, String b -> return true }
        adminTaskService.pageUtilService.developerSecurityService.metaClass.getImportConfigValue = { return false }
        adminTaskService.cssUtilService.developerSecurityService.metaClass.isAllowModify = { String a, String b -> return true }
        adminTaskService.cssUtilService.developerSecurityService.metaClass.getImportConfigValue = { return false }
        adminTaskService.virtualDomainUtilService.developerSecurityService.metaClass.isAllowModify = { String a, String b -> return true }
        adminTaskService.virtualDomainUtilService.developerSecurityService.metaClass.getImportConfigValue = { return false }

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
                    domain: this.class.getResource(fname).text
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
        results[results.size()-1].digested == artifacts.size()
    }
}
