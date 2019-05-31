/*******************************************************************************
 * Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import groovy.util.logging.Log4j
import net.hedtech.banner.security.DeveloperSecurityService
import net.hedtech.banner.sspb.PBUser

import javax.servlet.http.HttpSession

@Log4j
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
        if (!developerSecurityService.isAllowModify(pageInstance?.vdServiceName, developerSecurityService.VIRTUAL_DOMAIN_IND)) {
            log.error('user not authorized to save virtual domain')
            pageInstance.error = message(code:"user.not.authorized.create", args:[PBUser.getTrimmed().loginName])
            render (status: 403, text:  pageInstance.error)
        }

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
        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance,isProductionReadOnlyMode : developerSecurityService.isProductionReadOnlyMode(),
                                                      userDetailsInList : virtualDomainResourceService.list(['pluralizedResourceName':'virtualDomains.pbadmUserDetails',
                                                                                                             'encoded':true, 'controller':'CustomPage' , 'action':'get'])])
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
        render (view:"virtualDomainComposer", model: [pageInstance: pageInstance,
                                                      isProductionReadOnlyMode : developerSecurityService.isProductionReadOnlyMode(),
                                                      userDetailsInList : virtualDomainResourceService.list(['pluralizedResourceName':'virtualDomains.pbadmUserDetails',
                                                                                                             'encoded':true, 'controller':'CustomPage' , 'action':'get'])])
    }

    def deleteVirtualDomain = {
        if (!developerSecurityService.isAllowModify(params.vdServiceName, developerSecurityService.VIRTUAL_DOMAIN_IND)) {
            log.error('user not authorized to delete virtual domain')
            render (status: 403, text:  message(code:"user.not.authorized.delete", args:[PBUser.getTrimmed().loginName]))
        }else if (params.vdServiceName)  {
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
