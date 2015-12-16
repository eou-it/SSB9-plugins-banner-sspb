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
        vdController.composeVirtualDomain()
        expect:
        vdController.response.status == 200
        // println vdcController.response.contentAsString


    }

    void "test saveVirtualDomain"() {
        given:
            def vdController = new VirtualDomainComposerController()
            def params = [vdPutView: '', vdQueryView: 'select spriden_id from spriden', vdDeleteView: '', vdPostView: '', vdServiceName: 'pbTestSpriden']
            vdController.request.parameters = params
            vdController.saveVirtualDomain()
        expect:
            vdController.response.status == 200
            println vdController.response.contentAsString
    }
}
