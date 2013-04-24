package net.hedtech.banner.sspb

/**
 * Created by IntelliJ IDEA.
 * User: jzhong
 * Date: 4/1/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
class PageModelValidatorTests extends GroovyTestCase  {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }


    void testCompileAll() {
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
            def validateResult =  CompileService.preparePage(pageSource)
            if (validateResult.valid) {
                // generate JS first because it will set the binding correctly for some components
                def compiledJSCode=CompileService.compileController(validateResult.pageComponent)
                //println "Compiled Controller = $compiledJSCode\n"
                println "JavaScript is compiled"
                def htmlOutput= new File('TestJS.js')
                htmlOutput.text = compiledJSCode

                def compiledView = CompileService.compile2page(validateResult.pageComponent)
                //println "Compiled View = $compiledView\n"
                println "HTML is compiled"
                htmlOutput= new File('TestHtml.html')
                htmlOutput.text = compiledView

                def combinedView = CompileService.assembleFinalPage(compiledView, compiledJSCode)
                //println "Compbined View = $combinedView\n"
                println "Page is compiled"
                htmlOutput= new File(pageFilePath)
                htmlOutput.text = combinedView

            } else {
                println "modelFilePath validation error:\n" + validateResult.errors.join('\n')
                assert (false)
            }
        }
        assert(true)

    }
}
