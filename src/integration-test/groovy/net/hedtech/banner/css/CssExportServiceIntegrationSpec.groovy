/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.css

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

@Integration
@Rollback
class CssExportServiceIntegrationSpec extends Specification  {

    def cssExportService
    def cssService
    def grailsApplication
    def cssString = "body {color: red}"
    def pbConfig
    def path

    @Autowired
    WebApplicationContext ctx

    def setup() {
        GrailsWebMockUtil.bindMockWebRequest(ctx)

        cssService.developerSecurityService.metaClass.isProductionReadOnlyMode = { return true}
        cssService.developerSecurityService.metaClass.isAllowModify  = { String a, String b -> return true}
        pbConfig = grailsApplication.config.pageBuilder
        path = pbConfig.locations.css

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
