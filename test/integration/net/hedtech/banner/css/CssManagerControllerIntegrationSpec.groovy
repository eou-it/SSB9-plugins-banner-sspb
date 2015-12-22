package net.hedtech.banner.css

import grails.test.spock.IntegrationSpec
import java.io.File
import org.springframework.mock.web.MockMultipartFile

class CssManagerControllerIntegrationSpec extends IntegrationSpec {
    def cssService
    def cssFilePath   = "target/testData/css/testCss.css"
    def cssString = "body {color: red;}"
    def CssDirPath = "target/testData/css"

    def setup() {
        def pbConfig = grails.util.Holders.getConfig().pageBuilder
        def path = pbConfig.locations.css
        new File(path+"/testCss.json").write(cssString)
    }

    def cleanup() {

    }

    void "Integration uploadCss"() {
        given:
            def cssManagerController = new CssManagerController()
            def params = [cssName:"test", description: "test desc", file: new File(cssFilePath)]
            def contentStream = new FileInputStream(cssFilePath)
            def file = new MockMultipartFile("file",
                cssFilePath,
                "text/css",
                contentStream)
            cssManagerController.request.addFile(file)
            cssManagerController.uploadCss(params)
        expect:
            cssManagerController.response.status == 200

    }
}
