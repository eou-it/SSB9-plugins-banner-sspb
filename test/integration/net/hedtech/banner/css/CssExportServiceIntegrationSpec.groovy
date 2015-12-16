package net.hedtech.banner.css

import grails.test.spock.IntegrationSpec

class CssExportServiceIntegrationSpec extends IntegrationSpec {

    def cssExportService

    def setup() {
    }

    def cleanup() {
    }

    void "test list"() {
        given:
        def params = [offset: "0", constantName: "%pbadm%", max: "10", sortby: "constantName asc",  controller: "restfulApi" ,pluralizedResourceName: "cssexports"]
        def result = cssExportService.list(params)
        expect:
        assert result.size() == 1
    }

    void "test count"() {
        given:
        def params = [offset: "0", constantName: "%pbadm%", max: "10", sortby: "constantName asc",  controller: "restfulApi" ,pluralizedResourceName: "cssexports"]
        def result =cssExportService.count(params)
        expect:
        assert result == 1
    }

    void "test update"() {
        given:
        def params = [controller: "restfulApi", id: "10000", pluralizedResourceName: "cssexports"]
        def content = [constantName: "pbadm.ExportCss", exportCss: "1", id: "10000"]
        def result = cssExportService.update(content, params)
        expect:
        assert result!=null
    }

    void "test create"() {
        given:
        def params = [controller: "restfulApi", id: "10000", pluralizedResourceName: "cssexports"]
        def content = [constantName: "pbadm.ExportCss", exportCss: "1", id: "10000"]
        def result = cssExportService.create(content, params)
        expect:
        assert result!=null
    }
}
