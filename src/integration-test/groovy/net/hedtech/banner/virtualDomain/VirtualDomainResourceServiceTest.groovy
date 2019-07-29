/******************************************************************************
 *  Copyright 2019 Ellucian Company L.P. and its affiliates.                   *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import grails.util.Holders
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
@Rollback
class VirtualDomainResourceServiceTest extends Specification{

    @Autowired
    WebApplicationContext ctx

    def param=['url_encoding' :'utf-8' ,
                'id' : '123456'
    ]
    def sessionFactory
    def virtualDomainSqlService
    def developerSecurityService



    def setup(){
        GrailsWebMockUtil.bindMockWebRequest(ctx)
        def vd = new VirtualDomain(serviceName: 'integrationTest', codeGet:
                'select * from dual' , codeDelete: 'delete virtual_domain where service_name =:serviceName'
                , codePost: 'select * from virtual_domain where id =:id',
                codePut: 'select * from virtual_domain where id =:id', typeOfCode: 'S')

        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , roleName: 'GUEST', virtualDomain: vd)
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        def vdExist = VirtualDomain.findByServiceName('integrationTest')
        if(!vdExist) {
            vd.save(flush: true, failOnError: true)
        }
    }
    def cleanup(){
        RequestContextHolder.resetRequestAttributes()

    }
    void "test for list"(){
        given:
        def params = encodeParams()
        params<<param
        def virtualDomain = new VirtualDomainResourceService()
        virtualDomain.virtualDomainSqlService = virtualDomainSqlService
        virtualDomain.developerSecurityService = developerSecurityService
        when:
        def list = virtualDomain.list(params)
        then:
        list !=null
    }

    void  "test for loadVirtualDomain"(){
        given:
        def params = encodeParams()
        params<<param
        def virtualDomain = new VirtualDomainResourceService()
        virtualDomain.virtualDomainSqlService = virtualDomainSqlService
        virtualDomain.developerSecurityService = developerSecurityService
        when:
        def res =virtualDomain.loadVirtualDomain('integrationTest')
        then:
        res !=null
    }

    void  "test for show"(){
        given:
        def params = encodeParams()
        params<<param
        def virtualDomain = new VirtualDomainResourceService()
        virtualDomain.virtualDomainSqlService = virtualDomainSqlService
        virtualDomain.developerSecurityService = developerSecurityService
        when:
        def res = virtualDomain.show(params)
        then:
        res !=null
    }

    void  "test for count"(){
        given:
        def params = encodeParams()
        params<<param
        def virtualDomain = new VirtualDomainResourceService()
        virtualDomain.virtualDomainSqlService = virtualDomainSqlService
        virtualDomain.developerSecurityService = developerSecurityService
        when:
        def res = virtualDomain.count(params)
        then:
        res != "0"
    }

    void "test for create" (){
        given:
        def params = encodeParams()
        params << param
        def virtualDomain = new VirtualDomainResourceService()
        virtualDomain.virtualDomainSqlService = virtualDomainSqlService
        virtualDomain.developerSecurityService = developerSecurityService
        def data = ['id':0]
        when:
        virtualDomain.create(data, params)
        then:
        noExceptionThrown()
        /*final NullPointerException exception = thrown()
        exception !=null*/
    }

    void  "test for update"(){
        given:
        def params = encodeParams()
        params<<param
        def data = ['id':0]
        def virtualDomain = new VirtualDomainResourceService()
        virtualDomain.virtualDomainSqlService = virtualDomainSqlService
        virtualDomain.developerSecurityService = developerSecurityService
        when:
        def res =virtualDomain.update(data,params)
        then:
        res !=null
    }

    void  "test for delete"(){
        given:
        def params = encodeParams()
        params<<param
        def data = ['serviceName':'integrationTest']
        def virtualDomain = new VirtualDomainResourceService()
        virtualDomain.virtualDomainSqlService = virtualDomainSqlService
        virtualDomain.developerSecurityService = developerSecurityService
        when:
        def res =virtualDomain.delete(data,params)
        then:
        res !=null
    }

    void  "test for saveVirtualDomain"(){
        given:
        def params = encodeParams()
        params<<param
        def virtualDomain = new VirtualDomainResourceService()
        virtualDomain.virtualDomainSqlService = virtualDomainSqlService
        virtualDomain.developerSecurityService = developerSecurityService
        when:
        def res =virtualDomain.saveVirtualDomain('integrationTest',false, false, false, false,null)
        then:
        res.success== true
    }
    Map encodeParams(){
        def params = ['pluralizedResourceName':'virtualDomains.integrationTest',
                       'encoded':true, 'controller':'CustomPage' , 'action':'get' ]
        def crypt= Base64.encodeBase64("10".bytes)
        def key = Base64.encodeBase64("sprdine_id".bytes)
        def value = Base64.encodeBase64("123456".bytes)
        params.put(""+crypt+key, ""+crypt+value)
        return params
    }
}
