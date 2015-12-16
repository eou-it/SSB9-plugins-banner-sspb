package net.hedtech.banner.sspb

import org.springframework.security.core.context.SecurityContextHolder
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib

import net.hedtech.banner.security.BannerGrantedAuthority
import org.springframework.security.core.GrantedAuthority
import org.apache.commons.logging.LogFactory

// Integration with Banner - class to get user object for page builder
// Define a user object with relevant attributes from the Banner user
class PBUser {

    private def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }

    static def get() {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        def user
        LogFactory.getLog(this).info "Getting new PB User $userIn"
        //avoid direct dependency on BannerUser
        if (userIn.class.name.endsWith('BannerUser')) {
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
            userIn.authorities.each {
                authorities << it
            }
            // assume all authenticated users have WEBUSER role implicitly
            authorities << BannerGrantedAuthority.create( "SELFSERVICE-WEBUSER", "BAN_DEFAULT_M", null )
            user = [authenticated:  true, pidm: userIn.pidm,gidm: userIn.gidm, loginName: userIn.username, fullName: userIn.fullName,
                    authorities: authorities]


        }  else { //create guest authorities
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
            authorities << BannerGrantedAuthority.create( "SELFSERVICE-GUEST", "BAN_DEFAULT_M", null )
            user = [authenticated: false, pidm: null, gidm: null,  loginName: userIn,
                    fullName: localizer(code:"sspb.renderer.page.anonymous.full.name"),authorities: authorities]
        }
        user
    }
}
