/******************************************************************************
 *  Copyright 2018-2019 Ellucian Company L.P. and its affiliates.                   *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.util.Holders
import net.hedtech.banner.security.DeveloperSecurityService
import net.hedtech.banner.sspb.Page
import spock.lang.Specification

class VirtualDomainUtilServiceTest extends Specification{

    def param=['url_encoding' :'utf-8' ,
               'sssid' : '123456'
    ]
    def sessionFactory
    def virtualDomainSqlService
    def externalLocation = 'target/i18n'
    def pbConfig = grails.util.Holders.getConfig().pageBuilder

    def setup(){
        if(!pbConfig.locations.virtualDomain){
            pbConfig.locations.virtualDomain = 'target/testData/virtualDomain'
        }
        def subDir = new File(externalLocation)
        subDir.mkdirs()
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

    def cleanup() {
        def subDir = new File(externalLocation)
        subDir.deleteDir()
    }

    void "test for exportAllToFile"(){
        given:
        def virtualDomainUtil = new VirtualDomainUtilService()
        when:
        virtualDomainUtil.exportAllToFile(externalLocation)
        then:
        noExceptionThrown()
    }

    void "test for exportToFile with params"(){
        given:
        def virtualDomainUtil = new VirtualDomainUtilService()
        when:
        virtualDomainUtil.exportToFile('%','integrationTest',externalLocation, false)
        then:
        noExceptionThrown()
    }

    void "test for getTimestamp"(){
        given:
        def file = new File(externalLocation+"/integrationTest.json")
        when:
        def res = VirtualDomainUtilService.getTimestamp('integrationTest',externalLocation)
        then:
        noExceptionThrown()
    }

    void "test for importInitially"(){
        given:
        def virtualDomainUtil = new VirtualDomainUtilService()
        when:
        virtualDomainUtil.importInitially()
        then:
        noExceptionThrown()
    }

    void "test for importAllFromDir"(){
        given:
        def virtualDomainUtil = new VirtualDomainUtilService()
        virtualDomainUtil.developerSecurityService = Stub(DeveloperSecurityService) {
            getPreventImportByDeveloper() >> false
            isAllowImport(_,_) >> true
        }
        when:
        def res = virtualDomainUtil.importAllFromDir()
        then:
        res>=0
    }

}
