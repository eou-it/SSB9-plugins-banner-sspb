/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
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

    void "test CSS export list"() {
        given:
        def params = [offset: "0", constantName: "%testExportCss%", max: "10", sortby: "constantName asc"]
        when:
        def result = cssExportService.list(params)
        then:
        result.size() == 1
    }

    void "test CSS export update"() {
        given:
        def cssInstance = Css.findAllByConstantNameLike("testExportCss")
        def params = [controller: "restfulApi", id: cssInstance.id, pluralizedResourceName: "cssexports"]
        def content = [constantName: "testExportCss", export: 1, id: cssInstance.id]
        when:
        def result = cssExportService.update(content, params)
        then:
        result.size() == 3
        result.constantName == 'testExportCss'
        result.export == 1
        result.id != null
    }
}
