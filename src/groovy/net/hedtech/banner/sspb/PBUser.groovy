package net.hedtech.banner.sspb

import org.springframework.security.core.context.SecurityContextHolder
import net.hedtech.banner.security.BannerUser
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib

// Integration with Banner - class to get user object for page builder
// Define a user object with relevant attributes from the Banner user
class PBUser {
    private def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }
    static def get() {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        def user
        if (userIn instanceof BannerUser) {
            user = [authenticated:  true, pidm: userIn.pidm,gidm: userIn.gidm, loginName: userIn.username, fullName: userIn.fullName, roles: userIn.authorities]
        }  else {
            user = [authenticated: false, pidm: null,       gidm: null,        loginName: userIn,          fullName: localizer(code:"sspb.renderer.page.anonymous.full.name"),roles: ["ROLE_SELFSERVICE-WEBUSER-BAN_DEFAULT_M"]]
        }
        user
    }
}
