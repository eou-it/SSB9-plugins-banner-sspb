/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.gorm.transactions.Transactional
import groovy.util.logging.Log4j
import net.hedtech.banner.security.DeveloperSecurityService
import net.hedtech.banner.sspb.CommonService
import net.hedtech.banner.sspb.PBUser
import net.hedtech.banner.sspb.Page
import net.hedtech.restfulapi.AccessDeniedException
import org.hibernate.HibernateException

@Log4j
@Transactional
class VirtualDomainResourceService {

    def virtualDomainSqlService
    def developerSecurityService

    final static String vdPrefix = "virtualDomains."

    private def vdName (params) {
        String resourceName=params.pluralizedResourceName
        if ( resourceName.startsWith(vdPrefix)) {
            resourceName = resourceName.substring(vdPrefix.length())
        } else {
            resourceName = null
        }
    }

    // Interface for restful API - TODO may choose to put this in a separate service or move to VirtualDomainSqlService
    @Transactional(readOnly = true)
    def list(Map params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        def queryResult
        def result
        def serviceName = vdName(params)
        def vd = loadVirtualDomain(vdName(params))
        if (vd.error) {
            throw new VirtualDomainException( message(code:"sspb.virtualdomain.invalid.service.message", args:[serviceName]))
        }
        queryResult = virtualDomainSqlService.get(vd.virtualDomain, params)
        if (queryResult.error == "") {
            result = queryResult.rows
        } else {
            throw new VirtualDomainException( queryResult.error )
        }
        result
    }

    @Transactional(readOnly = true)
    def show(Map params) {
        list(params)[0]
    }

    @Transactional(readOnly = true)
    def count(Map params) {
        def queryResult
        def result
        def serviceName = vdName(params)
        def vd = loadVirtualDomain(serviceName)
        if (vd.error) {
            throw new VirtualDomainException( message(code:"sspb.virtualdomain.invalid.service.message", args:[serviceName]))
        }
        queryResult = virtualDomainSqlService.count(vd.virtualDomain, params)
        if (queryResult.error == "") {
            result = queryResult.totalCount
        } else {
            throw new VirtualDomainException(queryResult.error )
        }
        result
    }

    def create (Map data, params) {
        log.debug "Data for post/save/create:" + data
        def serviceName = vdName(params)
        if ('pbadmPageRoles' == serviceName) {
            Page page = Page.get(data?.PAGE_ID)
            if (!developerSecurityService.isAllowModify(page?.constantName, developerSecurityService.PAGE_IND)) {
                log.error('user not authorized to create page role grid')
                throw new AccessDeniedException("user.not.authorized.create", [PBUser.getTrimmed().loginName])
            }
        } else if ('pbadmVirtualDomainRoles' == serviceName) {
            VirtualDomain vd = VirtualDomain.get(data?.VIRTUAL_DOMAIN_ID)
            if (!developerSecurityService.isAllowModify(vd?.serviceName, developerSecurityService.VIRTUAL_DOMAIN_IND)) {
                log.error('user not authorized to create virtual domain role grid')
                throw new AccessDeniedException("user.not.authorized.create", [PBUser.getTrimmed().loginName])
            }
        }

        def vd = loadVirtualDomain(serviceName)
        if (vd.error) {
            throw new VirtualDomainException( message(code:"sspb.virtualdomain.invalid.service.message", args:[serviceName]))
        }
        virtualDomainSqlService.create(vd.virtualDomain,params,data)
        //data
        //Should really be querying the record again so any database trigger changes get reflected in client
    }

    def update (/*def id,*/ Map data, params) {
        log.debug "Data for put/update:" + data
        def serviceName = vdName(params)
        if ('pbadmPageRoles' == serviceName) {
            Page page = Page.get(data?.PAGE_ID)
            if (!developerSecurityService.isAllowModify(page?.constantName, developerSecurityService.PAGE_IND)) {
                log.error('user not authorized to create page role grid')
                throw new AccessDeniedException("user.not.authorized.create", [PBUser.getTrimmed().loginName])
            }
        } else if ('pbadmVirtualDomainRoles' == serviceName) {
            VirtualDomain vd = VirtualDomain.get(data?.VIRTUAL_DOMAIN_ID)
            if (!developerSecurityService.isAllowModify(vd?.serviceName, developerSecurityService.VIRTUAL_DOMAIN_IND)) {
                log.error('user not authorized to create virtual domain role grid')
                throw new AccessDeniedException("user.not.authorized.create", [PBUser.getTrimmed().loginName])
            }
        }
        def vd = loadVirtualDomain(serviceName)
        if (vd.error) {
            throw new VirtualDomainException( message(code:"sspb.virtualdomain.invalid.service.message", args:[serviceName]))
        }
        virtualDomainSqlService.update(vd.virtualDomain,params,data)
        //data
        //Should really be querying the record again so any database trigger changes get reflected in client
    }

    def delete (/*def id,*/ Map data,  params) {
        log.debug "Data for DELETE:" + data
        def serviceName = vdName(params)
        if ('pbadmPageRoles' == serviceName) {
            Page page = Page.get(data?.PAGE_ID)
            if (!developerSecurityService.isAllowModify(page?.constantName, developerSecurityService.PAGE_IND)) {
                log.error('user not authorized to create page role grid')
                throw new AccessDeniedException("user.not.authorized.create", [PBUser.getTrimmed().loginName])
            }
        } else if ('pbadmVirtualDomainRoles' == serviceName) {
            VirtualDomain vd = VirtualDomain.get(data?.VIRTUAL_DOMAIN_ID)
            if (!developerSecurityService.isAllowModify(vd?.serviceName, developerSecurityService.VIRTUAL_DOMAIN_IND)) {
                log.error('user not authorized to create virtual domain role grid')
                throw new AccessDeniedException("user.not.authorized.create", [PBUser.getTrimmed().loginName])
            }
        }

        def vd = loadVirtualDomain(serviceName)
        if (vd.error) {
            throw new VirtualDomainException( message(code:"sspb.virtualdomain.invalid.service.message", args:[serviceName]))
        }
        virtualDomainSqlService.delete(vd.virtualDomain,params)
    }




    // Services to retrieve and save a VirtualDomain

    def saveVirtualDomain(vdServiceName, vdQuery, vdPost, vdPut, vdDelete, vdOwner) {

        log.info "---------- Save $vdServiceName (VirtualDomainResourceService)-------------"
        def updateVD = true
        def success = false
        def error = ""
        def vd = null

        try {
            vd=VirtualDomain.findByServiceName(vdServiceName)
            if (!vd)   {
                vd = new VirtualDomain([serviceName:vdServiceName])
                updateVD = false
                vd.owner= PBUser.getTrimmed().oracleUserName
            }
            if (vd) {
                if(updateVD && vdOwner){
                    vd.owner=vdOwner
                }
                vd.codeGet=vdQuery
                vd.codePost=vdPost
                vd.codePut=vdPut
                vd.codeDelete=vdDelete
                vd = vd.save(flush:true, failOnError: true)
                if (vd)
                    success = true
            }
        } catch (HibernateException ex) {
            error = ex.getMessage()
            log.error ex
        }
        return [success:success, updated:updateVD, error:error, id:vd?.id, version:vd?.version, owner:vd?.owner]
    }

    def loadVirtualDomain(vdServiceName) {

        log.info "---------- load $vdServiceName (VirtualDomainResourceService)-------------"
        def success = false
        def error = message(code:"sspb.virtualdomain.invalid.service.message", args:[vdServiceName])
        def vd = null

        try {
            vd=VirtualDomain.findByServiceName(vdServiceName)
            if (vd) {
                success = true
                error = null
            }
        } catch (HibernateException ex) {
            error = ex.getMessage()
            log.error ex
        }
        return [success:success, virtualDomain:vd, error:error,
                allowModify: developerSecurityService.isAllowModify(vdServiceName,DeveloperSecurityService.VIRTUAL_DOMAIN_IND),
                allowUpdateOwner: developerSecurityService.isAllowUpdateOwner(vdServiceName, DeveloperSecurityService.VIRTUAL_DOMAIN_IND)]
    }
}
