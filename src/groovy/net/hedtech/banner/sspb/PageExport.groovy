/*******************************************************************************
 * Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb

// This class is only used for exporting to a file containing the modelView as a JSON object rather than string.
class PageExport  {

    String constantName
    String owner

    def extendsPage

    def modelView

    def pageRoles = []

    def developerSecurity = []

    Date fileTimestamp

    //Constructor
    PageExport (Page page) {
        this.constantName = page.constantName
        this.owner = page.owner
        this.modelView = page.modelMap
        this.extendsPage = page.extendsPage ? [constantName: page.extendsPage.constantName] : null
        this.fileTimestamp = new Date()
        page.pageRoles?.sort{it.roleName}.each { role ->
            this.pageRoles << [roleName: role.roleName, allow: role.allow]
        }
    }
}
