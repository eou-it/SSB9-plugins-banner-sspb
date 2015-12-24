package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec

class PageExportServiceIntegrationSpec extends IntegrationSpec {
    def pageExportService
    def pageService
    def setup() {
        Map pageMap = [pageName: "integrationTestPage",
                           source: '''{
                                     "type": "page",
                                     "name": "integrationTestPage",
                                     "title": "Integration Test Page",
                                     "scriptingLanguage": "JavaScript",
                                     "components": null
                                     }''']

        def result = pageService.create(pageMap,[:])
        assert result.statusCode == 0
    }

    def cleanup() {
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

    void "test create"() {
        given:
        def pageInstance = Page.findByConstantName("integrationTestPage")
        def params = [controller: "restfulApi", id: pageInstance.id, pluralizedResourceName: "pageexports"]
        def content = [constantName: "integrationTestPage", exportPage: "1", id: pageInstance.id]
        when:
        def result = pageExportService.create(content, params)
        then:
        result!=null
        result.constantName == "integrationTestPage"
        result.exportPage == "1"
        result.id!=null
    }

    void "test page export update method"() {
        given:
        def pageInstance = Page.findByConstantName("integrationTestPage")
        def params = [controller: "restfulApi", id: pageInstance.id, pluralizedResourceName: "pageexports"]
        def content = [constantName: "integrationTestPage", exportPage: "1", id: pageInstance.id]
        when:
        def result = pageExportService.update(content, params)
        then:
        result!=null
        result.constantName == "integrationTestPage"
        result.exportPage == "1"
        result.id!=null
    }

}
