package net.hedtech.banner.sspb

import grails.test.mixin.TestFor
import grails.test.GrailsUnitTestCase
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

/**
 * Created by IntelliJ IDEA.
 * User: jzhong
 * Date: 4/1/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */

class PageModelValidatorTests extends GrailsUnitTestCase   {
    def pageModelValidator

    public void setUp() {
        super.setUp()
        def defPath = "resource/PageModelDefinition.json"
        def pageDefText = this.class.classLoader.getResourceAsStream( 'PageModelDefinition.json' ).text
        //def pageDefText = new File(defPath).getText()
        def slurper = new groovy.json.JsonSlurper()
        def pageBuilderModel = slurper.parseText(pageDefText)
        pageModelValidator = new PageModelValidator()
        pageModelValidator.setPageBuilderModel(pageBuilderModel)

    }

    public void tearDown() {
        super.tearDown()
    }


    void testValidate() {
        // test all page model compilation
        def modelIds = 1..6
        def modelPath = "test/testData/model/PageModel"
        def getModelPath = { id -> return modelPath + id + '.json' }
        def jsonSlurper = new groovy.json.JsonSlurper()

        for (i in modelIds) {
            def modelFilePath = getModelPath(i)
            println "testing $modelFilePath}"
            def page = jsonSlurper.parseText(new File(modelFilePath).getText())
            def validateResult =  pageModelValidator.parseAndValidatePage(Page.modelToString(page.modelView))
            println "Validation result: valid =  $validateResult.valid"
            if (validateResult?.error) {
                println "There are ${validateResult.error.size()} validation error(s):"
                validateResult?.error.each { println it}
            }
            assert(validateResult.error.size()==0)
        }
    }
}
