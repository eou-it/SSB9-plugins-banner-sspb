package net.hedtech.banner.sspb

/**
 * Created by IntelliJ IDEA.
 * User: jzhong
 * Date: 4/1/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
import grails.test.mixin.Mock
import net.hedtech.banner.css.Css

@Mock(Css)
class CompileServiceTests extends GroovyTestCase  {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }


    void testCompileAll() {
        // test all page model compilation
        def modelIds = 1..6
        def modelPath = "test/testData/model/PageModel"
        def getModelPath = { id -> return modelPath + id + '.json' }
        def jsonSlurper = new groovy.json.JsonSlurper()

        for (i in modelIds) {
            def modelFilePath = getModelPath(i)
            println "\n\ntesting $modelFilePath}"

            def page = jsonSlurper.parseText(new File(modelFilePath).getText())

            if ( !(page.constantName && page.modelView)) {
                println "expected page source JSON object to have constantName and modelView properties"
            }
            // parse, normalize and validate page model
            def validateResult =  CompileService.preparePage(page.modelView)
            if (validateResult.valid) {
                // generate JS first because it will set the binding correctly for some components
                def compiledJSCode=CompileService.compileController(validateResult.pageComponent)
                //println "Compiled Controller = $compiledJSCode\n"
                println "JavaScript is compiled"

                def compiledView = CompileService.compile2page(validateResult.pageComponent)
                //println "Compiled View = $compiledView\n"
                println "HTML is compiled"

                def combinedView = CompileService.assembleFinalPage(compiledView, compiledJSCode)
                //println "Compbined View = $combinedView\n"
                println "Page is compiled"

            } else {
                println "modelFilePath validation error:\n" + validateResult.error.join('\n')
                assert (false)
            }
        }
        assert(true)

    }
}
