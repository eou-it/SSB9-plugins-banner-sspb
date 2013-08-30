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
            def saveResult =  virtualDomainService.saveVirtualDomain(pageInstance.vdServiceName,
                    pageInstance.vdQueryView, pageInstance.vdPostView, pageInstance.vdPutView, pageInstance.vdDeleteView)
            pageInstance.saveSuccess = saveResult.success
            pageInstance.updated = saveResult.updated
            pageInstance.error = saveResult.error
            pageInstance.id = saveResult.id
            pageInstance.version = saveResult.version
            pageInstance.submitted = true
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
        def pageInstance = params
        if (pageInstance.vdServiceName)  {
            def loadResult =  virtualDomainService.loadVirtualDomain(pageInstance.vdServiceName)
            (VirtualDomain) loadResult.virtualDomain.delete()

        }

        render (view:"virtualDomainComposer", model: [pageInstance: null])
    }


}
