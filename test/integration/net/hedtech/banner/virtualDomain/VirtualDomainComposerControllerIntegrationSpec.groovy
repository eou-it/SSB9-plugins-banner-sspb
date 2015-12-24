package net.hedtech.banner.virtualDomain

import grails.test.spock.IntegrationSpec
import net.hedtech.banner.virtualDomain.VirtualDomainComposerController

class VirtualDomainComposerControllerIntegrationSpec extends IntegrationSpec {
    def setup() {
    }

    def cleanup() {
    }

    void "test composeVirtualDomain"() {
        given:
        def vdController = new VirtualDomainComposerController()
        when: "calling composeVirtualDomain action"
        vdController.composeVirtualDomain()
        then: "renders virtual domain composer page"
        vdController.response.status == 200
        vdController.modelAndView.viewName == '/virtualDomainComposer/virtualDomainComposer'

    }

    void "test saveVirtualDomain"() {
        given:
        def vdController = new VirtualDomainComposerController()
        when: "saving the virtual domain"
        vdController.saveVirtualDomain()
        then: "should saves the virtual domain and renders the virtual domain composer page"
        vdController.response.status == 200
        vdController.modelAndView.viewName == '/virtualDomainComposer/virtualDomainComposer'
        where:
        params = [vdPutView: '', vdQueryView: 'select spriden_id from spriden', vdDeleteView: '', vdPostView: '', vdServiceName: 'pbTestSpriden']
    }
}
