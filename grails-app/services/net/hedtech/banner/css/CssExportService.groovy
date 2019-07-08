/******************************************************************************
 *  Copyright 2018-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

package net.hedtech.banner.css

import grails.gorm.transactions.Transactional
import net.hedtech.banner.security.CssSecurity
import net.hedtech.banner.sspb.CommonService
import net.hedtech.banner.sspb.Page
import org.hibernate.criterion.CriteriaSpecification

@Transactional
class CssExportService {
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
                    if (Css.declaredFields.find{ f -> tokens[0] == f.name }) {
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

        def css
        if (params.id && params.id.matches("[0-9]+")) {
            css = Css.get(params.id )
        } else {
            css = Css.fetchByConstantName(params.id?:params.constantName)
        }
        def cssExport = [:]
        //cssExport = css.properties['constantName', 'css', 'description', 'fileTimestamp']
        cssExport.constantName = css.constantName
        cssExport.css = css.css
        cssExport.description = css.description
        cssExport.fileTimestamp = css.fileTimestamp
        cssExport.developerSecurity = []
        cssExport.owner = null
        if(css && params.isAllowExportDSPermission && "Y".equalsIgnoreCase(params.isAllowExportDSPermission)){
            cssExport.owner = css.owner
            CssSecurity.fetchAllByCssId(css.id)?.each{ cs ->
                cssExport.developerSecurity << [type:cs.type, name:cs.id.developerUserId,allowModify:cs.allowModifyInd]
            }
        }

        cssExport
    }

    def list( params) {
        Map parameter = CommonService.decodeBase64(params)
        params.putAll(parameter);
        def max = Math.min( params.max ? params.max.toInteger() : 10000,  10000)
        def offset = params.offset ?: 0
        def sortBy = []
        def cssSet

        if (params.sortby) {
            sortBy=normalizeSortBy(params.sortby)
        }
        if (params.pageLike  ) {
            cssSet = cssForPages(params.pageLike)
            if (cssSet.empty)
                cssSet << "~"
        }
        def cr = Css.createCriteria()
        def result = cr.list (offset: offset, max: max)  {
            if  (params.constantName) {
                ilike ("constantName","%${params.constantName}%" )
            }
            if (cssSet) {
                'in'("constantName",cssSet)
            }
            if (sortBy[0]) {
                sortBy.each {
                    order(it.field,it.direction)
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

    // handle export of single css file
    def update(Map content, ignore) {
        def result
        if (content.export == 1) {
            def cssUtilService = new CssUtilService()
            cssUtilService.exportToFile(content)
            result = content
        }
        result
    }

    // return a list of referenced css
    def cssForPages(pageNameLike) {
        def slurper = new groovy.json.JsonSlurper()
        Set cssSet = new HashSet()
        def pages = Page.findAllByConstantNameIlike("%${pageNameLike}%")
        pages.each{ p ->
            def jsonPage = p.getMergedModelMap(false)
            def css = jsonPage.importCSS
            if (css) {
                def list = css?.tokenize(',')
                list?.each {
                    cssSet << it
                }
            }
        }
        cssSet
    }


}
