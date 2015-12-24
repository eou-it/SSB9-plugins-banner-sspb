package net.hedtech.banner.css

import grails.test.spock.IntegrationSpec

class CssRenderControllerIntegrationSpec extends IntegrationSpec {
    def cssService

    def setup() {
    }

    def cleanup() {
    }

    void "test stylesheet upload"() {
        given:
        def cssRenderController = new CssRenderController()
        def content =  [cssName: "testCss", description: "test", source: "body{color:red;}"]
        cssService.create(content, null)
        when:
        cssRenderController.params.name = "testCss"
        cssRenderController.loadCss()
        then:
        cssRenderController.response.status == 200
        cssRenderController.response.text == "body{color:red;}"
    }
}
