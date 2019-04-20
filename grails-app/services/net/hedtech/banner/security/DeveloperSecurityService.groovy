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

    static enableDeveloperSecurity = false
    static preventImportByDeveloper = false
    static productionMode = false
    static final String APP_ID = "EXTZ"
    static final String SUPER_USER = "GPBADMA"
    static final String PAGE_IND = "P"
    static final String CSS_IND = "C"
    static final String VIRTUAL_DOMAIN_IND = "V"
    static final String USER_GROUP = "INDIVIDUAL"


    static getGlobalSecurityValue(){
        List<ConfigurationData> results = ConfigurationData.fetchByType("boolean",APP_ID)
        results.each{
            Boolean loopValue = new Boolean(it.value)
            if(it.name.equals('pagebuilder.security.enableDeveloperSecurity')){
                enableDeveloperSecurity = loopValue
            }else if(it.name.equals('pagebuilder.security.preventImportByDeveloper')){
                preventImportByDeveloper = loopValue
            }else if(it.name.equals('pagebuilder.security.developerReadOnly')){
                productionMode = loopValue
            }
        }
    }

     static boolean isSuperUser() {
         boolean isSupUser=false
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        if (userIn?.class?.name?.endsWith('BannerUser')) {
            userIn.authorities.each {
                if (SUPER_USER.equals(it.objectName)) {
                    isSupUser = true
                    return isSupUser
                }
            }

        }
         return isSupUser
    }

     static boolean checkUserHasPrivilage(String constantName, String type, boolean isModify){
         def userIn = SecurityContextHolder?.context?.authentication?.principal
         String oracleUserId
         if (userIn?.class?.name?.endsWith('BannerUser')) {
             oracleUserId = userIn?.getOracleUserName()
         }else{
             return false
         }
         boolean hasPrivilage=false
        if(PAGE_IND.equalsIgnoreCase(type)){
            def page = Page.findByConstantName(constantName)
            if((page && page.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(page.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = PageSecurity.fetchAllByPageId(page?.id)
                    secList.each{
                        if(USER_GROUP.equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.pageSecKey.developerUserId)){
                                hasPrivilage = true
                                return hasPrivilage
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.pageSecKey.developerUserId, oracleUserId)
                            if(userList){
                                hasPrivilage = true
                                return hasPrivilage
                            }
                        }
                    }
                }
            }
            return hasPrivilage
        }
        else if(VIRTUAL_DOMAIN_IND.equalsIgnoreCase(type)){
            def domain = VirtualDomain.findByServiceName(constantName)
            if((domain && domain.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(domain.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = VirtualDomainSecurity.fetchAllByVirtualDomainId(domain?.id)
                    secList.each{
                        if(USER_GROUP.equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.domainSecKey.developerUserId)){
                                hasPrivilage = true
                                return hasPrivilage
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.domainSecKey.developerUserId ,oracleUserId )
                            if(userList) {
                                hasPrivilage = true
                                return hasPrivilage
                            }
                        }
                    }
                }
            }
            return hasPrivilage
        }else if(CSS_IND.equalsIgnoreCase(type)){
            def css = Css.fetchByConstantName(constantName)
            if((css && css.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(css.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = CssSecurity.fetchAllByCssId(css?.id)
                    secList.each{
                        if(USER_GROUP.equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.cssSecKey.developerUserId)){
                                hasPrivilage = true
                                return hasPrivilage
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.cssSecKey.developerUserId, oracleUserId)
                            if(userList) {
                                hasPrivilage = true
                                return hasPrivilage
                            }
                        }
                    }
                }
            }
            return hasPrivilage
        }else{
            return hasPrivilage
        }
    }

     static boolean isAllowImport(String constantName, String type){

         getGlobalSecurityValue()
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

    static boolean isAllowModify(String constantName, String type){
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

    static boolean isAllowUpdateOwner(String constantName, String type){
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
}
