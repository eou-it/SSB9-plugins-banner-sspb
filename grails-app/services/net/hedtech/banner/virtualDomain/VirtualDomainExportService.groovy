/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

package net.hedtech.banner.virtualDomain

import net.hedtech.banner.sspb.Page
import net.hedtech.banner.sspb.PageComponent
import org.hibernate.criterion.CriteriaSpecification

class VirtualDomainExportService {
    static transactional = false //Getting error connection closed without this

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
        vdExport
    }

    def list(params) {
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
                like("serviceName", params.serviceName)
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
            }
        }
        result
    }

    // handle export of single vd
    def update(Map content, ignore) {
        def result
        if (content.export == 1) {
            def vdUtilService = new VirtualDomainUtilService()
            vdUtilService.exportToFile(content.serviceName)
            result = content
        }
        result
    }

    // return a list of referenced virtual domains
    def vdForPages(pageNameLike) {
        Set vdSet = new HashSet()
        def pages = Page.findAllByConstantNameLike(pageNameLike)
        //this is gonna be pretty expensive probably
        pages.each{ p ->
            def jsonPage =  p.getMergedModelMap(false)
            def pComponent = PageComponent.parseJSON(jsonPage)
            def vds = pComponent.findComponents([PageComponent.COMP_TYPE_RESOURCE])
            vds.each { res ->
                if ( res.resource?.startsWith(VirtualDomainService.vdPrefix)) {
                    vdSet << res.resource.substring(VirtualDomainService.vdPrefix.length())
                }
            }
        }
        vdSet
    }
}
