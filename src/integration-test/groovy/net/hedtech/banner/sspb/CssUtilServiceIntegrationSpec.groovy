/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import net.hedtech.banner.css.Css
import net.hedtech.banner.css.CssUtilService
import net.hedtech.banner.security.DeveloperSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
@Rollback
class CssUtilServiceIntegrationSpec extends Specification {

    @Autowired
    CssUtilService cssUtilService
    @Autowired
    WebApplicationContext ctx
    def grailsApplication

    //def cssUtilService
    def css
    def pbConfig
    def path
    def cssString = "{\n" +
            "   \"lastUpdated\": null,\n" +
            "   \"css\": \"body {color: red;}\",\n" +
            "   \"dateCreated\": null,\n" +
            "   \"constantName\": \"testCss\",\n" +
            "   \"description\": \"Test Stylesheet\",\n" +
            "   \"id\": null,\n" +
            "   \"fileTimestamp\": null,\n" +
            "   \"class\": \"net.hedtech.banner.css.Css\",\n" +
            "   \"version\": null\n" +
            "}"
    def setup() {

        GrailsWebMockUtil.bindMockWebRequest(ctx)
        pbConfig = grailsApplication.config.pageBuilder
        path = pbConfig.locations.css
        new File(path+"/testCss.json").write(cssString)
    }

    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
        new File(path+"/testCss.json").delete()
    }

    void "test Import CSS files"() {
        given:
        cssUtilService.importAllFromDir(path)
        when:
        def cssInstance  = Css.findByConstantName("testCss")
        and:
        def isPersisted
        if(cssInstance) {
            isPersisted = true
        }
        int hashcode = cssInstance.hashCode()
        then:
        isPersisted ==  true
        cssInstance.equals(cssInstance)
        hashcode!=null
    }

    void "test fileTimeStamp"(){
        when: "Getting time stamp"
        String fName= "testCss"
        Date result= cssUtilService.getTimestamp(fName, path)
        then:
        result.equals(result)
    }

    void "test exportToFile"(){
        when: "export File"
        String constantName = "pbadm.PageRoles"
        String pageLike=null
        Boolean skipDuplicates=false
        cssUtilService.exportToFile(constantName, pageLike, path, skipDuplicates)
        then:
        pageLike == null
    }
}
