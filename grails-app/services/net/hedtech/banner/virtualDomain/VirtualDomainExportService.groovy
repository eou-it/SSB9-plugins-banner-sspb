package net.hedtech.banner.virtualDomain

import net.hedtech.banner.sspb.Page
import net.hedtech.banner.sspb.PageComponent

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
                    result += [field: tokens[0], direction: tokens[1]]
                }
            }
        }
        result
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
        def result = cr.list(offset: offset, max: max, paginationEnabledList: false) {
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
        }
        def listResult = []
        result.each {
            // trim the object since we only need to return the serviceName properties for listing
            listResult << [serviceName: it.serviceName, id: it.id, lastUpdated: it.lastUpdated,
                    fileTimestamp: it.fileTimestamp, version: it.version]
        }

        listResult
    }

    //Todo: add pageLike criteria
    def count(Map params) {
        def result
        if (params.serviceName) {
            result = VirtualDomain.countByServiceNameLike(params.serviceName)
        } else {
            result = VirtualDomain.count()
        }
        return result
    }

    def create(Map content, ignore) {
        def result
        if (content.exportVirtualDomain == "1") {
            def vdUtilService = new VirtualDomainUtilService()
            vdUtilService.exportToFile(content.serviceName, content.pageLike)
            result = content
        }
        result
    }

    // handle export of single vd
    def update(/*def id,*/ Map content, ignore) {
        def result
        if (content.exportVirtualDomain == "1") {
            def vdUtilService = new VirtualDomainUtilService()
            vdUtilService.exportToFile(content.serviceName)
            result = content
        }
        result
    }

    // return a list of referenced virtual domains
    def vdForPages(pageNameLike) {
        def slurper = new groovy.json.JsonSlurper()
        Set vdSet = new HashSet()
        def pages = Page.findAllByConstantNameLike(pageNameLike)
        //this is gonna be pretty expensive probably
        pages.each{ p ->
            def jsonPage = slurper.parseText(p.modelView)
            def pComponent = PageComponent.parseJSON(jsonPage)
            def vds = pComponent.findComponents([PageComponent.COMP_TYPE_RESOURCE])
            //println pComponent.showHierarchy()

            vds.each { res ->
                if ( res.resource?.startsWith(VirtualDomainService.vdPrefix)) {
                    vdSet << res.resource.substring(VirtualDomainService.vdPrefix.length())
                }
            }
        }
        vdSet
    }
}
