/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.css

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import net.hedtech.banner.security.DeveloperSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
@Rollback
class CssRenderControllerIntegrationSpec extends Specification {

    @Autowired
    CssRenderController cssRenderController
    @Autowired
    WebApplicationContext ctx
    def cssService

    def setup() {
        GrailsWebMockUtil.bindMockWebRequest(ctx)
    }

    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }

    void "test stylesheet upload"() {
        given:
        def content =  [cssName: "testCss", description: "test", source: "body{color:red;}"]
        cssService.developerSecurityService.metaClass.isAllowModify  = { String a, String b -> return true}
        cssService.create(content, null)
        when:
        cssRenderController.params.name = "testCss"
        cssRenderController.loadCss()
        then:
        cssRenderController.response.status == 200
    }
}
