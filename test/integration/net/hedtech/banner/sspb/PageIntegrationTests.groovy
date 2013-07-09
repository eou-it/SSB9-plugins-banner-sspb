package net.hedtech.banner.sspb

import org.junit.*

class PageIntegrationTests {

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
        def pageUtilService = new PageUtilService()
        def pagePath = "test/testData/model"
        pageUtilService.importAllFromFile(pagePath)
        def errors = pageUtilService.compileAll()
        assert errors.empty
    }

    @Test
    void testExport() {
        def pageUtilService = new PageUtilService()
        def pagePath = "test/testData/model"
        pageUtilService.exportAllToFile(pagePath)
    }

    @Test
    void testCompile() {
        def pageUtilService = new PageUtilService()
        def errors = pageUtilService.compileAll()
        assert errors.empty
    }


}
