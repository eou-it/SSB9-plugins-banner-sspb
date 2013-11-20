package net.hedtech.banner.virtualDomain

class VirtualDomainExportService {
    static transactional = false //Getting error connection closed without this

    def normalizeSortBy = { sortBy ->
        def result = []
        if (sortBy) {
            def sortByList = []
            if (sortBy instanceof String)
                sortByList << sortBy
            else
                sortByList = sortBy
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

        if (params.sortby) {
            sortBy = normalizeSortBy(params.sortby)
        }
        def cr = VirtualDomain.createCriteria()
        def result = cr.list(offset: offset, max: max, paginationEnabledList: false) {
            if (params.serviceName) {
                like("serviceName", params.serviceName)
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


    def count(Map params) {
        log.trace "VirtualDomainExportService.count invoked"
        def result
        if (params.serviceName)
            result = VirtualDomain.countByServiceNameLike(params.serviceName)
        else
            result = VirtualDomain.count()
        return result
    }

    def create(Map content, params) {
        log.trace "VirtualDomainExportService.create invoked"
        def result
        if (content.exportVirtualDomain == "1") {
            def vdUtilService = new VirtualDomainUtilService()
            vdUtilService.exportToFile(content.serviceName)
            result = content
        }
        log.trace "VirtualDomainExportService.create returning $result"
        result
    }

    // handle export of single
    def update(def id, Map content, params) {
        log.trace "VirtualDomainExportService.update invoked"
        def result
        if (content.exportVirtualDomain == "1") {
            def vdUtilService = new VirtualDomainUtilService()
            vdUtilService.exportToFile(content.serviceName)
            result = content
        }
        result
    }
}
