/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
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
    static final String SUPER_USER = "GPBADMA"

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
                if (SUPER_USER.equals(it.objectName)) {
                    return true
                }
            }

        }
         return false
    }

     static boolean checkUserHasPrivilage(String constantName, String type, boolean isModify){
         def userIn = SecurityContextHolder?.context?.authentication?.principal
         String oracleUserId
         if (userIn?.class?.name?.endsWith('BannerUser')) {
             oracleUserId = userIn?.getOracleUserName()
         }else{
             return false
         }
         boolean found = false
         String id
        if("P".equalsIgnoreCase(type)){
            def page = Page.findByConstantName(constantName)
            id=page.id
            if((page && page.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(page.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = PageSecurity.findById(id)
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
            def domain = VirtualDomain.findByServiceName(constantName)
            id = domain.id
            if((domain && domain.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(domain.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = VirtualDomainSecurity.findById(id)
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
            def css = Css.fetchByConstantName(constantName)
            id = css.id
            if((css && css.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(css.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = CssSecurity.findById(id)
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

     static boolean allowImport(String constantName, String type){
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

    static boolean allowModify(String constantName, String type){
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

    static boolean allowUpdateOwner(String constantName, String type){
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
