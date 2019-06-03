/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.css

import grails.test.spock.IntegrationSpec
import net.hedtech.banner.security.DeveloperSecurityService

import java.io.File
import org.springframework.mock.web.MockMultipartFile

class CssManagerControllerIntegrationSpec extends IntegrationSpec {
    def cssDirPath   = "target/testData/css"
    def cssString = "body {color: red;}"
    def setup() {
        new File(cssDirPath).mkdir()
        new File(cssDirPath+"/testCss.json").write(cssString)
    }

    def cleanup() {
        new File(cssDirPath+"/testCss.json").delete()
    }

    void "Integration test uploading of CSS file"() {
        given: "mock multipart file "
        def cssManagerController = new CssManagerController()
                cssManagerController.developerSecurityService = Stub(DeveloperSecurityService) {
                    isProductionReadOnlyMode() >> true
                    isAllowModify(_,_) >> true
        }
        cssManagerController.cssService.developerSecurityService = cssManagerController.developerSecurityService
        def contentStream = new FileInputStream(cssDirPath + "/testCss.json")
        def file = new MockMultipartFile("file",
                cssDirPath + "/testCss.json",
                "text/css",
                contentStream)
        cssManagerController.request.addFile(file)
        when: "Upload CSS file"
        cssManagerController.params.cssName = "test"
        cssManagerController.params.description = "test desc"
        cssManagerController.params.file =  new File(cssDirPath + "/testCss.json")
        cssManagerController.uploadCss()
        then: "status code with 0 should be returned"
        cssManagerController.response.status == 200
        cssManagerController.response.json.statusCode == 0

    }
}
