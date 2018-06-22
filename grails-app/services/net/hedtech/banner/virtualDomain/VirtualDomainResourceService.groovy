/******************************************************************************
 *  Copyright 2013-2018 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import groovy.util.logging.Log4j
import net.hedtech.banner.sspb.CommonService
import org.apache.commons.codec.binary.Base64
import org.hibernate.HibernateException

@Log4j
class VirtualDomainResourceService {

    def virtualDomainSqlService

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

    def show(Map params) {
        list(params)[0]
    }

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
        def vd = loadVirtualDomain(serviceName)
        if (vd.error) {
            throw new VirtualDomainException( message(code:"sspb.virtualdomain.invalid.service.message", args:[serviceName]))
        }
        virtualDomainSqlService.delete(vd.virtualDomain,params)
    }




    // Services to retrieve and save a VirtualDomain

    def saveVirtualDomain(vdServiceName, vdQuery, vdPost, vdPut, vdDelete) {

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
            }
            if (vd) {
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
        return [success:success, updated:updateVD, error:error, id:vd?.id, version:vd?.version]
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
        return [success:success, virtualDomain:vd, error:error]
    }
}
