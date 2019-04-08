package net.hedtech.banner.security

import net.hedtech.banner.css.Css
import net.hedtech.banner.general.ConfigurationData
import net.hedtech.banner.sspb.Page
import net.hedtech.banner.virtualDomain.VirtualDomain
import org.springframework.security.core.context.SecurityContextHolder


class DeveloperSecurityService {

    static enableDeveloperSecurity = true
    static preventImportByDeveloper = false
    static productionMode = false
    def private static appId = "EXTZ"
    static final String SUPERUSER = "GPBADMA"

    DeveloperSecurityService(){
        getGlobalSecurityValue()
    }

    static getGlobalSecurityValue(){
        def results = ConfigurationData.fetchByType("boolean",appId)
        enableDeveloperSecurity = (results && results.get('pagebuilder.security.enableDeveloperSecurity'))?
                results.get('pagebuilder.security.enableDeveloperSecurity'):enableDeveloperSecurity
        preventImportByDeveloper = (results && results.get('pagebuilder.security.preventImportByDeveloper'))?
                results.get('pagebuilder.security.enableDeveloperSecurity'):preventImportByDeveloper
        productionMode = (results && results.get('pagebuilder.security.developerReadOnly'))?
                results.get('pagebuilder.security.developerReadOnly'):productionMode

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

     static boolean checkUserHasPrivilage(Long id, String type, boolean isModify){
         def userIn = SecurityContextHolder?.context?.authentication?.principal
         String oracleUserId
         if (userIn?.class?.name?.endsWith('BannerUser')) {
             oracleUserId = userIn?.getOracleUserName()
         }else{
             return false
         }
         boolean found = false
        if("P".equalsIgnoreCase(type)){
            def page = Page.findById(id)
            if((page && page.pageOwner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(page.pageAllowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = PageSecurity.findById(id.toString())
                    secList.each{
                        if("INDIVIDUAL".equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.pageSecKey.developerUserId)){
                                return true
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.pageSecKey.developerUserId)
                            userList.each{
                                if(oracleUserId.equalsIgnoreCase(it.profileUserId)){
                                    return true
                                }
                            }
                        }
                    }
                }
            }
            return false
        }
        else if("V".equalsIgnoreCase(type)){
            def domain = VirtualDomain.findById(id)
            if((domain && domain.virtualDomainOwner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(domain.virtualDomainAllowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = VirtualDomainSecurity.findById(id.toString())
                    secList.each{
                        if("INDIVIDUAL".equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.domainSecKey.developerUserId)){
                                return true
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.domainSecKey.developerUserId)
                            userList.each{
                                if(oracleUserId.equalsIgnoreCase(it.profileUserId)){
                                    return true
                                }
                            }
                        }
                    }
                }
            }
            return false
        }else if("C".equalsIgnoreCase(type)){
            def css = Css.findById(id.toString())
            if((css && css.cssOwner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(css.cssAllowAll)){
                return true
            }else{
                if(isModify){
                    def secList = CssSecurity.findById(id.toString())
                    secList.each{
                        if("INDIVIDUAL".equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.cssSecKey.developerUserId)){
                                return true
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.cssSecKey.developerUserId)
                            userList.each{
                                if(oracleUserId.equalsIgnoreCase(it.profileUserId)){
                                    return true
                                }
                            }
                        }
                    }
                }
            }
            return false
        }else{
            return false
        }
    }

     static boolean allowImport(Long constantName, String type){
        if(isSuperUser()){
            return true
        }else if(preventImportByDeveloper){
            return false
        }else if(enableDeveloperSecurity && !preventImportByDeveloper ){
            return checkUserHasPrivilage(constantName, type, false)
        }else if (!preventImportByDeveloper){
            return true
        }else{
            return false
        }
    }

    static boolean allowModify(Long constantName, String type){
        if(isSuperUser()){
            return true
        }else if(productionMode){
            return false
        }else if(enableDeveloperSecurity && !productionMode){
            return checkUserHasPrivilage(constantName, type, true)
        }else if (!productionMode){
            return true
        }else{
            return false
        }
    }

    static boolean allowUpdateOwner(Long constantName, String type){
        if(isSuperUser()){
            return true
        }else if(productionMode){
            return false
        }else if(enableDeveloperSecurity && !productionMode){
            return checkUserHasPrivilage(constantName, type, false)
        }else if (!productionMode){
            return true
        }else{
            return false
        }
    }

    def static getSecurityData(def id, def type){
        return ["allowImport": allowImport(id, type), "allowModify": allowModify(id, type),
                "allowUpdateOwner":allowUpdateOwner(id, type)]
    }


}
