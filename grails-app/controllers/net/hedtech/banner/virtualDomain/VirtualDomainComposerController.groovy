/*******************************************************************************
 * Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import net.hedtech.banner.security.DeveloperSecurityService

import javax.servlet.http.HttpSession

class VirtualDomainComposerController {
    static defaultAction = "loadVirtualDomain"
    def virtualDomainResourceService
    def developerSecurityService

    def composeVirtualDomain = {
        def pageInstance = filter(params)
        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance])
    }

    def saveVirtualDomain = {
        def pageInstance = filter(params)
        if (params.vdServiceName)  {
            if ( validateInput(params)) {
                def saveResult = virtualDomainResourceService.saveVirtualDomain(params.vdServiceName,
                        params.vdQueryView, params.vdPostView, params.vdPutView, params.vdDeleteView, params.owner)
                pageInstance.saveSuccess = saveResult.success
                pageInstance.updated = saveResult.updated
                pageInstance.error = saveResult.error
                pageInstance.id = saveResult.id
                pageInstance.version = saveResult.version
                pageInstance.owner=saveResult.owner
                pageInstance.submitted = true
                pageInstance.allowModify = developerSecurityService.isAllowModify(params.vdServiceName,DeveloperSecurityService.VIRTUAL_DOMAIN_IND )
                pageInstance.allowUpdateOwner = developerSecurityService.isAllowUpdateOwner(params.vdServiceName,DeveloperSecurityService.VIRTUAL_DOMAIN_IND )
            } else {
                pageInstance.error = message(code:"sspb.virtualdomain.invalid.service.message", args:[pageInstance.vdServiceName])
                render (status: 400, text:  pageInstance.error)
            }
        }
        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance,isProductionReadOnlyMode : developerSecurityService.isProductionReadOnlyMode()])
    }

    def loadVirtualDomain = {
        def pageInstance = filter(params)
        if (pageInstance.vdServiceName) {
           if (validateInput(params)) {
               def loadResult = virtualDomainResourceService.loadVirtualDomain(pageInstance.vdServiceName)
               HttpSession session = request.getSession()
               session.setAttribute("pageId", loadResult.virtualDomain.id.toString())
               pageInstance.loadSuccess = loadResult.success
               pageInstance.error = loadResult.error
               pageInstance.loadSubmitted = true
               if (loadResult.success) {
                   pageInstance.vdQueryView = loadResult.virtualDomain.codeGet
                   pageInstance.vdPostView = loadResult.virtualDomain.codePost
                   pageInstance.vdPutView = loadResult.virtualDomain.codePut
                   pageInstance.vdDeleteView = loadResult.virtualDomain.codeDelete
                   pageInstance.id = loadResult.virtualDomain.id
                   pageInstance.owner = loadResult.virtualDomain.owner
                   pageInstance.allowModify = loadResult.allowModify
                   pageInstance.allowUpdateOwner = loadResult.allowUpdateOwner
               }
           } else {
               pageInstance.error = message(code:"sspb.virtualdomain.invalid.service.message", args:[pageInstance.vdServiceName])
               render (status: 400, text:  pageInstance.error)
           }
        }
        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance,isProductionReadOnlyMode : developerSecurityService.isProductionReadOnlyMode()])
    }

    def deleteVirtualDomain = {
        if (params.vdServiceName)  {
            VirtualDomain.withTransaction {
                def vd = VirtualDomain.find{serviceName==params.vdServiceName}
                vd.delete(failOnError:true)
            }
        }
        render (view:"virtualDomainComposer", model: [pageInstance: null, isProductionReadOnlyMode : developerSecurityService.isProductionReadOnlyMode()])
    }


    private def filter(params) {
        def vo = [:]
        vo.vdServiceName = params.vdServiceName
        vo.vdQueryView   = params.vdQueryView
        vo.vdPostView    = params.vdPostView
        vo.vdPutView     = params.vdPutView
        vo.vdDeleteView  = params.vdDeleteView
        vo.id            = params.id
        vo.owner         = params.owner
        vo
    }

    private def validateInput(params) {
        def name = params?.vdServiceName
        def valid = (name?.size() <= 60)
        valid &= name ==~ /[a-zA-Z]+[a-zA-Z0-9_\-]*/
        valid
    }
}
