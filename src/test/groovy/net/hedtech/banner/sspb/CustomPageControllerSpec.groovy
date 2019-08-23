/*******************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.sspb

import grails.testing.web.controllers.ControllerUnitTest
import grails.util.Holders
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
class CustomPageControllerSpec extends Specification implements ControllerUnitTest<CustomPageController> {

    void "test page"() {
        given:
        def parm1 = [id: "menu"]

        when:
        controller.request.parameters = parm1
        controller.page()

        then:
        controller.response.status == 200
    }

    void "test custom page with url"() {
        given:
        def param2 = [id: "pbadm.ssoauth"]

        when:
        controller.request.parameters = param2
        controller.page()

        then:
        controller.response.redirectedUrl == "/themeEditor"
    }

    void "test PB enabled"() {
        given:
        Holders.config.pageBuilder.enabled=true

        when:
        controller.page()

        then:
        controller.response.status == 200
    }

    void "test PB notEnabled"(){
        given:
        Holders.config.pageBuilder.enabled = false

        when:
        controller.page()

        then:
        controller.response.status == 302
    }

    void "test get HTML"() {
        given:
        def parm1 = [id: "menu"]

        when:
        controller.request.parameters = parm1
        controller.getHTML()

        then:
        controller.response.status == 404

    }
}
