/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.css

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import net.hedtech.banner.security.DeveloperSecurityService
import net.hedtech.restfulapi.AccessDeniedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.mock.web.MockMultipartHttpServletRequest
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
@Rollback
class CssManagerControllerIntegrationSpec extends Specification{
    def cssDirPath
    def cssString = "body {color: red;}"

    @Autowired
    WebApplicationContext ctx

    @Autowired
    CssManagerController controller

    def grailsApplication
    def pbConfig

    def setup() {CssManagerControllerIntegrationSpec
        GrailsWebMockUtil.bindMockWebRequest(ctx)
        //  new File(cssDirPath).mkdir()
        pbConfig = grailsApplication.config.pageBuilder

        cssDirPath = pbConfig.locations.css
        new File(cssDirPath+"/testCss.json").write(cssString)
    }

    def cleanup() {
        new File(cssDirPath+"/testCss.json").delete()
        RequestContextHolder.resetRequestAttributes()
    }

    void "Integration test uploading of CSS file"() {
        given: "mock multipart file "
        controller.developerSecurityService.metaClass.isProductionReadOnlyMode = { return true}
        controller.developerSecurityService.metaClass.isAllowUpdateOwner  = { String a, String b -> return true}
        controller.developerSecurityService.metaClass.isAllowModify  = { String a, String b -> return true}

        controller.cssService.developerSecurityService = controller.developerSecurityService
        def contentStream = new FileInputStream(cssDirPath + "/testCss.json")
        def file = new MockMultipartFile("file",
                cssDirPath + "/testCss.json",
                "text/css",
                contentStream.getBytes())
        controller.metaClass.request = new MockMultipartHttpServletRequest()
        controller.request.addFile(file)
        when: "Upload CSS file"
        controller.params.cssName = "test"
        controller.params.description = "test desc"
        controller.params.file =  new File(cssDirPath + "/testCss.json")
        controller.uploadCss()
        then: "status code with 0 should be returned"
        controller.response.status == 200
        def resData = grails.converters.JSON.parse("$controller.response.content")
        resData!=null
        resData.statusCode == 0
    }

    void "Integration test uploading of CSS file with production mode on"() {
        given: "mock multipart file "
        controller.developerSecurityService.metaClass.isProductionReadOnlyMode = { return false}
        controller.developerSecurityService.metaClass.isAllowUpdateOwner  = { String a, String b -> return false}
        controller.developerSecurityService.metaClass.isAllowModify  = { String a, String b -> return false}
        controller.cssService.developerSecurityService = controller.developerSecurityService
        def contentStream = new FileInputStream(cssDirPath + "/testCss.json")
        def file = new MockMultipartFile("file",
                cssDirPath + "/testCss.json",
                "text/css",
                contentStream.getBytes())
        controller.metaClass.request = new MockMultipartHttpServletRequest()
        controller.request.addFile(file)
        when: "Upload CSS file"
        controller.params.cssName = "test"
        controller.params.description = "test desc"
        controller.params.file =  new File(cssDirPath + "/testCss.json")
        controller.uploadCss()
        then: "Exception has been thrown"
        AccessDeniedException accessDeniedException = thrown()
        accessDeniedException != null
        accessDeniedException.httpStatusCode == 403
    }
}
