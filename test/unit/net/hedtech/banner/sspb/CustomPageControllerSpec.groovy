package net.hedtech.banner.sspb

import grails.util.Holders
import spock.lang.Specification


/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
class CustomPageControllerSpec extends Specification {
    CustomPageController customPageController

    def setup() {
        customPageController = new CustomPageController()
    }

    void "test page"() {
        given:
        def parm1 = [id: "menu"]

        when:
        customPageController.request.parameters = parm1
        customPageController.page()

        then:
        customPageController.response.status == 200
    }

    void "test custom page with url"() {
        given:
        def param2 = [id: "pbadm.ssoauth"]

        when:
        customPageController.request.parameters = param2
        customPageController.page()

        then:
        customPageController.response.redirectedUrl == "/themeEditor"
    }

    void "test PB enabled"() {
        given:
        def result = Holders.config.pageBuilder?.enabled
        Holders.config.pageBuilder.enabled=true

        when:
        customPageController.request.parameters = result
        customPageController.page()

        then:
        customPageController.response.status == 200
    }

    void "test get HTML"() {
        given:
        def parm1 = [id: "menu"]

        when:
        customPageController.request.parameters = parm1
        customPageController.getHTML()

        then:
        customPageController.response.status == 404

    }
}
