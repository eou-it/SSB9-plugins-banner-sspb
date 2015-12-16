package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec

class PageExportServiceIntegrationSpec extends IntegrationSpec {
    def pageExportService
    def setup() {
    }

    def cleanup() {
    }

    void "test list"() {
        given:
        def params = [offset: "0", constantName: "%pbadm%", max: "10", sortby: "constantName asc",  controller: "restfulApi" ,pluralizedResourceName: "pageexports"]
        def result =pageExportService.list(params)
        expect:
        assert result.size() > 0
    }

    void "test count"() {
        given:
        def params = [offset: "0", constantName: "%pbadm%", max: "10", sortby: "constantName asc",  controller: "restfulApi" ,pluralizedResourceName: "pageexports"]
        def result =pageExportService.count(params)
        expect:
        assert result > 0
    }

    void "test create"() {
        given:
        def params = [controller: "restfulApi", id: "10005", pluralizedResourceName: "pageexports"]
        def content = [constantName: "pbadm.ExportCss", exportPage: "1", id: "10005"]
        def result = pageExportService.create(content, params)
        expect:
        assert result!=null
    }

    void "test update"() {
        given:
        def params = [controller: "restfulApi", id: "10005", pluralizedResourceName: "pageexports"]
        def content = [constantName: "pbadm.ExportCss", exportPage: "1", id: "10005"]
        def result = pageExportService.update(content, params)
        expect:
        assert result!=null
    }

}
