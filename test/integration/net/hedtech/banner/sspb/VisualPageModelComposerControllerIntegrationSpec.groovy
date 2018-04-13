package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec
import spock.lang.Specification


class VisualPageModelComposerControllerIntegrationSpec extends IntegrationSpec  {

    VisualPageModelComposerController controller
    public void setup() {
        controller = new VisualPageModelComposerController()
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
