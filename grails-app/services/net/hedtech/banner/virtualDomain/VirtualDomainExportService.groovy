/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

package net.hedtech.banner.virtualDomain

import net.hedtech.banner.security.VirtualDomainSecurity
import net.hedtech.banner.sspb.CommonService
import net.hedtech.banner.sspb.Page
import net.hedtech.banner.sspb.PageComponent
import org.hibernate.criterion.CriteriaSpecification

class VirtualDomainExportService {
    static transactional = false //Getting error connection closed without this
    def dateConverterService
    def normalizeSortBy = { sortBy ->
        def result = []
        if (sortBy) {
            def sortByList = []
            if (sortBy instanceof String) {
                sortByList << sortBy
            } else {
                sortByList = sortBy
            }
            for (s in sortByList) {
                //replace operator and special characters to avoid sql injection
                s = (String) s.tr(" ',.<>?;:|+=!/&*(){}[]`~@#\$%\"^-", " ")
                if (s) {
                    def tokens = s.split() //split sortby on whitespace
                    if (VirtualDomain.declaredFields.find{ f -> tokens[0] == f.name }) {
                        result += [field: tokens[0], direction: tokens[1]]
                    }
                }
            }
        }
        result
    }

    def show(params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        if(params.id && params.id.contains('^')){
            params.isAllowExportDSPermission = params.id?.substring(params.id?.length()-1,params.id?.length())
            params.id = params.id?.substring(0,params.id?.length()-2)
        }

        def vd
        if (params.id && params.id.matches("[0-9]+")) {
            vd = VirtualDomain.get(params.id )
        } else {
            vd = VirtualDomain.findByServiceName(params.id?:params.constantName)
        }

        def vdExport = [:]
        def vdRoles = []
        vd.virtualDomainRoles.each{
            vdRoles << it.properties["allowDelete", "allowGet", "allowPost", "allowPut","roleName"]
        }
        vdExport = vd.properties['serviceName', 'typeOfCode', 'dataSource',
                'codeGet', 'codePost', 'codePut', 'codeDelete', 'fileTimestamp']
        vdExport.virtualDomainRoles = vdRoles
        vdExport.developerSecurity = []
        vdExport.owner = null
        if(vd && params.isAllowExportDSPermission && "Y".equalsIgnoreCase(params.isAllowExportDSPermission)){
            vdExport.owner = vd.owner
            VirtualDomainSecurity.fetchAllByVirtualDomainId(vd.id)?.each{ vs ->
                vdExport.developerSecurity << [type:vs.type, name:vs.id.developerUserId,allowModify:vs.allowModifyInd]
            }
        }

        vdExport
    }

    def list(params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        def max = Math.min(params.max ? params.max.toInteger() : 10000, 10000)
        def offset = params.offset ?: 0
        def sortBy = []
        def vdSet

        if (params.sortby) {
            sortBy = normalizeSortBy(params.sortby)
        }
        if (params.pageLike  ) {
            vdSet = vdForPages(params.pageLike)
            if (vdSet.empty)
                vdSet << "~"
        }
        def cr = VirtualDomain.createCriteria()
        def result = cr.list(offset: offset, max: max, paginationEnabledList: true) {
            if (params.serviceName) {
                ilike("serviceName", "%${params.serviceName}%")
            }
            if (vdSet) {
                'in'("serviceName",vdSet)
            }
            if (sortBy[0]) {
                sortBy.each {
                    order(it.field, it.direction)
                }
            } else {
                order("serviceName", "asc")
            }
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                property("id","id")
                property("serviceName","serviceName")
                property("lastUpdated","lastUpdated")
                property("fileTimestamp","fileTimestamp")
                property("version","version")
                property("owner","owner")
            }
        }
        result?.each{
            it.lastUpdated = it.lastUpdated? dateConverterService.parseGregorianToDefaultCalendar(it.lastUpdated) : ''
            it.fileTimestamp = it.fileTimestamp? dateConverterService.parseGregorianToDefaultCalendar(it.fileTimestamp) : ''
            it.isAllowExportDSPermission = 'N'
        }

        result
    }

    // handle export of single vd
    def update(Map content, ignore) {
        def result
        if (content.export == 1) {
            def vdUtilService = new VirtualDomainUtilService()
            vdUtilService.exportToFile(content)
            result = content
        }
        result
    }

    // return a list of referenced virtual domains
    def vdForPages(pageNameLike) {
        Set vdSet = new HashSet()
        def pages = Page.findAllByConstantNameIlike("%${pageNameLike}%")
        //this is gonna be pretty expensive probably
        pages.each{ p ->
            def jsonPage =  p.getMergedModelMap(false)
            def pComponent = PageComponent.parseJSON(jsonPage)
            def vds = pComponent.findComponents([PageComponent.COMP_TYPE_RESOURCE])
            vds.each { res ->
                if ( res.resource?.startsWith(VirtualDomainResourceService.vdPrefix)) {
                    vdSet << res.resource.substring(VirtualDomainResourceService.vdPrefix.length())
                }
            }
        }
        vdSet
    }
}
