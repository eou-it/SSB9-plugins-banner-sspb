package net.hedtech.banner.sspb

import org.junit.*

class PageIntegrationTests {

    def pageUtilService
    def workPath   = "target/testData/model"
    def sourcePath = "test/testData/model"

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
        //pageUtilService.exportAllToFile(pagePath)
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testImport() {
        pageUtilService.importAllFromDir(workPath)
    }

    @Test
    void testExport() {
        pageUtilService.exportAllToFile(workPath)
    }

    @Test
    void testCompile() {
        def errors = pageUtilService.compileAll()
        assert errors.empty
    }

}
