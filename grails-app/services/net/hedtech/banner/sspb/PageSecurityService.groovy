/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb

import grails.plugin.springsecurity.SpringSecurityUtils
import groovy.util.logging.Log4j
import org.omg.CORBA.portable.ApplicationException

@Log4j
class PageSecurityService {
    static transactional = true
    static final datasource = 'sspb'
    def springSecurityService
    def grailsApplication

    // For Restful interface
    def update(/*def id,*/ Map ignore, params)  {
        def page=Page.get(params.id);
        mergePage(page)
    }
    def create(Map content, ignore)  {
        update( /*content.pageId,*/ content, [id:content.pageId])
    }
    def delete(Map ignore, params) {
        def url = "/customPage/page/${params.constantName}/**"
        def rm=Requestmap.findByUrl(url)
        if (rm) {
            rm.delete()
        }
    }

    // Called from application bootstrap
    def init () {
        log.trace "************  Initializing Request map **********"
        //def session = grails.util.Holders.grailsApplication.mainContext.sessionFactory.currentSession
        //Next delete introduces hibernate errors (that don't seem to matter)
        //def tx = session.beginTransaction();
        //Requestmap.executeUpdate("delete Requestmap"); //delete all records to make sure we have a clean start
        //Requestmap.where { (url!='%') }.updateAll(configAttribute:"ROLE_ADMIN") //give role that users won't have
        //session.flush()
        //session.clear()
        //tx.commit()
        Requestmap.findAll().each {
            it.configAttribute="denyAll"
        }


       ConfigObject co = SpringSecurityUtils.getSecurityConfig()
       if (co.securityConfigType.toString() == "Requestmap") {
           mergeInterceptUrlMap(co.interceptUrlMap)
           mergePages()
           //Clear cache after changing the Requestmap.
           springSecurityService.clearCachedRequestmaps()
       }
    }

    private def mergeInterceptUrlMap( map) {
        //parse the interceptUrlMap and merge into the Requestmap
        for (entry in map) {
            try {
                def url=entry.key
                def roles=entry.value
                def configAttribute=""
                if (roles[0].equals(grailsApplication.config.pbAdminRolesDefault) ) {
                    roles[0] = grailsApplication.config.pageBuilder.adminRoles?:'denyAll'
                }
                for (role in roles )  {
                    configAttribute+=","+role
                }
                if (configAttribute.length()>0)
                    configAttribute=configAttribute.substring(1) //strip first comma
                saveRequestmap(url, configAttribute)
            }
            catch(ApplicationException e) {
                log.error "Exception merging $entry: \n $e"
            }
        }
    }

    def mergePages() {
        def pages = Page.getAll()
        for (page in pages) {
            mergePage(page, false)
        }
    }

    def mergePage(Page page, clearCache=true) {
        def url = "/customPage/page/${page.constantName}/**"
        def configAttribute=""
        def rm
        try {
            for (role in page.pageRoles )  {
                if (role.allow) {
                    configAttribute += toConfigAttribute(role.roleName)
                }
            }
            if (configAttribute.length()>0) {
                configAttribute = configAttribute.substring(1) //strip first comma
            }
            rm=saveRequestmap(url, configAttribute)
        }
        catch(ApplicationException e) {
            log.error "Exception merging $url - $configAttribute: \n $e"
        }
        if (clearCache) {
            springSecurityService.clearCachedRequestmaps()
        }
        return rm
    }

    private def toConfigAttribute(roleName) {
        def result
        final def admin = "ADMIN-"
        if (roleName=="GUEST") {
            result = ",IS_AUTHENTICATED_ANONYMOUSLY"
        } else if ( roleName.startsWith(admin) ) {
            def adminRoles = grails.util.Holders.config.pageBuilder.adminRoles.split(',')
            def r = "ROLE_${roleName.minus(admin)}"
            result = adminRoles.find { it.startsWith(r) }
            result = result?",$result": ",${r}_BAN_DEFAULT_M" //Should we get guraobj_default_role here instead of BAN_DEFAULT_M
        } else {
            result = ",ROLE_SELFSERVICE-${roleName}_BAN_DEFAULT_M"
        }
        result
    }


    private def saveRequestmap(String url, String configAttribute) {
        def rm=Requestmap.findByUrl(url)

        if ( rm ) {
            if ( ( rm.configAttribute.compareTo(configAttribute) != 0) || !configAttribute)  {
                rm.configAttribute = configAttribute
                if (configAttribute) {
                    rm.save()
                    log.info "Updated Requestmap entry $url : $configAttribute"
                } else {
                    rm.delete(flush: true)
                    log.info "Removed Requestmap entry for url $url"
                }

            } else {
                log.info "Requestmap entry for url $url is not changed"
            }
        } else {
            if (configAttribute) {
                rm=new Requestmap (url: url, configAttribute: configAttribute)
                rm.save()
                log.debug "Created new Requestmap entry $url : $configAttribute"
            }
        }
        return rm
    }
}
