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

    //CompileServiceTests + saving the page

    /* TODO Fix importAllFromFile
    @Test

    void testImport() {
        def pageUtilService = new PageUtilService()
        def pagePath = "test/testData/model"
        pageUtilService.importAllFromFile(pagePath)
        pageUtilService.compileAll()
    }
    */

    @Test
    void testExport() {
        def pageUtilService = new PageUtilService()
        def pagePath = "test/testData/model"
        pageUtilService.exportAllToFile(pagePath)
    }

    //CompileServiceTests + saving the page
    @Test
    void testCompileAndCreate() {
        // test all page model compilation
        def maxModelId = 6
        def modelPath = "test/testData/model/PageModel"
        def getModelPath = { id -> return modelPath + id + '.json' }

        def pagePath = "target/compiledPage/page"
        def getPagePath = { id -> return pagePath + id + '.html' }

        for (i in (1..maxModelId)) {
            def modelFilePath = getModelPath(i)
            def pageFilePath = getPagePath(i)

            println "testing $modelFilePath}"

            def pageSource = new File(modelFilePath).getText()

            // parse, normalize and validate page model
            def validateResult = CompileService.preparePage(pageSource)
            if (validateResult.valid) {
                // generate JS first because it will set the binding correctly for some components
                def compiledJSCode = CompileService.compileController(validateResult.pageComponent)
                //println "Compiled Controller = $compiledJSCode\n"
                println "JavaScript is compiled"
                def htmlOutput = new File('TestJS.js')
                htmlOutput.text = compiledJSCode

                def compiledView = CompileService.compile2page(validateResult.pageComponent)
                //println "Compiled View = $compiledView\n"
                println "HTML is compiled"
                htmlOutput = new File('TestHtml.html')
                htmlOutput.text = compiledView

                def combinedView = CompileService.assembleFinalPage(compiledView, compiledJSCode)
                //println "Compbined View = $combinedView\n"
                println "Page is compiled"
                htmlOutput = new File(pageFilePath)
                htmlOutput.text = combinedView

                //save model in db
                def pageName = "testPage$i"

                def page = Page.findByConstantName(pageName)

                if (!page)  {
                    page = new Page([constantName: pageName])
                    println "Creating new Page $pageName"
                }

                if (page) {
                    page.modelView = pageSource
                    page.compiledView = compiledView
                    page.compiledController = compiledJSCode
                    page = page.save(flush: true)
                }
                page = Page.get(page.id)
                if (page) {
                    println "Saved $pageName"
                } else {
                    println "failed to save Page in database"
                    assert (false)
                }

            } else {
                println "modelFilePath validation error:\n" + validateResult.errors.join('\n')
                assert (false)
            }
        }
        assert (true)

    }

}
