/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.sspb

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
class VisualPageModelComposerControllerIntegrationSpec extends Specification  {

    @Autowired
    WebApplicationContext ctx

    @Autowired
    VisualPageModelComposerController controller

    def setup() {
        GrailsWebMockUtil.bindMockWebRequest(ctx)
        controller.developerSecurityService.metaClass.isProductionReadOnlyMod = { return true }
    }
    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }
      void "Load page"(){
        when:
        controller.loadComposerPage()
        then:
        controller.response.status== 200
    }

    void "page model Def"(){
        given:
        def content
        when:
        controller.pageModelDef()
        then:
        controller.response.status== 200
    }

}
