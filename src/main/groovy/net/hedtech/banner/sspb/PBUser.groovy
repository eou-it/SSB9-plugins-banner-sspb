/*******************************************************************************
 * Copyright 2013-2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.util.Holders
import net.hedtech.banner.security.DeveloperSecurityService
import net.hedtech.banner.tools.PBSessionTracker
import org.apache.commons.logging.LogFactory
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

// Integration with Banner - class to get user object for page builder
// Define a user object with relevant attributes from the Banner user
class PBUser {

    private def static localizer = { mapToLocalize ->
        new ValidationTagLib().message(mapToLocalize)
    }

    static def get() {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        def username = userIn ? userIn?.username : "_anonymousUser"
        if (userIn && PBSessionTracker.cachedMap.containsKey(username)) {
            LogFactory.getLog(this).info "Getting cache PB User $userIn"
            return PBSessionTracker.cachedMap.get(username)
        }
        LogFactory.getLog(this).info "Getting new PB User $userIn"
        PBSessionTracker.cachedMap.put(username, [:])
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
        //avoid direct dependency on BannerUser
        if (userIn?.class?.name?.endsWith('BannerUser')) {
            userIn.authorities.each {
                if (it.objectName) {
                    authorities << [objectName: it.objectName, roleName: it.roleName]
                } else if (grails.util.Holders.config.pageBuilder.adminRoles?.contains(it.toString())) {
                    authorities << [objectName: it.objectName, roleName: it.roleName]
                }
            }
            PBSessionTracker.cachedMap.get(username) << [authenticated : true, pidm: userIn.pidm, gidm: userIn.gidm, loginName: userIn.username,
                                                                 fullName: userIn.fullName,
                                                                 oracleUserName: userIn?.oracleUserName?.toUpperCase() ?: '', authorities: authorities]

        } else {
            PBSessionTracker.cachedMap.get(username) << [authenticated : false, pidm: null, gidm: null, loginName: userIn ? userIn?.username : "_anonymousUser",
                                                                 oracleUserName: '',
                                                                 fullName      : localizer(code: "sspb.renderer.page.anonymous.full.name"), authorities: authorities]
        }
        //give user guest role to be consistent with ability to view pages with IS_AUTHENTICATED_ANONYMOUSLY role
        PBSessionTracker.cachedMap.get(username).authorities << [objectName: "SELFSERVICE-GUEST", roleName: "BAN_DEFAULT_M"]

        return PBSessionTracker.cachedMap.get(username)
    }

    //Dont include data that should not be exposed
    static def getTrimmed() {
        def user = PBUser.get()
        def userInfo =
                [authenticated : user.authenticated,
                 loginName     : user.loginName, fullName: user.fullName,
                 oracleUserName: user?.oracleUserName,
                 isSuperUser   : DeveloperSecurityService.isSuperUser()]
        boolean isEnabled = Holders.config?.pageBuilder?.development?.authorities?.enabled
        if (isEnabled) {
            userInfo << [authorities: user.authorities]
        }
        return userInfo
    }

}
