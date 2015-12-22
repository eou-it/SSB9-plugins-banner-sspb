package net.hedtech.banner.sspb

class PageExportService {
    static transactional = false  //Getting error connection closed without this

    def normalizeSortBy = {sortBy ->
        def result=[]
        if (sortBy ) {
            def sortByList=[]
            if (sortBy instanceof String) {
                sortByList << sortBy
            } else {
                sortByList = sortBy
            }
            for (s in sortByList) {
                //replace operator and special characters to avoid sql injection
                s=(String) s.tr(" ',.<>?;:|+=!/&*(){}[]`~@#\$%\"^-", " ")
                if (s) {
                    def tokens=s.split() //split sortby on whitespace
                    result +=[field: tokens[0], direction: tokens[1] ]
                }
            }
        }
        result
    }

    def list( params) {
        def max = Math.min( params.max ? params.max.toInteger() : 10000,  10000)
        def offset = params.offset ?: 0
        def sortBy = []
        if (params.sortby) {
            sortBy=normalizeSortBy(params.sortby)
        }
        def cr = Page.createCriteria()
        def result = cr.list (offset: offset, max: max) {
            if  (params.constantName) {
                like ("constantName",params.constantName )
            }
            if (sortBy[0]) {
                sortBy.each {
                    order(it.field,it.direction)
                }
            }
            else {
                order ("constantName", "asc")
            }
            /* want to reduce the data in result set but doesn't seem to work like this
            projections {
                property('id', 'id')
                property('lastUpdated', 'lastUpdated')
                property('constantName', 'constantName')
            }
            */
        }
        def listResult = []
        result.each {
            // trim the object since we only need to return the constantName properties for listing
            listResult << [constantName : it.constantName, id: it.id, lastUpdated: it.lastUpdated,
                           fileTimestamp: it.fileTimestamp, version: it.version]
        }
        listResult
    }

    def count(Map params) {
        def result
        if (params.constantName) {
            result = Page.countByConstantNameLike(params.constantName)
        } else {
            result = Page.count()
        }
        return result
    }

    def create(Map content, ignore) {
        def result
        if (content.exportPage == "1") {
            def pageUtilService = new PageUtilService()
            pageUtilService.exportToFile(content.constantName)
            result = content
        }
        result
    }

    def update(/*def id,*/ Map content, ignore) {
        def result
        if (content.exportPage == "1") {
            def pageUtilService = new PageUtilService()
            pageUtilService.exportToFile(content.constantName)
            result = content
        }
        result
    }
}
