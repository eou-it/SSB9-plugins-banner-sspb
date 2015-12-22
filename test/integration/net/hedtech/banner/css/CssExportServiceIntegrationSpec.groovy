package net.hedtech.banner.css

import grails.test.spock.IntegrationSpec
import net.hedtech.banner.tools.PBUtilServiceBase

class CssExportServiceIntegrationSpec extends IntegrationSpec {

    def cssExportService
    def cssService
    def cssString = "body {color: red}"
    def pbConfig = grails.util.Holders.getConfig().pageBuilder
    def path = pbConfig.locations.css

    def setup() {
        new File(path+"/testExportCss.json").write(cssString)
        def result = cssService.create([cssName: "testExportCss.json", source:"TEST", description:"testExportCss"], [:])
        assert result.statusCode == 0
    }

    def cleanup() {
        def path = pbConfig.locations.css
        new File(path+"/testExportCss.json").delete()
    }

    void "test list"() {
        given:
        def params = [offset: "0", constantName: "%testExportCss%", max: "10", sortby: "constantName asc",  controller: "restfulApi" ,pluralizedResourceName: "cssexports"]
        def result = cssExportService.list(params)
        expect:
        assert result.size() == 1
    }

    void "test update"() {
        given:
        def cssInstance = Css.findAllByConstantNameLike("testExportCss")
        def params = [controller: "restfulApi", id: cssInstance.id, pluralizedResourceName: "cssexports"]
        def content = [constantName: "testExportCss", exportCss: "1", id: "10000"]
        def result = cssExportService.update(content, params)
        expect:
        assert result.size() == 3
        assert result.constantName == 'testExportCss'
        assert result.exportCss == "1"
        assert result.id != null
    }

    void "test create"() {
        given:
        def cssInstance = Css.findAllByConstantNameLike("testExportCss")
        def params = [controller: "restfulApi", id: cssInstance.id, pluralizedResourceName: "cssexports"]
        def content = [constantName: "testExportCss", exportCss: "1", id: "10000"]
        def result = cssExportService.create(content, params)
        expect:
        assert result.size() == 3
        assert result.constantName == 'testExportCss'
        assert result.exportCss == "1"
        assert result.id != null
    }
}
