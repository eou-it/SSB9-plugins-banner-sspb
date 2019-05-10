/******************************************************************************
 *  Copyright 2013-2018 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec

class PageExportServiceIntegrationSpec extends IntegrationSpec {
    def pageExportService
    def pageService

    def testPage
    def setup() {
        Map pageMap = [pageName: "integrationTestPage",
                           source: '''{
                                     "type": "page",
                                     "name": "integrationTestPage",
                                     "title": "Integration Test Page",
                                     "scriptingLanguage": "JavaScript",
                                     "components": null
                                     }''']

        testPage = pageService.create(pageMap,[:])
        assert testPage.statusCode == 0
    }

    def cleanup() {
       // pageService.delete([:], "integrationTestPage")
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
