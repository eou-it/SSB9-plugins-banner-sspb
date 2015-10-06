package net.hedtech.banner.sspb

import grails.converters.JSON
import grails.test.spock.IntegrationSpec

class PageUtilServiceIntegrationSpec extends IntegrationSpec {

    def pageUtilService
    def workPath = "target/testData/model"
    def sourcePath = "test/testData/model"

    def setup() {
        // Setup logic here
        new File(workPath).mkdirs()
        new File(workPath).eachFileMatch(~/.*.json.*/) { file ->
            file.delete()
        }
        new AntBuilder().copy(todir: workPath) {
            fileset(dir: sourcePath)
        }
    }


    def cleanup() {
    }


    void "Integration test import"() {

        when:
        println "Before import check that test pages do not exist"

        then:
        Page.findByConstantName("testBasePage") == null
        Page.findByConstantName("testBasePage2") == null
        Page.findByConstantName("testExt1") == null
        Page.findByConstantName("testBasePage") == null

        when:
        pageUtilService.importAllFromDir(workPath)

        then: "test extended pages created"
        Page.findByConstantName("testBasePage") != null
        Page.findByConstantName("testBasePage2") != null
        Page.findByConstantName("testExt1") != null
        Page.findByConstantName("testBasePage") != null
        Page.findByConstantName("testExt1").extendsPage == Page.findByConstantName("testBasePage")
        Page.findByConstantName("extPage2").extendsPage == Page.findByConstantName("testBasePage2")
        println "import test complete"
    }


    void "Integration test export"() {
        when:
        pageUtilService.exportAllToFile(workPath)

        then: "test extended pages exported"
        new File(workPath, "testBasePage.json").exists()
        new File(workPath, "testBasePage2.json").exists()
        new File(workPath, "testExt1.json").exists()
        new File(workPath, "extPage2.json").exists()
        println "All test pages successfully exported"

        when:
        File testBasePage = new File(workPath, "testBasePage.json")
        def baseJson
        JSON.use("deep") {
            baseJson = JSON.parse(testBasePage.text)
        }

        then: "test non extended file details exported"
        baseJson.constantName == "testBasePage"
        baseJson.has("extendsPage")
        !baseJson.extendsPage  // extendsPage is null
        println "test base page 1 correctly exported"

        when:
        File textExt1File = new File(workPath, "testExt1.json")
        def ext1Json
        JSON.use("deep") {
            ext1Json = JSON.parse(textExt1File.text)
        }

        then: "test extended file details exported"
        ext1Json.constantName == "testExt1"
        ext1Json.has("extendsPage")
        ext1Json.extendsPage != null
        ext1Json.extendsPage.constantName == "testBasePage"
        println "test extended page 1 correctly exported"

        when:
        File testBasePage2 = new File(workPath, "testBasePage2.json")
        def base2Json
        JSON.use("deep") {
            base2Json = JSON.parse(testBasePage2.text)
        }

        then: "test non extended file 2 details exported"
        base2Json.constantName == "testBasePage2"
        base2Json.has("extendsPage")
        !base2Json.extendsPage  // extendsPage is null
        println "test base page 2 correctly exported"

        when:
        File textExt2File = new File(workPath, "extPage2.json")
        def ext2Json
        JSON.use("deep") {
            ext2Json = JSON.parse(textExt2File.text)
        }

        then: "test extended file 2 details exported"
        ext2Json.constantName == "extPage2"
        ext2Json.has("extendsPage")
        ext2Json.extendsPage != null
        ext2Json.extendsPage.constantName == "testBasePage2"
        println "test extended page 2 correctly exported"
    }

    void "Integration test compileAll"() {
        when:
        def errors = pageUtilService.compileAll()

        then:
        errors.empty
    }
}
