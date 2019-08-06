/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
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
class PageExportServiceIntegrationSpec extends Specification {

    @Autowired
    PageExportService pageExportService
    @Autowired
    WebApplicationContext ctx
    def pageService
    def grailsApplication

    def testPage
    def pbConfig

    def setup() {
        pbConfig = grailsApplication.config.pageBuilder
        GrailsWebMockUtil.bindMockWebRequest(ctx)
        if(!pbConfig.locations.page){
            pbConfig.locations.page = 'target/testData/model'
        }
        Map pageMap = [pageName: "integrationTestPage",
                           source: '''{
                                     "type": "page",
                                     "name": "integrationTestPage",
                                     "title": "Integration Test Page",
                                     "scriptingLanguage": "JavaScript",
                                     "components": null
                                     }''']
        pageService.developerSecurityService.metaClass.isProductionReadOnlyMode = { return true}
        pageService.developerSecurityService.metaClass.isAllowUpdateOwner  = { String a, String b -> return true}
        pageService.developerSecurityService.metaClass.isAllowModify  = { String a, String b -> return true}
        testPage = pageService.create(pageMap,[:])
        assert testPage.statusCode == 0
    }

    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }

    void "test show"() {
        given:
        def params1 = [constantName: "integrationTestPage", controller: "restfulApi" ,pluralizedResourceName: "pageexports"]
        def params2 = [id: "integrationTestPage",  controller: "restfulApi" ,pluralizedResourceName: "pageexports"]
        def params3 = [id: testPage.page.id.toString(), controller: "restfulApi" ,pluralizedResourceName: "pageexports"]
        def params4 = [constantName: "NotExistingPageName",  controller: "restfulApi" ,pluralizedResourceName: "pageexports"]
        when:
        def result1 =pageExportService.show(params1)
        def result2 =pageExportService.show(params2)
        def result3 =pageExportService.show(params3)
        def result4 =pageExportService.show(params4)
        then:
        result1 != null
        result1.constantName == "integrationTestPage"
        result2 != null
        result2.constantName == "integrationTestPage"
        result3 != null
        result3.constantName == "integrationTestPage"
        result4 == null
    }

    void "test list"() {
        given:
        def params = [offset: "0", constantName: "%integrationTestPage%", max: "10", sortby: "constantName asc",  controller: "restfulApi" ,pluralizedResourceName: "pageexports"]
        when:
        def result =pageExportService.list(params)
        then:
        result.size() == 1
        result[0].constantName == "integrationTestPage"
    }

    void "test page export update method"() {
        given:
        def pageInstance = Page.findByConstantName("integrationTestPage")
        def params = [controller: "restfulApi", id: pageInstance.id, pluralizedResourceName: "pageexports"]
        def content = [constantName: "integrationTestPage", export: 1, id: pageInstance.id]
        when:
        def result = pageExportService.update(content, params)
        then:
        result!=null
        result.constantName == "integrationTestPage"
        result.export == 1
        result.id!=null
    }

}
