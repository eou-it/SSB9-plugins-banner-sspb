package net.hedtech.banner.virtualDomain

import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib

class VirtualDomainService {
    def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }
    def virtualDomainSqlService

    private String vdPrefix = "virtualDomains."   // Todo: might want to get rid of plural or choose other prefix

    private def vdName (params) {
        String resourceName=params.pluralizedResourceName
        if ( resourceName.startsWith(vdPrefix)) {
            resourceName = resourceName.substring(vdPrefix.length())
        } else {
            resourceName = null
        }
    }

    // Interface for restful API - TODO may choose to put this in a separate service or move to VirtualDomainSqlService
    // if the service name can be configured, which fails now.
    def list(Map params) {
        def queryResult
        def result
        def vd = loadVirtualDomain(vdName(params))
        if (vd.error) {
            throw new Exception( localizer(code:"sspb.virtualdomain.invalid.service.message") )//TODO  how should we handle with restful API?, i18n
        }
        queryResult = virtualDomainSqlService.get(vd.virtualDomain, params)
        if (queryResult.error == "") {
            result = queryResult.rows
        } else {
            throw new Exception(queryResult.error )
        }
        result
    }

    def show(Map params) {
        def queryResult
        def result
        def vd = loadVirtualDomain(vdName(params))
        if (vd.error) {
            throw new Exception( localizer(code:"sspb.virtualdomain.invalid.service.message"))//TODO  how should we handle with restful API?, i18n
        }
        queryResult = virtualDomainSqlService.get(vd.virtualDomain, params)
        if (queryResult.error == "") {
            result = queryResult.rows
        } else {
            throw queryResult.error
        }
        result[0]
    }

    def count(Map params) {
        def queryResult
        def result
        def vd = loadVirtualDomain(vdName(params))
        if (vd.error) {
            throw new Exception( localizer(code:"sspb.virtualdomain.invalid.service.message") )//TODO  how should we handle errors with restful API?, i18n
        }
        queryResult = virtualDomainSqlService.count(vd.virtualDomain, params)
        if (queryResult.error == "") {
            result = queryResult.totalCount
        } else {
            throw new Exception(queryResult.error )
        }
        result
    }

    //TODO
    //missing methods show

    def create (Map data, params) {
        println "Data for post/save/create:" + data
        def vd = loadVirtualDomain(vdName(params))
        if (vd.error) {
            throw new Exception( localizer(code:"sspb.virtualdomain.invalid.service.message"))
        }
        virtualDomainSqlService.create(vd.virtualDomain,params,data)
        //data
        //Should really be querying the record again so any database trigger changes get reflected in client
    }

    def update (def id, Map data, params) {
        println "Data for put/update:" + data
        def vd = loadVirtualDomain(vdName(params))
        if (vd.error) {
            throw new Exception( localizer(code:"sspb.virtualdomain.invalid.service.message"))
        }
        virtualDomainSqlService.update(vd.virtualDomain,params,data)
        //data
        //Should really be querying the record again so any database trigger changes get reflected in client
    }

    def delete (def id, Map data,  params) {
        println "Data for DELETE:" + data
        def vd = loadVirtualDomain(vdName(params))
        if (vd.error) {
            throw new Exception( localizer(code:"sspb.virtualdomain.invalid.service.message"))
        }
        virtualDomainSqlService.delete(vd.virtualDomain,params)
    }




    // Services to retrieve and save a VirtualDomain

    def saveVirtualDomain(vdServiceName, vdQuery, vdPost, vdPut, vdDelete) {

        println "---------- Save $vdServiceName -------------"
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
        } catch (Exception ex) {
            error = ex.getMessage()
        }
        return [success:success, updated:updateVD, error:error, id:vd?.id, version:vd?.version]
    }

    def loadVirtualDomain(vdServiceName) {

        println "---------- load $vdServiceName -------------"
        def success = false
        def error = "$vdServiceName not found"
        def vd = null

        try {
            vd=VirtualDomain.findByServiceName(vdServiceName)
            if (vd) {
                success = true
                error = null
            }
        } catch (Exception ex) {
            error = ex.getMessage()
        }
        return [success:success, virtualDomain:vd, error:error]
    }
}
