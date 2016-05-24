package net.hedtech.banner.sspb

import org.springframework.security.core.context.SecurityContextHolder
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib

import net.hedtech.banner.security.BannerGrantedAuthority
import org.springframework.security.core.GrantedAuthority
import org.apache.commons.logging.LogFactory

// Integration with Banner - class to get user object for page builder
// Define a user object with relevant attributes from the Banner user
class PBUser {

    static def userNameCache
    static def userCache

    private def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }

    static def get() {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        if (userIn.username.equals(userNameCache) ) {
            return userCache
        }
        LogFactory.getLog(this).info "Getting new PB User $userIn"
        //avoid direct dependency on BannerUser
        if (userIn.class.name.endsWith('BannerUser')) {
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
            userIn.authorities.each {
                authorities << [objectName: it.objectName, roleName: it.roleName ]
            }
            userCache = [authenticated:  true, pidm: userIn.pidm,gidm: userIn.gidm, loginName: userIn.username, fullName: userIn.fullName,
                    authorities: authorities]

        }  else { //create guest authorities
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
            authorities << [objectName: "SELFSERVICE-GUEST", roleName: "BAN_DEFAULT_M"] //BannerGrantedAuthority.create( "SELFSERVICE-GUEST", "BAN_DEFAULT_M", null )
            userCache = [authenticated: false, pidm: null, gidm: null,  loginName: userIn.username,
                    fullName: localizer(code:"sspb.renderer.page.anonymous.full.name"),authorities: authorities]
        }
        userNameCache = userIn.username
        userCache
    }

    //Dont include data that should not be exposed
    static def getTrimmed() {
        def user = PBUser.get()
        [authenticated:  user.authenticated, pidm: user.pidm?0:user.pidm,
         loginName: user.loginName, fullName: user.fullName, authorities: user.authorities]
    }

}
