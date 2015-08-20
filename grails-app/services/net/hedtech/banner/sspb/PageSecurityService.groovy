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
    static datasource = 'sspb'
    def springSecurityService

    // For Restful interface
    def update(/*def id,*/ Map content, params)  {
        def page=Page.get(params.id);
        def rm=mergePage(page)
    }
    def create(Map content, params)  {
        def rm=update( /*content.pageId,*/ content, [id:content.pageId])
    }
    def delete(Map content, params) {
        def url = "/customPage/page/${params.constantName}/**"
        def rm=Requestmap.findByUrl(url)
        if (rm) {
            rm.delete()
        }
    }

    // Called from application bootstrap
    def init () {
        println "************  Initializing Request map **********"
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
        if (clearCache) {
            springSecurityService.clearCachedRequestmaps()
        }
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
                rm.save(validate: false) //try no validate to get rid of annoying message
                println "Created new Requestmap entry $url : $configAttribute"
            }
        }
        return rm
    }
}
