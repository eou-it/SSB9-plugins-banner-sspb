/******************************************************************************
 *  Copyright 2016 Ellucian Company L.P. and its affiliates.                  *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import org.hibernate.criterion.CriteriaSpecification

class VirtualDomainService {

    static transactional = false //Getting error connection closed without this

    def list(Map params) {
        log.trace "VirtualDomainService.list invoked with params $params"
        def max = Math.min( params.max ? params.max.toInteger() : 10000,  10000)
        def offset = params.offset ?: 0
        def cr = VirtualDomain.createCriteria()
        def result = cr.list(offset: offset, max: max, paginationEnabledList: true) {
            if (params.serviceName) {
                like("serviceName", params.serviceName)
            }
            order("serviceName", "asc")
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
        log.trace "VirtualDomainService.list is returning a ${result.getClass().simpleName} containing ${result.size()} rows"
        result
    }

    def show(Map params) {
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
}
