/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class VirtualDomainIntegrationTests extends BaseIntegrationTestCase {

    def virtualDomainUtilService
    def workPath   = "build/target/testData/virtualDomain"
    def sourcePath = "/testData/virtualDomain"

    @Before
    void setUp() {
        virtualDomainUtilService.developerSecurityService.metaClass.isAllowModify = {String a, String b-> return true}
        // Setup logic here
        new File(workPath).mkdirs()
        new File(workPath).eachFileMatch(~/.*.json/) { file ->
            file.delete()
        }
        new AntBuilder().copy(todir: workPath) {
            fileset(dir: this.class.getResource(sourcePath).getPath())
        }
    }

    @After
    void tearDown() {
        // Tear down logic here
    }
    @Test
    void testImport() {
        virtualDomainUtilService.importAllFromDir(workPath)
    }

    @Test
    void testExport() {
        virtualDomainUtilService.exportAllToFile(workPath)
    }

}
