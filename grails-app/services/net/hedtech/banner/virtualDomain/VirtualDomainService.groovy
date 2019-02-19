/******************************************************************************
 *  Copyright 2016-2019 Ellucian Company L.P. and its affiliates.                  *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import net.hedtech.banner.sspb.CommonService
import org.hibernate.criterion.CriteriaSpecification
import org.springframework.context.i18n.LocaleContextHolder

class VirtualDomainService {

    static transactional = false //Getting error connection closed without this

    def list(Map params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        log.trace "VirtualDomainService.list invoked with params $params"
        params = extractReqPrams(params)
        def max = Math.min( params.max ? params.max.toInteger() : 10000,  10000)
        def offset = params.offset ?: 0
        def cr = VirtualDomain.createCriteria()
        def result = cr.list(offset: offset, max: max, paginationEnabledList: true) {
            if (params.serviceName) {
                ilike("serviceName", params.serviceName)
            }
            order(params.sort ?: 'serviceName', params.order ?: 'asc')
            if (params.noData=="TRUE") {
                resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
                projections {
                    property("id", "id")
                    property("serviceName", "serviceName")
                    property("lastUpdated", "lastUpdated")
                    property("fileTimestamp", "fileTimestamp")
                    property("version", "version")
                }
            }
        }

        if(params.containsKey('getGridData')){
            def listResult = []
            Locale locale = LocaleContextHolder.getLocale()
            String date_format = "dd/MM/yyyy"
            if(locale && ['ar','fr_CA'].contains(locale.toString())){
                date_format = "yyyy/MM/dd"
            }

            result.each {
                listResult << [serviceName : it.serviceName, id: it.id, version: it.version, dateCreated:it.dateCreated?.format(date_format), lastUpdated:it.lastUpdated?.format(date_format)]
            }
            log.trace "VirtualDomainService.list is returning a ${listResult.getClass().simpleName} containing ${listResult.size()} rows"

            return listResult
        }

        log.trace "VirtualDomainService.list is returning a ${result.getClass().simpleName} containing ${result.size()} rows"
        result
    }

    def count(Map params) {
        log.trace "PageService.count invoked"
        params = extractReqPrams(params)
        if (params.serviceName) {
            VirtualDomain.countByServiceNameIlike(params.serviceName)
        } else {
            VirtualDomain.count()
        }
    }

    def show(Map params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        if(params && params.containsKey('getGridData')) {
            def returnMap = [
                    result: list(params),
                    length: count(params)
            ]

            return returnMap

        }
        log.trace "VirtualDomainService.show invoked"
        def result
        if (params.serviceName) {
            result = VirtualDomain.findByServiceName(params.serviceName)
        } else if (validateInput([serviceName: params.id])) {
            result = VirtualDomain.findByServiceName(params.id)
        } else {
            result = VirtualDomain.get(params.id)
        }
        if (result) {
            log.trace "VirtualDomainService.show returning Service: ${result.serviceName}, id: ${result.id}"
        }
        result
    }

    def create(Map content, ignore) {
        log.trace "VirtualDomainService.create invoked"
        if (!validateInput(content)) {
            throw new RuntimeException(message(code:"sspb.virtualdomain.invalid.service.message", args:[content.serviceName]))
        }
        def result = new VirtualDomain(content)
        result.save(flush:true, failOnError: true)
    }

    def update(Map content, params) {
        log.trace "VirtualDomainService.update invoked"
        if (!validateInput(content)) {
            throw new RuntimeException(message(code:"sspb.virtualdomain.invalid.service.message", args:[content.serviceName]))
        }
        def result = VirtualDomain.get(params.id?:content.id)
        result.serviceName = content.serviceName
        result.codeGet = content.codeGet
        result.codePost = content.codePost
        result.codePut = content.codePut
        result.codeDelete = content.codeDelete
        result.save(flush:true, failOnError: true)
    }

    def delete(Map content, params) {
        log.trace "VirtualDomainService.delete invoked"
        def result = VirtualDomain.get(params.id?:content.id)
        result.delete(flush:true, failOnError: true)
    }

    private def validateInput(content) {
        def name = content?.serviceName
        def valid = (name?.size() <= 60)
        valid &= name ==~ /[a-zA-Z]+[a-zA-Z0-9_\-]*/
        valid
    }

    def extractReqPrams(Map reqParams) {
        Map params = [:]
        if(reqParams && reqParams.searchString){
            params.serviceName = "%$reqParams.searchString%"
        }

        if(reqParams && reqParams.sortColumnName){
            params.sort = reqParams.sortColumnName
        }

        if(reqParams && 'true'.equalsIgnoreCase(reqParams.ascending)){
            params.order = 'asc'
        }

        if(reqParams && 'false'.equalsIgnoreCase(reqParams.ascending)){
            params.order = 'desc'
        }

        params << reqParams
    }
}
