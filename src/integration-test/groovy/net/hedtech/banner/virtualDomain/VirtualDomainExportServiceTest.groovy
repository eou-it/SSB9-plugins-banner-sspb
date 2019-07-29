/******************************************************************************
 *  Copyright 2019 Ellucian Company L.P. and its affiliates.                   *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import grails.util.Holders
import net.hedtech.banner.sspb.Page
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
@Rollback
class VirtualDomainExportServiceTest extends Specification{

    @Autowired
    WebApplicationContext ctx
    def grailsApplication

    def param=['url_encoding' :'utf-8' ,
               'sssid' : '123456'
    ]
    def sessionFactory
    def virtualDomainSqlService
    def pbConfig /*= grails.util.Holders.getConfig().pageBuilder*/


    def setup(){
        GrailsWebMockUtil.bindMockWebRequest(ctx)
        pbConfig = grailsApplication.config.pageBuilder
        if(!pbConfig.locations.virtualDomain){
            pbConfig.locations.virtualDomain = 'target'
        }
        def vd = new VirtualDomain(serviceName: 'integrationTest', codeGet:
                'select * from dual' , codeDelete: 'delete virtual_domain where service_name =:serviceName'
                , codePost: 'select * from virtual_domain where id =:id',
                codePut: 'select * from virtual_domain where id =:id', typeOfCode: 'S')
        def page = new Page(constantName: 'integrationTest', modelView: '{ "name": "Test", "ID" : "1"}' ,
                compiledView: '{ "name": "compiled", "ID" : "1"}',
                compiledController: '{ "name": "controller", "ID" : "1"}' , deltaVersion: 0)
        page.save()

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

    void "test for show"(){
        given:
        def params = encodeParams()
        params<<param
        def virtualDomainExport = new VirtualDomainExportService()
        when:
        def res = virtualDomainExport.show(params)
        then:
        res.size()>0
    }

    void "test for list"(){
        given:
        def params = encodeParams()
        params<<param
        def virtualDomainExport = new VirtualDomainExportService()
        when:
        def res = virtualDomainExport.list(params)
        then:
        res !=null
    }

    void "test for update"(){
        given:

        Map params = ['export':1 , 'serviceName': 'integrationTest']
        def virtualDomainExport = new VirtualDomainExportService()
        when:
        def res = virtualDomainExport.update(params, false)
        then:
        res.size()>0
    }

    void "test for vdForPages"(){
        given:

        Map params = encodeParams();
        params << param
        def virtualDomainExport = new VirtualDomainExportService()
        when:
        def res = virtualDomainExport.vdForPages('integrationTest')
        then:
        res.size()!=null
    }

    Map encodeParams(){
        def params = ['pluralizedResourceName':'virtualDomains.integrationTest',
                      'encoded':true, 'controller':'CustomPage' , 'action':'get' , 'constantName':'integrationTest' ,
                        'sortby': 'ascending', 'pageLike' : 'integrationTest', 'serviceName' : 'integrationTest'
        ]
        def crypt= Base64.encodeBase64("10".bytes)
        def key = Base64.encodeBase64("sssss".bytes)
        def value = Base64.encodeBase64("123456".bytes)
        params.put(""+crypt+key, ""+crypt+value)
        return params
    }
}
