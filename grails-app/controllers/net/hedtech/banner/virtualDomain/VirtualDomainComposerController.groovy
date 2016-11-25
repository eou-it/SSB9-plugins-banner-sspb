/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

class VirtualDomainComposerController {
    static defaultAction = "loadVirtualDomain"
    def virtualDomainService

    def composeVirtualDomain = {
        def pageInstance = params
        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance])
    }

    def saveVirtualDomain = {
        def pageInstance = params
        if (pageInstance.vdServiceName)  {
            if ( validateInput(params)) {
                def saveResult = virtualDomainService.saveVirtualDomain(pageInstance.vdServiceName,
                        pageInstance.vdQueryView, pageInstance.vdPostView, pageInstance.vdPutView, pageInstance.vdDeleteView)
                pageInstance.saveSuccess = saveResult.success
                pageInstance.updated = saveResult.updated
                pageInstance.error = saveResult.error
                pageInstance.id = saveResult.id
                pageInstance.version = saveResult.version
                pageInstance.submitted = true
            } else {
                pageInstance.error = message(code:"sspb.virtualdomain.invalid.service.message", args:[pageInstance.vdServiceName])
                render (status: 400, text:  pageInstance.error)
            }
        }
        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance])
    }

    def loadVirtualDomain = {
        def pageInstance = params
        if (pageInstance.vdServiceName)  {
            def loadResult =  virtualDomainService.loadVirtualDomain(pageInstance.vdServiceName)
            pageInstance.loadSuccess = loadResult.success
            pageInstance.error = loadResult.error
            pageInstance.loadSubmitted = true
            if (loadResult.success) {
                pageInstance.vdQueryView  = loadResult.virtualDomain.codeGet
                pageInstance.vdPostView   = loadResult.virtualDomain.codePost
                pageInstance.vdPutView   = loadResult.virtualDomain.codePut
                pageInstance.vdDeleteView = loadResult.virtualDomain.codeDelete
            }
        }

        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance])
    }

    def deleteVirtualDomain = {
        if (params.vdServiceName)  {
            VirtualDomain.withTransaction {
                def vd = VirtualDomain.find{serviceName==params.vdServiceName}
                vd.delete(failOnError:true)
            }
        }
        render (view:"virtualDomainComposer", model: [pageInstance: null])
    }

    private def validateInput(params) {
        def name = params?.vdServiceName
        def valid = (name?.size() <= 60)
        valid &= name ==~ /[a-zA-Z]+[a-zA-Z0-9_\-]*/
        valid
    }
}
