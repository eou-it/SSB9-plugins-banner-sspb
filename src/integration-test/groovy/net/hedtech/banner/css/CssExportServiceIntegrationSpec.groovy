/******************************************************************************
 *  Copyright 2013-2018 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.css

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.security.DeveloperSecurityService
import spock.lang.Specification

@Integration
@Rollback
class CssExportServiceIntegrationSpec extends Specification  {

    def cssExportService
    def developerSecurityService
    def cssService
    def grailsApplication
    def cssString = "body {color: red}"
    def pbConfig
    def path

    def setup() {
        pbConfig = grailsApplication.config.pageBuilder
        path = pbConfig.locations.css
        cssService.developerSecurityService = developerSecurityService

        if(!pbConfig.locations.css){
            pbConfig.locations.css = 'target'
        }
        path = pbConfig.locations.css
        new File(path+"/testExportCss.json").write(cssString)
        def result = cssService.create([cssName: "testExportCss.json", source:"TEST", description:"testExportCss"], [:])
        assert result.statusCode == 0
    }

    def cleanup() {
        pbConfig = grailsApplication.config.pageBuilder
        path = pbConfig.locations.css
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

    void "export css"(){
        when: "css, page details"
        def params = [constantName: "pbDefault"]
        def cssExport = cssExportService.show(params)
        then:
        cssExport
    }

    void "page name like"(){
        when:"page name"
        String pageName = "pbadm.PageRoles"
        def object = cssExportService.cssForPages(pageName)
        then:
        object.size()==0
    }
}
