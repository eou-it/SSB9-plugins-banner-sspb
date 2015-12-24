package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec
import net.hedtech.banner.css.Css

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
        path = pbConfig.locations.css
        new File(path+"/testCss.json").write(cssString)
    }

    def cleanup() {
    }

    void "test Import CSS files"() {
        given:
        cssUtilService.importAllFromDir()
        when:
        def cssInstance  = Css.findByConstantName("testCss")
        and:
        def isPersisted
        if(cssInstance) {
            isPersisted = true
        }
        then:
        isPersisted ==  true

    }
}
