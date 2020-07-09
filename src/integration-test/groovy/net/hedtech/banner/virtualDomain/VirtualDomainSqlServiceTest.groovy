/******************************************************************************
 *  Copyright 2019-2020 Ellucian Company L.P. and its affiliates.                   *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import grails.util.Holders
import groovy.sql.Sql
import net.hedtech.restfulapi.AccessDeniedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification

@Integration
@Rollback
class VirtualDomainSqlServiceTest extends Specification{

    @Autowired
    WebApplicationContext ctx

    def virtualDomainSqlService
    def params=['url_encoding' :'utf-8' ,
                'id' : '123456'
    ]


    def sessionFactory

    def setup(){
        GrailsWebMockUtil.bindMockWebRequest(ctx)
    }
    def cleanup(){
        RequestContextHolder.resetRequestAttributes()

    }

    void "test for get"() {
        given:
        def vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                'select * from dual', typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory

        when:
        virtualDomainSqlService.get(vd, params)
        then:
        final AccessDeniedException exception = thrown()
        exception.errorMessage == 'user.not.authorized.get'
    }

    void "test for gets2"() {
        given:
        def vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                'select * from page', typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        params << [sortby:'CONSTANT_NAME asc']
        when:
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.totalCount >0
    }


    void "test for getwith roles"(){
        given:
        def vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                'select * from dual', typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet

        when:
        def res = virtualDomainSqlService.get(vd,params)
        then:
        res.totalCount >0
    }

    void "test for count"(){
        given:
        def vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                'select * from dual' , typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet

        when:
        def count = virtualDomainSqlService.count(vd,params)
        then:
        count.totalCount >0
    }

    void "test for delete"(){
        given:
        def vd = new VirtualDomain(serviceName: 'testPage', codeDelete:
                'delete virtual_domain_Temp where service_name="Test"', typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        vd.codeDelete

        when:
        def delete = virtualDomainSqlService.delete(vd,params)
        then:
        final VirtualDomainException exception = thrown()
        exception !=null
    }

    void "test for update"(){
        given:
        String par = 'testPage'
        def vd = new VirtualDomain(serviceName: 'testPage', codePut:
                "update virtual_domain set last_updated = sysdate where service_name=:param_id", typeOfCode: 's', id: 0)
       def data = ['id':0]
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        vd.codeDelete

        when:
        def update = virtualDomainSqlService.update(vd,params, data)
        then:
        update.id == "123456"
    }

    void "test for updates2"(){
        given:
        String par = 'testPage'
        def vd = new VirtualDomain(serviceName: 'testPage', codePut:
                "update virtual_domain set last_updated = sysdate where service_name=:param_id", typeOfCode: 'S', id:123456)
        def data = ['id':123456 , '\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{4}':'2018-12-25']
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        vd.codeDelete

        when:
        def update = virtualDomainSqlService.update(vd,params, data)
        then:
        update.id == "123456"
    }

    void "test for create"(){
        given:
        String par = 'testPage'
        def vd = new VirtualDomain(serviceName: 'testPage', codePost:
                "select * from virtual_domain where id=:param_id", typeOfCode: 's', id: 0)
        def data = ['id':0]
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        vd.codeDelete

        when:
        def create = virtualDomainSqlService.create(vd,params, data)
        then:
        noExceptionThrown()
    }

    void "test for gets with collon"() {
        given:
        def vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                'select * from page;;;', typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        params << [sortby:'CONSTANT_NAME asc']
        when:
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.totalCount >0
    }

    void "test for count with params"(){
        given:
        def vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                'select * from dual;;' , typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet

        when:
        def count = virtualDomainSqlService.count(vd,params)
        then:
        count.totalCount >0
    }

    void "test for gets with semicolon"() {
        given:
        def vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                "select 'data:image/jpeg;base64' as imageName from dual;", typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        grails.util.Holders.config.pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'
        def grailsApplication =Holders.getGrailsApplication()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true , id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles=vdrSet
        when:
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.rows.imageName[0] =="data:image/jpeg;base64"
    }

    void "test for get without order by"() {
        given:
        def actualRows = getSqlQueryResults()
        def vd = mockVirtualDomain()
        when:
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.totalCount == actualRows.size()
        res.rows != null
        res.rows.size() == res.totalCount
        actualRows.eachWithIndex { it, index ->
            def currentRow = res.rows[index]
            it.rownum == currentRow.rownum
            it.dummy_table_id == currentRow.dummy_table_id
            it.dummy_table_name == currentRow.dummy_table_name
            String.valueOf(it.id).bytes.encodeAsHex() == currentRow.id
        }

    }

    void "test for get with single order by column with desc"() {
        given:
        def actualRows = getSqlQueryResults('order by rownum desc')
        def vd = mockVirtualDomain()
        when:
        params << [sortby:'rownum desc']
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.totalCount == actualRows.size()
        res.rows != null
        res.rows.size() == res.totalCount
        actualRows.eachWithIndex { it, index ->
            def currentRow = res.rows[index]
            it.rownum == currentRow.rownum
            it.dummy_table_id == currentRow.dummy_table_id
            it.dummy_table_name == currentRow.dummy_table_name
            String.valueOf(it.id).bytes.encodeAsHex() == currentRow.id
        }
    }


    void "test for get with multiple order by columns comma separated"() {
        given:
        def actualRows = getSqlQueryResults('order by dummy_table_id asc, dummy_table_name desc')
        def vd = mockVirtualDomain()
        when:
        params << [sortby:'dummy_table_id asc, dummy_table_name desc']
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.totalCount == actualRows.size()
        res.rows != null
        res.rows.size() == res.totalCount
        actualRows.eachWithIndex { it, index ->
            def currentRow = res.rows[index]
            it.rownum == currentRow.rownum
            it.dummy_table_id == currentRow.dummy_table_id
            it.dummy_table_name == currentRow.dummy_table_name
            String.valueOf(it.id).bytes.encodeAsHex() == currentRow.id
        }
    }

    void "test for get with multiple order by columns list"() {
        given:
        def actualRows = getSqlQueryResults('order by dummy_table_id desc, dummy_table_name asc')
        def vd = mockVirtualDomain()
        when:
        params << [sortby: ['dummy_table_id asc', 'dummy_table_name desc']]
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.totalCount == actualRows.size()
        res.rows != null
        res.rows.size() == res.totalCount
        actualRows.eachWithIndex { it, index ->
            def currentRow = res.rows[index]
            it.rownum == currentRow.rownum
            it.dummy_table_id == currentRow.dummy_table_id
            it.dummy_table_name == currentRow.dummy_table_name
            String.valueOf(it.id).bytes.encodeAsHex() == currentRow.id
        }
    }

    void "test for get with single order by id column"() {
        given:
        def actualRows = getSqlQueryResults('order by id asc')
        def vd = mockVirtualDomain()
        when:
        params << [sortby: 'id asc']
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.totalCount == actualRows.size()
        res.rows != null
        res.rows.size() == res.totalCount
        actualRows.eachWithIndex { it, index ->
            def currentRow = res.rows[index]
            it.rownum == currentRow.rownum
            it.dummy_table_id == currentRow.dummy_table_id
            it.dummy_table_name == currentRow.dummy_table_name
            String.valueOf(it.id).bytes.encodeAsHex() == currentRow.id
        }
    }

    void "test for get with multiple order by columns"() {
        given:
        def actualRows = getSqlQueryResults('order by dummy_table_id asc, dummy_table_name desc')
        def vd = mockVirtualDomain('order by rownum desc')
        when:
        params << [sortby:'dummy_table_id asc, dummy_table_name desc']
        def res = virtualDomainSqlService.get(vd, params)
        then:
        res.totalCount == actualRows.size()
        res.rows != null
        res.rows.size() == res.totalCount
        actualRows.eachWithIndex { it, index ->
            def currentRow = res.rows[index]
            it.rownum == currentRow.rownum
            it.dummy_table_id == currentRow.dummy_table_id
            it.dummy_table_name == currentRow.dummy_table_name
            String.valueOf(it.id).bytes.encodeAsHex() == currentRow.id
        }
    }

    private def getSqlQueryResults(orderBy = '') {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        return sql.rows(getSqlQuery() + orderBy)
    }

    private def mockVirtualDomain( addon = '') {
        def vd = new VirtualDomain(serviceName: 'testPage', codeGet:
                getSqlQuery()+addon, typeOfCode: 's', id: 0)
        virtualDomainSqlService = new VirtualDomainSqlService()
        virtualDomainSqlService.grailsApplication = Holders.getGrailsApplication()
        virtualDomainSqlService.sessionFactory = sessionFactory
        def vdr = new VirtualDomainRole(allowGet: true, allowPost: true, allowDelete: true, allowPut: true, id: 0, roleName: 'GUEST')
        def vdrSet = new HashSet<VirtualDomainRole>()
        vdrSet.add(vdr)
        vd.virtualDomainRoles = vdrSet
        return vd
    }

    private def getSqlQuery() {
        def query = """
                    select * from (
                        select rownum, 1 as id, 1 as dummy_table_id, 'a' as dummy_table_name from dual
                        union
                        select rownum+1,2 as id, 2 as dummy_table_id, 'b' as dummy_table_name from dual
                        union 
                        select rownum+2,3 as id, 1 as dummy_table_id, 'c' as dummy_table_name from dual ) dummy_table
                    """
        return query
    }
}

