/*******************************************************************************
 * Copyright 2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.util.Holders
import net.hedtech.banner.i18n.DateConverterService
import net.hedtech.banner.security.DeveloperSecurityService
import org.apache.commons.codec.binary.Base64
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class VirtualDomainSpec extends Specification {

    def params=['url_encoding' :'utf-8' ,
                'id' : '123456',
                'getGridData':'GridData'
    ]
    def virtualDomainService
    def vd

    def setup() {
        virtualDomainService = new VirtualDomainService()
        virtualDomainService.dateConverterService = new DateConverterService()
        virtualDomainService.developerSecurityService = new DeveloperSecurityService()
        vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                'select * from dual', typeOfCode: 'S')
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , roleName: 'GUEST', virtualDomain: vd)
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        vd.save()
    }

    def cleanup() {
    }

    void "test list"() {
        when:
        def result = virtualDomainService.list(params)
        then:
        result.size()>0
    }

    void "test for show"(){
        when:
        def param = ['serviceName': 'testPage']
        def result = virtualDomainService.show(param)
        then:
        result.id !=null

        when:
        def vds = VirtualDomain.findByServiceName('testPage')
        param = ['id':'testPage']
        def res = virtualDomainService.show(param)
        then:
        res.id == vds.id

        when:
        param = ['id':""+vds.id]
        res = virtualDomainService.show(param)
        then:
        res.id == vds.id

    }

    void "test for create"(){
        when:
        params << ['serviceName':'testPage2', 'codeGet': 'select *', 'version':0 ]
        virtualDomainService.create(params, null)
        then:
        noExceptionThrown()
    }

    void "test for update"(){
        when:
        def vds = VirtualDomain.findByServiceName('testPage')
        params << ['id':vds.id , 'serviceName':'testPage', 'codeGet': 'select *', 'version':0 ]
        virtualDomainService.update(params, params)
        then:
        noExceptionThrown()
    }

    void "test for delete"(){
        when:
        def vds = VirtualDomain.findByServiceName('testPage')
        params << ['id':vds.id , 'serviceName':'testPage', 'codeGet': 'select *', 'version':0 ]
        virtualDomainService.delete(params, params)
        then:
        noExceptionThrown()
    }
}
