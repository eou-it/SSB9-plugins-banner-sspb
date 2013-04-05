package net.hedtech.banner.virtualDomain

class VirtualDomainComposerController {

    def composeVirtualDomain = {
        def pageInstance = new VirtualDomainPage(params)

        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance])

    }

    def saveVirtualDomain = {
        def pageInstance = new VirtualDomainPage(params)
        if (pageInstance.vdName)  {
            def saveResult =  virtualDomainService.saveVirtualDomain(pageInstance.vdName,
                    pageInstance.vdQueryView, pageInstance.vdSaveView, pageInstance.vdDeleteView)
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
        def pageInstance = new VirtualDomainPage(params)
        if (pageInstance.loadVdName)  {
            def loadResult =  virtualDomainService.loadVirtualDomain(pageInstance.loadVdName)
            pageInstance.loadSuccess = loadResult.success
            pageInstance.error = loadResult.error
            pageInstance.loadSubmitted = true
            if (loadResult.success) {
                pageInstance.vdQueryView = loadResult.vd.codeGet
                pageInstance.vdSaveView = loadResult.vd.codePost
                pageInstance.vdDeleteView = loadResult.vd.codeDelete
            }
        }

        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance])
    }

}
