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

    void "test importAllFromDir"() {
        given:
        def result = cssUtilService.importAllFromDir()
        expect:
        def cssInstance  = Css.findByConstantName("testCss")
        def isPersisted
        if(cssInstance) {
            isPersisted = true
        }
        assert isPersisted ==  true

    }
}
