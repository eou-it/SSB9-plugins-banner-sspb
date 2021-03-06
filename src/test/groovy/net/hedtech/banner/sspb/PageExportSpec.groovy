/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class PageExportSpec extends Specification implements DomainUnitTest<Page> {

    @Unroll
    def "Test Constructor PageExport"() {
        given:
        def page = new Page (constantName: "test", extendsPage: null, modelView: "{}", fileTimestamp: new Date(),
                             pageRoles: [[ "allow": true, "roleName": "WTAILORADMIN" ]] )

        def pageExport = new PageExport(page)
        expect:
        pageExport != null
        pageExport.constantName == page.constantName
        pageExport.extendsPage.equals(null)
        pageExport.modelView == [:]
        pageExport.fileTimestamp != page.fileTimestamp
        pageExport.pageRoles.size() == 1
    }
}
