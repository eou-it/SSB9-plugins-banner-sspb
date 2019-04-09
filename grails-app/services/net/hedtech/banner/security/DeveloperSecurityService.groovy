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
            if(it.name.equals('pagebuilder.security.enableDeveloperSecurity')){
                enableDeveloperSecurity = it.value
            }else if(it.name.equals('pagebuilder.security.preventImportByDeveloper')){
                preventImportByDeveloper = it.value
            }else if(it.name.equals('pagebuilder.security.developerReadOnly')){
                productionMode = it.value
            }
        }
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
        if(PAGE_IND.equalsIgnoreCase(type)){
            def page = Page.findByConstantName(constantName)
            id=page.id
            if((page && page.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(page.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = PageSecurity.findById(id)
                    secList.each{
                        if(USER_GROUP.equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.pageSecKey.developerUserId)){
                                return true
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.pageSecKey.developerUserId, oracleUserId)
                            if(userList){
                                return true
                            }
                        }
                    }
                }
            }
            return false
        }
        else if(VIRTUAL_DOMAIN_IND.equalsIgnoreCase(type)){
            def domain = VirtualDomain.findByServiceName(constantName)
            id = domain.id
            if((domain && domain.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(domain.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = VirtualDomainSecurity.findById(id)
                    secList.each{
                        if(USER_GROUP.equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.domainSecKey.developerUserId)){
                                return true
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.domainSecKey.developerUserId ,oracleUserId )
                            if(userList)
                                return true
                        }
                    }
                }
            }
            return false
        }else if(CSS_IND.equalsIgnoreCase(type)){
            def css = Css.fetchByConstantName(constantName)
            id = css.id
            if((css && css.owner?.equalsIgnoreCase(oracleUserId)) || "Y".equalsIgnoreCase(css.allowAllInd)){
                return true
            }else{
                if(isModify){
                    def secList = CssSecurity.findById(id)
                    secList.each{
                        if(USER_GROUP.equalsIgnoreCase(it.type)){
                            if(oracleUserId.equals(it.cssSecKey.developerUserId)){
                                return true
                            }
                        }else{
                            def userList = BusinessProfile.findByProfile(it.cssSecKey.developerUserId, oracleUserId)
                            if(userList)
                                return true
                        }
                    }
                }
            }
            return false
        }else{
            return false
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
