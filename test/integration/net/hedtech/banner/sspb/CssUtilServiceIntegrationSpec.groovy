/******************************************************************************
 *  Copyright 2013-2018 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec
import net.hedtech.banner.css.Css
import net.hedtech.banner.security.DeveloperSecurityService

class CssUtilServiceIntegrationSpec extends IntegrationSpec {

    def cssUtilService
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
        pbConfig = grails.util.Holders.getConfig().pageBuilder
        pbConfig.locations.css = 'target/testData/css'
     //   path = System.getProperty("java.io.tmpdir");
        path = pbConfig.locations.css
        new File(path+"/testCss.json").write(cssString)
    }

    def cleanup() {
        new File(path+"/testCss.json").delete()
    }

    void "test Import CSS files"() {
        given:
        cssUtilService.developerSecurityService = Stub(DeveloperSecurityService) {
            getPreventImportByDeveloper() >> false
            isAllowImport(_,_) >> true
        }
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
