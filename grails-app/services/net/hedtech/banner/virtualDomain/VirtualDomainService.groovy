/******************************************************************************
 *  Copyright 2016 Ellucian Company L.P. and its affiliates.                  *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.transaction.Transactional

@Transactional
class VirtualDomainService {

    def list(Map params) {
        log.trace "VirtualDomainService.list invoked with params $params"
        def result
        def max = Math.min( params.max ? params.max.toInteger() : 10000,  10000)
        def offset = params.offset ?: 0
        def qp= [offset: offset, max: max, sort: 'serviceName']
        if  (params.serviceName) {
            result = VirtualDomain.findAllByServiceNameLike(params.serviceName, qp)
        } else {
            result = VirtualDomain.list( qp )
        }
        def listResult = []
        result.each {
            listResult << [serviceName : it.serviceName, id: it.id, version: it.version]
        }
        log.trace "VirtualDomainService.list is returning a ${result.getClass().simpleName} containing ${result.size()} style sheets"
        listResult
    }


    def count(Map params) {
        log.trace "PageService.count invoked"
        if (params.constantName) {
            VirtualDomain.countByServiceNameLike(params.serviceName)
        } else {
            VirtualDomain.count()
        }
    }

    def show(Map params) {
        log.trace "VirtualDomainService.show invoked"
        def result
        result = VirtualDomain.findByServiceName(params.id)
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
        result.codeGet = content.codeGet
        result.codePost = content.codePost
        result.codePut = content.codePut
        result.codeDelete = content.codeDelete
        result.save(flush:true, failOnError: true)
    }

    private def validateInput(content) {
        def name = content?.serviceName
        def valid = (name?.size() <= 60)
        valid &= name ==~ /[a-zA-Z]+[a-zA-Z0-9_\-]*/
        valid
    }
}
