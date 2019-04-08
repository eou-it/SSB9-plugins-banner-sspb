/*******************************************************************************
 * Copyright 2013-2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.util.Holders
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.apache.commons.logging.LogFactory

// Integration with Banner - class to get user object for page builder
// Define a user object with relevant attributes from the Banner user
class PBUser {

    static def userNameCache
    static def userCache
    static final String SUPERUSER = "GPBADMA"

    private def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }

    static def get() {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        if (userIn && userIn?.username?.equals(userNameCache) ) {
            return userCache
        }
        LogFactory.getLog(this).info "Getting new PB User $userIn"
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
        //avoid direct dependency on BannerUser
        if (userIn?.class?.name?.endsWith('BannerUser')) {
            userIn.authorities.each {
                if (it.objectName) {
                    authorities << [objectName: it.objectName, roleName: it.roleName]
                } else if ( grails.util.Holders.config.pageBuilder.adminRoles?.contains(it.toString()) ) {
                    authorities << [objectName: it.objectName, roleName: it.roleName]
                }
            }
            userCache = [authenticated:  true, pidm: userIn.pidm,gidm: userIn.gidm, loginName: userIn.username, fullName: userIn.fullName,
                    authorities: authorities]

        } else {
            userCache = [authenticated: false, pidm: null, gidm: null,  loginName: userIn?userIn?.username:"_anonymousUser",
                         fullName: localizer(code:"sspb.renderer.page.anonymous.full.name"),authorities: authorities]
        }
        //give user guest role to be consistent with ability to view pages with IS_AUTHENTICATED_ANONYMOUSLY role
        userCache.authorities << [objectName: "SELFSERVICE-GUEST", roleName: "BAN_DEFAULT_M"]

        userNameCache = userIn?.username
        userCache
    }

    //Dont include data that should not be exposed
    static def getTrimmed() {
        def user = PBUser.get()
        def userInfo =
                [authenticated:  user.authenticated,
                 loginName: user.loginName, fullName: user.fullName , isSuperUser:isSuperUser()]
        boolean isEnabled = Holders.config?.pageBuilder?.development?.authorities?.enabled
        if(isEnabled){
            userInfo<<[authorities: user.authorities]
        }
        return userInfo
    }

    static boolean isSuperUser() {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        if (userIn?.class?.name?.endsWith('BannerUser')) {
            userIn.authorities.each {
                if (SUPERUSER.equalsIgnoreCase(it.objectName)) {
                    return true
                }
            }
        }
        return false
    }

}
