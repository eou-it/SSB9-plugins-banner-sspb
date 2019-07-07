/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.gorm.transactions.Transactional
import net.hedtech.banner.security.PageSecurity
import org.hibernate.criterion.CriteriaSpecification

@Transactional(readOnly = true)
class PageExportService {
    static transactional = false  //Getting error connection closed without this
    def dateConverterService

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
                    if (Page.declaredFields.find{ f -> tokens[0] == f.name }) {
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
        def pageExport
        def page
        if (params.id && params.id.matches("[0-9]+")) {
            page = Page.get(params.id )
        } else {
            page = Page.findByConstantName(params.id?:params.constantName)
        }
        if (page) {
            pageExport = new PageExport(page)
        }

        if(page && params.isAllowExportDSPermission && "Y".equalsIgnoreCase(params.isAllowExportDSPermission)){
            pageExport.owner = page.owner
            PageSecurity.fetchAllByPageId(page.id)?.each{ ps ->
                pageExport.developerSecurity << [type:ps.type, name:ps.id.developerUserId,allowModify:ps.allowModifyInd]
            }
        }

          pageExport
    }

    def list( params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        def max = Math.min( params.max ? params.max.toInteger() : 10000,  10000)
        def offset = params.offset ?: 0
        def sortBy = []
        if (params.sortby) {
            sortBy=normalizeSortBy(params.sortby)
        }
        def cr = Page.createCriteria()
        def result = cr.list (offset: offset, max: max) {
            if  (params.constantName) {
                ilike ("constantName","%${params.constantName}%" )
            }
            if (sortBy[0]) {
                sortBy.each {
                    order(it.field, it.direction)
                }
            }
            else {
                order ("constantName", "asc")
            }
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                property("id","id")
                property("constantName","constantName")
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

    def update(Map content, ignore) {
        def result
        if (content.export == 1) {
            def pageUtilService = new PageUtilService()
            pageUtilService.exportToFile(content)
            result = content
        }
        result
    }
}
