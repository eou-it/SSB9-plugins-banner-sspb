package net.hedtech.banner.virtualDomain

import static org.junit.Assert.*
import org.junit.*
import grails.converters.JSON

class VirtualDomainIntegrationTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }
    @Test
    void testImport() {
        def vdUtilService = new VirtualDomainUtilService()
        def vdPath = "test/testData/virtualDomain"
        vdUtilService.importAllNewFromDir(vdPath)
    }

    @Test
    void testExport() {
        def vdUtilService = new VirtualDomainUtilService()
        def vdPath = "test/testData/virtualDomain"
        vdUtilService.exportAllToFile(vdPath)
    }

}
