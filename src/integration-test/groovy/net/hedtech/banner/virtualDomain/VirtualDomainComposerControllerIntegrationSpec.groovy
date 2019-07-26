/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import net.hedtech.banner.security.DeveloperSecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class VirtualDomainComposerControllerIntegrationSpec extends Specification  {

    @Autowired
    WebApplicationContext ctx
    @Autowired
    VirtualDomainComposerController vdController
    def setup() {
        GrailsWebMockUtil.bindMockWebRequest(ctx)
    }

    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }

    void "test composeVirtualDomain"() {
        given:
        vdController.webRequest.params.putAll([:])
        when: "calling composeVirtualDomain action"
        vdController.composeVirtualDomain()
        then: "renders virtual domain composer page"
        vdController.response.status == 200
        vdController.modelAndView.viewName == '/virtualDomainComposer/virtualDomainComposer'

    }

    void "test saveVirtualDomain"() {
        given:
        vdController.virtualDomainResourceService.metaClass.list = { Map params ->
            return [:]
        }
        /*vdController.developerSecurityService = Stub(DeveloperSecurityService) {
            isAllowModify(_,_) >> true
        }*/
        when: "saving the virtual domain"
        vdController.webRequest.params.putAll(params)
        vdController.saveVirtualDomain()
        then: "should saves the virtual domain and renders the virtual domain composer page"
        vdController.response.status == 200
        vdController.modelAndView.viewName == '/virtualDomainComposer/virtualDomainComposer'
        null != vdController.modelAndView.model.pageInstance.id
        VirtualDomain vd = VirtualDomain.findByServiceName(params.vdServiceName)
        null != vd
        vd.id == vdController.modelAndView.model.pageInstance.id
        where:
        params = [vdPutView: '', vdQueryView: 'select spriden_id from spriden', vdDeleteView: '', vdPostView: '', vdServiceName: 'pbTestSpriden']
    }

    void "test deleteVirtualDomain"() {
        given:
        vdController.virtualDomainResourceService.metaClass.list = { Map params ->
            return [:]
        }
        vdController.developerSecurityService.metaClass.isAllowModify = {String a, String b->return true}
        when: "saving the virtual domain"
        vdController.webRequest.params.putAll(params)
        vdController.saveVirtualDomain()
        then: "should saves the virtual domain and renders the virtual domain composer page"
        vdController.response.status == 200
        vdController.modelAndView.viewName == '/virtualDomainComposer/virtualDomainComposer'
        null != vdController.modelAndView.model.pageInstance.id
        VirtualDomain vd = VirtualDomain.findByServiceName(params.vdServiceName)
        null != vd
        vd.id == vdController.modelAndView.model.pageInstance.id
        when : "delete the virtual domain when isAllowModify is false"
        vdController.webRequest.params.putAll(params)
        vdController.deleteVirtualDomain()
        then:
        vdController.response.status == 200
        null == vdController.modelAndView.model.pageInstance
        VirtualDomain vd1 = VirtualDomain.findByServiceName(params.vdServiceName)
        null == vd1
        where:
        params = [vdPutView: '', vdQueryView: 'select spriden_id from spriden', vdDeleteView: '', vdPostView: '', vdServiceName: 'pbTestSpriden']
    }


    void "test saveVirtualDomain when isAllowModify is false"() {
        given:
        vdController.virtualDomainResourceService.metaClass.list = { Map params ->
            return [:]
        }
        vdController.developerSecurityService.metaClass.isAllowModify = {String a, String b-> return false}
        when: "saving the virtual domain"
        vdController.webRequest.params.putAll(params)
        vdController.saveVirtualDomain()
        then: "should not saves the virtual domain and renders the virtual domain composer page"
        vdController.response.status == 403
        vdController.response.text == '_anonymousUser is not authorized to create record'
        VirtualDomain vd = VirtualDomain.findByServiceName(params.vdServiceName)
        null == vd
        where:
        params = [vdPutView: '', vdQueryView: 'select spriden_id from spriden', vdDeleteView: '', vdPostView: '', vdServiceName: 'pbTestSpriden']
    }

    void "test deleteVirtualDomain when isAllowModify is false"() {
        given:
        vdController.virtualDomainResourceService.metaClass.list = { Map params ->
            return [:]
        }
        vdController.developerSecurityService.metaClass.isAllowModify = {String a, String b-> return true}
        when: "saving the virtual domain"
        vdController.webRequest.params.putAll(params)
        vdController.saveVirtualDomain()
        then: "should saves the virtual domain and renders the virtual domain composer page"
        vdController.response.status == 200
        vdController.modelAndView.viewName == '/virtualDomainComposer/virtualDomainComposer'
        null != vdController.modelAndView.model.pageInstance.id
        VirtualDomain vd = VirtualDomain.findByServiceName(params.vdServiceName)
        null != vd
        vd.id == vdController.modelAndView.model.pageInstance.id
        when : "delete the virtual domain when isAllowModify is false"
        vdController.developerSecurityService.metaClass.isAllowModify = {String a, String b->return false}
        vdController.webRequest.params.putAll(params)
        vdController.deleteVirtualDomain()
        then:
        vdController.response.status == 403
        vdController.response.text == '_anonymousUser is not authorized to delete record'
        VirtualDomain vd1 = VirtualDomain.findByServiceName(params.vdServiceName)
        null != vd1
        where:
        params = [vdPutView: '', vdQueryView: 'select spriden_id from spriden', vdDeleteView: '', vdPostView: '', vdServiceName: 'pbTestSpriden']
    }

}
