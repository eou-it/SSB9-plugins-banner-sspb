package net.hedtech.banner.css

import grails.test.spock.IntegrationSpec

class CssRenderControllerIntegrationSpec extends IntegrationSpec {
    def cssRenderController = new CssRenderController()
    def cssFilePath   = "target/testData/css/testCss.css"
    def cssString = "body {color: red;}"
    def CssDirPath = "target/testData/css"

    def setup() {
        new File(CssDirPath).mkdirs()
        new File(cssFilePath).write(cssString)
    }


    def cleanup() {
    }

    void "test loadCss"() {
        given:
            def params = [name: "testCss", controller: "cssRender"]
            cssRenderController.request.parameters = params
            cssRenderController.loadCss()
        expect:
            cssRenderController.response.status == 200
    }
}
