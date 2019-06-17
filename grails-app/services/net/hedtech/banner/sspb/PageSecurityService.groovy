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
    //static final datasource = 'sspb'
    def springSecurityService

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
        def requestMapIndex = getRequestMapIndex()
        ConfigObject co = SpringSecurityUtils.getSecurityConfig()
        if (co.securityConfigType.toString() == "Requestmap") {
            requestMapIndex = mergeInterceptUrlMap(co.interceptUrlMap, requestMapIndex)
            requestMapIndex = mergePages(requestMapIndex)
            applyRequestMapIndex(requestMapIndex)
            //Clear cache after changing the Requestmap.
            springSecurityService.clearCachedRequestmaps()
        }
    }

    //Get a map from the requestmap domain with url as key
    private def getRequestMapIndex() {
        def index = [:]
        Requestmap.getAll().each{ it ->
            index[it.url] = [domain: it, referenced: false, configAttribute: ""]
        }
        index
    }

    private def applyRequestMapIndex(map) {
        map.each { key, it ->
            saveRequestmap(key, it.configAttribute, it.domain, false)
        }
    }

    private def mergeInterceptUrlMap( map, requestMapIndex) {
        //parse the interceptUrlMap and merge into the Requestmap
        map.each { it ->
            def match = requestMapIndex[it.pattern]
            if (!match) {
                match = [domain: null]
                requestMapIndex[it.pattern] = match
            }
            match.configAttribute = getConfigAttribute(it.configAttributes)
        }
        requestMapIndex
    }

    def mergePages(requestMapIndex) {
        def pages = Page.getAll()
        for (page in pages) {
            def url = pageUrl(page.constantName)
            def match = requestMapIndex[url]
            if (!match) {
                match = [domain: null]
                requestMapIndex[url] = match
            }
            match.configAttribute = getPageConfigAttribute(page)
            match.referenced = true
        }
        requestMapIndex
    }

    private def pageUrl(name) {
        "/customPage/page/${name}/**"
    }

    private def getConfigAttribute(roles) {
        def configAttribute=""
        if (roles) {
            for (role in roles.sort(false) )  {
                configAttribute += ","+role
            }
            configAttribute = configAttribute.startsWith(',')?configAttribute.substring(1):configAttribute
        }
        configAttribute
    }

    def mergePage(Page page, clearCache=true) {
        def url = pageUrl(page.constantName)
        def configAttribute = ""
        def rm
        try {
            configAttribute = getPageConfigAttribute(page)
            rm = saveRequestmap(url, configAttribute)
        }
        catch (ApplicationException e) {
            log.error "Exception merging $url - $configAttribute: \n $e"
        }
        if (clearCache) {
            springSecurityService.clearCachedRequestmaps()
        }
        return rm
    }

    private def getPageConfigAttribute(page) {
        def roles = []
        page.pageRoles.each {
            if (it.allow) {
                roles << toConfigAttribute(it.roleName)
            }
        }
        getConfigAttribute(roles)
    }

    private def toConfigAttribute(roleName) {
        def result
        final def admin = "ADMIN-"
        if (roleName=="GUEST") {
            result = "IS_AUTHENTICATED_ANONYMOUSLY"
        } else if ( roleName.startsWith(admin) ) {
            def adminRoles = grails.util.Holders.config.pageBuilder.adminRoles.split(',')
            def r = "ROLE_${roleName.minus(admin)}"
            result = adminRoles.find { it.startsWith(r) }
            result = result?"$result": "${r}_BAN_DEFAULT_M"
        } else {
            result = "ROLE_SELFSERVICE-${roleName}_BAN_DEFAULT_M"
        }
        result
    }

    private def saveRequestmap(String url, String configAttribute,  rm = null, doFind = true) {
        if (!rm && doFind) {
            rm = Requestmap.findByUrl(url)
        }

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
