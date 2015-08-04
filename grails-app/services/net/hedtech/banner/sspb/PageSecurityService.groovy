package net.hedtech.banner.sspb

import grails.plugin.springsecurity.SpringSecurityUtils

/**
 * Created with IntelliJ IDEA.
 * User: hvthor
 * Date: 20/08/13
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
class PageSecurityService {
    static transactional = true
    def springSecurityService

    // For Restful interface
    def update(/*def id,*/ Map content, params)  {
        def page=Page.get(id);
        def rm=mergePage(page)
    }
    def create(Map content, params)  {
        def rm=update(content.pageId, content, params)
    }

    // Called from application bootstrap
    def init () {
        println "************  Initializing Request map **********"
        Requestmap.executeUpdate("delete Requestmap"); //delete all records to make sure we have a clean start
        ConfigObject co = SpringSecurityUtils.getSecurityConfig()
        if (co.securityConfigType.toString() == "Requestmap")   {
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
                for (role in roles )  {
                    configAttribute+=","+role
                }
                if (configAttribute.length()>0)
                    configAttribute=configAttribute.substring(1) //strip first comma
                saveRequestmap(url, configAttribute)
            }
            catch(e) {
                println "Exception merging $entry: \n $e"
            }
        }
    }

    def mergePages() {
        def pages = Page.getAll()
        for (page in pages) {
            mergePage(page)
        }
        //TODO delete Requestmap entries for deleted pages
    }

    def mergePage(Page page) {
        def url = "/customPage/page/${page.constantName}/**"
        def configAttribute=""
        def rm
        try {
            for (role in page.pageRoles )  {
                if (role.allow) {
                    if (role.roleName == "GUEST") {
                        configAttribute+=",IS_AUTHENTICATED_ANONYMOUSLY"
                    } else {
                        configAttribute+=",ROLE_SELFSERVICE-${role.roleName}_BAN_DEFAULT_M"
                    }
                }
            }
            if (configAttribute.length()>0)
                configAttribute=configAttribute.substring(1) //strip first comma
            rm=saveRequestmap(url, configAttribute)
        }
        catch(e) {
            println "Exception merging $url - $configAttribute: \n $e"
        }
        springSecurityService.clearCachedRequestmaps()
        return rm
    }



    private def saveRequestmap(String url, String configAttribute) {
        def rm=Requestmap.findByUrl(url)

        if ( rm ) {
            if ( ( rm.configAttribute.compareTo(configAttribute) != 0) || !configAttribute)  {
                rm.configAttribute = configAttribute
                if (configAttribute) {
                    rm.save()
                    println "Updated Requestmap entry $url : $configAttribute"
                } else {
                    rm.delete()
                    println "Removed Requestmap entry for url $url"
                }

            } else {
                println "Requestmap entry for url $url is not changed"
            }
        } else {
            if (configAttribute) {
                rm=new Requestmap (url: url, configAttribute: configAttribute)
                rm.save()
                println "Created new Requestmap entry $url : $configAttribute"
            }
        }
        return rm
    }
}
