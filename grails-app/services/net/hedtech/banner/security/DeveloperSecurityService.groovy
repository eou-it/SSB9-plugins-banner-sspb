/*******************************************************************************
 Copyright 2019-2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.security

import grails.gorm.transactions.Transactional
import grails.util.Holders
import groovy.util.logging.Slf4j
import net.hedtech.banner.css.Css
import net.hedtech.banner.general.ConfigurationData
import net.hedtech.banner.sspb.Page
import net.hedtech.banner.virtualDomain.VirtualDomain
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Propagation

@Slf4j
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


    void loadSecurityConfiguration() {
        log.debug('loading security configuration - start')
        def appId = Holders.config.app.appId
        List<ConfigurationData> results = ConfigurationData.fetchByType("boolean", appId)
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
        log.debug('loading security configuration - end')
    }

    static def getImportConfigValue() {
        def appId = Holders.config.app.appId
        def importData = ConfigurationData.fetchByNameAndType(PREVENT_IMPORT_BY_DEVELOPER, "boolean", appId)
        if (log.isDebugEnabled()) {
            log.debug "import config flag value is ${importData?.value ?: false}"
        }
        return importData ? importData.value : false
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
        if (log.isDebugEnabled()) {
            log.debug "login user is a super user -> ${isSupUser}"
        }
         return isSupUser
    }

    boolean checkUserHasPrivilage(String constantName, String type, boolean isModify = false, boolean isUpdateOwner=false) {
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        String oracleUserId
        if (userIn?.class?.name?.endsWith('BannerUser')) {
            oracleUserId = userIn.oracleUserName?.toUpperCase()
        } else {
            return false
        }
        Boolean hasPrivilage = false
        if (PAGE_IND.equalsIgnoreCase(type)) {
            hasPrivilage = isPageHasPrivilege(constantName, oracleUserId, isModify, isUpdateOwner)
        } else if (VIRTUAL_DOMAIN_IND.equalsIgnoreCase(type)) {
            hasPrivilage = isVirtualDomainHasPrivilege(constantName, oracleUserId, isModify, isUpdateOwner)
        } else if (CSS_IND.equalsIgnoreCase(type)) {
            hasPrivilage = isCssHasPrivilege(constantName, oracleUserId, isModify, isUpdateOwner)
        }

        if (log.isDebugEnabled()) {
            log.debug "login user has privilege to access on  ${constantName} -> ${hasPrivilage}"
        }

        return hasPrivilage

    }

    protected boolean isCssHasPrivilege(String constantName, String oracleUserId, boolean isModify, boolean isUpdateOwner) {
        Boolean isCssHasPrivilege = false
        Css css = Css.fetchByConstantName(constantName)
        if(!css){
            isCssHasPrivilege = true
        }else if (css && (css.owner?.equalsIgnoreCase(oracleUserId) || (!isUpdateOwner && "Y".equalsIgnoreCase(css.allowAllInd)))) {
            isCssHasPrivilege = true
        } else if (isModify) {
            List<CssSecurity> secList = CssSecurity.fetchAllByCssId(css?.id)
            for (CssSecurity cs : secList) {
                if (USER_GROUP.equalsIgnoreCase(cs.type)) {
                    if (oracleUserId.equals(cs.id.developerUserId) && "Y".equals(cs.allowModifyInd)) {
                        isCssHasPrivilege = true
                    }
                } else {
                    if("Y".equals(cs.allowModifyInd)) {
                        def userList = BusinessProfile.findByProfile(cs.id.developerUserId, oracleUserId)
                        if (userList) {
                            isCssHasPrivilege = true
                        }
                    }
                }
            }
        }
        return isCssHasPrivilege
    }

    protected boolean isVirtualDomainHasPrivilege(String constantName, String oracleUserId, boolean isModify, boolean isUpdateOwner) {
        Boolean isVirtualDomainHasPrivilege = false
        VirtualDomain domain = VirtualDomain.findByServiceName(constantName)
        if(!domain){
            isVirtualDomainHasPrivilege = true
        } else if (domain && (domain.owner?.equalsIgnoreCase(oracleUserId) || (!isUpdateOwner && "Y".equalsIgnoreCase(domain.allowAllInd)))) {
            isVirtualDomainHasPrivilege = true
        } else if (isModify) {
                List<VirtualDomainSecurity> secList = VirtualDomainSecurity.fetchAllByVirtualDomainId(domain?.id)
                for (VirtualDomainSecurity vs : secList) {
                    if (USER_GROUP.equalsIgnoreCase(vs.type)) {
                        if (oracleUserId.equals(vs.id.developerUserId) && "Y".equals(vs.allowModifyInd)) {
                            isVirtualDomainHasPrivilege = true
                        }
                    } else {
                        if("Y".equals(vs.allowModifyInd)) {
                            def userList = BusinessProfile.findByProfile(vs.id.developerUserId, oracleUserId)
                            if (userList) {
                                isVirtualDomainHasPrivilege = true
                            }
                        }
                    }
                }
        }
        return isVirtualDomainHasPrivilege
    }

    protected boolean isPageHasPrivilege(String constantName, String oracleUserId, boolean isModify, boolean isUpdateOwner) {
        Boolean isPageHasPrivilege = false
        Page page = Page.findByConstantName(constantName)
        if(!page){
            isPageHasPrivilege = true
        }else if (page && (page.owner?.equalsIgnoreCase(oracleUserId) || (!isUpdateOwner && "Y".equalsIgnoreCase(page.allowAllInd)))) {
            isPageHasPrivilege = true
        } else if (isModify) {
            List<PageSecurity> secList = PageSecurity.fetchAllByPageId(page?.id)
            for (PageSecurity ps : secList) {
                if (USER_GROUP.equalsIgnoreCase(ps.type)) {
                    if (oracleUserId.equals(ps.id.developerUserId) && "Y".equals(ps.allowModifyInd)) {
                        isPageHasPrivilege = true
                    }
                } else {
                    if("Y".equals(ps.allowModifyInd)) {
                        def userList = BusinessProfile.findByProfile(ps.id.developerUserId, oracleUserId)
                        if (userList) {
                            isPageHasPrivilege = true
                        }
                    }
                }
            }
        }
        return isPageHasPrivilege
    }

    boolean isAllowImport(String constantName, String type){
        loadSecurityConfiguration()
        if(isSuperUser()){
            return true
        } else if (preventImportByDeveloper){
            return false
        } else if (enableDeveloperSecurity && !preventImportByDeveloper ){
            return checkUserHasPrivilage(constantName, type, true, false)
        } else if (!enableDeveloperSecurity && !preventImportByDeveloper ){
            return true
        } else {
            return false
        }
    }

    boolean isAllowModify(String constantName, String type){
        loadSecurityConfiguration()
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
        loadSecurityConfiguration()
        if(isSuperUser()){
            return true
        }else if(productionMode){
            return false
        }else if(enableDeveloperSecurity && !productionMode){
            return checkUserHasPrivilage(constantName, type, false, true)
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
