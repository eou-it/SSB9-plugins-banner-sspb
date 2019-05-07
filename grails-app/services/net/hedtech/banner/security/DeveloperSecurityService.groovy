/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security

import net.hedtech.banner.css.Css
import net.hedtech.banner.general.ConfigurationData
import net.hedtech.banner.sspb.Page
import net.hedtech.banner.virtualDomain.VirtualDomain
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS )
class DeveloperSecurityService {

    boolean enableDeveloperSecurity = false
    boolean preventImportByDeveloper = false
    boolean productionMode = false
    static final String APP_ID = "EXTZ"
    static final String SUPER_USER = "GPBADMA"
    static final String PAGE_IND = "P"
    static final String CSS_IND = "C"
    static final String VIRTUAL_DOMAIN_IND = "V"
    static final String USER_GROUP = "INDIVIDUAL"
    static final String ENABLE_DEVELOPER_SECURITY = 'pagebuilder.security.enableDeveloperSecurity'
    static final String PREVENT_IMPORT_BY_DEVELOPER = 'pagebuilder.security.preventImportByDeveloper'
    static final String DEVELOPER_READONLY='pagebuilder.security.developerReadOnly'


    DeveloperSecurityService(){
        loadSecurityConfiguration()
    }

    void loadSecurityConfiguration() {
        List<ConfigurationData> results = ConfigurationData.fetchByType("boolean", APP_ID)
        results.each {
            switch (it.name){
                case ENABLE_DEVELOPER_SECURITY :
                    enableDeveloperSecurity = new Boolean(it.value)
                    break
                case PREVENT_IMPORT_BY_DEVELOPER :
                    preventImportByDeveloper = new Boolean(it.value)
                    break
                case DEVELOPER_READONLY :
                    productionMode = new Boolean(it.value)
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

    boolean checkUserHasPrivilage(String constantName, String type, boolean isModify = false) {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        String oracleUserId
        if (userIn?.class?.name?.endsWith('BannerUser')) {
            oracleUserId = userIn?.getOracleUserName()
        } else {
            return false
        }
        Boolean hasPrivilage = false
        if (PAGE_IND.equalsIgnoreCase(type)) {
            hasPrivilage = isPageHasPrivilege(constantName, oracleUserId, isModify)
        } else if (VIRTUAL_DOMAIN_IND.equalsIgnoreCase(type)) {
            hasPrivilage = isVirtualDomainHasPrivilege(constantName, oracleUserId, isModify)
        } else if (CSS_IND.equalsIgnoreCase(type)) {
            hasPrivilage = isCssHasPrivilege(constantName, oracleUserId, isModify)
        }

        return hasPrivilage

    }

    protected boolean isCssHasPrivilege(String constantName, String oracleUserId, boolean isModify) {
        Boolean isCssHasPrivilege = false
        Css css = Css.fetchByConstantName(constantName)
        if (css && (css.owner?.equalsIgnoreCase(oracleUserId) || "Y".equalsIgnoreCase(css.allowAllInd))) {
            isCssHasPrivilege = true
        } else if (isModify) {
            List<CssSecurity> secList = CssSecurity.fetchAllByCssId(css?.id)
            for (CssSecurity cs : secList) {
                if (USER_GROUP.equalsIgnoreCase(cs.type)) {
                    if (oracleUserId.equals(cs.id.developerUserId)) {
                        isCssHasPrivilege = true
                    }
                } else {
                    def userList = BusinessProfile.findByProfile(cs.id.developerUserId, oracleUserId)
                    if (userList) {
                        isCssHasPrivilege = true
                    }
                }
            }
        }
        return isCssHasPrivilege
    }

    protected boolean isVirtualDomainHasPrivilege(String constantName, String oracleUserId, boolean isModify) {
        Boolean isVirtualDomainHasPrivilege = false
        VirtualDomain domain = VirtualDomain.findByServiceName(constantName)
        if (domain && (domain.owner?.equalsIgnoreCase(oracleUserId) || "Y".equalsIgnoreCase(domain.allowAllInd))) {
            isVirtualDomainHasPrivilege = true
        } else if (isModify) {
                List<VirtualDomainSecurity> secList = VirtualDomainSecurity.fetchAllByVirtualDomainId(domain?.id)
                for (VirtualDomainSecurity vs : secList) {
                    if (USER_GROUP.equalsIgnoreCase(vs.type)) {
                        if (oracleUserId.equals(vs.id.developerUserId)) {
                            isVirtualDomainHasPrivilege = true
                        }
                    } else {
                        def userList = BusinessProfile.findByProfile(vs.id.developerUserId, oracleUserId)
                        if (userList) {
                            isVirtualDomainHasPrivilege = true
                        }
                    }
                }
        }
        return isVirtualDomainHasPrivilege
    }

    protected boolean isPageHasPrivilege(String constantName, String oracleUserId, boolean isModify) {
        Boolean isPageHasPrivilege = false
        Page page = Page.findByConstantName(constantName)
        if (page && (page.owner?.equalsIgnoreCase(oracleUserId) || "Y".equalsIgnoreCase(page.allowAllInd))) {
            isPageHasPrivilege = true
        } else if (isModify) {
            List<PageSecurity> secList = PageSecurity.fetchAllByPageId(page?.id)
            for (PageSecurity ps : secList) {
                if (USER_GROUP.equalsIgnoreCase(ps.type)) {
                    if (oracleUserId.equals(ps.id.developerUserId)) {
                        isPageHasPrivilege = true
                    }
                } else {
                    def userList = BusinessProfile.findByProfile(ps.id.developerUserId, oracleUserId)
                    if (userList) {
                        isPageHasPrivilege = true
                    }
                }
            }
        }
        return isPageHasPrivilege
    }

    boolean isAllowImport(String constantName, String type){
         if(isSuperUser()){
            return true
        }else if(preventImportByDeveloper){
            return false
        }else if(enableDeveloperSecurity && !preventImportByDeveloper ){
            return checkUserHasPrivilage(constantName, type)
        }else if (!preventImportByDeveloper){
            return true
        }else{
            return false
        }
    }

    boolean isAllowModify(String constantName, String type){
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

    boolean isAllowUpdateOwner(String constantName, String type){
        if(isSuperUser()){
            return true
        }else if(productionMode){
            return false
        }else if(enableDeveloperSecurity && !productionMode){
            return checkUserHasPrivilage(constantName, type)
        }else if (!productionMode){
            return true
        }else{
            return false
        }
    }

    boolean isProductionReadOnlyMode(){
        loadSecurityConfiguration()
        return isSuperUser() || !productionMode
    }
}
