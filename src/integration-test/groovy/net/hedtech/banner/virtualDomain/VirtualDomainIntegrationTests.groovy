/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import org.junit.After
import org.junit.Before
import org.junit.Test

class VirtualDomainIntegrationTests {

    def virtualDomainUtilService
    def workPath   = "target/testData/virtualDomain"
    def sourcePath = "test/testData/virtualDomain"

    @Before
    void setUp() {
        // Setup logic here
        new File(workPath).mkdirs()
        new File(workPath).eachFileMatch(~/.*.json/) { file ->
            file.delete()
        }
        new AntBuilder().copy(todir: workPath) {
            fileset(dir: sourcePath)
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
